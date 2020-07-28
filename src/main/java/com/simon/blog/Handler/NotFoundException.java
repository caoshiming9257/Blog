package com.simon.blog.Handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/8 16:04
 */
/**
 * 当异常为notFound的时才会进入该异常类
 * */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
