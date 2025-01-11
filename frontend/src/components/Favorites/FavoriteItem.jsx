import React from "react";
import styles from "./FavoriteItem.module.css";
import { LuStar } from "react-icons/lu";

const FavoriteItem = ({ place, onClick }) => (
  <button className={styles.favoriteButton} onClick={() => onClick(place)}>
    <LuStar className={styles.starIcon}/>
    {place}
  </button>
);

export default FavoriteItem;
