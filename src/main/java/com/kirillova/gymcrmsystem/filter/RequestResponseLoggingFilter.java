package com.kirillova.gymcrmsystem.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String requestBody = getRequestBody(request);

        log.info("Incoming Request: {} {}?{}; Request Body: {}", method, uri, queryString, requestBody);

        CustomHttpServletResponseWrapper responseWrapper = new CustomHttpServletResponseWrapper(response);

        filterChain.doFilter(request, responseWrapper);

        String responseBody = responseWrapper.getCaptureAsString();
        int status = response.getStatus();

        log.info("Response: Status: {}; Response Body: {}", status, responseBody);

        response.getOutputStream().write(responseBody.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    private static class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {

        private final ByteArrayOutputStream capture;
        private ServletOutputStream outputStream;
        private PrintWriter writer;

        public CustomHttpServletResponseWrapper(HttpServletResponse response) throws IOException {
            super(response);
            capture = new ByteArrayOutputStream(response.getBufferSize());
            outputStream = new CustomServletOutputStream(capture);
            writer = new PrintWriter(capture);
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return writer;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return outputStream;
        }

        @Override
        public void flushBuffer() throws IOException {
            if (writer != null) {
                writer.flush();
            }
            if (outputStream != null) {
                outputStream.flush();
            }
            super.flushBuffer();
        }

        public String getCaptureAsString() throws IOException {
            flushBuffer();
            return capture.toString(getCharacterEncoding());
        }

        private static class CustomServletOutputStream extends ServletOutputStream {
            private final ByteArrayOutputStream capture;

            public CustomServletOutputStream(ByteArrayOutputStream capture) {
                this.capture = capture;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }

            @Override
            public void write(int b) throws IOException {
                capture.write(b);
            }
        }
    }
}
