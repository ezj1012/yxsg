package com.yxbear.sg.svc.egimpl.jobs;

import com.yxbear.core.bean.EntityBean;

import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedTask<T extends EntityBean<Integer>> implements Delayed {

    private final T t;

    private final long triggerTime;

    public DelayedTask(T t, long triggerTime) {
        this.t = t;
        this.triggerTime = triggerTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long delayMillis = triggerTime - System.currentTimeMillis();
        return unit.convert(delayMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), other.getDelay(TimeUnit.MILLISECONDS));
    }

    public T getEntity() {
        return t;
    }

    @Override
    public int hashCode() {
        return Objects.hash(t);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DelayedTask<?> other = (DelayedTask<?>) obj;
        return Objects.equals(t.getId(), other.t.getId());
    }

}
