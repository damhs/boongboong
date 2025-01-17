import React, { useEffect, useState } from "react";
import tmap_api_key from "../config_key";
// import { TMap } from '@/types';

const { Tmapv3 } = window;
const Map = () => {
  const [map, setMap] = useState(null); // 지도 객체를 상태로 관리
  const [socket, setSocket] = useState(null);
  const [currentPosition, setCurrentPosition] = useState({ latitude: 0, longitude: 0 });

  useEffect(() => {
    // Tmap 초기화 함수
    const initTmap = (latitude, longitude) => {
      const mapDiv = document.getElementById("map_div");
      if (!mapDiv.firstChild) {
        const tmap = new Tmapv3.Map("map_div", {
          center: new Tmapv3.LatLng(latitude, longitude), // 유저의 현재 위치
          width: "100%", // 지도의 넓이
          height: "800px", // 지도의 높이
          zoom: 17, // 지도 줌레벨
          pitch: 60,
        });
        setMap(tmap);

        // 마커 추가
        new Tmapv3.Marker({
          position: new Tmapv3.LatLng(latitude, longitude),
          map: tmap,
        });
      }
    };

    // const getRP = () => {
    //   var s_latlng = new Tmapv3.LatLng (37.553756, 126.925356);
    //   var e_latlng = new Tmapv3.LatLng (37.554034, 126.975598);
      
    //   var optionObj = {
    //     reqCoordType:"WGS84GEO", //요청 좌표계 옵셥 설정입니다.
    //     resCoordType:"WGS84GEO",  //응답 좌표계 옵셥 설정입니다.
    //     trafficInfo:"Y"
    //   };
      
    //   var params = {
    //     onComplete:onComplete,
    //     onProgress:onProgress,
    //     onError:onError
    //   };
      
    //   // TData 객체 생성
    //   var tData = new Tmapv3.extension.TData();
    
    //   // TData 객체의 경로요청 함수
    //   tData.getRoutePlanJson(s_latlng, e_latlng, optionObj, params);
    // }

    // const onComplete = () => {
    //   console.log(this._responseData); //json으로 데이터를 받은 정보들을 콘솔창에서 확인할 수 있습니다.
     
    //   var jsonObject = new Tmapv3.extension.GeoJSON();
    //   var jsonForm = jsonObject.rpTrafficRead(this._responseData);
    
    //   //교통정보 표출시 생성되는 LineColor 입니다.
    //       var trafficColors = {
      
    //           // 사용자가 임의로 색상을 설정할 수 있습니다.
    //           // 교통정보 옵션 - 라인색상
    //           trafficDefaultColor:"#000000", //교통 정보가 없을 때
    //           trafficType1Color:"#009900", //원할
    //           trafficType2Color:"#7A8E0A", //서행
    //           trafficType3Color:"#8E8111",  //정체
    //           trafficType4Color:"#FF0000"  //정체
    //       };
    //       jsonObject.drawRouteByTraffic(map, jsonForm, trafficColors);
    //   map.setCenter(new Tmapv3.LatLng(37.55676159947993,126.94734232774672));
    //   map.setZoom(13);
    // }

    // const onProgress = () => {}
    // const onError= () => {
    //   alert("onError");
    // }

    // 유저의 현재 위치 가져오기
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        setCurrentPosition({ latitude, longitude });
        initTmap(latitude, longitude); // 현재 위치를 기반으로 지도 초기화
        console.log("User Location:", latitude, longitude);
        // getRP();
        // console.log("getRP");
      },
      (error) => {
        console.error("Geolocation error:", error);
        // 기본 위치를 서울로 설정 (37.56520450, 126.98702028)
        setCurrentPosition({ latitude: 37.56520450, longitude: 126.98702028 });
        initTmap(37.56520450, 126.98702028);
        console.log("Fallback to default location: Seoul");
      }
    );

    // 0.1초마다 위치 전송
    const interval = setInterval(() => {
      if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify(currentPosition));
      }
    }, 100); // 0.1초

    // Tmap 스크립트 로드
    const script = document.createElement("script");
    script.src = `https://apis.openapi.sk.com/tmap/vectorjs?version=1&appKey=${tmap_api_key}`; // YOUR_APP_KEY를 실제 Tmap API 키로 대체
    script.async = true;
    document.body.appendChild(script);

    // 컴포넌트 언마운트 시 스크립트 제거
    return () => {
      document.body.removeChild(script);
      clearInterval(interval);
    };
  }, []);

  return (
    <div>
      <div id="map_div" style={{ width: "100%", height: "400px" }}></div>
    </div>
  );
};

export default Map;
