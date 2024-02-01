package com.springmvc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
	public static boolean crfDoubleData = true;
    @Autowired
    @Qualifier("customUserDetailsService")
    UserDetailsService userDetailsService;
 
    @Autowired(required=true)
    PersistentTokenRepository tokenRepository;
 
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        		.antMatchers("/").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('USER') or hasRole('Data Manager') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
        		.antMatchers("/pincode/**").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('USER') or hasRole('Data Manager') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
                .antMatchers("/dashboard/**").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('SUPERADMIN') or hasRole('USER') or hasRole('Data Manager') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
                .antMatchers("/administration/**").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('Data Manager') or hasRole('USER') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
                .antMatchers("/instrument/**").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('Data Manager') or hasRole('USER') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
                .antMatchers("/designReview/**").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('Data Manager') or hasRole('USER') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
                .antMatchers("/studyReview/**").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('Data Manager') or hasRole('USER') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
                .antMatchers("/amandment/**").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('Data Manager') or hasRole('USER') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
                .antMatchers("/formCalculation/**").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('Data Manager') or hasRole('USER') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
                .antMatchers("/acclimatization/**").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('Data Manager') or hasRole('USER') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
                .antMatchers("/accession/**").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('Data Manager') or hasRole('USER') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
                .antMatchers("/grouping/**").access("hasRole('ADMIN') or hasRole('Group leader') or hasRole('QC') or hasRole('QA') or hasRole('SUPERADMIN') or hasRole('Data Manager') or hasRole('USER') or hasRole('Data Specialist') or hasRole('Monitor') or hasRole('SD')or hasRole('TFM')")
                
                
                
                .and().logout().invalidateHttpSession(true).logoutUrl("/logout")
                .and().formLogin().loginPage("/login")
                .loginProcessingUrl("/login").successHandler(new LoginSuccessSecurityHandler())
                .failureUrl("/login?error").usernameParameter("username").passwordParameter("password").and()
                .rememberMe().rememberMeParameter("remember-me").tokenRepository(tokenRepository)
                .tokenValiditySeconds(1800).and().csrf()
                .and().exceptionHandling().accessDeniedPage("/accessDenied");    //.accessDeniedHandler(new SecurityExceptionHandling())
        	http.sessionManagement().maximumSessions(1).expiredUrl("/login?expired");
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
 
    @Bean
    public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
        PersistentTokenBasedRememberMeServices tokenBasedservice = new PersistentTokenBasedRememberMeServices(
                "remember-me", userDetailsService, tokenRepository);
        return tokenBasedservice;
    }
 
    @Bean
    public AuthenticationTrustResolver getAuthenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }
    

}

