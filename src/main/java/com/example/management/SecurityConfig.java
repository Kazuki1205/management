package com.example.management;

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

/**
 * セキュリティ設定クラス
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	/**
	 * パスワードをハッシュ化
	 * 
	 * @return BCryptPasswordEncoder パスワードエンコーダ
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
        http.authorizeRequests()
        
        	// ADMIN権限のみ許可
        	.antMatchers("/employee/**", "/department/**").hasRole("ADMIN")
        	
        	// ADMIN・OFFICE権限のみ許可
        	.antMatchers("/production/register**", "/production/edit**", "/shipping/register**").hasAnyRole("ADMIN", "OFFICE")
        	
        	// ADMIN・FIELD権限のみ許可
        	.antMatchers("/report/register**", "/report/edit**").hasAnyRole("ADMIN", "FIELD")
        	
        	// ADMIN・SALE権限のみ許可
        	.antMatchers("/order/register**", "/order/edit**").hasAnyRole("ADMIN", "SALE")
        	
        	// 指定したURL形式のみ全て許可。
        	.antMatchers("/", "/login", "/login-failure", "/logout-complete", "/ajax/**").permitAll()
        	
        	// その他リクエストは、ログイン状態でのみ許可。
        	.anyRequest().authenticated()
        	
            // ログアウトURL、ログアウト成功時のURL、ログアウト後のCookie・Session・トークンの処理について設定。
            .and().logout().logoutUrl("/logout").logoutSuccessUrl("/logout-complete").clearAuthentication(true)
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true).permitAll().and().csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            
            // ログインフォームURL、ログイン成功時のURL、ログイン失敗時のURLを設定。
            .and().formLogin().loginPage("/login").defaultSuccessUrl("/menu").failureUrl("/login-failure");
        // @formatter:on
	}

}