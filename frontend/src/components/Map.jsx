import React, { useEffect, useState } from "react";

const Map = () => {
  const [map, setMap] = useState(null); // 지도 객체를 상태로 관리

  useEffect(() => {
    // Tmap 초기화 함수
    const initTmap = (latitude, longitude) => {
      const mapDiv = document.getElementById("map_div");
      if (!mapDiv.firstChild) {
        const tmap = new window.Tmapv3.Map("map_div", {
          center: new window.Tmapv3.LatLng(latitude, longitude), // 유저의 현재 위치
          width: "100%", // 지도의 넓이
          height: "100%", // 지도의 높이
          zoom: 17, // 지도 줌레벨
          pitch: 60,
        });
        setMap(tmap);

        // 마커 추가
        new window.Tmapv3.Marker({
          position: new window.Tmapv3.LatLng(latitude, longitude),
          map: tmap,
        });
      }
    };

    // 유저의 현재 위치 가져오기
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        initTmap(latitude, longitude); // 현재 위치를 기반으로 지도 초기화
        console.log("User Location:", latitude, longitude);
      },
      (error) => {
        console.error("Geolocation error:", error);
        // 기본 위치를 서울로 설정 (37.56520450, 126.98702028)
        initTmap(37.56520450, 126.98702028);
        console.log("Fallback to default location: Seoul");
      }
    );

    // Tmap 스크립트 로드
    const script = document.createElement("script");
    script.src = "https://apis.openapi.sk.com/tmap/vectorjs?version=1&appKey="; // YOUR_APP_KEY를 실제 Tmap API 키로 대체
    script.async = true;
    document.body.appendChild(script);

    // 컴포넌트 언마운트 시 스크립트 제거
    return () => {
      document.body.removeChild(script);
    };
  }, []);

  return (
    <div>
      <div id="map_div" style={{ width: "100%", height: "400px" }}></div>
    </div>
  );
};

export default Map;
