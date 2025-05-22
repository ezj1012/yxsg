package com.yxbear.sg.engine.loader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
public abstract class SgJobWorker implements Closeable {

    @Data
    @AllArgsConstructor
    public static class Task {
        String key;
        Runnable job;
    }

    final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    final Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    protected SgJobWorker() {
        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadNamePrefix("SgJobScheduler-");
        taskScheduler.initialize();
    }

    public abstract void updateJobPeriod();

    public void execute(final Runnable job) {
        taskScheduler.execute(job);
    }

    public void schedule(Task task, int delay) {
        cancelPreviousTask(task.getKey());
        try {
            ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(task.getJob(), Duration.ofSeconds(delay));
            scheduledTasks.put(task.getKey(), future);
        } catch (Exception e) {
            log.error("Failed to schedule task with fixed delay: {}", task.getKey(), e);
        }
    }

    public void schedule(Task task, String cron) {
        cancelPreviousTask(task.getKey());
        try {
            ScheduledFuture<?> future = taskScheduler.schedule(task.getJob(), new CronTrigger(cron));
            scheduledTasks.put(task.getKey(), future);
        } catch (Exception e) {
            log.error("Failed to schedule task with cron expression: {}", task.getKey(), e);
        }
    }

    private void cancelPreviousTask(String key) {
        ScheduledFuture<?> previousTask = scheduledTasks.remove(key);
        if (previousTask != null && !previousTask.isCancelled()) {
            previousTask.cancel(true);
        }
    }


    @Override
    public void close() throws IOException {
        taskScheduler.shutdown();
    }
}
