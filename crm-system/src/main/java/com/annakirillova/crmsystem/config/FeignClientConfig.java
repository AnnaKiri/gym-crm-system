package com.annakirillova.crmsystem.config;

import feign.Feign;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.openfeign.FeignLogbookLogger;

@Configuration
@RequiredArgsConstructor
@Profile("!unit-tests")
@EnableFeignClients(basePackages = "com.annakirillova.crmsystem.feign")
public class FeignClientConfig {
    private final Logbook logbook;

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

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .logger(new FeignLogbookLogger(logbook))
                .logLevel(feign.Logger.Level.FULL);
    }
}
