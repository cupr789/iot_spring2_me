package com.iot.spring.common.aspect;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.iot.spring.dao.NaverTransDAO;
import com.iot.spring.dao.impl.NaverTransDAOImpl;

@Service
@Aspect
public class LogPrintAspect {
	
	@Autowired
	NaverTransDAO ntDao;
	
	
	private static final Logger log = LoggerFactory.getLogger(LogPrintAspect.class);
	@Before("execution(* com.iot.spring.controller.*Controller.*(..))")
	public void beforeLog(JoinPoint jp) {
		log.info("@Before =>{}",jp);
	}
	
	@Around("execution(* com.iot.spring.controller.*Controller.*(..))")
	public Object aroundLog(ProceedingJoinPoint pjp) throws IOException {
		log.info("@Around begin");
		Object obj=null;
		long startTime = System.currentTimeMillis();
		try {
			obj=pjp.proceed();
			log.info("정상진행? =>{}",obj);
		} catch (Throwable e) {
			log.error("@Around error=>{}",e);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("errorMsg",ntDao.getText(e.getMessage()));
			return map;
		}
		log.info("@Around end, RunTime : {} ms",System.currentTimeMillis()-startTime);
		return obj;
	}
	
	@After("execution(* com.iot.spring.controller.*Controller.*(..))")
	public void afterLog(JoinPoint jp) {
		log.info("@after =>{}",jp);
	}
}
