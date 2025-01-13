package com.example.shoong.config;

import com.example.shoong.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final DataSource dataSource; // JDBC 접근(remember-me 토큰 보관)
  private final CustomUserDetailsService userDetailsService;

  public SecurityConfig(DataSource dataSource, CustomUserDetailsService userDetailsService) {
    this.dataSource = dataSource;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      CustomUserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) throws Exception {

    // 1) AuthenticationProvider를 만들어서 userDetailsService, passwordEncoder를 연결
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);

    // 2) Spring Security에 등록
    http.authenticationProvider(authProvider);

    // (1) 인증/인가 설정
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/login", "/signup", "/css/**", "/js/**", "/api/**").permitAll()
        .anyRequest().authenticated() // 나머지는 로그인 필요
    );

    // (2) 폼 로그인 설정
    http.formLogin(form -> form
        .loginPage("/login") // 커스텀 로그인 페이지
        .loginProcessingUrl("/login") // <form action="/login" method="POST">
        .usernameParameter("id") // form에서 <input name="id">를 username으로 사용
        .passwordParameter("password") // <input name="password">
        .defaultSuccessUrl("/") // 로그인 성공 시 이동할 URL
        .permitAll());

    // (3) 로그아웃 설정
    http.logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .deleteCookies("JSESSIONID", "remember-me")
        .permitAll());

    // (4) Remember-me(자동 로그인) 설정
    http.rememberMe(rememberMe -> rememberMe
        .key("RANDOM_KEY_1234") // 토큰 생성용 키
        .tokenValiditySeconds(1 * 24 * 3600) // 1일
        .rememberMeParameter("remember-me") // <input name="remember-me">
        .userDetailsService(userDetailsService)
        .tokenRepository(persistentTokenRepository()));

    // (5) CSRF 설정 (실제 환경에서는 활성화 권장)
    http.csrf(csrf -> csrf.disable());

    return http.build();
  }

  // DB에 remember-me 토큰을 저장하기 위한 빈 설정
  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
    repo.setDataSource(dataSource);
    // repo.setCreateTableOnStartup(true); // 초기 실행 시 테이블 자동 생성
    return repo;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("http://localhost:3000"); // or "*"
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    configuration.setAllowCredentials(true); // withCredentials 시 필요

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration); // 모든 경로
    return source;
  }
}
