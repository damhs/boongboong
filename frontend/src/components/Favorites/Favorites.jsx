import React, { useEffect, useRef, useState } from "react";
import styles from "./Favorites.module.css";
import FavoriteItem from "./FavoriteItem";
import { ReactComponent as EditIcon } from "../../assets/icons/edit.svg";
// import axios from "axios";
// import config from "../../config";

// const baseurl = config.backendUrl;


const Favorites = ({ favorites, onPlaceClick }) => {
  const scrollRef = useRef(null);
  const [isDragging, setIsDragging] = useState(false);
  const [startX, setStartX] = useState(0);
  const [scrollLeft, setScrollLeft] = useState(0);
  // const [favorites, setFavorites] = useState([]);

  const handleMouseDown = (e) => {
    setIsDragging(true);
    setStartX(e.pageX - scrollRef.current.offsetLeft);
    setScrollLeft(scrollRef.current.scrollLeft);
  };

  const handleMouseMove = (e) => {
    if (!isDragging) return;
    const x = e.pageX - scrollRef.current.offsetLeft;
    const walk = (x - startX) * 1; // 드래그 속도 조정
    scrollRef.current.scrollLeft = scrollLeft - walk;
  };

  const handleMouseUp = () => {
    setIsDragging(false);
  };

  // API 호출
  // const callFavorites = async () => {
  //   try {
  //     const res = await axios.get(`${baseurl}/favorites/${userID}`);
  //     setFavorites(res.data); // API에서 받은 데이터를 state에 저장
  //   } catch (error) {
  //     console.error("Failed to get favorites:", error);
  //   }
  // };

  // // 컴포넌트가 처음 렌더링될 때 또는 userID가 변경될 때 데이터 가져오기
  // useEffect(() => {
  //   if (userID) {
  //     callFavorites();
  //   }
  // }, [userID]);

  return(
    <div className={styles.container}>
      <div className={styles.label}>
        즐겨찾는 장소
        <button className={styles.editButton}>
          <EditIcon/>
        </button>
      </div>
      <div
        className={styles.scrollContainer}
        ref={scrollRef}
        onMouseDown={handleMouseDown}
        onMouseMove={handleMouseMove}
        onMouseUp={handleMouseUp}
        onMouseLeave={handleMouseUp} // 마우스가 벗어날 때 드래그 종료
      >
        {favorites.map((place, index) => (
          <FavoriteItem key={index} place={place} onClick={onPlaceClick} />
        ))}
      </div>
    </div>
  )
};

export default Favorites;
