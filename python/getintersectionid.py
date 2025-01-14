import requests
import pandas as pd
import json

# 1. API 키를 config.json에서 불러오기
def load_api_key(config_file="config.json"):
    try:
        with open(config_file, "r") as file:
            config = json.load(file)
        return config["api_key"]
    except FileNotFoundError:
        print("config.json 파일을 찾을 수 없습니다.")
        exit()
    except KeyError:
        print("config.json에 'api_key' 항목이 없습니다.")
        exit()

# 2. API 호출 함수 (페이지별 호출)
def fetch_crossroad_data(api_url, api_key, page):
    try:
        print(f"페이지 {page} 데이터 호출 중...")
        response = requests.get(f"{api_url}?apikey={api_key}&numOfRows=1000&pageNo={page}", verify=False)

        if response.status_code == 200:
            print(f"페이지 {page} 데이터 호출 성공")
            return response.json()
        else:
            print(f"페이지 {page} 데이터 호출 실패: {response.status_code}")
            return []
    except requests.exceptions.RequestException as e:
        print(f"API 요청 중 오류 발생: {e}")
        return []

# 3. 모든 페이지 데이터 가져오기
def fetch_all_crossroad_data(api_url, api_key, total_pages=3):
    all_data = []
    for page in range(1, total_pages + 1):
        page_data = fetch_crossroad_data(api_url, api_key, page)
        if isinstance(page_data, list):  # API 데이터가 올바른지 확인
            all_data.extend(page_data)
        else:
            print(f"페이지 {page} 응답 데이터 형식 오류")
    return all_data

# 4. 위도/경도 매칭 (근사치 비교 포함)
def match_crossroads(signal_data, crossroad_data):
    # 교차로 데이터에서 필요한 열만 추출하고 소수점 6자리로 반올림
    crossroads = [
        {
            "itstId": item["itstId"],
            "latitude": round(item["mapCtptIntLat"], 8),
            "longitude": round(item["mapCtptIntLot"], 8)
        }
        for item in crossroad_data
    ]
    crossroad_df = pd.DataFrame(crossroads)

    # 매칭 로직: 근사치 비교
    def find_crossroad_id(row):
        lat = round(row["latitude"], 8)
        lon = round(row["longitude"], 8)

        # 근사치 비교 (허용 오차 ±0.0001)
        match = crossroad_df[
            (crossroad_df["latitude"].apply(lambda x: abs(x - lat) < 1e-4)) &
            (crossroad_df["longitude"].apply(lambda x: abs(x - lon) < 1e-4))
        ]
        if not match.empty:
            return match.iloc[0]["itstId"]
        return None

    # 신호등 데이터와 매칭 수행
    signal_data["crossroad_id"] = signal_data.apply(find_crossroad_id, axis=1)

    # 매칭 결과 디버깅
    print("\n=== 매칭 결과 (샘플 10개) ===")
    print(signal_data[["latitude", "longitude", "crossroad_id"]].head(10))
    
    return signal_data

# 5. 메인 함수
def main():
    # API URL 및 키
    api_url = "http://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xCrossroadMapInformation/1.0"
    api_key = load_api_key()

    # API 호출
    crossroad_data = fetch_all_crossroad_data(api_url, api_key, total_pages=3)
    if not crossroad_data:
        print("API 데이터가 비어 있습니다.")
        return

    # 기존 신호등 데이터 불러오기
    signal_file = "서울시_신호등_현황.xlsx"
    signal_data = pd.read_excel(signal_file)

    # 위도/경도 매칭
    matched_data = match_crossroads(signal_data, crossroad_data)

    # 매칭된 결과 저장
    output_file = "matched_traffic_lights_with_crossroads.xlsx"
    matched_data.to_excel(output_file, index=False)
    print(f"\n매칭 결과 저장 완료: {output_file}")

if __name__ == "__main__":
    main()
