/**
 * 
 */
package com.gfactor.page.main.internal;

import java.io.File;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.gfactor.osgi.api.export.iface.IGetOutPageInfoService;
import com.gfactor.osgi.api.export.util.GetResourceFileUtil;
import com.gfactor.osgi.api.export.util.OutPageXmlParserUtil;

/**
 * @author momo
 *
 */
class GetOutPageInfoServiceImpl implements IGetOutPageInfoService,ApplicationContextAware {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	

	private ApplicationContext ctx;
	

	 /**
     * Get outpage information on xml file with fully class name (e.g com.my.Test), it will return hashmap
     * Key as below : [bundleSymbolicName],[entry_point],[version]
     * @param fullyClassName
     * @param outPage
     * @return HashMap<String,String>
     */
	@Override
	public HashMap<String, String> getOutPageInfoMap(String className,String outPage){
		File xmlFile = GetResourceFileUtil.getFileByApplicationContext("classpath:/META-INF/OutPageInfo.xml", ctx);
		return OutPageXmlParserUtil.getOutPageInfoMapByFullyClassName(className, outPage, xmlFile);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;		
	}

}
