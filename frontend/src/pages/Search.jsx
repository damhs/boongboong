// Search.jsx
import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import axios from "axios";
import Header from "../components/Header/Header";
import SearchBar from "../components/SearchBar/SearchBar";
import ArrivalTime from "../components/ArrivalTime/ArrivalTime";
import Favorites from "../components/Favorites/Favorites";
import RecentHistory from "../components/RecentHistory/RecentHistory";
import config from "../config";

const baseurl = config.backendUrl;

function Search() {
  const navigate = useNavigate();
  const location = useLocation();

  const userID = "1efd1a4b-706a-6e71-a44d-e7b1f23b2697";

  const storedDeparture = localStorage.getItem("departure");
  const storedArrival = localStorage.getItem("arrival");
  const initialDeparture = storedDeparture ? JSON.parse(storedDeparture) : null;
  const initialArrival = storedArrival ? JSON.parse(storedArrival) : null;
  const [departure, setDeparture] = useState(initialDeparture);
  const [arrival, setArrival] = useState(initialArrival);

  const [favorites, setFavorites] = useState([]);
  const [recentPath, setRecentPath] = useState([]);
  const [recentHistory, setRecentHistory] = useState([]);

  // favorites 가져오기
  const fetchFavorites = async () => {
    try {
      const response = await axios.get(`${baseurl}/favorites/${userID}`);
      setFavorites(response.data);
    } catch (error) {
      console.error("즐겨찾기 데이터를 가져오는 중 오류 발생:", error);
    }
  };

  // 최근 경로 데이터 가져오기
  const fetchRecentPaths = async () => {
    try {
      const response = await axios.get(`${baseurl}/paths/${userID}/recents`);
      setRecentPath(response.data);
    } catch (error) {
      console.error("최근 경로 데이터를 가져오는 중 오류 발생:", error);
    }
  };

  // 최근 내역 데이터 가져오기
  const fetchRecentHistory = async () => {
    try {
      const response = await axios.get(`${baseurl}/recents/${userID}`);
      setRecentHistory(response.data);
    } catch (error) {
      console.error("최근 내역 데이터를 가져오는 중 오류 발생:", error);
    }
  }; 

  // 경로 삭제
  const handlePathDelete = async (pathId) => {
    try {
      await axios.delete(`${baseurl}/paths/${pathId}`);
      setRecentPath((prevPaths) => prevPaths.filter((p) => p.id !== pathId));
    } catch (error) {
      console.error("경로 삭제 중 오류 발생:", error);
    }
  };

  // 최근 내역 삭제
  const handleItemDelete = async (itemId) => {
    try {
      await axios.delete(`${baseurl}/recents/${itemId}`);
      setRecentHistory((prevHistory) => prevHistory.filter((item) => item.id !== itemId));
    } catch (error) {
      console.error("최근 내역 삭제 중 오류 발생:", error);
    }
  };
  
  // 최근 내역 선택
  const handleSelectRecent = async (recentItem) => {
    try {
      console.log('location.state?.placeType:', location.state?.placeType);
      const response = await axios.get(`${baseurl}/places/${recentItem.placeID}`);
      const matchedPlace = response.data;
      console.log("최근 내역 선택:", matchedPlace);

      if (!matchedPlace) {
        console.error("최근 내역에 해당하는 장소를 찾을 수 없습니다. placeID: ", recentItem.placeID);
        return;
      }

      const placeType = location.state?.placeType ?? "arrival";

      console.log("placeType:", placeType);
      if (placeType === "departure") {
        setDeparture(matchedPlace);
        localStorage.setItem("departure", JSON.stringify(matchedPlace));
        console.log("출발지:", matchedPlace);
      } else if (placeType === "arrival") {
        setArrival(matchedPlace);
        localStorage.setItem("arrival", JSON.stringify(matchedPlace));
        console.log("도착지:", matchedPlace);
      } else{
        console.error("placeType이 올바르지 않습니다. placeType:", location.state?.placeType);
      }

      navigate("/search", { state: { matchedPlace, placeType: location.state?.placeType } });
  } catch (error) {
      console.error(
        "일치하는 place를 찾을 수 없습니다. placeID:",
        recentItem.placeID,
        "오류 메시지:",
        error.message
      );
    }
  };

  useEffect(() => {
    fetchFavorites();
    fetchRecentPaths();
    fetchRecentHistory();

    if (location.state?.selectedPlace && location.state?.placeType) {
      const { selectedPlace, placeType } = location.state;

      if (placeType === 'departure') {
        setDeparture(selectedPlace);
        localStorage.setItem("departure", JSON.stringify(selectedPlace));
      } else if (placeType === 'arrival') {
        setArrival(selectedPlace);
        localStorage.setItem("arrival", JSON.stringify(selectedPlace));

      }
    } else if (location.state?.searchInput) {
      const { searchInput, placeType } = location.state;

      if (placeType === 'departure') {
        setDeparture(searchInput);
        localStorage.setItem("departure", JSON.stringify(searchInput));
      } else if (placeType === 'arrival') {
        setArrival(searchInput);
        localStorage.setItem("arrival", JSON.stringify(searchInput));
      }
    }
  }, [location.state, navigate]);

  const handleInputClick = (type) => {
    console.log(`Navigating to /search-place with placeType: ${type}`);
    navigate('/search-place', {
      state: { ...location.state, placeType: type },
    });
  };

  // const fetchCoordinates = async (address, type) => {
  //   window.naver.maps.Service.geocode(
  //     {query: address},
  //     (status, response) => {
  //       if (status !== window.naver.maps.Service.Status.OK) {
  //         console.error(`${type} 좌표 변환 실패: ${status}`);
  //         return;
  //       }

  //       const item = response.v2.addresses[0];
  //       if (!item) {
  //         console.error(`${type} 좌표를 찾을 수 없습니다.`);
  //         return;
  //       }

  //       console.log(`${type} 좌표: 위도(${item.y}), 경도(${item.x})`);
  //     }
  //   );
  // };

  const handleFindPath = async () => {
    if (!departure || !arrival) {
      console.error("출발지와 도착지를 모두 입력하세요.");
      return;
    }

    console.log("길찾기를 시작합니다...");

    try {
      const response = await axios.post(`${baseurl}/paths`, {
        userID,
        originID: departure,
        destinationID: arrival,
      });
      console.log("최단 경로:", response.data);
    } catch (error) {
      console.error("경로 찾기 중 오류 발생:", error);
    } 


    // departure/arrival이 객체인 경우, .name 처럼 주소 필드를 꺼내서 사용해야 할 수도 있음
    // 아래 예시에서는 그냥 departure/arrival을 문자열 주소라 가정
    // await fetchCoordinates(departure, "출발지");
    // await fetchCoordinates(arrival, "도착지");
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