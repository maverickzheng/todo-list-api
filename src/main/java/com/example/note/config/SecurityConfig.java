// package com.example.note.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.Customizer;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import
// org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;

// @EnableWebSecurity
// @Configuration
// public class SecurityConfig {
// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
// throws Exception {

// httpSecurity.csrf(customizer -> customizer.disable()). // 防止跨站請求偽造
// authorizeHttpRequests(
// (auth) -> auth
// // 指定所列匹配規則中的路徑，允許所有用戶訪問，即不需要進行身份驗證
// .requestMatchers("/v3/api-docs/**", "/swagger-ui/**",
// "/swagger-ui.html")
// .permitAll()

// // 指定所列匹配規則中的路徑，允許特定用戶訪問，需要進行身份驗證
// .requestMatchers("/api/notes**")
// .hasAnyRole("ADMIN")

// .anyRequest()
// .authenticated())

// .httpBasic(Customizer.withDefaults());

// return httpSecurity.build();
// }

// @Bean
// public UserDetailsService userDetailsService() {
// InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
// manager.createUser(User.withUsername("root")
// .password(passwordEncoder().encode("admin"))
// .roles("ADMIN")
// .build());
// manager.createUser(User.withUsername("user")
// .password(passwordEncoder().encode("user"))
// .roles("USER")
// .build());
// return manager;
// }

// @Bean
// public PasswordEncoder passwordEncoder() {
// return new BCryptPasswordEncoder();
// }

// }
