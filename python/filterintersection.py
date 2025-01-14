import pandas as pd

# Load the Excel file and filter rows
def filter_crossroad_ids(input_file, output_file):
    # Load the Excel file
    data = pd.read_excel(input_file)
    
    # Filter rows with non-null 'crossroad_id'
    filtered_data = data.dropna(subset=["crossroad_id"])
    
    # Save the filtered data to a new Excel file
    filtered_data.to_excel(output_file, index=False)
    print(f"Filtered data saved to: {output_file}")

# Main function
def main():
    input_file = "matched_traffic_lights_with_crossroads.xlsx"
    output_file = "Filtered_Crossroad_Data.xlsx"
    
    # Filter the data
    filter_crossroad_ids(input_file, output_file)

if __name__ == "__main__":
    main()
