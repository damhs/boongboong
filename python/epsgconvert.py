from pyproj import Transformer
import pandas as pd

# EPSG:5186 -> EPSG:4326 변환기 생성
transformer = Transformer.from_crs("EPSG:5186", "EPSG:4326", always_xy=True)

# 엑셀 파일 경로 (EPSG:5186 형태의 좌표를 가진 파일)
input_file = "seoul_traffic_lights.xlsx"
output_file = "seoul_traffic_lights_converted.xlsx"

# 엑셀 파일 읽기
data = pd.read_excel(input_file)
print(data.columns)

# 데이터 확인 (X, Y 좌표 열 이름 변경 가능)
# X 좌표가 'x', Y 좌표가 'y'라는 열 이름을 가진 경우로 가정
x_column = "X좌표"
y_column = "Y좌표"

# 좌표 변환 수행
converted_coords = data.apply(
    lambda row: transformer.transform(row[x_column], row[y_column]), axis=1
)

# 변환 결과를 DataFrame에 추가
data["longitude"] = [coord[0] for coord in converted_coords]  # 경도
data["latitude"] = [coord[1] for coord in converted_coords]   # 위도

# 변환 결과를 엑셀로 저장
data.to_excel(output_file, index=False)

print(f"변환 완료! 파일이 저장되었습니다: {output_file}")
