package com.gfactor.auth.xml.internal;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.gfactor.osgi.api.export.util.GetResourceFileUtil;


public class ContextResourceLoader implements ApplicationContextAware {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	

	private ApplicationContext applicationctx;
	private File returnFile = null;
	  
	
	 
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {		
		this.applicationctx = applicationContext;
//		Resource rs = applicationctx.getResource("file:META-INF/UserXml/UserDetail.xml");

	}
	
	public File getResourceFile(String classPathStr) {
		logger.info("ContextResourceLoader getResourceFile str = "+classPathStr);
		Resource rs = applicationctx.getResource(classPathStr);
		logger.info("rs = " + rs);
		
		if(rs != null){
			try {
				returnFile =  rs.getFile();
			} catch (IOException e) {
				returnFile = GetResourceFileUtil.getFileByApplicationContext(classPathStr, applicationctx);
				logger.info("exception ... reget file..= "+returnFile);
				e.printStackTrace();
			} 
		}		
		
		
		return returnFile;		
		
	}

}
