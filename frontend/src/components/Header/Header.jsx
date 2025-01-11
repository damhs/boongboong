import React from "react";
import {Link} from "react-router-dom"
import styles from "./Header.module.css";
import { ReactComponent as BackIcon } from "../../assets/icons/back.svg";

const Header = () => (
  <div className={styles.header}>
    <Link to="/" className={styles.backButton} aria-label="Go back">
      <BackIcon />
    </Link>
  </div>
);

export default Header;
