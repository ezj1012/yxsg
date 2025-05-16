package com.yxbear.sg.engine.loader;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface SgJobWorker {

    @Data
    @AllArgsConstructor
    public static class Task {
        String key;
        Runnable job;
    }

    void processBuilding();

    void schedule(Task task, int delay);

    void schedule(Task task, String cron);
}
