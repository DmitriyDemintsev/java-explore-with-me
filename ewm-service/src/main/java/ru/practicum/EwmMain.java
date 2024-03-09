package ru.practicum;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;

@SpringBootApplication(scanBasePackages = {"ru.practicum"})
public class EwmMain {
    public static void main(String[] args) {
        SpringApplication.run(EwmMain.class, args);
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        var filter = new CommonsRequestLoggingFilter();
        LoggingSystem.get(this.getClass().getClassLoader()).setLogLevel(
                filter.getClass().getName(), LogLevel.DEBUG
        );
        filter.setIncludeQueryString(true);
        filter.setIncludeHeaders(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(1024);
        return filter;
    }
}
