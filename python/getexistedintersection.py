import pandas as pd
import requests
import json
import time

# Load API key from config
def load_api_key(config_file="config.json"):
    try:
        with open(config_file, "r") as file:
            config = json.load(file)
        return config["api_key"]
    except FileNotFoundError:
        print("config.json file not found.")
        exit()

# Fetch data from API for a given crossroad_id with retries and rate-limit handling
def fetch_signal_phase_data(api_url, api_key, crossroad_id, max_retries=5):
    url = f"{api_url}?apikey={api_key}&numOfRows=1&pageNo=1&itstId={int(crossroad_id)}"
    retries = 0

    while retries < max_retries:
        try:
            response = requests.get(url, verify=False, timeout=10)  # Disable SSL verification
            if response.status_code == 200:
                data = response.json()
                if isinstance(data, list) and data:  # Check if data is a non-empty list
                    return data[0]  # Return the most recent record
            elif response.status_code == 429:
                print(f"Rate limit hit for crossroad_id: {crossroad_id}. Retrying after delay...")
                time.sleep(60)  # Wait for a minute before retrying
            elif response.status_code == 404:
                print(f"No data found for crossroad_id: {crossroad_id}")
                return None
            else:
                print(f"Unexpected status code {response.status_code} for crossroad_id: {crossroad_id}")
                return None
        except requests.exceptions.ConnectionError as e:
            print(f"Connection error for crossroad_id: {crossroad_id}. Retrying...")
        except requests.exceptions.Timeout as e:
            print(f"Timeout for crossroad_id: {crossroad_id}. Retrying...")
        except Exception as e:
            print(f"Unexpected error for crossroad_id: {crossroad_id}: {e}. Retrying...")
        
        retries += 1
        time.sleep(2 ** retries)  # Exponential backoff
    print(f"Max retries exceeded for crossroad_id: {crossroad_id}")
    return None

# Process the filtered data and fetch additional info from the API
def process_crossroad_data(input_file, output_file, api_url, api_key):
    # Load the filtered data
    data = pd.read_excel(input_file)
    
    # Prepare a list to store the results
    results = []

    # Iterate over each row with a valid crossroad_id
    for _, row in data.iterrows():
        crossroad_id = row["crossroad_id"]
        print(f"Processing crossroad_id: {crossroad_id}...")
        api_data = fetch_signal_phase_data(api_url, api_key, crossroad_id)
        
        if api_data:
            # Add API data to the existing row
            combined_data = {**row.to_dict(), **api_data}
            results.append(combined_data)

    # Create a DataFrame from the results and save to an Excel file
    if results:
        result_df = pd.DataFrame(results)
        result_df.to_excel(output_file, index=False)
        print(f"Processed data saved to: {output_file}")
    else:
        print("No matching data found in the API for the provided crossroad_ids.")

# Main function
def main():
    # API configuration
    api_url = "http://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xSignalPhaseTimingInformation/1.0"
    api_key = load_api_key()
    
    # File paths
    input_file = "Filtered_Crossroad_Data.xlsx"
    output_file = "Processed_Crossroad_Data.xlsx"
    
    # Process the data
    process_crossroad_data(input_file, output_file, api_url, api_key)

if __name__ == "__main__":
    main()
