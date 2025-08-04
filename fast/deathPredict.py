import pandas as pd
import numpy as np
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import PolynomialFeatures
import warnings
import joblib 
import datetime 

warnings.filterwarnings('ignore')



# 1. 데이터 로드 및 전처리
def load_and_preprocess_data(file_path):
    """지역별 월별 사망자수 데이터를 로드하고 시계열로 변환"""
    
    encodings = ['euc-kr', 'cp949', 'utf-8', 'cp1252']
    df = None
    
    for encoding in encodings:
        try:
            df = pd.read_csv(file_path, encoding=encoding)
            print(f"파일 인코딩: {encoding}")
            break
        except:
            continue
    
    if df is None:
        raise ValueError("파일을 읽을 수 없습니다.")
    
    region_col = df.columns[0]
    df = df.rename(columns={region_col: 'region'})
    
    month_columns = [col for col in df.columns if col != 'region' and '.' in col]
    month_columns = [col for col in month_columns if len(col.split('.')) == 2 and 
                    col.split('.')[0].isdigit() and col.split('.')[1].isdigit()]
    month_columns.sort(key=lambda x: (int(x.split('.')[0]), int(x.split('.')[1])))
    
    df_regions = df[df['region'] != '전국'].copy()
    
    regional_data = {}
    
    for idx, row in df_regions.iterrows():
        region_name = str(row['region']).strip()
        monthly_records = []
        
        for month_col in month_columns:
            try:
                year_month = month_col.split('.')
                year = int(year_month[0])
                month = int(year_month[1])
                
                if month == 1 and f"{year}.10" in month_columns:
                    continue
                elif month == 1:
                    month = 10
                
                death_count = row[month_col]
                
                if pd.notna(death_count) and death_count > 0:
                    date = pd.to_datetime(f"{year}-{month:02d}-01")
                    
                    monthly_records.append({
                        'date': date,
                        'year': year,
                        'month': month,
                        'quarter': (month - 1) // 3 + 1,
                        'season': 0 if month in [12,1,2] else 1 if month in [3,4,5] else 2 if month in [6,7,8] else 3,
                        'death_count': float(death_count),
                        'covid_period': 1 if year >= 2020 else 0
                    })
            except:
                continue
        
        if monthly_records:
            region_df = pd.DataFrame(monthly_records)
            region_df = region_df.sort_values('date').reset_index(drop=True)
            
            region_df['month_sin'] = np.sin(2 * np.pi * region_df['month'] / 12)
            region_df['month_cos'] = np.cos(2 * np.pi * region_df['month'] / 12)
            region_df['year_normalized'] = (region_df['year'] - region_df['year'].min()) / (region_df['year'].max() - region_df['year'].min())
            
            regional_data[region_name] = region_df
    
    print(f"총 {len(regional_data)}개 지역 데이터 전처리 완료")
    return regional_data

