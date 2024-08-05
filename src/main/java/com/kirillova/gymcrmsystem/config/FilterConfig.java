package com.kirillova.gymcrmsystem.config;

import com.kirillova.gymcrmsystem.filter.AuthenticationFilter;
import com.kirillova.gymcrmsystem.filter.RequestResponseLoggingFilter;
import com.kirillova.gymcrmsystem.filter.TransactionIdFilter;
import com.kirillova.gymcrmsystem.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final AuthenticationService authenticationService;

    @Bean
    public FilterRegistrationBean<TransactionIdFilter> transactionFilter() {
        FilterRegistrationBean<TransactionIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TransactionIdFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter() {
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestResponseLoggingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter(authenticationService));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(3);
        return registrationBean;
    }
}
