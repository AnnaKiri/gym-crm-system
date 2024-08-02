package com.kirillova.gymcrmsystem.filter;

import com.kirillova.gymcrmsystem.health.StatusHealthIndicator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(request);
        String requestBody = requestWrapper.getRequestBody();

        log.info("Incoming Request: {} {}?{}; Request Body: {}", request.getMethod(), request.getRequestURI(), request.getQueryString(), requestBody);

        CustomHttpServletResponseWrapper responseWrapper = new CustomHttpServletResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        String responseBody = responseWrapper.getCaptureAsString();
        int status = response.getStatus();

        if (HttpStatus.valueOf(status).is5xxServerError()) {
            StatusHealthIndicator.informStatusBad();
        } else {
            StatusHealthIndicator.informStatusOk();
        }

        log.info("Response: Status: {}; Response Body: {}", status, responseBody);

        response.getOutputStream().write(responseBody.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }

    private static class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] body;

        public CustomHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            body = request.getInputStream().readAllBytes();
        }

        @Override
        public ServletInputStream getInputStream() {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return byteArrayInputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() {
                    return byteArrayInputStream.read();
                }
            };
        }

        @Override
        public BufferedReader getReader() {
            InputStream inputStream = new ByteArrayInputStream(body);
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        }

        public String getRequestBody() {
            return new String(body, StandardCharsets.UTF_8);
        }
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
