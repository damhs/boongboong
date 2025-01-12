// src/components/Map.jsx
import React, { useEffect, useRef } from 'react';

function Map() {
  const mapRef = useRef(null);

  const mapInstanceRef = useRef(null);

  useEffect(() => {
    // 사용자 위치를 얻는 함수
    const getUserLocation = () => {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            const { latitude, longitude } = position.coords;
            initMap(latitude, longitude);  // 지도 초기화
          },
          (error) => {
            console.error('Error getting location:', error);
            // 위치 가져오기 실패 시, 기본 좌표(예: 서울시청)로 설정
            initMap(37.5666102, 126.9783881);
          },
          {
            enableHighAccuracy: false,
            timeout: 5000,
            maximumAge: 0
          }
        );
      } else {
        // Geolocation을 지원하지 않는 브라우저인 경우
        console.warn('Geolocation is not supported by this browser.');
        initMap(37.5666102, 126.9783881);
      }
    };

    // 지도를 초기화하는 함수
    const initMap = (lat, lng) => {
      if (window.naver?.maps) {
        const mapOptions = {
          center: new window.naver.maps.LatLng(lat, lng),
          zoom: 14
        };
        // mapRef.current가 가리키는 <div>에 지도를 생성
        mapInstanceRef.current = new window.naver.maps.Map(mapRef.current, mapOptions);
      }
    };

    // 실제 위치 정보 가져오기
    getUserLocation();
  }, []);

  return (
    <div
      ref={mapRef}
      style={{ width: '100%', height: '600px' }}
    ></div>
  );
}

export default Map;
