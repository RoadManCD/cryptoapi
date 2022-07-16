package com.crypto.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsynchConfiguration {
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor(@Value("${async-executor.config.poolsize}") final int poolSize ,
                                  @Value("${async-executor.config.maxpoolsize}") final int maxPoolSize ,
                                  @Value("${async-executor.config.queuecapacity}") final int queueCapacity ,
                                  @Value("${async-executor.config.threadname}") final String threadName) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadName);
        executor.initialize();
        return executor;
    }
}
