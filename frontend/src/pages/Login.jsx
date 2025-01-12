import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from "./Login.module.css";
import { ReactComponent as Logo } from "../assets/icons/logo.svg";


const Login = () => {
    const signupNavigation = useNavigate()
    const handleButtonClick = () => {
        signupNavigation('/signup');
      };

    return (
        <div className={styles.container}>
            <Logo className={styles.logo}/>
            <div className={styles.title}>Login</div>
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
            </form>
            <form className={styles.form}>
            <button type="submit" className={styles.loginButton}>
                로그인
            </button>
            <button
                type="button"
                className={styles.signupButton}
                onClick={handleButtonClick}
            >
                회원가입
            </button>
            </form>
        </div>
    );
};

export default Login;
