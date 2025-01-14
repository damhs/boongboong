// src/components/Map.jsx
import React, { useEffect, useRef } from "react";

function Map() {
  const mapRef = useRef(null); // 지도 div에 접근하기 위한 ref
  const mapInstanceRef = useRef(null); // Tmap 인스턴스를 저장할 ref

  useEffect(() => {
    // Tmap SDK 스크립트를 동적으로 로드하는 함수
    const loadTmapScript = () => {
      return new Promise((resolve, reject) => {
        // 이미 Tmapv2가 로드되어 있다면 바로 resolve
        if (window.Tmapv2) {
          resolve();
          return;
        }

        // Tmapv2 스크립트 태그 생성
        const script = document.createElement("script");
        script.src = "https://apis.openapi.sk.com/tmap/jsv2?version=1&appKey=API-KEY"; // 실제 API 키로 교체
        script.async = true;

        // 스크립트 로드 성공 시
        script.onload = () => {
          if (window.Tmapv2) {
            resolve();
          } else {
            reject(new Error("Tmapv2 라이브러리가 로드되지 않았습니다."));
          }
        };

        // 스크립트 로드 실패 시
        script.onerror = () => {
          reject(new Error("Tmapv2 스크립트 로드 실패"));
        };

        // head에 스크립트 태그 추가
        document.head.appendChild(script);
      });
    };

    // Tmap 초기화 함수
    const initTmap = () => {
      const { Tmapv2 } = window;

      if (!Tmapv2) {
        console.error("Tmapv2 라이브러리가 로드되지 않았습니다.");
        return;
      }

      const defaultLat = 37.566481622437934;
      const defaultLng = 126.98502302169841;

      // 지도 초기화
      const mapDiv = document.getElementById('map_div');

      // map_div가 이미 존재하면 초기화하지 않음
      if (!mapDiv.firstChild) {
        mapInstanceRef.current = new Tmapv2.Map(mapRef.current, {
          center: new Tmapv2.LatLng(defaultLat, defaultLng),
          width: "100%",
          height: "600px",
          zoom: 15,
          pitch: 45, // 지도 기울기 설정 (tilt)
      })};
    };

    // 스크립트 로드 후 지도 초기화
    loadTmapScript()
      .then(() => {
        initTmap();
      })
      .catch((error) => {
        console.error(error.message);
      });

    // Cleanup 함수 (필요 시 추가)
    return () => {
      // 예: 지도 인스턴스 정리 등
      if (mapInstanceRef.current) {
        mapInstanceRef.current.dispose(); // Tmapv2에서는 dispose 메서드가 있을 수 있습니다. 공식 문서 참고.
      }
    };
  }, []);

  return <div id="map_div" ref={mapRef} style={{ width: "100%", height: "600px" }} />;
}

export default Map;
