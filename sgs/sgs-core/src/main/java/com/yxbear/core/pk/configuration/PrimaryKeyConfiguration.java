package com.yxbear.core.pk.configuration;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.yxbear.core.pk.PrimaryKeyGeneratorFactory;
import com.yxbear.core.pk.PrimaryKeyGeneratorProvider;
import com.yxbear.core.pk.PrimaryKeySegmentLoaderStrategy;
import com.yxbear.core.pk.support.DBPrimaryKeySegmentLoaderStrategy;
import com.yxbear.core.pk.support.DefaultPrimaryKeyGeneratorFactory;

@Configuration
public class PrimaryKeyConfiguration {

    @Bean
    @ConditionalOnMissingBean(PrimaryKeySegmentLoaderStrategy.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    PrimaryKeySegmentLoaderStrategy initPKLoader(DataSource dataSource) {
        // return new PrimaryKeySegmentLoaderStrategy() {
        //
        // @Override
        // public PrimaryKeySegment load(String pkName, int batchSize) {
        // return null;
        // }
        //
        // };
        return new DBPrimaryKeySegmentLoaderStrategy(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean(PrimaryKeyGeneratorFactory.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    PrimaryKeyGeneratorFactory initPrimaryKeyGeneratorFacotry(PrimaryKeySegmentLoaderStrategy loaderStrategy) {
        DefaultPrimaryKeyGeneratorFactory defaultPrimaryKeyGeneratorFactory = new DefaultPrimaryKeyGeneratorFactory(loaderStrategy, 500);
        PrimaryKeyGeneratorProvider.setPrimaryKeyGeneratorFactory(defaultPrimaryKeyGeneratorFactory);
        return defaultPrimaryKeyGeneratorFactory;
    }

}
