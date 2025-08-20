#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
사망자 수 예측 모델 - 최적화 버전 (FastAPI 연동용) / 2026 년까지 예측
"""

import json
import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, r2_score
import joblib
import warnings
warnings.filterwarnings('ignore')

# 기존 함수들은 그대로 유지합니다.
# load_training_data, train_prediction_model, get_previous_year_data,
# calculate_growth_rate, add_predictions_to_training_data
def load_training_data(csv_file):
    """피벗 테이블 형태의 CSV 데이터 로드 및 전처리"""
    df = None
    for encoding in ['utf-8', 'cp949', 'euc-kr']:
        try:
            df = pd.read_csv(csv_file, encoding=encoding)
            print(f"✓ CSV 파일 로드 성공 (인코딩: {encoding})")
            break
        except:
            continue
    
    if df is None:
        raise ValueError(f"CSV 파일을 읽을 수 없습니다: {csv_file}")
    
    region_col = df.columns[0]
    date_columns = []
    for col in df.columns[1:]:
        if '.' in str(col) and len(str(col).split('.')) == 2:
            year_month = str(col).split('.')
            if len(year_month[0]) == 4 and len(year_month[1]) <= 2:
                date_columns.append(col)
    
    df_long = pd.melt(
        df[[region_col] + date_columns], 
        id_vars=[region_col], 
        value_vars=date_columns,
        var_name='date_str', 
        value_name='value'
    )
    df_long.columns = ['region', 'date_str', 'value']
    
    def fix_date_format(date_str):
        parts = str(date_str).split('.')
        year = parts[0]
        month = parts[1].zfill(2)
        return f"{year}-{month}"
    
    df_long['date'] = df_long['date_str'].apply(fix_date_format)
    df_long['date'] = pd.to_datetime(df_long['date'])
    df_long['year'] = df_long['date'].dt.year
    df_long['month'] = df_long['date'].dt.month
    df_long['region_encoded'] = pd.Categorical(df_long['region']).codes
    df_long['value'] = pd.to_numeric(df_long['value'], errors='coerce')
    df_long['value'] = df_long['value'].fillna(0)
    
    return df_long

def train_prediction_model(df):
    """예측 모델 훈련"""
    features = ['year', 'month', 'region_encoded']
    X = df[features]
    y = df['value']
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    model = RandomForestRegressor(n_estimators=100, random_state=42, n_jobs=-1)
    model.fit(X_train, y_train)
    y_pred = model.predict(X_test)
    mse = mean_squared_error(y_test, y_pred)
    r2 = r2_score(y_test, y_pred)
    return model

def get_previous_year_data(df, target_year, target_months, regions):
    """전년도 동월 데이터 조회"""
    previous_year = target_year - 1
    previous_data = {}
    for month in target_months:
        for region in regions:
            prev_data = df[(df['year'] == previous_year) & 
                           (df['month'] == month) & 
                           (df['region'] == region)]
            if not prev_data.empty:
                previous_data[(region, month)] = prev_data['value'].iloc[0]
            else:
                previous_data[(region, month)] = None
    return previous_data

def calculate_growth_rate(current_value, previous_value):
    """전년 동월 대비 증감률 계산"""
    if previous_value is None or previous_value == 0:
        return 0.0
    growth_rate = ((current_value - previous_value) / previous_value) * 100
    return round(growth_rate, 1)

def add_predictions_to_training_data(original_df, predictions):
    """예측 결과를 학습 데이터에 추가"""
    pred_records = []
    for pred in predictions:
        date_parts = pred['date'].split('-')
        year = int(date_parts[0])
        month = int(date_parts[1])
        pred_records.append({
            'region': pred['regionName'],
            'date_str': f"{year}.{month:02d}",
            'date': pd.to_datetime(pred['date']),
            'year': year,
            'month': month,
            'region_encoded': original_df[original_df['region'] == pred['regionName']]['region_encoded'].iloc[0],
            'value': pred['predictedDeaths']
        })
    pred_df = pd.DataFrame(pred_records)
    combined_df = pd.concat([original_df, pred_df], ignore_index=True)
    combined_df = combined_df.sort_values(['region', 'date']).reset_index(drop=True)
    return combined_df

# -----------------------------------------------------------------
# 📌 FastAPI 연동을 위한 함수 추가
# -----------------------------------------------------------------

def train_and_save_models(data_file_path, model_file_path):
    """
    전체 예측 모델을 훈련하고 joblib 파일로 저장하는 함수.
    이 함수는 main.py의 lifespan 이벤트에서 호출됩니다.
    """
    print("모델 학습 및 저장 시작...")
    
    # 2024년 예측을 위한 모델 학습
    original_df = load_training_data(data_file_path)
    model_2024 = train_prediction_model(original_df)
    
    # 2025년 예측을 위한 모델 학습
    predictions_2024 = make_full_predictions(model_2024, original_df, 2024)
    extended_df = add_predictions_to_training_data(original_df, predictions_2024)
    model_2025 = train_prediction_model(extended_df)
    
    # 2026년 예측을 위한 모델 학습
    predictions_2025 = make_full_predictions(model_2025, extended_df, 2025)
    extended_df_2026 = add_predictions_to_training_data(extended_df, predictions_2025)
    model_2026 = train_prediction_model(extended_df_2026)
    
    # 모델 및 데이터를 딕셔너리로 묶어 joblib 파일로 저장
    ai_models = {
        'model_2024': model_2024,
        'model_2025': model_2025,
        'model_2026': model_2026,
        'training_data': extended_df_2026,
    }
    joblib.dump(ai_models, model_file_path)
    print(f"✓ AI 모델 및 데이터가 '{model_file_path}'에 저장되었습니다.")
    return ai_models


def make_full_predictions(model, df, target_year, target_months=None):
    """
    주어진 모델과 데이터로 전체 지역/월에 대한 예측을 수행하는 함수.
    이 함수는 main.py의 make_predictions 함수와 동일한 역할을 수행합니다.
    """
    if target_months is None:
        target_months = list(range(1, 13))
    
    regions = df['region'].unique()
    region_encodings = {region: df[df['region'] == region]['region_encoded'].iloc[0] for region in regions}
    
    previous_year_data = get_previous_year_data(df, target_year, target_months, regions)
    
    predictions_result = []
    
    for month in target_months:
        month_predictions = []
        for region in regions:
            X_pred = [[target_year, month, region_encodings[region]]]
            prediction = model.predict(X_pred)[0]
            prediction = max(0, prediction)
            month_predictions.append({'region': region, 'prediction': prediction})
        
        regional_data = [p for p in month_predictions if p['region'] != '전국']
        total_deaths = sum([p['prediction'] for p in regional_data])
        
        for pred_data in month_predictions:
            region = pred_data['region']
            prediction = pred_data['prediction']
            previous_value = previous_year_data.get((region, month))
            growth_rate = calculate_growth_rate(prediction, previous_value)
            
            if region != '전국' and total_deaths > 0:
                regional_percentage = (prediction / total_deaths) * 100
            else:
                regional_percentage = 100.0 if region == '전국' else 0.0
            
            result_record = {
                'date': f'{target_year}-{month:02d}',
                'regionName': region,
                'predictedDeaths': int(round(prediction)),
                'growthRate': growth_rate,
                'regionalPercentage': round(regional_percentage, 1),
                'previousYearDeaths': int(previous_value) if previous_value else None
            }
            predictions_result.append(result_record)
    
    return predictions_result

# main.py의 make_predictions와 동일한 시그니처를 맞추기 위한 래퍼 함수
def make_predictions(ai_models, training_data, year, months, region):
    """
    main.py의 요구사항에 맞춰 예측을 수행하는 래퍼 함수.
    ai_models 딕셔너리를 사용하여 예측합니다.
    """
    # 요청 년도에 따라 적절한 모델 선택
    if year == 2024:
        model = ai_models['model_2024']
    elif year == 2025:
        model = ai_models['model_2025']
    elif year == 2026:
        model = ai_models['model_2026']
    else:
        # 2024년, 2025년 또는 2026년이 아닌 경우 처리
        print(f"⚠ 경고: 예측 가능한 년도가 아닙니다: {year}. 2024년, 2025년 또는 2026년을 요청하세요.")
        return []

    # 전체 예측을 수행
    predictions = make_full_predictions(model, training_data, year, months)

    # 요청된 지역에 해당하는 결과만 필터링하여 반환
    if region:
        filtered_predictions = [pred for pred in predictions if pred['regionName'] == region]
        return filtered_predictions
    
    return predictions