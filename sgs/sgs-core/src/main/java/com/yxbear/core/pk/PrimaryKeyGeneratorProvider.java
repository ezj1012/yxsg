package com.yxbear.core.pk;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yxbear.core.exception.CoreException;

public class PrimaryKeyGeneratorProvider {

    private static final Map<String, PrimaryKeyGenerator> map = new ConcurrentHashMap<>();

    private static PrimaryKeyGeneratorFactory pkGeneratorFactory = null;

    public static void setPrimaryKeyGeneratorFactory(PrimaryKeyGeneratorFactory pkGeneratorFactory) {
        if (pkGeneratorFactory == null) { throw new CoreException("pkGeneratorFactory is null!"); }
        PrimaryKeyGeneratorProvider.pkGeneratorFactory = pkGeneratorFactory;
    }

    public static PrimaryKeyGenerator getPKGenerator(String name) {
        if (pkGeneratorFactory == null) { throw new CoreException("pkGeneratorFactory uninit!"); }
        return map.computeIfAbsent(name, key -> pkGeneratorFactory.createGeneratorStrategy(name));
    }

    private static PrimaryKeyGenerator getPKGenerator(String name, PrimaryKeySegmentLoaderStrategy strategy) {
        if (pkGeneratorFactory == null) { throw new CoreException("pkGeneratorFactory uninit!"); }
        return map.computeIfAbsent(name, key -> pkGeneratorFactory.createGeneratorStrategy(name, strategy));
    }

    public static Long next(String name) {
        return getPKGenerator(name).next();
    }

    public static Long next(String name, PrimaryKeySegmentLoaderStrategy strategy) {
        return getPKGenerator(name, strategy).next();
    }

}
