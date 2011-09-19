/**
 * 
 */
package com.gfactor.locate.internal;

import java.util.HashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.osgi.context.BundleContextAware;

import com.gfactor.locate.IPageClassLocateService;
import com.gfactor.pageinfo.jpa.Bndpageinfo;
import com.gfactor.pageinfo.service.IGetPageObjectInfoService;

/**
 * @author momo
 *
 */
public class PageClassLocateServiceImpl implements IPageClassLocateService ,BundleContextAware{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	

	private BundleContext bundleCtx;
	
	@Autowired
	private IGetPageObjectInfoService getPageObjectInfoService;
	
	
	public Class<?> getPageClass(Bndpageinfo bndobj) {
		Bundle bundle = bundleCtx.getBundle(bndobj.getId());		
		logger.info("to get bundle by id = " +bndobj.getId() +" , bundle ="+bundle);
		
		Class<?> clazz = null;
		try {
			logger.info("loading class name = " + bndobj.getClass_name());
			 clazz = bundle.loadClass(bndobj.getClass_name());
			 
			 logger.info("locate bundleclass on " +bundle.getSymbolicName()+ " = "+ clazz);
			 
		} catch (ClassNotFoundException e) {
			logger.error("no class found..." + e);
			e.printStackTrace();
		}
		
		return clazz;
	}
	
	public Class<?> getPageClass(String bundleName, String bundleVer,String entryPoint) {
		
		Bndpageinfo bndobj = getPageObjectInfoService.getBndPageInfo(bundleName, bundleVer, entryPoint);		
		Class<?> clazz = getPageClass(bndobj);
		return clazz;
	}
	
	@Override
	public Class<?> getPageClass(HashMap<String, String> inputMap) {
		Bndpageinfo bndobj = getPageObjectInfoService.getBndPageInfo(inputMap.get("bundleSymbolicName"), inputMap.get("version"), inputMap.get(""));
		Class<?> clazz = getPageClass(bndobj);
		return clazz;
	}
	
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleCtx = bundleContext;
	}

}
