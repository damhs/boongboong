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

  // 최근 경로 선택
  const handleSelectPath = async (path) => {
    try{
      const response = await axios.get(`${baseurl}/paths/${path.pathID}`);
      const matchedPath = response.data;
      console.log("최근 경로 선택:", matchedPath);

      if (!matchedPath) {
        console.error("최근 경로에 해당하는 장소를 찾을 수 없습니다. placeID: ", path.pathID);
        return;
      }

      const originResponse = await axios.get(`${baseurl}/places/${matchedPath.originID}`);
      const destinationResponse = await axios.get(`${baseurl}/places/${matchedPath.destinationID}`);
      const originPlace = originResponse.data;
      const destinationPlace = destinationResponse.data;

      if (!originPlace || !destinationPlace) {
        console.error("경로에 해당하는 장소를 찾을 수 없습니다. originID:", matchedPath.originID, "destinationID:", matchedPath.destinationID);
        return;
      }

      localStorage.setItem("departure", JSON.stringify(originPlace));
      localStorage.setItem("arrival", JSON.stringify(destinationPlace));

      // navigate("/search", { state: { matchedPlace, placeType: "arrival" } });
    } catch (error) {
      console.error("일치하는 place를 찾을 수 없습니다. placeID:", path.destinationID, "오류 메시지:", error.message);
    }
  };

  // 최근 내역 삭제
  const handleItemDelete = async (itemId) => {
    try {
      await axios.delete(`${baseurl}/recents/${itemId}`);
      setRecentHistory((prevHistory) => prevHistory.filter((item) => item.id !== itemId));
      console.log("항목 삭제 완료:", itemId);
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

    if (location.state?.selectedPlace && location.state?.placeType && departure && arrival) {
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

  const addPath = async () => {
    const recentPath = await axios.get(`${baseurl}/paths/${userID}/recents`);
    // 경로가 이미 존재하는지 확인
    const existingPath = recentPath.data.find(
      (path) => path.originID === departure.placeID && path.destinationID === arrival.placeID
    );

    if (existingPath) {
      console.log("이미 존재하는 경로입니다:", existingPath);
      axios.put(`${baseurl}/paths/${existingPath.pathID}`, {
        updatedAt: new Date().toISOString()
      });
    }
    else {
      // path 등록
      try {
        const response = await axios.post(`${baseurl}/paths`, {
          userID: userID,
          originID: departure.placeID,
          destinationID: arrival.placeID,
        });
        console.log("경로가 등록되었습니다:", response.data);
      } catch (error) {
        console.error("경로 등록 중 오류 발생:", error);
      } 
    }


    // origin과 destination의 좌표 가져오기
    try {
      const start_response = await axios.get(`${baseurl}/places/${departure.placeID}`);
      const start_latitude = start_response.data.latitude;
      const start_longitude = start_response.data.longitude;

      const goal_response = await axios.get(`${baseurl}/places/${arrival.placeID}`);
      const goal_latitude = goal_response.data.latitude;
      const goal_longitude = goal_response.data.longitude;

      return { start_latitude, start_longitude, goal_latitude, goal_longitude };
    } catch (error) {
      console.error("좌표를 가져오는 중 오류 발생:", error);
    }
  }

  const handleFindPath = async () => {
    if (!departure || !arrival) {
      console.error("출발지와 도착지를 모두 입력하세요.");
      return;
    }

    console.log("길찾기를 시작합니다...");
    
    const { start_latitude, start_longitude, goal_latitude, goal_longitude } = await addPath();
    console.log("좌표:", start_latitude, start_longitude, goal_latitude, goal_longitude);

    try {
      const response = await axios.get(`/api/search-path`, {
        params: {
          start: `${start_longitude},${start_latitude}`,
          goal: `${goal_longitude},${goal_latitude}`
        }
      });

      const guide = response.data;

      if (guide.route && guide.route.traoptimal && guide.route.traoptimal.length > 0) {
        const guides = guide.route.traoptimal[0].guide;
        const paths = guide.route.traoptimal[0].path;

        if (guides && guides.length > 0 && Array.isArray(paths)) {
          guides.forEach((step, index) => {
            if (step.instructions && typeof step.pointIndex === "number") {
              if (step.pointIndex >= 0 && step.pointIndex < paths.length) {
                console.log(`Step ${index + 1}: ${step.instructions} (${paths[step.pointIndex][0]}, ${paths[step.pointIndex][1]})`);
              } else {
                console.log(`Step ${index + 1}: ${step.instructions} (잘못된 좌표 데이터)`);
              }
            } else {
              console.log(`Step ${index + 1}: 데이터 누락`);
            }
          });
        } else {
          console.log("길 안내 데이터가 없습니다.");
        }
      } else {
        console.log("경로 데이터가 없습니다.");
      }
    } catch (error) {
      console.error("경로를 찾는 중 오류 발생:", error);
    }
    // 
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
        onPathSelect={handleSelectPath}
        onItemDelete={handleItemDelete}
        onItemSelect={handleSelectRecent}
      />
    </div>
  );
};

export default Search;