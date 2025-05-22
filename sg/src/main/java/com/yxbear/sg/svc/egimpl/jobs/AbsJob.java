package com.yxbear.sg.svc.egimpl.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;


import com.yxbear.core.bean.EntityBean;
import com.yxbear.sg.engine.SgListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbsJob<T extends EntityBean<Integer>> implements Runnable, SgListener<T> {

    DelayQueue<DelayedTask<T>> queue = new DelayQueue<>();

    // 业务保证只有一个线程修改, 其它线程只读
    private volatile long maxTriggerTime = 0;

    private volatile boolean running = true;

    private volatile boolean lastLoadIsEmpty = false;

    @Override
    public void run() {
        while (running) {
            try {
                while (!queue.isEmpty()) {
                    DelayedTask<T> data = queue.take();
                    process(data.getEntity());
                }
                loadDatas();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void stop() {
        running = false;
    }

    /**
     * 加载周期内的数据
     */
    private void loadDatas() {
        try {
            if (lastLoadIsEmpty && System.currentTimeMillis() < maxTriggerTime) {
                Thread.sleep(1000);
                return;
            }

            List<T> loads = this.loads();
            if (loads.isEmpty()) {
                log.info("本次没有加载到数据");
                lastLoadIsEmpty = true;
                maxTriggerTime = Math.max(maxTriggerTime, System.currentTimeMillis()) + 30000;
                return;
            }
            lastLoadIsEmpty = false;

            List<DelayedTask<T>> list = new ArrayList<>(loads.size());
            long tempTime = maxTriggerTime;
            for (T t : loads) {
                long triggerTime = getTriggerTime(t);
                tempTime = Math.max(tempTime, triggerTime);
                list.add(new DelayedTask<>(t, triggerTime));
            }
            maxTriggerTime = tempTime;
            queue.addAll(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void onEvent(T t) {
        // 保证应该触发的数据在队列中
        long triggerTime = getTriggerTime(t);
        if (triggerTime > maxTriggerTime) {
            return;
        }
        queue.add(new DelayedTask<T>(t, triggerTime));
    }

    abstract long getTriggerTime(T t);

    // 加载数据库中的数据.
    abstract List<T> loads();

    // 处理数据,并主动删除已经处理过的数据
    abstract void process(T t);

}
