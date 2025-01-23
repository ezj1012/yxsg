package com.yxbear.core.pk.support;

import com.yxbear.core.pk.PrimaryKeyGenerator;
import com.yxbear.core.pk.PrimaryKeySegment;
import com.yxbear.core.pk.PrimaryKeySegmentLoaderStrategy;

public class DefaultPrimaryKeyGenerator implements PrimaryKeyGenerator {

    private final String name;

    private PrimaryKeySegmentLoaderStrategy loader;

    private PrimaryKeySegment segment;

    private int batchSize = 500;

    private Long cur;

    public DefaultPrimaryKeyGenerator(String name, Integer batchSize, PrimaryKeySegmentLoaderStrategy loader) {
        this.name = name;
        this.loader = loader;
        if (batchSize != null && batchSize > 0) {
            this.batchSize = batchSize;
        }
    }

    @Override
    public Long next() {
        if (segment == null || cur > segment.getMax()) {
            synchronized (name) {
                this.segment = loader.load(name, batchSize);
                this.cur = segment.getMin();
            }
        }
        return this.cur++;
    }

}
