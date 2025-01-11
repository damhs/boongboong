import React from "react";
import styles from "./SearchBar.module.css";
import { IoSearch } from "react-icons/io5";


const SearchBar = () => (
  <div className={styles.container}>
    <div className={styles.inputContainer}>
      <input type="text" placeholder="출발지 입력" className={styles.input} />
      <IoSearch className={styles.searchIcon}/>
    </div>
    <div className={styles.inputContainer}>
      <input type="text" placeholder="도착지 입력" className={styles.input} />
      <IoSearch className={styles.searchIcon}/>
    </div>
  </div>
);

export default SearchBar;
