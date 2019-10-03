package com.example.releasenotes.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggerHandlerInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LoggerHandlerInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String repository = parameterMap.get("repository")[0];
		String milestone = parameterMap.get("milestone")[0];
		logger.debug("Release notes generation requested for project: " + repository + " for milestone: " + milestone);
		return true;
	}

}
