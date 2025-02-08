package com.yxbear.sg.configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.yxbear.core.coder.configuration.EnableCoder;
import com.yxbear.core.pk.configuration.EnablePrimaryKeyServer;
import com.yxbear.sg.domain.bean.SGProps;
import com.yxbear.sg.engine.SgContext;
import com.yxbear.sg.engine.SgEngine;
import com.yxbear.sg.engine.loader.SgDataLoader;
import com.yxbear.sg.engine.loader.SgWorldDataLoader;
import com.yxbear.sg.engine.support.DefaultSgDataLoader;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;

@Configuration
@PropertySource({"classpath:sg.properties"})
@EnableConfigurationProperties({SGProps.class})
@EnablePrimaryKeyServer
@EnableCoder
public class SgConfiguration implements WebMvcConfigurer {

    static {
        System.setProperty("pagehelper.banner", "false");
    }

    SGProps sgProps;

    Set<String> permitAll = new HashSet<>(Arrays.asList(
            //
            // "/so/**", "/system/configuration",

            "/rsm/**",
            //
            "/favicon.*", "/adm/**",
            //
            "/*/*.js", "/*/*.css", "/*/*.jpg", "/*/*.jpeg", "/*/*.bmp", //
            "/*/*.gif", "/*/*.png", "/*/*.ico", "/*/*.swf", "/*/*.eot", //
            "/*/*.svg", "/*/*.ttf", "/*/*.woff", "/*/*.woff2", "/*/*.htm", //
            "/*/*.html", "/*/*.txt", //
            "/*/*.xml", "/*/*.json"));

    @Autowired
    public void setSgProps(SGProps sgProps) {
        this.sgProps = sgProps;
        permitAll.addAll(sgProps.getPermitAll());
    }

    @Bean
    SaServletFilter getSaServletFilter(SGProps sgProps) {

        return new SaServletFilter()
                //
                .addInclude("/**")
                //
                .addExclude(permitAll.toArray(new String[permitAll.size()]))
                //
                .setAuth(obj ->
                {
                    if (!StpUtil.isLogin()) {
                        if (SaHolder.getRequest().isAjax()) {
                            SpringMVCUtil.getResponse().setStatus(401);
                            SaRouter.back(SaResult.error("未登录").setCode(401));
                        } else {
                            // String url = SaHolder.getRequest().getRequestPath();
                            // if (Objects.equals("/", url) || Objects.equals("", url)) {
                            // SaHolder.getRequest().forward("/index.html");
                            // SaRouter.back();
                            // } else {
                            // String back = SaFoxUtil.joinParam(SaHolder.getRequest().getUrl(), SpringMVCUtil.getRequest().getQueryString());
                            // SaHolder.getResponse().redirect("/sso/login?back=" + SaFoxUtil.encodeUrl(back));
                            // SaHolder.getResponse().redirect("/");
                            // SaRouter.back();
                            // }
                        }
                    }
                });
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HashSet<Object> igset = new HashSet<>();
        permitAll.stream().filter(a -> a.startsWith("/mgr")).forEach(igset::add);
        igset.addAll(sgProps.getPermitPermissionAll());
        String[] excludePathPatterns = igset.toArray(String[]::new);

        // 开启权限注解.
        registry.addInterceptor(new SaInterceptor(handler -> {
            SaRouter.match("/mgr/**", r -> StpUtil.checkRole("admin"));
        })).addPathPatterns("/**").excludePathPatterns(excludePathPatterns);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/rsm/**").addResourceLocations(
                //
                new FileSystemResource(sgProps.getRsmDir() + "/"));
        // 保留默认静态资源映射
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    @Bean
    SgEngine initSgEngine(SgWorldDataLoader worldDataLoader) {
        SgDataLoader dataMgr = new DefaultSgDataLoader();
        SgContext ctx = new SgContext(dataMgr, worldDataLoader);
        return new SgEngine(ctx);
    }

}
