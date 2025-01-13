// SearchBar.jsx
import React from "react";
import styles from "./SearchBar.module.css";
import { IoSearch } from "react-icons/io5";
import { ReactComponent as FindPathIcon } from "../../assets/icons/findpath_icon.svg";


const SearchBar = ({ departure, arrival, onDepartureClick, onArrivalClick, onButtonClick }) => (
  <div className={styles.container}>
    <div className={styles.inputWrapper}>
      <div className={styles.inputContainer}>
        <input 
          type="text" 
          placeholder="출발지 입력" 
          className={styles.input} 
          value={departure}
          onClick={onDepartureClick}
          readOnly
        />
        <IoSearch className={styles.searchIcon}/>
      </div>
      <div className={styles.inputContainer}>
      <input 
          type="text" 
          placeholder="도착지 입력" 
          className={styles.input} 
          value={arrival}
          onClick={onArrivalClick}
          readOnly
        />
        <IoSearch className={styles.searchIcon}/>
      </div>
    </div>
    <button className={styles.navigateButton} onClick={onButtonClick}>
      <FindPathIcon className={styles.navigationIcon} />
      <span>길찾기</span>
    </button>
  </div>
);

export default SearchBar;
