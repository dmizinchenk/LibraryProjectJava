package ru.Zinchenko.LibraryProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.Zinchenko.LibraryProject.security.securityUser.UserDetailsServiceImplementation;
import ru.Zinchenko.LibraryProject.security.util.AuthProviderImplement;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImplementation detailService;

    @Autowired
    public SecurityConfig(UserDetailsServiceImplementation detailService){
        this.detailService = detailService;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/styles/*.css");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //конфигурируем spring security
        //конфигурируем автоиризацию
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                .antMatchers("/book/*", "/logout", "/order/getBook/*", "/order/returnBook/*").authenticated()
                .antMatchers("/order","/order/**").hasAnyRole("ADMIN", "EMPLOYEE")
                .antMatchers( "/login", "/register").anonymous()
                .anyRequest().permitAll()
                .and()
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/login_proc") //адрес для обработки авторизации формы
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login");
    }

    //настройка аутентификации
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authProvider);
        auth.userDetailsService(detailService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
