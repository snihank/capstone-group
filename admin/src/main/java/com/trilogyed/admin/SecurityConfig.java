package com.trilogyed.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder authBuilder) throws Exception {

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        authBuilder.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username, password, enabled from users where username = ?")
                .authoritiesByUsernameQuery(
                        "select username, authority from authorities where username = ?")
                .passwordEncoder(encoder);
    }
    public void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.httpBasic();

        httpSecurity.authorizeRequests()
                .mvcMatchers("/login").authenticated()

                /*******************************FOR CUSTOMER ********************************************************/
                .mvcMatchers(HttpMethod.POST, "/customers").hasAnyAuthority("ADMIN","MANAGER","TEAM_LEAD") // Team Lead can create customer
                .mvcMatchers(HttpMethod.DELETE, "/customers/{id}").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/customers/{id}").hasAnyAuthority("ADMIN","MANAGER","TEAM_LEAD")
                /*******************************FOR INVENTORY ********************************************************/
                .mvcMatchers(HttpMethod.POST, "/inventory").hasAnyAuthority("ADMIN","MANAGER")
                .mvcMatchers(HttpMethod.DELETE, "/inventory/{id}").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/inventory/{id}").hasAnyAuthority("ADMIN","MANAGER","TEAM_LEAD","EMPLOYEE") // employee can update inventory
                /*******************************FOR LEVEL-UP ********************************************************/
                .mvcMatchers(HttpMethod.POST, "/levelup").hasAnyAuthority("ADMIN","MANAGER")
                .mvcMatchers(HttpMethod.DELETE, "/levelup/{id}").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/levelup").hasAnyAuthority("ADMIN","MANAGER","TEAM_LEAD")
                /*******************************FOR PRODUCT ********************************************************/
                .mvcMatchers(HttpMethod.POST, "/products").hasAnyAuthority("ADMIN","MANAGER")
                .mvcMatchers(HttpMethod.DELETE, "/products/{id}").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/products/{id}").hasAnyAuthority("ADMIN","MANAGER","TEAM_LEAD")
                /*******************************FOR INVOICE ********************************************************/
                .mvcMatchers(HttpMethod.POST, "/invoices").hasAnyAuthority("ADMIN","MANAGER")
                .mvcMatchers(HttpMethod.DELETE, "/invoices").hasAnyAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/invoices/{id}").hasAnyAuthority("ADMIN","MANAGER","TEAM_LEAD")

                .anyRequest().permitAll();

        httpSecurity
                .logout()
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/allDone")
                .deleteCookies("JSESSIONID")
                .deleteCookies("XSRF-TOKEN")
                .invalidateHttpSession(true);

        httpSecurity
                .csrf()     //before testing it in Postman do .csrf.disable(); and comment out the next line
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
