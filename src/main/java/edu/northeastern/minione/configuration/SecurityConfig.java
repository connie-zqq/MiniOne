package edu.northeastern.minione.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * The file is to create the security configuration that uses jdbc.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Datasource bean.
     */
    @Autowired
    private DataSource dataSource;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    /**
     * The method is to configure authentication using jdbc.
     * It specifies the password encoder, datasource, and two SQL statements (define in the application.properties file).
     *
     * @param auth the AuthenticationManagerBuilder to use
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * Enable HTTP Security. The method is to ensure any requests to the application requires authentication.
     * The authentication is done with form based login.
     *
     * @param http the httpSecurity object
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/home", "/error/**", "/spaces", "spaces/**", "/follows", "/users/logout", "/users/register", "/users/login").permitAll()
                .anyRequest().authenticated() //set restriction
                .and()
                .formLogin()
                .loginPage("/users/login").failureUrl("/users/login?error=true").defaultSuccessUrl("/users/login")
                .usernameParameter("userName").passwordParameter("passwordHash")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                .logoutSuccessUrl("/").and().exceptionHandling()
                .accessDeniedPage("/error/403");
    }

    /**
     * Ignore static resources from Spring Security.
     *
     * @param web the webSecurity object
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**", "/public/**", "/css/**", "/img/**", "/js/**");
    }
}
