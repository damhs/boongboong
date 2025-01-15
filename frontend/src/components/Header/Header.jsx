import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Header.module.css";
import { ReactComponent as BackIcon } from "../../assets/icons/back.svg";

const Header = () => {
  const navigate = useNavigate();

  const goBack = () => {
    navigate(-1);  // 직전 페이지로 이동
  };

  return (
    <div className={styles.header}>
      <button onClick={goBack} className={styles.backButton} aria-label="Go back">
        <BackIcon />
      </button>
    </div>
  );
};

export default Header;
