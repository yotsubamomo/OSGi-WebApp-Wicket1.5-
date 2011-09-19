package com.gfactor.osgi.api.export.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public final class GetResourceFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(GetResourceFileUtil.class);

    
    /**
     * Get file from given ApplicationContext and fileName string, 
     * If fileName no prefix is specified, the bundle space (osgibundle:) will be used.
     * SpringDM support classpath:,classpath*:,osgibundlejar: and osgibundle:
     * @param fileName
     * @param ctx
     * @return File
     */
	public static File getFileByApplicationContext(String fileName,ApplicationContext ctx){
		return getResourceFile(fileName,ctx);
	}
	
	

	private static File getResourceFile(String classPathStr,ApplicationContext ctx) {
		logger.info("GetResourceFileUtil getResourceFile....");
		logger.info("classpathstr = " + classPathStr);
		logger.info("ApplicationContext = " + ctx);
		
		File returnFile = null;
		Resource rs = ctx.getResource(classPathStr);
		logger.info("Resource = " + rs);
		
		if(rs != null){
			try {
				returnFile =  rs.getFile();
			} catch (IOException e) {
				logger.error("getResourceFile fail... " + e);
			} 
		}
		logger.info("getResourceFile  = " + returnFile);
		
		return returnFile;		
		
	}

}
