package com.annakirillova.crmsystem.config;

import feign.Logger;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Encoder encoder(ObjectFactory<HttpMessageConverters> converters) {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }

    @Bean
    public feign.RequestInterceptor tracingRequestInterceptor(Tracer tracer) {
        return requestTemplate -> {
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                requestTemplate.header("X-B3-TraceId", currentSpan.context().traceId());
                requestTemplate.header("X-B3-SpanId", currentSpan.context().spanId());
                requestTemplate.header("X-B3-Sampled", currentSpan.context().sampled() ? "1" : "0");
            }
        };
    }
}