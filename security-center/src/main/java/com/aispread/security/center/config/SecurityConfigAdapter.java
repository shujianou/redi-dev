package com.aispread.security.center.config;

import com.aispread.security.center.handle.*;
import com.aispread.security.center.jwt.filter.JwtAuthenticationTokenFilter;
import com.aispread.security.center.provider.SecurityAuthenticationProvider;
import com.aispread.security.center.service.SecurityUserServiceImpl;
import com.aispread.service.api.security.UserCheckApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by Vim 2018/11/25 0:38
 *
 * @author Vim
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigAdapter extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //替换自带的UserDetailService
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
        //注册自定义验证Provider
        auth.authenticationProvider(new SecurityAuthenticationProvider(userCheckApi));
    }

    /**
     * 装载BCrypt密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler goAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 使用 JWT，关闭token
                .and()
                .httpBasic()
                // 未经过认证的用户访问受保护的资源
                .authenticationEntryPoint(new AuthenticationEntryPoint())

                .and()
                .authorizeRequests()
                // 任何用户都可以访问URL以"/resources/", equals "/signup", 或者 "/about"开头的URL。
                .antMatchers("/resources/**", "/login", "/auth/**").permitAll()
                //以 "/admin/" 开头的URL只能由拥有 "ROLE_ADMIN"角色的用户访问。请注意我们使用 hasRole 方法
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/uuu").access("hasRole('USER') or hasRole('ADMIN') ")
                .anyRequest().authenticated()
                .and()

                .formLogin()
                // .loginPage("/login")
                //.loginProcessingUrl("/login").defaultSuccessUrl("/index", true).failureUrl("/login?error")
                .successHandler(goAuthenticationSuccessHandler())
                // 认证失败
                .failureHandler(new AuthenticationFailureHandler())
                .permitAll()

                .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandler())
                .permitAll();

        // 记住我
        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService).tokenValiditySeconds(300);


        http.exceptionHandling()
                // 已经认证的用户访问自己没有权限的资源处理
                .accessDeniedHandler(new AccessDeniedHandler())
                .and().addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private SecurityUserServiceImpl userDetailsService;

    @Autowired
    private UserCheckApi userCheckApi;
}
