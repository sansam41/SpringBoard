package org.zerock.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.zerock.security.CustomLoginSuccessHandler;
import org.zerock.security.CustomUserDetailsService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Configuration
@Log4j
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Setter(onMethod_ = {@Autowired})
	private DataSource dataSource;
	
	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService customUserService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl repo=new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);
		return repo;
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		log.info("configure JDBC......................");
		
		auth.userDetailsService(customUserService())
		.passwordEncoder(passwordEncoder());
	}
	


	@Override
	public void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
			.antMatchers("/sample/all").permitAll()
			.antMatchers("/sample/admin").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/sample/member").access("hasRole('ROLE_MEMBER')");
		
		http.formLogin()
		.loginPage("/customLogin")
		.loginProcessingUrl("/login");
		
		http.logout()
		.logoutUrl("/customLogout")
		.invalidateHttpSession(true)
		.deleteCookies("remember-me","JSESSIO_ID");
		
		http.rememberMe()
		.key("zerock")
		.tokenRepository(persistentTokenRepository())
		.tokenValiditySeconds(604800);
		
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter,CsrfFilter.class);
	}
	

}
