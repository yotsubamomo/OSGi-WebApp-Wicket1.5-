package com.gfactor.tracker.internal;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.osgi.context.BundleContextAware;

import com.gfactor.pageinfo.service.IRegisterWicketPageBndIdentify;
import com.gfactor.tracker.iface.RegisterEntryPoint;

public class PageRegisterServiceTracker implements BundleContextAware {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	
	private ServiceTracker<RegisterEntryPoint, Object> tracker;
	private BundleContext bundleCtx;
	
	@Autowired
	private IRegisterWicketPageBndIdentify iRegisterWicketPageBndIdentify;

//	public PageRegisterServiceTracker(BundleContext bundleContext) throws InvalidSyntaxException {
//        this.bundleContext = bundleContext;
//        this.tracker = new ServiceTracker<Snap, Object>(bundleContext, createFilter(bundleContext), new OsgiSnapRegistryCustomizer());
//    }
	
	
	public void init(){
		logger.info("PageRegisterServiceTracker open init...");
		createTracker();
		this.tracker.open();
//		
//		Thread t = new Thread(){
//			public void run(){
//				try {
//					Thread.sleep(1000);
//					// Get a list of the RegisterEntryPoint services and notify them.
//					Object[] services = tracker.getServices();
//					if (services != null) {
//						for(Object s : services) {
//							RegisterEntryPoint reg = (RegisterEntryPoint)s;
//							logger.info("PageRegisterServiceTracker init, tracker sevice result = " + reg.getRegisterPageInfoMap() );
//						}
//					}
//				} catch (InterruptedException e) {
//					// Ignore interruptions.
//					logger.error("ServiceTracker Exception =  "+ e);
//				}
//			}
//			
//		};
	}
	
	public void destroy(){
		logger.info("PageRegisterServiceTracker destroy ...");
		this.tracker.close();
	}
	
	private void createTracker(){
		OsgiPageInforRegisterTracker customer = new OsgiPageInforRegisterTracker(this.bundleCtx);
		customer.setiRegisterWicketPageBndIdentify(iRegisterWicketPageBndIdentify);
		this.tracker = new ServiceTracker<RegisterEntryPoint, Object>(bundleCtx, RegisterEntryPoint.class, customer);
//		this.tracke
	}
	
	@Override
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleCtx = bundleContext;		
	}
	
	
	
}
