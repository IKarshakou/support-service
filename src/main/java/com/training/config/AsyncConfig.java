package com.training.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
//@EnableAsync
@EnableScheduling
//@RequiredArgsConstructor
public class AsyncConfig {// implements AsyncConfigurer, WebMvcConfigurer, SchedulingConfigurer {

//    private final MeterRegistry meterRegistry;

//    @Override
//    public Executor getAsyncExecutor() {
//        return ContextExecutorService.wrap(Executors.newCachedThreadPool(), ContextSnapshot::captureAll);
//    }
//
//    @Override
//    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//        configurer.setTaskExecutor(new SimpleAsyncTaskExecutor(r -> new Thread(ContextSnapshot.captureAll().wrap(r))));
//    }
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.setScheduler(threadPoolTaskScheduler());
//    }

//    @Bean(name = "taskExecutor", destroyMethod = "shutdown")
//    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
//        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler() {
//            @Override
//            protected ExecutorService initializeExecutor(ThreadFactory threadFactory,
//                                                         RejectedExecutionHandler rejectedExecutionHandler) {
//                ExecutorService executorService = super.initializeExecutor(threadFactory, rejectedExecutionHandler);
//                return ContextExecutorService.wrap(executorService, ContextSnapshot::captureAll);
//            }
//        };
//        threadPoolTaskScheduler.initialize();
//        return threadPoolTaskScheduler;
//    }

//    @Bean
//    public ExecutorService executorService(final MeterRegistry registry) {
//        return ExecutorServiceMetrics.monitor(registry, Executors.newFixedThreadPool(20), "my executor", Tags.of("key", "value"));
//    }
}
