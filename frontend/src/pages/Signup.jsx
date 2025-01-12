import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from "./Login.module.css";
import { ReactComponent as Logo } from "../assets/icons/logo.svg";


const Signup = () => {
  return (
    <div className={styles.container}>
        <Logo className={styles.logo}/>
        <div className={styles.title}>Sign up</div>
        <form className={styles.inputform}>
            <input
                type="text"
                placeholder="아이디"
                className={styles.input}
            />
            <input
                type="password"
                placeholder="비밀번호"
                className={styles.input}
            />
            <input
                type="password"
                placeholder="비밀번호 확인"
                className={styles.input}
                
            />
            <button type="submit" className={styles.loginButton}>
                회원가입
            </button>
        </form>

    </div>
  );
};

export default Signup;