# 2. 모델링 클래스
class RegionalMonthlyDeathModels:
    def __init__(self, regional_data):
        self.models = {}
        self.poly_features = {}
        self.regional_data = regional_data 
        self.year_min = regional_data['서울']['year'].min() if '서울' in regional_data else 2004
        self.year_max = regional_data['서울']['year'].max() if '서울' in regional_data else 2023
    
    def fit_linear_seasonal_model(self, data, region_name):
        X = data[['year_normalized', 'month_sin', 'month_cos', 'covid_period']].values
        y = data['death_count'].values
        model = LinearRegression()
        model.fit(X, y)
        self.models[region_name]['linear_seasonal'] = {
            'model': model,
            'score': model.score(X, y),
            'method': 'Linear + Seasonal'
        }
    def predict_all_regions(self, start_date, num_months=12):
        all_results = []
        for region in self.models.keys():
            try:
                preds = self.predict_next_months(region, start_date, num_months)
                all_results.extend(preds)
            except Exception as e:
                print(f"[{region}] 예측 실패: {e}")
        return all_results
        
    def fit_polynomial_seasonal_model(self, data, region_name, degree=2):
        year_poly = PolynomialFeatures(degree=degree, include_bias=False)
        year_features = year_poly.fit_transform(data[['year_normalized']])
        seasonal_features = data[['month_sin', 'month_cos', 'covid_period']].values
        X = np.hstack([year_features, seasonal_features])
        y = data['death_count'].values
        model = LinearRegression()
        model.fit(X, y)
        self.models[region_name]['polynomial_seasonal'] = {
            'model': model,
            'poly_features': year_poly,
            'score': model.score(X, y),
            'method': f'Polynomial(degree={degree}) + Seasonal'
        }
        self.poly_features[region_name] = year_poly
        
    def fit_random_forest_model(self, data, region_name):
        features = data[['year_normalized', 'month', 'quarter', 'season', 'month_sin', 'month_cos', 'covid_period']].values
        y = data['death_count'].values
        
        model = RandomForestRegressor(n_estimators=100, max_depth=8, random_state=42, min_samples_split=5)
        model.fit(features, y)
        
        self.models[region_name]['random_forest'] = {
            'model': model,
            'score': model.score(features, y),
            'method': 'Random Forest'
        }

    def train_all_models_for_region(self, data, region_name):
        """한 지역에 대해 모든 모델을 학습시킵니다."""
        self.models[region_name] = {}
        
        try:
            self.fit_linear_seasonal_model(data, region_name)
            print(f"[{region_name}] 선형+계절성 모델 학습 완료. R²: {self.models[region_name]['linear_seasonal']['score']:.3f}")
        except Exception as e:
            print(f"[{region_name}] 선형+계절성 모델 학습 실패: {e}")
            
        try:
            self.fit_polynomial_seasonal_model(data, region_name)
            print(f"[{region_name}] 다항식+계절성 모델 학습 완료. R²: {self.models[region_name]['polynomial_seasonal']['score']:.3f}")
        except Exception as e:
            print(f"[{region_name}] 다항식+계절성 모델 학습 실패: {e}")
            
        try:
            self.fit_random_forest_model(data, region_name)
            print(f"[{region_name}] 랜덤포레스트 모델 학습 완료. R²: {self.models[region_name]['random_forest']['score']:.3f}")
        except Exception as e:
            print(f"[{region_name}] 랜덤포레스트 모델 학습 실패: {e}")

    def get_last_year_death_count(self, region, target_date):
        """과거 데이터에서 이전 연도의 사망자수를 찾아 반환합니다."""
        if region not in self.regional_data:
            return 0 
        
        data = self.regional_data[region]
        
        prev_year = target_date.year - 1
        prev_month = target_date.month
        
        death_count = data[(data['year'] == prev_year) & (data['month'] == prev_month)]['death_count'].values
        return death_count[0] if len(death_count) > 0 else 0

    def predict_next_months(self, region, start_date, num_months=12):
        """특정 지역의 특정 시작일로부터 num_months만큼의 사망자 수를 예측합니다."""
        if region not in self.models:
            raise ValueError(f"'{region}' 지역에 대한 학습된 모델이 없습니다.")
            
        all_monthly_preds_by_model = []
        
        for model_name, model_info in self.models[region].items():
            model = model_info['model']
            monthly_preds_for_model = []
            
            for i in range(num_months):
                target_date = start_date + pd.DateOffset(months=i)
                target_year_norm = (target_date.year - self.year_min) / (self.year_max - self.year_min) if self.year_max > self.year_min else 0.5
                target_month = target_date.month
                
                previous_year_deaths = self.get_last_year_death_count(region, target_date)

                if model_name == 'linear_seasonal':
                    features = [target_year_norm, np.sin(2 * np.pi * target_month / 12), np.cos(2 * np.pi * target_month / 12), 1 if target_date.year >= 2020 else 0]
                    X_future = np.array([features])
                    pred = model.predict(X_future)[0]
                
                elif model_name == 'polynomial_seasonal':
                    poly_features = self.poly_features[region]
                    year_poly = poly_features.transform([[target_year_norm]])
                    features = np.hstack([year_poly, [[np.sin(2 * np.pi * target_month / 12), np.cos(2 * np.pi * target_month / 12), 1 if target_date.year >= 2020 else 0]]])
                    pred = model.predict(features)[0]

                elif model_name == 'random_forest':
                    features = [
                        target_year_norm, target_month, (target_month - 1) // 3 + 1,
                        0 if target_month in [12,1,2] else 1 if target_month in [3,4,5] else 2 if target_month in [6,7,8] else 3,
                        np.sin(2 * np.pi * target_month / 12), np.cos(2 * np.pi * target_month / 12),
                        1 if target_date.year >= 2020 else 0
                    ]
                    X_future = np.array([features])
                    pred = model.predict(X_future)[0]

                monthly_preds_for_model.append(max(0, pred))
            
            all_monthly_preds_by_model.append(monthly_preds_for_model)

        ensemble_predictions = np.mean(all_monthly_preds_by_model, axis=0)
        final_results = []
        for i in range(num_months):
            target_date = start_date + pd.DateOffset(months=i)
            final_results.append({
                "date": f"{target_date.year}-{target_date.month:02d}",
                "region": region,
                "predictedDeaths": int(round(ensemble_predictions[i], 0))
            })
            
        return final_results

def train_and_save_models(file_path, model_filename='regional_models.joblib'):
    """데이터를 로드하고 모델을 학습시킨 후 저장하는 함수"""
    print("AI 모델 학습을 시작합니다...")
    regional_data = load_and_preprocess_data(file_path)
    
    models_manager = RegionalMonthlyDeathModels(regional_data)
    for region, data in regional_data.items():
        if not data.empty:
            models_manager.train_all_models_for_region(data, region)
    
    joblib.dump(models_manager, model_filename)
    print(f"AI 모델이 '{model_filename}' 파일로 저장되었습니다.")
    return models_manager