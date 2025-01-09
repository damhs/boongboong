import axios from 'axios';

const BASE_URL = 'https://api.seoul.go.kr/traffic';

export const fetchTrafficData = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/real-time-data`, {
      params: { key: 'YOUR_API_KEY' },
    });
    return response.data;
  } catch (error) {
    console.error('Failed to fetch traffic data:', error);
    throw error;
  }
};
