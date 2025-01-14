import pymysql
import pandas as pd
import uuid

# MySQL 연결 정보 설정
def get_db_connection():
    return pymysql.connect(
        host="localhost",
        user="root",
        password="s&03221021",
        database="shoongdb",
        charset="utf8mb4"
    )

# 엑셀 데이터를 읽는 함수
def read_excel_data(file_path):
    # Excel 파일 읽기
    df = pd.read_excel(file_path)
    # 숫자 데이터에서 소수점 제거
    df['itstId'] = df['itstId'].apply(lambda x: str(int(x)) if pd.notnull(x) else None)
    return df

# MySQL에 데이터 삽입 함수
def insert_data_to_mysql(df, connection):
    try:
        with connection.cursor() as cursor:
            # 삽입 쿼리 정의
            insert_query = """
            INSERT INTO Light (lightID, itstId, latitude, longitude)
            VALUES (%s, %s, %s, %s)
            """

            # 데이터 삽입 반복
            for _, row in df.iterrows():
                light_id = str(uuid.uuid4())  # UUID 생성
                cursor.execute(insert_query, (
                    light_id,  # VARCHAR(36)로 저장
                    row['itstId'],  # 소수점 제거된 값
                    row['latitude'],
                    row['longitude'],
                ))

        # 트랜잭션 커밋
        connection.commit()
        print("Data inserted successfully!")

    except Exception as e:
        print(f"Error: {e}")
        connection.rollback()

    finally:
        connection.close()

# 메인 함수
def main():
    # 엑셀 파일 경로 설정
    file_path = "Filtered_Crossroad_Data.xlsx"  # 필요한 경로로 수정

    # 데이터 읽기
    df = read_excel_data(file_path)

    # MySQL 연결
    connection = get_db_connection()

    # 데이터 삽입
    insert_data_to_mysql(df, connection)

if __name__ == "__main__":
    main()
