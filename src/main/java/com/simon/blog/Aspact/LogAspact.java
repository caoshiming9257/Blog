package com.simon.blog.Aspact;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/8 16:27
 */
@Aspect
@Component
public class LogAspact {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**切面*/
    @Pointcut("execution(* com.simon.blog.web.*.*(..))")
    public void log(){

    }

    /**前置通知
     * 获取请求时的url，ip，类和方法名，参数
     * */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        /*通过获取request对象来获取url和ip*/
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url,ip,classMethod,args);
        logger.info("Requect : {}",requestLog);

    }

    /**最终通知*/
    @After("log()")
    public void doAfter(){
//        logger.info("-----After-----");
    }

    /**后置通知*/
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("result : {}",result);
    }

    /**
     * 定义内部类，用来保存数据
     * */
    private class RequestLog{
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
