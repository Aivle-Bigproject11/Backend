#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
사망자 수 예측 모델 - 최적화 버전
- UTF-8 인코딩 처리
- 지역별 사망자 비율 계산
- 2024-2025년 통합 예측
"""

import json
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error, r2_score
from datetime import datetime
import sys
import warnings
warnings.filterwarnings('ignore')

# UTF-8 인코딩 설정
import locale
import codecs
sys.stdout = codecs.getwriter('utf-8')(sys.stdout.buffer)


def load_training_data(csv_file):
    """피벗 테이블 형태의 CSV 데이터 로드 및 전처리"""
    # CSV 파일 로드 (인코딩 자동 감지)
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
    
    print(f"원본 CSV 데이터 형태: {df.shape}")
    print(f"첫 번째 컬럼 (지역): {df.columns[0]}")
    
    # 첫 번째 컬럼을 지역으로 설정
    region_col = df.columns[0]
    
    # 날짜 컬럼들만 추출 (YYYY.MM 형태)
    date_columns = []
    for col in df.columns[1:]:
        if '.' in str(col) and len(str(col).split('.')) == 2:
            year_month = str(col).split('.')
            if len(year_month[0]) == 4 and len(year_month[1]) <= 2:
                date_columns.append(col)
    
    print(f"감지된 날짜 컬럼 수: {len(date_columns)}")
    print(f"날짜 범위: {date_columns[0]} ~ {date_columns[-1]}")
    
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
        month = parts[1].zfill(2)  # 1 -> 01
        return f"{year}-{month}"
    
    df_long['date'] = df_long['date_str'].apply(fix_date_format)
    df_long['date'] = pd.to_datetime(df_long['date'])
    df_long['year'] = df_long['date'].dt.year
    df_long['month'] = df_long['date'].dt.month
    
    # 지역명을 범주형 데이터로 변환하여 모델 학습에 사용 (내부 인코딩)
    df_long['region_encoded'] = pd.Categorical(df_long['region']).codes
    
    # 결측값이나 문자열 값 처리
    df_long['value'] = pd.to_numeric(df_long['value'], errors='coerce')
    df_long['value'] = df_long['value'].fillna(0)  # NaN을 0으로 대체
    
    print(f"전처리 완료된 데이터 형태: {df_long.shape}")
    print(f"지역 수: {df_long['region'].nunique()}")
    print(f"기간: {df_long['date'].min()} ~ {df_long['date'].max()}")
    
    return df_long


def train_prediction_model(df):
    """예측 모델 훈련"""
    # 피처와 타겟 분리
    features = ['year', 'month', 'region_encoded']
    X = df[features]
    y = df['value']
    
    # 훈련/테스트 데이터 분할
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    
    # Random Forest 모델 생성 및 훈련
    model = RandomForestRegressor(n_estimators=100, random_state=42, n_jobs=-1)
    model.fit(X_train, y_train)
    
    # 모델 평가
    y_pred = model.predict(X_test)
    mse = mean_squared_error(y_test, y_pred)
    r2 = r2_score(y_test, y_pred)
    
    print(f"\n모델 평가 결과:")
    print(f"  MSE: {mse:.2f}, R2 Score: {r2:.4f}")
    
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


def make_predictions(model, df, target_year, target_months=None):
    """예측 실행 (Java 호환 형식)"""
    
    if target_months is None:
        target_months = list(range(1, 13))  # 1월~12월
    
    regions = df['region'].unique()
    region_encodings = {region: df[df['region'] == region]['region_encoded'].iloc[0] 
                       for region in regions}
    
    # 전년도 데이터 조회
    print(f"전년도({target_year-1}) 동월 데이터 조회 중...")
    previous_year_data = get_previous_year_data(df, target_year, target_months, regions)
    
    # 예측 결과 생성
    predictions_result = []
    
    for month in target_months:
        month_predictions = []
        
        # 해당 월의 모든 지역 예측값 먼저 계산
        for region in regions:
            X_pred = [[target_year, month, region_encodings[region]]]
            prediction = model.predict(X_pred)[0]
            
            # 음수 방지
            prediction = max(0, prediction)
            
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
            
            # 전년도 동월 데이터
            previous_value = previous_year_data.get((region, month))
            
            # 증감률 계산
            growth_rate = calculate_growth_rate(prediction, previous_value)
            
            # 지역별 비율 계산
            if region != '전국' and total_deaths > 0:
                regional_percentage = (prediction / total_deaths) * 100
            else:
                regional_percentage = 100.0 if region == '전국' else 0.0
            
            # 결과 저장 (간소화된 형식)
            result_record = {
                'date': f'{target_year}-{month:02d}',
                'year': target_year,
                'month': month,
                'regionName': region,
                'predictedDeaths': int(round(prediction)),
                'growthRate': growth_rate,
                'regionalPercentage': round(regional_percentage, 1),
                'previousYearDeaths': int(previous_value) if previous_value else None
            }
            
            predictions_result.append(result_record)
    
    print(f"총 {len(predictions_result)}개의 예측 레코드가 생성되었습니다.")
    
    return predictions_result


def add_predictions_to_training_data(original_df, predictions):
    """예측 결과를 학습 데이터에 추가"""
    print(f"예측 결과를 학습 데이터에 추가 중...")
    
    pred_records = []
    for pred in predictions:
        # date에서 년월 추출
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
    
    # 기존 학습 데이터와 결합
    combined_df = pd.concat([original_df, pred_df], ignore_index=True)
    combined_df = combined_df.sort_values(['region', 'date']).reset_index(drop=True)
    
    print(f"  기존 데이터: {len(original_df)} 레코드")
    print(f"  예측 데이터: {len(pred_df)} 레코드") 
    print(f"  결합된 데이터: {len(combined_df)} 레코드")
    
    return combined_df


def save_predictions_to_json(predictions, output_file):
    """예측 결과를 JSON 파일로 저장 (UTF-8 인코딩, 메타데이터 제거)"""
    
    # 날짜와 지역명 순으로 정렬
    predictions.sort(key=lambda x: (x['date'], x['regionName']))
    
    # UTF-8 인코딩으로 JSON 저장
    with open(output_file, 'w', encoding='utf-8') as file:
        json.dump(predictions, file, ensure_ascii=False, indent=2, default=str)
    
    print(f"\n✓ 예측 결과를 {output_file}에 저장했습니다. (UTF-8 인코딩)")
    
    # 결과 요약 출력
    print("\n예측 결과 샘플 (처음 3개):")
    for i, record in enumerate(predictions[:3]):
        print(f"  {i+1}. 날짜: {record['date']}")
        print(f"     지역: {record['regionName']}")
        print(f"     예측 사망자: {record['predictedDeaths']}명")
        print(f"     증감률: {record['growthRate']}%")
        print(f"     지역별 비율: {record['regionalPercentage']}%")


def print_summary_statistics(predictions):
    """예측 결과 통계 요약"""
    df_pred = pd.DataFrame(predictions)
    
    print("\n" + "="*60)
    print("예측 결과 통계 요약")
    print("="*60)
    
    # 연도별 통계
    for year in sorted(df_pred['year'].unique()):
        year_data = df_pred[df_pred['year'] == year]
        print(f"\n{year}년 예측:")
        
        # 전국 총계 (전국 제외한 지역들의 합)
        regional_data = year_data[year_data['regionName'] != '전국']
        total_deaths = regional_data['predictedDeaths'].sum()
        print(f"  총 예측 사망자 수: {total_deaths:,}명")
        
        # 상위 5개 지역
        top_regions = regional_data.groupby('regionName')['predictedDeaths'].sum().nlargest(5)
        print(f"  \n  사망자 수 상위 5개 지역:")
        for region, deaths in top_regions.items():
            percentage = (deaths / total_deaths * 100) if total_deaths > 0 else 0
            print(f"    - {region}: {int(deaths):,}명 ({percentage:.1f}%)")
        
        # 월별 평균
        monthly_avg = regional_data.groupby('month')['predictedDeaths'].sum()
        print(f"  \n  월별 전국 총 사망자:")
        for month in range(1, 13):
            if month in monthly_avg.index:
                print(f"    {month:2d}월: {int(monthly_avg[month]):,}명")


def main_sequential_prediction(training_data_path, 
                             output_combined='predict_result_final.json',
                             target_months=None):
    """연속 예측 메인 함수: 2024 예측 → 데이터 추가 → 2025 예측 → 통합 출력"""
    
    print("="*60)
    print("사망자 수 예측 시스템")
    print("="*60)
    print("- UTF-8 인코딩 적용")
    print("- 지역별 사망자 비율 계산")
    print("="*60)
    
    # === STEP 1: 2024년 예측 ===
    print("\n[STEP 1] 2004~2023 데이터로 2024년 예측")
    print("-"*40)
    
    # 학습 데이터 로드
    original_df = load_training_data(training_data_path)
    
    # 첫 번째 모델 훈련 (2004~2023)
    model_2024 = train_prediction_model(original_df)
    
    # 2024년 예측
    predictions_2024 = make_predictions(model_2024, original_df, 2024, target_months)
    
    # === STEP 2: 2025년 예측 ===
    print("\n[STEP 2] 2024년 예측 결과를 추가하여 2025년 예측")
    print("-"*40)
    
    # 2024년 예측 결과를 학습 데이터에 추가
    extended_df = add_predictions_to_training_data(original_df, predictions_2024)
    
    # 확장된 데이터로 새 모델 훈련 (2004~2024)
    print("확장된 데이터셋으로 모델 재훈련 중...")
    model_2025 = train_prediction_model(extended_df)
    
    # 2025년 예측
    predictions_2025 = make_predictions(model_2025, extended_df, 2025, target_months)
    
    # === STEP 3: 결과 통합 및 저장 ===
    print("\n[STEP 3] 2024-2025 예측 결과 통합")
    print("-"*40)
    
    # 2024년과 2025년 결과 통합
    combined_predictions = predictions_2024 + predictions_2025
    
    # 통합 결과 저장
    save_predictions_to_json(combined_predictions, output_combined)
    
    # 통계 요약 출력
    print_summary_statistics(combined_predictions)
    
    # === 최종 결과 ===
    print("\n" + "="*60)
    print("예측 완료!")
    print("="*60)
    print(f"✓ 2024년 예측: {len(predictions_2024)} 레코드")
    print(f"✓ 2025년 예측: {len(predictions_2025)} 레코드")
    print(f"✓ 통합 파일: {output_combined} ({len(combined_predictions)} 레코드)")
    print(f"✓ 인코딩: UTF-8")
    
    return combined_predictions


# 실행 예시
if __name__ == "__main__":
    import os
    
    # 파일 경로 설정
    training_data_path = '/Users/andohyung/Downloads/예측모델/AI_Model/지역_월별_사망자수_데이터_최종.csv'
    
    # 파일 존재 확인
    if not os.path.exists(training_data_path):
        print(f"⚠ 경고: CSV 파일을 찾을 수 없습니다: {training_data_path}")
        print("스크립트와 같은 디렉토리에 CSV 파일을 위치시키거나, 아래에 파일의 전체 경로를 입력하세요.")
        try:
            input_path = input("CSV 파일 경로를 입력하세요: ").strip()
            # 따옴표 제거 (경로 복사 시 포함될 수 있음)
            if input_path.startswith('"') and input_path.endswith('"'):
                input_path = input_path[1:-1]
            training_data_path = input_path
        except EOFError:  # 비대화형 환경에서 실행될 경우
            print("입력 경로를 받지 못했습니다. 스크립트를 종료합니다.")
            sys.exit(1)

    # 연속 예측 실행 (2024-2025 통합 결과)
    if os.path.exists(training_data_path):
        try:
            combined_predictions = main_sequential_prediction(
                training_data_path=training_data_path,
                output_combined='predict_result_final.json',
                target_months=None  # None이면 1-12월 전체
            )
            
            print("\n 예측이 성공적으로 완료되었습니다!")
            print("결과 파일: predict_result_final.json")
            
        except Exception as e:
            print(f"\n 오류 발생: {e}")
            import traceback
            traceback.print_exc()
    else:
        print(f"⚠ 최종적으로 파일 경로를 확인할 수 없습니다: {training_data_path}")