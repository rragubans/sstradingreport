package com.bofa.sstradingreport;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@PropertySource("classpath:build.properties")
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	return application.sources(Application.class);
    }

    public static void main(String... args) {	
	SpringApplication.run(Application.class, args);
    }
    
     @Bean(destroyMethod="shutdown")
     public Executor taskScheduler(final @Value("${sstradingreport.scheduled-thread-pool-size:10}") int scheduledThreadPoolSize) {
         return Executors.newScheduledThreadPool(scheduledThreadPoolSize);
     }
}
