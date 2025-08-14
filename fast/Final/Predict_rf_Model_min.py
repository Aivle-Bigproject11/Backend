#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
(출력 제거) 사망자 예측 및 인력배치 시스템
- 사망자 수 예측 (Random Forest 기반)
- 장례식장 가중치 적용
- 인력배치 최적화
- 통합 JSON 결과 생성
"""

import json
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error, r2_score
import warnings
warnings.filterwarnings('ignore')


def generate(training_csv_path, funeral_csv_path, total_staff=400, output_json_path=None):
    """
    사망자 예측부터 인력배치까지 모든 과정을 수행하는 통합 함수
    
    Args:
        training_csv_path (str): 지역별 월별 사망자수 학습 데이터 CSV 경로
        funeral_csv_path (str): 장례식장 데이터 CSV 경로  
        total_staff (int): 총 배치할 직원 수 (기본값: 400)
        output_json_path (str): 출력 JSON 파일 경로 (None이면 자동 생성)
    
    Returns:
        list: 예측 및 인력배치 정보가 포함된 결과 데이터
    """
    
    # === STEP 1: 학습 데이터 로드 및 전처리 ===
    df = None
    for encoding in ['utf-8', 'cp949', 'euc-kr']:
        try:
            df = pd.read_csv(training_csv_path, encoding=encoding)
            break
        except:
            continue
    
    if df is None:
        raise ValueError(f"학습 데이터를 읽을 수 없습니다: {training_csv_path}")
    
    # 첫 번째 컬럼을 지역으로 설정
    region_col = df.columns[0]
    
    # 날짜 컬럼들만 추출 (YYYY.MM 형태)
    date_columns = []
    for col in df.columns[1:]:
        if '.' in str(col) and len(str(col).split('.')) == 2:
            year_month = str(col).split('.')
            if len(year_month[0]) == 4 and len(year_month[1]) <= 2:
                date_columns.append(col)
    
    # 피벗 테이블을 long format으로 변환
    df_long = pd.melt(
        df[[region_col] + date_columns], 
        id_vars=[region_col], 
        value_vars=date_columns,
        var_name='date_str', 
        value_name='value'
    )
    
    # 컬럼명 정리
    df_long.columns = ['region', 'date_str', 'value']
    
    # 날짜 문자열을 표준 형태로 변환 (YYYY-MM)
    def fix_date_format(date_str):
        parts = str(date_str).split('.')
        year = parts[0]
        month = parts[1].zfill(2)
        return f"{year}-{month}"
    
    df_long['date'] = df_long['date_str'].apply(fix_date_format)
    df_long['date'] = pd.to_datetime(df_long['date'])
    df_long['year'] = df_long['date'].dt.year
    df_long['month'] = df_long['date'].dt.month
    
    # 지역명을 범주형 데이터로 변환
    df_long['region_encoded'] = pd.Categorical(df_long['region']).codes
    
    # 결측값이나 문자열 값 처리
    df_long['value'] = pd.to_numeric(df_long['value'], errors='coerce')
    df_long['value'] = df_long['value'].fillna(0)
    
    # === STEP 2: Random Forest 모델 훈련 ===
    features = ['year', 'month', 'region_encoded']
    X = df_long[features]
    y = df_long['value']
    
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    
    model = RandomForestRegressor(n_estimators=100, random_state=42, n_jobs=-1)
    model.fit(X_train, y_train)
    
    # === STEP 3: 2024-2025년 예측 ===
    regions = df_long['region'].unique()
    region_encodings = {region: df_long[df_long['region'] == region]['region_encoded'].iloc[0] 
                       for region in regions}
    
    def get_previous_year_data(target_year, target_months):
        """전년도 동월 데이터 조회"""
        previous_year = target_year - 1
        previous_data = {}
        
        for month in target_months:
            for region in regions:
                prev_data = df_long[(df_long['year'] == previous_year) & 
                                  (df_long['month'] == month) & 
                                  (df_long['region'] == region)]
                
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
    
    def make_year_predictions(target_year, target_months=None):
        """특정 연도 예측"""
        if target_months is None:
            target_months = list(range(1, 13))
        
        previous_year_data = get_previous_year_data(target_year, target_months)
        predictions_result = []
        
        for month in target_months:
            month_predictions = []
            
            # 해당 월의 모든 지역 예측값 계산
            for region in regions:
                X_pred = [[target_year, month, region_encodings[region]]]
                prediction = model.predict(X_pred)[0]
                prediction = max(0, prediction)  # 음수 방지
                
                month_predictions.append({
                    'region': region,
                    'prediction': prediction
                })
            
            # 지역별 비율 계산
            regional_data = [p for p in month_predictions if p['region'] != '전국']
            total_deaths = sum([p['prediction'] for p in regional_data])
            
            # 각 지역에 대한 최종 결과 생성
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
                    'regionalPercentage': round(regional_percentage, 1)
                }
                
                predictions_result.append(result_record)
        
        return predictions_result
    
    # 2024년 예측
    predictions_2024 = make_year_predictions(2024)
    
    # 2024년 예측 결과를 학습 데이터에 추가
    pred_records = []
    for pred in predictions_2024:
        date_parts = pred['date'].split('-')
        year = int(date_parts[0])
        month = int(date_parts[1])
        
        pred_records.append({
            'region': pred['regionName'],
            'date_str': f"{year}.{month:02d}",
            'date': pd.to_datetime(pred['date']),
            'year': year,
            'month': month,
            'region_encoded': df_long[df_long['region'] == pred['regionName']]['region_encoded'].iloc[0],
            'value': pred['predictedDeaths']
        })
    
    pred_df = pd.DataFrame(pred_records)
    
    # 기존 학습 데이터와 결합
    extended_df = pd.concat([df_long, pred_df], ignore_index=True)
    extended_df = extended_df.sort_values(['region', 'date']).reset_index(drop=True)
    
    # 확장된 데이터로 새 모델 훈련
    X_extended = extended_df[features]
    y_extended = extended_df['value']
    model_2025 = RandomForestRegressor(n_estimators=100, random_state=42, n_jobs=-1)
    model_2025.fit(X_extended, y_extended)
    
    # 2025년 예측
    df_long = extended_df  # 전역 변수 업데이트
    model = model_2025     # 모델 업데이트
    
    predictions_2025 = make_year_predictions(2025)
    
    # 2024년과 2025년 결과 통합
    all_predictions = predictions_2024 + predictions_2025
    
    # === STEP 4: 장례식장 가중치 계산 ===
    funeral_df = None
    for encoding in ['utf-8', 'euc-kr', 'cp949']:
        try:
            funeral_df = pd.read_csv(funeral_csv_path, encoding=encoding)
            break
        except:
            continue
    
    if funeral_df is None:
        raise ValueError(f"장례식장 데이터를 읽을 수 없습니다: {funeral_csv_path}")
    
    # 지역별 장례식장 개수 계산
    region_col = funeral_df.columns[0]
    region_counts = funeral_df[region_col].value_counts().to_dict()
    
    # 가중치 계산 (0.8 ~ 1.2 범위)
    max_count = max(region_counts.values())
    min_count = min(region_counts.values())
    
    funeral_weights = {}
    for region, count in region_counts.items():
        if max_count == min_count:
            weight = 1.0
        else:
            normalized = (count - min_count) / (max_count - min_count)
            weight = 0.8 + (0.4 * normalized)
        funeral_weights[region] = round(weight, 3)
    
    # === STEP 5: 인력배치 계산 ===
    def find_weight(region_name):
        """지역 매핑 함수 (유연한 매칭)"""
        if region_name in funeral_weights:
            return funeral_weights[region_name]
        
        region_key = region_name.replace('특별시', '').replace('광역시', '') \
                               .replace('특별자치시', '').replace('도', '')
        
        for funeral_region, weight in funeral_weights.items():
            funeral_key = funeral_region.replace('특별시', '').replace('광역시', '') \
                                       .replace('특별자치시', '').replace('특별자치도', '') \
                                       .replace('도', '')
            if region_key in funeral_key or funeral_key in region_key:
                return weight
        
        return 1.0  # 기본 가중치
    
    # 월별로 데이터 그룹화
    months_data = {}
    for pred in all_predictions:
        if pred['regionName'] == '전국':
            continue
            
        month_key = f"{pred['date']}"
        if month_key not in months_data:
            months_data[month_key] = []
        months_data[month_key].append(pred)
    
    enhanced_predictions = []
    
    for month_key, month_preds in months_data.items():
        allocations = []
        
        for pred in month_preds:
            original_staff = round(total_staff * pred['regionalPercentage'] / 100)
            weight = find_weight(pred['regionName'])
            
            allocations.append({
                'region': pred['regionName'],
                'original_percentage': pred['regionalPercentage'],
                'original_staff': original_staff,
                'funeral_weight': weight,
                'enhanced_percentage': pred['regionalPercentage'] * weight,
                'prediction': pred
            })
        
        # 비율 정규화
        total_enhanced = sum([a['enhanced_percentage'] for a in allocations])
        for alloc in allocations:
            alloc['enhanced_percentage'] = (alloc['enhanced_percentage'] / total_enhanced) * 100
            alloc['enhanced_staff'] = round(total_staff * alloc['enhanced_percentage'] / 100)
            alloc['staff_change'] = alloc['enhanced_staff'] - alloc['original_staff']
        
        # 총합 조정 (반올림 오차 보정)
        total_allocated = sum([a['enhanced_staff'] for a in allocations])
        if total_allocated != total_staff:
            diff = total_staff - total_allocated
            max_alloc = max(allocations, key=lambda x: x['enhanced_staff'])
            max_alloc['enhanced_staff'] += diff
            max_alloc['staff_change'] += diff
        
        # JSON 데이터 생성
        for alloc in allocations:
            enhanced_pred = {
                'date': alloc['prediction']['date'],
                'regionName': alloc['prediction']['regionName'],
                'predictedDeaths': alloc['prediction']['predictedDeaths'],
                'growthRate': alloc['prediction']['growthRate'],
                'staff': alloc['enhanced_staff'],
                'staffChange': alloc['staff_change']
            }
            
            enhanced_predictions.append(enhanced_pred)
    
    # === STEP 6: 전국 데이터 추가 ===
    for pred in all_predictions:
        if pred['regionName'] == '전국':
            enhanced_predictions.append(pred)
    
    # 날짜순 정렬
    enhanced_predictions.sort(key=lambda x: (x['date'], x['regionName']))
    
    # === STEP 7: JSON 저장 ===
    if output_json_path is None:
        output_json_path = 'death_prediction_with_staffing.json'
    
    with open(output_json_path, 'w', encoding='utf-8') as f:
        json.dump(enhanced_predictions, f, ensure_ascii=False, indent=2)
    
    return enhanced_predictions


# 사용 예시
if __name__ == "__main__":
    try:
        result = generate(
            training_csv_path="지역_월별_사망자수_데이터_최종.csv",
            funeral_csv_path="장례식장현황데이터.csv",
            total_staff=400,
            output_json_path="result.json"
        )
        
    except FileNotFoundError as e:
        print(f"파일을 찾을 수 없습니다: {e}")
    except Exception as e:
        print(f"오류 발생: {e}")
        import traceback
        traceback.print_exc()