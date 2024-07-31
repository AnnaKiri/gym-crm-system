package com.kirillova.gymcrmsystem.config;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MySpringMvcDispatcherSerlvetInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerTransactionIdFilter(aServletContext);
        registerRequestResponseLoggingFilter(aServletContext);
        registerHiddenFieldFilter(aServletContext);
        registerAuthenticationFilter(aServletContext);
    }

    private void registerHiddenFieldFilter(ServletContext aContext) {
        aContext.addFilter("hiddenHttpMethodFilter",
                new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null, true, "/*");
    }

    private void registerAuthenticationFilter(ServletContext aContext) {
        FilterRegistration.Dynamic registration = aContext.addFilter("authenticationFilter", DelegatingFilterProxy.class);
        registration.setInitParameter("targetBeanName", "authenticationFilter");
        registration.addMappingForUrlPatterns(null, false, "/*");
    }

    private void registerTransactionIdFilter(ServletContext aContext) {
        FilterRegistration.Dynamic registration = aContext.addFilter("transactionIdFilter", DelegatingFilterProxy.class);
        registration.setInitParameter("targetBeanName", "transactionIdFilter");
        registration.addMappingForUrlPatterns(null, false, "/*");
    }

    private void registerRequestResponseLoggingFilter(ServletContext aContext) {
        FilterRegistration.Dynamic registration = aContext.addFilter("requestResponseLoggingFilter", DelegatingFilterProxy.class);
        registration.setInitParameter("targetBeanName", "requestResponseLoggingFilter");
        registration.addMappingForUrlPatterns(null, false, "/*");
    }
}