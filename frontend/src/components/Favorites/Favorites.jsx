import React, { useEffect, useRef, useState } from "react";
import styles from "./Favorites.module.css";
import FavoriteItem from "./FavoriteItem";
import { ReactComponent as EditIcon } from "../../assets/icons/edit.svg";


const Favorites = ({ favorites, onPlaceClick }) => {
  const scrollRef = useRef(null);
  const [isDragging, setIsDragging] = useState(false);
  const [startX, setStartX] = useState(0);
  const [scrollLeft, setScrollLeft] = useState(0);

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
        {favorites.map((item) => (
          <FavoriteItem key={item.favoriteID} place={item.favoriteName} onClick={onPlaceClick} />
        ))}
      </div>
    </div>
  )
};

export default Favorites;
