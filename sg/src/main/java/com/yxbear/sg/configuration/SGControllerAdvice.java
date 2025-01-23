package com.yxbear.sg.configuration;

import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.alibaba.fastjson2.JSON;
import com.yxbear.core.bean.R;
import com.yxbear.core.exception.CoreException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ControllerAdvice(basePackages = {"com.yxbear.sg"})
public class SGControllerAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 自身框架的异常默认认为是可以给用户直接看到的
     * 
     * @param request
     * @param response
     * @param exception
     */
    @ExceptionHandler({CoreException.class})
    public void frameworkExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception, InputStream is) {
        log.error(exception.getMessage(), exception);
        int stateCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(stateCode);
        // AJAX 请求统一处理方法
        R<?> r = new R<>(exception);
        try (PrintWriter pw = response.getWriter();) {
            pw.write(JSON.toJSONString(r));
            pw.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    // @ExceptionHandler({NotLoginException.class})
    // public void notLoginExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception, InputStream is) {
    // log.error(exception.getMessage(), exception);
    // int stateCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    //
    // response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    // response.setStatus(stateCode);
    // // AJAX 请求统一处理方法
    // R<?> r = new R<>(exception);
    // try (PrintWriter pw = response.getWriter();) {
    // pw.write(JSON.toJSONString(r));
    // pw.flush();
    // } catch (Exception e) {
    // log.error(e.getMessage(), e);
    // }
    // }

    // /** 异常统一封装 **/
    @ExceptionHandler({})
    public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        // 统一打日志
        log.error(exception.getMessage(), exception);

        int stateCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = exception.getMessage();
        if (exception instanceof HttpMessageNotReadableException) {
            message = "参数错误!";
        }
        // 对异常处理
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(stateCode);
        // AJAX 请求统一处理方法
        R<String> r = new R<>(false, stateCode, message);
        try (PrintWriter pw = response.getWriter();) {
            pw.write(JSON.toJSONString(r));
            pw.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 需要测试文件类型会不会走这个方法,如果文件类型会走则应该返回false
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) { return new R<>(body); }
        if (body instanceof R) {
            R<?> t = (R<?>) body;
            if (t.getCode() != 200) {
                HttpStatus resolve = HttpStatus.resolve(t.getCode());
                if (resolve != null) {
                    response.setStatusCode(resolve);
                }
            }
            return body;
        } else if (body instanceof String) { return JSON.toJSONString(new R<>(body)); }
        return new R<>(body);
    }

}
