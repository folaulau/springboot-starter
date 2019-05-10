package com.lovemesomecoding.error;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;


/**
 * Handle all async uncaught exception
 * @author fkaveinga
 */
public class CustomAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void handleUncaughtException(Throwable e, Method method, Object... listOfObjects) {
		System.out.println();
		log.error("****** handleUncaughtException(...) ******");
		log.error("*  Class name: "+method.getDeclaringClass());
		log.error("*  Method name - "+method.getName());
		log.error("*  Exception message - "+e.getMessage());
		
		for(Object param : listOfObjects){
			log.error("*  Param - "+param);
		}
		log.error("*************************************");
		System.out.println();
	}

}
