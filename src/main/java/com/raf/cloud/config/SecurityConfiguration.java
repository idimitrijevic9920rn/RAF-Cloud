package com.raf.cloud.config;


import com.raf.cloud.model.enums.Role;
import com.raf.cloud.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/machine/**").permitAll()

                .requestMatchers("/api/users/add/**").hasAuthority(Role.CAN_CREATE_USERS.toString())
                .requestMatchers("/api/users/getAll/**").hasAuthority(Role.CAN_READ_USERS.toString())
                .requestMatchers("/api/users/update/**").hasAuthority(Role.CAN_UPDATE_USERS.toString())
                .requestMatchers("/api/users/delete/**").hasAuthority(Role.CAN_DELETE_USERS.toString())



                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors(Customizer.withDefaults());

        return http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

}
