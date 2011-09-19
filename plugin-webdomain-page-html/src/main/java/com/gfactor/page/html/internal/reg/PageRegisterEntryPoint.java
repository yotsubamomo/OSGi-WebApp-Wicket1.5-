package com.gfactor.page.html.internal.reg;

import java.util.HashMap;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.osgi.context.BundleContextAware;

import com.gfactor.osgi.api.export.util.BundleContextInfoUtil;
import com.gfactor.page.html.internal.page.NonePage;
import com.gfactor.tracker.iface.RegisterEntryPoint;

/**
 * @author momo
 *
 */
public class PageRegisterEntryPoint implements RegisterEntryPoint ,BundleContextAware{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	
	private BundleContext bundleContext;
	/* (non-Javadoc)
	 * @see com.gfactor.tracker.iface.RegisterEntryPoint#getRegisterPageInfoMap()
	 */
	@Override
	public HashMap<String, String> getRegisterPageInfoMap() {
		logger.info("PageRegisterEntryPoint , getMap....");
		HashMap<String,String> resultMap = BundleContextInfoUtil.getBundleInfoMapWithClassAndEntrypoint(this.bundleContext,NonePage.class.getName(), "imagePage1");
		
		logger.info("PageRegisterEntryPoint map = "+resultMap);
		return resultMap;
	}
	
	
	@Override
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		
	}
}
