import React, { useEffect } from "react";

const Map = () => {
  useEffect(() => {
    // Tmap 초기화 함수
    const initTmap = () => {
      // Tmapv3.Map을 사용하여 지도 생성
      const mapDiv = document.getElementById("map_div");
      if (!mapDiv.firstChild) {
        const map = new window.Tmapv3.Map("map_div", {
          center: new window.Tmapv3.LatLng(37.56520450, 126.98702028), // 초기 좌표
          width: "100%", // 지도의 넓이
          height: "400px", // 지도의 높이
          zoom: 17, // 지도 줌레벨
          pitch: 60
        });
      }
    };

    // Tmap 스크립트 로드
    const script = document.createElement("script");
    script.src = "https://apis.openapi.sk.com/tmap/vectorjs?version=1&appKey=";
    script.async = true;
    script.onload = initTmap; // 스크립트 로드 후 initTmap 호출
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
