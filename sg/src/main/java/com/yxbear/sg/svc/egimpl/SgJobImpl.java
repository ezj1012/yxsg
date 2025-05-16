package com.yxbear.sg.svc.egimpl;

import com.yxbear.core.CommUtils;
import com.yxbear.sg.domain.mapper.mem.MemCityBuildUpgradingMapper;
import com.yxbear.sg.domain.model.mem.CMemCityBuildUpgrading;
import com.yxbear.sg.domain.model.mem.MemCityBuildUpgrading;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.loader.SgJobWorker;
import com.yxbear.sg.svc.play.CitySvc;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SgJobImpl implements SgJobWorker, InitializingBean {

    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    final SgEngine sg;
    final CitySvc citySvc;

    final MemCityBuildUpgradingMapper buMapper;


    @Override
    public void afterPropertiesSet() throws Exception {
        taskScheduler.initialize();
    }

    @Override
    public void processBuilding() {
        if (!sg.isRunning()) {
            return;
        }

        long time = System.currentTimeMillis();
        try {
            List<MemCityBuildUpgrading> list = buMapper.queryList(CMemCityBuildUpgrading.builder().endEndTime(time).build(), "id");
            if (!CommUtils.isEmpty(list)) {
                citySvc.finishBuildUpgrading(list);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public synchronized void schedule(Task task, int delay) {
        ScheduledFuture<?> remove = scheduledTasks.remove(task.getKey());
        if (remove != null) {
            remove.cancel(true);
        }
        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleWithFixedDelay(task.getJob(), Duration.ofSeconds(delay));
        scheduledTasks.put(task.getKey(), scheduledFuture);
    }

    @Override
    @SneakyThrows
    public synchronized void schedule(Task task, String cron) {
        ScheduledFuture<?> remove = scheduledTasks.remove(task.getKey());
        if (remove != null) {
            remove.cancel(true);
        }
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task.getJob(), new CronTrigger(cron));
        scheduledTasks.put(task.getKey(), scheduledFuture);
    }


}
