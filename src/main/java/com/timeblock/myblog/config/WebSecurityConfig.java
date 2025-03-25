package com.timeblock.myblog.config;

import com.timeblock.myblog.filter.JwtAuthenticationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf.disable())//사용자가 의도하지 않는 행위를 하는 것을 방지
                .httpBasic((httpBasic) -> httpBasic.disable()) //로그인 창 뜨는 것을 막음
                .sessionManagement(Customizer.withDefaults())
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

//                .and()
                .authorizeRequests()
                .requestMatchers("/","/api/v1/auth/**","/api/v1/search/**", "/file/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/board/**", "/api/v1/user/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(new FailedAuthenticationEntryPoint())); //exception 발생 시, 아래 메시지 생성

        httpSecurity
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}

class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {


        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("{\"code:\":\"AF\",\"message\": \"Authorization failed\"}");
    }
}