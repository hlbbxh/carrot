/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.web.exceptions;

import com.alibaba.fastjson.JSON;
import com.mtons.mblog.base.lang.Result;
import com.mtons.mblog.base.lang.MtonsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理
 * @author langhsu
 *
 */
@Slf4j
@Component
public class DefaultExceptionHandler implements HandlerExceptionResolver { // 实现了HandlerExceptionResolver接口
	private static final String errorView = "/error"; //定义的全局最终变量
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		//instanceof布尔 判断是不是谁的子类
		if (ex instanceof IllegalArgumentException || ex instanceof IllegalStateException || ex instanceof MtonsException) {
			log.error(ex.getMessage());
		} else {
			log.error(ex.getMessage(), ex);
		}

		ModelAndView view = null;
		String ret = ex.getMessage();
		
		//判断是不是ajax
		if (isAjax(handler)) {
			try {
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().print(JSON.toJSONString(Result.failure(ret)));
			} catch (IOException e) {
				// do something
			}
			
			view = new ModelAndView();
		} else {
			Map<String, Object> map = new HashMap<String, Object>();  
			map.put("error", ret);//错误信息
	        map.put("base", request.getContextPath());//错误路径
			view = new ModelAndView(errorView, map);//放入error信息
		}
		return view;
	}
	
	/**
	 * 判断是否 ajax 调用
	 * 
	 * @param handler
	 * @return
	 */
	private boolean isAjax(Object handler) {
		if (handler != null && handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ResponseBody.class);  
			return responseBodyAnn != null;
		}
		
		return false;
	}
	
}
