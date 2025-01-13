// Search.jsx
import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import Header from "../components/Header/Header";
import SearchBar from "../components/SearchBar/SearchBar";
import ArrivalTime from "../components/ArrivalTime/ArrivalTime";
import Favorites from "../components/Favorites/Favorites";
import RecentHistory from "../components/RecentHistory/RecentHistory";

function Search() {
  const navigate = useNavigate();
  const location = useLocation();

  const [departure, setDeparture] = useState(localStorage.getItem("departure") || '');
  const [arrival, setArrival] = useState(localStorage.getItem("arrival") || '');

  const [favorites, setFavorites] = useState(["집", "기숙사", "회사"]);

  const [recentPath, setRecentPath] = useState([
    { id: 1, start: "대전역", end: "세종관" },
    { id: 2, start: "카이스트 택시 승강장", end: "대전역" }
  ]);

  const [recentHistory, setRecentHistory] = useState([
    { id: 1, name: "집", address: "대전광역시 유성구 대학로 291" },
    { id: 2, name: "대전역", address: "대전 동구 중앙로 215" },
    { id: 3, name: "카이스트 택시 승강장", address: "대전 동구 중앙로 215" },
  ]);

  // const userID = "0000";

  const handlePathDelete = (pathId) => {
    setRecentPath((prevPaths) => prevPaths.filter((p) => p.id !== pathId));
  };

  const handleItemDelete = (itemId) => {
    setRecentHistory((prevHistory) => prevHistory.filter((item) => item.id !== itemId));
  };
  
  const handleSelectRecent = (item) => {
    // 최근 내역 중 하나 선택 시 → 첫 번째 화면으로 돌아가면서 전달
    const selectedPlace = item.name;
    if (location.state?.placeType === 'departure') {
      setDeparture(selectedPlace);
      localStorage.setItem("departure", selectedPlace);
    } else if (location.state?.placeType === 'arrival') {
      setArrival(selectedPlace);
      localStorage.setItem("arrival", selectedPlace);
    }
    navigate('/search', { state: { selectedPlace, placeType: location.state?.placeType } });
  };

  useEffect(() => {
    if (location.state?.selectedPlace && location.state?.placeType) {
      const { selectedPlace, placeType } = location.state;

      if (placeType === 'departure') {
        setDeparture(selectedPlace);
        localStorage.setItem("departure", selectedPlace);
      } else if (placeType === 'arrival') {
        setArrival(selectedPlace);
        localStorage.setItem("arrival", selectedPlace);

      }
    } else if (location.state?.searchInput) {
      const { searchInput, placeType } = location.state;

      if (placeType === 'departure') {
        setDeparture(searchInput);
        localStorage.setItem("departure", searchInput);
      } else if (placeType === 'arrival') {
        setArrival(searchInput);
        localStorage.setItem("arrival", searchInput);
      }
    }

    // 필요하다면 location.state를 초기화
    // navigate('.', { replace: true, state: {} });
  }, [location.state, navigate]);

  const handleInputClick = (type) => {
    console.log(`Navigating to /search-place with placeType: ${type}`);
    navigate('/search-place', {
      state: { ...location.state, placeType: type },
    });
  };

  const fetchCoordinates = async (address, type) => {
    window.naver.maps.Service.geocode(
      {query: address},
      (status, response) => {
        if (status !== window.naver.maps.Service.Status.OK) {
          console.error(`${type} 좌표 변환 실패: ${status}`);
          return;
        }

        const item = response.v2.addresses[0];
        if (!item) {
          console.error(`${type} 좌표를 찾을 수 없습니다.`);
          return;
        }

        console.log(`${type} 좌표: 위도(${item.y}), 경도(${item.x})`);
      }
    )
  };

  const handleFindPath = async () => {
    if (!departure || !arrival) {
      console.error("출발지와 도착지를 모두 입력하세요.");
      return;
    }

    console.log("길찾기를 시작합니다...");
    await fetchCoordinates(departure, "출발지");
    await fetchCoordinates(arrival, "도착지");
  };

  return (
    <div style={{ padding: "16px", fontFamily: "Arial, sans-serif" }}>
      <Header />
      <SearchBar 
        departure={departure}
        arrival={arrival}
        onDepartureClick={() => handleInputClick('departure')}
        onArrivalClick={() => handleInputClick('arrival')}
        onButtonClick={handleFindPath}
      />
      <ArrivalTime />
      <Favorites favorites={favorites} />
      <RecentHistory
        recentPath={recentPath}
        recentHistory={recentHistory}
        onPathDelete={handlePathDelete}
        onItemDelete={handleItemDelete}
        onItemSelect={handleSelectRecent}
      />
    </div>
  );
};

export default Search;