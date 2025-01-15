// SearchPlace.jsx
import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import styles from "../components/SearchBar/SearchBar.module.css";
import { IoSearch } from "react-icons/io5";
import Header from "../components/Header/Header";
import RecentHistory from "../components/RecentHistory/RecentHistory";
import SearchResult from "../components/SearchResult/SearchResult";
import config from "../config";

const baseurl = config.backendUrl;

function SearchPlace() {
  const navigate = useNavigate();
  const location = useLocation();
  const placeType = location.state?.placeType;
  const userID = "1efd1a4b-706a-6e71-a44d-e7b1f23b2697";

  const [searchInput, setSearchInput] = useState('');
  const [recentHistory, setRecentHistory] = useState([]);
  const [searchResults, setSearchResults] = useState([]);
  const [activeComponent, setActiveComponent] = useState("Recent");

  const fetchRecentHistory = async () => {
    try {
      const response = await axios.get(`${baseurl}/recents/${userID}`);
      setRecentHistory(response.data);
    } catch (error) {
      console.error("Failed to fetch recent history:", error);
    }
  };

  const handleSelectRecent = async (recentItem) => {
    // 최근 내역 중 하나 선택 시 → 첫 번째 화면으로 돌아가면서 전달
    console.log('location.state?.placeType:', location.state?.placeType);
    const response = await axios.get(`${baseurl}/places/${recentItem.placeID}`);
    const matchedPlace = response.data;
    console.log("최근 내역 선택:", matchedPlace);
    
    if (placeType === "departure") {
      localStorage.setItem("departure", JSON.stringify(matchedPlace));
    } else if (placeType === "arrival") {
      localStorage.setItem("arrival", JSON.stringify(matchedPlace));
    }

    navigate('/search', { state: { matchedPlace, placeType } });
  };


  const handleSearch = async () => {
    try{
      const response = await axios.get('/api/search-place', {
        params: {
          text: searchInput
        }
      });
      
      setSearchResults(response.data.items);

      if (placeType === "departure") {
        localStorage.setItem("departure", JSON.stringify(searchInput));
      } else if (placeType === "arrival") {
        localStorage.setItem("arrival", JSON.stringify(searchInput));
      }

      setActiveComponent("Search");

    } catch (error) {
      console.error("Failed to search:", error);
    }
  };

  const stripHtmlTags = (html) => {
    return html.replace(/<\/?[^>]+(>|$)/g, ""); // HTML 태그 제거
  };

  const fetchCoordinates = (address, type) => {
    return new Promise((resolve, reject) => {
      window.naver.maps.Service.geocode(
        { query: address },
        (status, response) => {
          if (status !== window.naver.maps.Service.Status.OK) {
            console.error(`${type} 좌표 변환 실패: ${status}`);
            reject(new Error(`${type} 좌표 변환 실패: ${status}`));
            return;
          }
  
          const item = response.v2.addresses[0];
          if (!item) {
            console.error(`${type} 좌표를 찾을 수 없습니다.`);
            reject(new Error(`${type} 좌표를 찾을 수 없습니다.`));
            return;
          }
  
          console.log(`${type} 좌표: 위도(${item.y}), 경도(${item.x})`);
          resolve({ latitude: parseFloat(item.y), longitude: parseFloat(item.x) });
        }
      );
    });
  };

  const handleSelect = async (selectedPlace) => {
    // 출발지 또는 도착지 여부를 함께 전달
    console.log("SELECTED PLACE", selectedPlace)
    const title = stripHtmlTags(selectedPlace.title);
    try {
      const places = await axios.get(`${baseurl}/places`);
      var existingPlace = places.data.find((place) => place.placeName === title);
      if (existingPlace) {
        console.log("이미 추가된 장소입니다.: ", existingPlace);
      }
      else {
        const { latitude, longitude } = await fetchCoordinates(selectedPlace.roadAddress, selectedPlace.title);

        // place 추가
        const response = await axios.post(`${baseurl}/places`, {
          placeType: "SPOT",
          placeName: stripHtmlTags(selectedPlace.title),
          latitude : latitude,
          longitude : longitude,
          etc: stripHtmlTags(selectedPlace.roadAddress)
        });

        existingPlace = response.data;
        console.log("추가된 장소:", existingPlace);
      }

      const recents = await axios.get(`${baseurl}/recents/${userID}`);
      var recentEntry = recents.data.find((recent) => recent.placeID === existingPlace.placeID);

      if (recentEntry) {
        const recentID = recentEntry.recentID;
        const putInfo = await axios.delete(`${baseurl}/recents/${recentID}`);
        console.log(`최근 내역 삭제 완료: ${putInfo.data}`);
      }
    
      // recents에 추가
      const postInfo = await axios.post(`${baseurl}/recents`, {
        userID: userID,
        placeID: existingPlace.placeID
      });
      console.log(`최근 내역 추가 완료: ${postInfo.data}`);
    
  
      // if (recents.data.some((recent) => recent.placeID === selectedPlace.placeID)) {
      //   const recentID = recents.data.find((recent) => recent.placeID === selectedPlace.placeID).recentID;
      //   const putInfo = await axios.put(`${baseurl}/recents/${recentID}`, {
      //     updatedAt: new Date().toISOString()
      //   });
      //   console.log(`최근 내역 갱신 완료: ${putInfo.data}`);
      // } else {
      //   // recents에 추가
      //   const postInfo = await axios.post(`${baseurl}/recents`, {
      //     userID: userID,
      //     placeID: selectedPlace.placeID
      //   });
      //   console.log(`최근 내역 추가 완료: ${postInfo.data}`);
      // };


      // localStorage에 저장
      if (placeType === "departure") {
        localStorage.setItem("departure", JSON.stringify(existingPlace));
      } else if (placeType === "arrival") {
        localStorage.setItem("arrival", JSON.stringify(existingPlace));
      }

      navigate("/search", {
        state: { existingPlace, placeType: placeType }, // "departure" 또는 "arrival" 설정
      });
    } catch (error) {
      console.error("Failed to update recent history:", error);
    }
  };

  const handleItemDelete = (itemId) => {
    setRecentHistory((prevHistory) => prevHistory.filter((item) => item.id !== itemId));
  };

  useEffect(() => {
    fetchRecentHistory();
  }, []);

  return (
    <div style={{ padding: "16px", fontFamily: "Arial, sans-serif" }}>
      <Header />
      <div className={styles.inputWrapper} 
        style={{marginTop: "20px", marginBottom: "20px", marginLeft: "3vw", marginRight: "3vw"} 
      }>  
        <div className={styles.inputContainer}>
          <input
            type="text"
            placeholder= {placeType==="departure"?"출발지 입력":"도착지 입력"}
            className={styles.input}
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
          />
          <IoSearch className={styles.searchIcon} onClick={handleSearch}/>
        </div>
      </div>
      <div style={{marginLeft: "3vw", marginRight: "3vw"}}>
      {activeComponent === "Recent" && (
          <RecentHistory
            recentHistory={recentHistory}
            onItemDelete={handleItemDelete}
            onItemSelect={handleSelectRecent}
            style={{marginLeft: "3vw", marginRight: "3vw"} 
          }/>
        )}
        {activeComponent === "Search" && 
          <SearchResult searchResults={searchResults} onSelect={handleSelect}/>}
      </div>
    </div>
  );
}

export default SearchPlace;
