package at.qe.skeleton.configs;

import at.qe.skeleton.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Spring configuration for web security.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Configuration
@EnableWebSecurity()
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    SchedulerTaskConfig schedulerTaskConfig;

    @Autowired
    EmailService emailService;

    @Bean
    protected LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomizedLogoutSuccessHandler();
    }


    @PostConstruct
    void startTaskTimer(){
        schedulerTaskConfig.startAbsenceTaskTimer();
        schedulerTaskConfig.startEmailTaskTimer();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.headers().frameOptions().disable(); // needed for H2 console

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login.xhtml")
                .logoutSuccessHandler(this.logoutSuccessHandler());

        String manager = "MANAGER";
        String admin = "ADMIN";
        String facilityManager = "FACILITY_MANAGER";

        http.authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/error/**")
                .permitAll()
                .antMatchers("/secured/**")
                .permitAll()
                // Only access with admin role
                .antMatchers("/users/**")
                .hasAnyAuthority(facilityManager, admin, manager)
                .antMatchers("/rooms/**")
                .hasAnyAuthority(facilityManager, admin)
                .antMatchers("/sensors/**")
                .hasAnyAuthority(facilityManager, admin)
                .antMatchers("/auditLog/**")
                .hasAnyAuthority(admin,facilityManager,manager)
                // Allow only certain roles to use websockets (only logged in users)
                .and()
                .formLogin()
                .usernameParameter("username")
                .loginPage("/login.xhtml")
                .failureUrl("/login.xhtml?error")
                .permitAll()
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/secured/dashboard.xhtml");

        http.exceptionHandling().accessDeniedPage("/error/access_denied.xhtml");
        http.sessionManagement().invalidSessionUrl("/error/invalid_session.xhtml");

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //Configure roles and passwords via datasource
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, roles from users where username=?")
                .passwordEncoder(passwordEncoder());
    }


    @Bean
    public static PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
