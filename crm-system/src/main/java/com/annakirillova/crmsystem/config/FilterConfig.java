package com.annakirillova.crmsystem.config;

import com.annakirillova.crmsystem.filter.RequestResponseLoggingFilter;
import com.annakirillova.crmsystem.filter.TokenValidationFilter;
import com.annakirillova.crmsystem.filter.TransactionIdFilter;
import com.annakirillova.crmsystem.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FilterConfig {
    private final TokenService tokenService;

    @Bean
    public FilterRegistrationBean<TokenValidationFilter> tokenValidationFilter() {
        FilterRegistrationBean<TokenValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenValidationFilter(tokenService));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<TransactionIdFilter> transactionFilter() {
        FilterRegistrationBean<TransactionIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TransactionIdFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter() {
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestResponseLoggingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(3);
        return registrationBean;
    }


}
