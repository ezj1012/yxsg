package com.yxbear.core.pk;

public interface PrimaryKeyGeneratorFactory {

    /**
     * 获取主键生成器
     * 
     * @param name
     * @return
     */
    PrimaryKeyGenerator createGeneratorStrategy(String name);

    PrimaryKeyGenerator createGeneratorStrategy(String name, PrimaryKeySegmentLoaderStrategy loader);

}
