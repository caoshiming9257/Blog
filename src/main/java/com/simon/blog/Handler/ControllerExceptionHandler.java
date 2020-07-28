package com.simon.blog.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/8 15:27
 * @description: 统一异常处理类
 */

/**ControllerAdvice:会拦截含有controller注解的类*/
@ControllerAdvice
public class ControllerExceptionHandler {

    /**获取日志对象*/
    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 统一处理所有的Exception及其子类的异常方法
     * */
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception {
        logger.error("Request URL : {}, Exception : {}",request.getRequestURL(),e);

        /*指定某些异常不用当前的异常类来处理*/
        if (AnnotationUtils.findAnnotation(e.getClass(),ResponseStatus.class) != null){
            throw e;
        }

        ModelAndView mav = new ModelAndView();
        mav.addObject("url",request.getRequestURL());
        mav.addObject("exception",e);
        /*指定异常跳转的具体页面*/
        mav.setViewName("error/error");
        return mav;
    }
}
