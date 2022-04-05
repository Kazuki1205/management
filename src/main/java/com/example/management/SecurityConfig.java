package com.example.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.example.management.SecurityConfig;
import com.example.management.service.UserDetailsServiceImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	protected static Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	/**
	 * パスワードをハッシュ化
	 * 
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * ユーザーの認証方式を設定
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceImpl);
	}

	/**
	 * セキュリティ対象外を設定
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/images/**", "/scripts/**", "/h2-console/**");
	}

	/**
	 * URL毎のセキュリティ設定
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
        http.authorizeRequests().antMatchers("/", "/login", "/login-failure", "/logout-complete").permitAll()
        	.anyRequest().authenticated()
            // ログアウト処理
            .and().logout().logoutUrl("/logout").logoutSuccessUrl("/logout-complete").clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true).permitAll().and().csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            // フォーム
            .and().formLogin().loginPage("/login").defaultSuccessUrl("/menu").failureUrl("/login-failure");
        // @formatter:on
	}

}