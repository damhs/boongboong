// SearchPlace.jsx
import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import styles from "../components/SearchBar/SearchBar.module.css";
import { IoSearch } from "react-icons/io5";
import RecentHistory from "../components/RecentHistory/RecentHistory";

function SearchPlace() {
  const navigate = useNavigate();
  const location = useLocation();
  const placeType = location.state?.placeType;
  const [searchInput, setSearchInput] = useState('');
  

  // 가정: 실제로는 localStorage, DB 등에서 받아온 최근 내역 목록
  const [recentHistory, setRecentHistory] = useState([
      { id: 1, name: "집", address: "대전광역시 유성구 대학로 291" },
      { id: 2, name: "대전역", address: "대전 동구 중앙로 215" },
      { id: 3, name: "카이스트 택시 승강장", address: "대전 동구 중앙로 215" },
    ]);

  const handleSelectRecent = (item) => {
    // 최근 내역 중 하나 선택 시 → 첫 번째 화면으로 돌아가면서 전달
    const selectedPlace = item.name;

    if (placeType === "departure") {
      localStorage.setItem("departure", selectedPlace);
    } else if (placeType === "arrival") {
      localStorage.setItem("arrival", selectedPlace);
    }

    navigate('/search', { state: { selectedPlace, placeType } });
  };


  const handleSearch = () => {
    // 세 번째 화면으로 이동하면서 검색 키워드 전달
    // navigate('/search-result', {
    //   state: { keyword: searchInput },
    // });
    if (placeType === "departure") {
      localStorage.setItem("departure", searchInput);
    } else if (placeType === "arrival") {
      localStorage.setItem("arrival", searchInput);
    }

    navigate('/search', {
      state: { 
        searchInput: searchInput,
        placeType: placeType 
      },
    });
  };

  const handleItemDelete = (itemId) => {
    setRecentHistory((prevHistory) => prevHistory.filter((item) => item.id !== itemId));
  };

  return (
    <div>
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
          <IoSearch 
            className={styles.searchIcon} 
            onClick={handleSearch}
          />
        </div>

      </div>
      <div style={{marginLeft: "3vw", marginRight: "3vw"}}>
        <RecentHistory
          recentHistory={recentHistory}
          onItemDelete={handleItemDelete}
          onItemSelect={handleSelectRecent}
          style={{marginLeft: "3vw", marginRight: "3vw"} 
        }/>
      </div>
    </div>
  );
}

export default SearchPlace;
