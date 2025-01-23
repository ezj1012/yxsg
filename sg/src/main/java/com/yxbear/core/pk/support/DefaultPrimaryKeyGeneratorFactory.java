package com.yxbear.core.pk.support;

import com.yxbear.core.pk.PrimaryKeyGenerator;
import com.yxbear.core.pk.PrimaryKeyGeneratorFactory;
import com.yxbear.core.pk.PrimaryKeySegmentLoaderStrategy;

public class DefaultPrimaryKeyGeneratorFactory implements PrimaryKeyGeneratorFactory {

    private PrimaryKeySegmentLoaderStrategy defaultLoaderStrategy;

    private Integer batchSize;

    public DefaultPrimaryKeyGeneratorFactory(PrimaryKeySegmentLoaderStrategy defaultLoaderStrategy, Integer batchSize) {
        super();
        this.defaultLoaderStrategy = defaultLoaderStrategy;
        this.batchSize = batchSize;
    }

    @Override
    public PrimaryKeyGenerator createGeneratorStrategy(String name) {
        return new DefaultPrimaryKeyGenerator(name, batchSize, defaultLoaderStrategy);
    }

    @Override
    public PrimaryKeyGenerator createGeneratorStrategy(String name, PrimaryKeySegmentLoaderStrategy loader) {
        return new DefaultPrimaryKeyGenerator(name, batchSize, loader);
    }

}
