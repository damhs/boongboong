// src/components/Map.jsx
import React, { useEffect, useRef } from 'react';

function Map() {
  const mapRef = useRef(null);
  const mapInstanceRef = useRef(null);
  const carMarkerRef = useRef(null); // 차량 마커를 저장할 ref
  const carIconUrl = "차량아이콘.png"; // 차량 아이콘 이미지 경로

  useEffect(() => {
    // 지도 초기화 함수
    const initMap = (lat, lng) => {
      if (window.naver?.maps) {
        const mapOptions = {
          center: new window.naver.maps.LatLng(lat, lng),
          zoom: 16, // 네비게이션 느낌을 위한 확대 레벨
        };
        mapInstanceRef.current = new window.naver.maps.Map(mapRef.current, mapOptions);

        // 차량 마커 초기화
        carMarkerRef.current = new window.naver.maps.CustomOverlay({
          position: new window.naver.maps.LatLng(lat, lng),
          content: `
            <div id="carIcon" style="
              width: 32px; 
              height: 32px; 
              background: url('${carIconUrl}') no-repeat center; 
              background-size: contain;
              transform: rotate(0deg);
            "></div>
          `,
          zIndex: 1,
          map: mapInstanceRef.current,
        });
      }
    };

    // 사용자 위치를 얻는 함수
    const getUserLocation = () => {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            const { latitude, longitude } = position.coords;
            initMap(latitude, longitude);  // 지도 초기화
            // 실시간 차량 위치 업데이트 시작
            startRealtimeUpdates(latitude, longitude);
          },
          (error) => {
            console.error('Error getting location:', error);
            // 위치 가져오기 실패 시, 기본 좌표(예: 서울시청)로 설정
            initMap(37.5666102, 126.9783881);
            // 실시간 차량 위치 업데이트 시작 (기본 위치)
            startRealtimeUpdates(37.5666102, 126.9783881);
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
        // 실시간 차량 위치 업데이트 시작 (기본 위치)
        startRealtimeUpdates(37.5666102, 126.9783881);
      }
    };

    // 실시간 차량 위치 업데이트 함수 (WebSocket 사용 예시)
    const startRealtimeUpdates = (initialLat, initialLng) => {
      // WebSocket 연결 설정 (실제 서버 주소로 변경 필요)
      const socket = new WebSocket('wss://your-websocket-server.com');

      socket.onopen = () => {
        console.log('WebSocket connection established');
        // 필요 시 서버에 초기 데이터 요청
      };

      socket.onmessage = (event) => {
        // 서버로부터 차량 위치 데이터 수신 (예: JSON 형식)
        try {
          const data = JSON.parse(event.data);
          const { latitude, longitude, heading } = data;

          updateCarPosition(latitude, longitude, heading);
        } catch (err) {
          console.error('Error parsing WebSocket message:', err);
        }
      };

      socket.onerror = (error) => {
        console.error('WebSocket error:', error);
      };

      socket.onclose = () => {
        console.log('WebSocket connection closed');
        // 재연결 로직 추가 가능
      };
    };

    // 차량 위치 및 헤딩 업데이트 함수
    const updateCarPosition = (lat, lng, heading) => {
      if (mapInstanceRef.current && carMarkerRef.current) {
        const newPosition = new window.naver.maps.LatLng(lat, lng);

        // 지도 중심 이동
        mapInstanceRef.current.setCenter(newPosition);

        // 차량 마커 위치 업데이트
        carMarkerRef.current.setPosition(newPosition);

        // 차량 아이콘 회전
        const carIconEl = document.getElementById("carIcon");
        if (carIconEl) {
          carIconEl.style.transform = `rotate(${heading}deg)`;
        }
      }
    };

    // 실제 위치 정보 가져오기 및 지도 초기화
    getUserLocation();

    // 컴포넌트 언마운트 시 WebSocket 연결 종료
    return () => {
      // WebSocket 연결 종료 로직 필요
      // 예: socket.close();
    };
  }, []);

  return (
    <div
      ref={mapRef}
      style={{ width: '100%', height: '600px' }}
    ></div>
  );
}

export default Map;
