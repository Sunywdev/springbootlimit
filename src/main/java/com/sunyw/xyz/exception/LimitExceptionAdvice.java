package com.sunyw.xyz.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;


/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class LimitExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public Object invoke(Exception https) {
        log.info ("开始执行[Exception]异常捕获");
        ModelAndView modelAndView = new ModelAndView (new MappingJackson2JsonView ());
        modelAndView.addObject ("code", HttpStatus.INTERNAL_SERVER_ERROR.value()+"");
        modelAndView.addObject ("msg", https.getMessage ());
        return modelAndView;
    }

    @ExceptionHandler(value = LimitException.class)
    public Object invoke(LimitException https) {
        log.info ("开始执行[LimitException]异常捕获");
        ModelAndView modelAndView = new ModelAndView (new MappingJackson2JsonView ());
        modelAndView.addObject ("code", https.getCode ());
        modelAndView.addObject ("msg", https.getDesc ());
        return modelAndView;
    }
}
