package com.yxbear.core.coder.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yxbear.core.coder.CoderController;

@Configuration
@EnableConfigurationProperties({ProjectProps.class})
public class CoderConfiguration {

    @Bean
    CoderController initCoderController() {
        return new CoderController();
    }

}
