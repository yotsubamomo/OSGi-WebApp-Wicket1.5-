package com.gfactor.tracker.internal;

import java.util.HashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfactor.pageinfo.jpa.Bndpageinfo;
import com.gfactor.pageinfo.service.IRegisterWicketPageBndIdentify;
import com.gfactor.tracker.iface.RegisterEntryPoint;


public final class OsgiPageInforRegisterTracker implements ServiceTrackerCustomizer<RegisterEntryPoint, Object> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	
	private BundleContext bundleCtx;
	private RegisterThread thread;
	
//	@Autowired
	private IRegisterWicketPageBndIdentify iRegisterWicketPageBndIdentify;

	public OsgiPageInforRegisterTracker(BundleContext context){
		this.bundleCtx = context;
	}
	
	@Override
	public Object addingService(ServiceReference<RegisterEntryPoint> reference) {
		RegisterEntryPoint service = (RegisterEntryPoint) bundleCtx.getService(reference);
		logger.info("iRegisterWicketPageBndIdentify= "+ iRegisterWicketPageBndIdentify);
		logger.info("RegisterThread , servic ..reference =  "  + reference);			
		iRegisterWicketPageBndIdentify.clearExpiredPageInfo(getServiceBndObject(service));
		iRegisterWicketPageBndIdentify.registerPageInfo(getServiceBndObject(service));
//		thread = new RegisterThread(service);
//		thread.start();
		return service;
	}

	@Override
	public void modifiedService(ServiceReference<RegisterEntryPoint> reference, Object service) {
		logger.info("modifiedService , servic ..reference,Object  =  "  + reference+", "+ service);
		// removedService(reference, service);
		// addingService(reference);		
	}

	@Override
	public void removedService(ServiceReference<RegisterEntryPoint> reference, Object service) {
		logger.info("removedService , servic ..reference,Object  =  "  + reference+", "+ service);
		RegisterEntryPoint removeService = (RegisterEntryPoint) bundleCtx.getService(reference);
		iRegisterWicketPageBndIdentify.unregisterPageInfo(getServiceBndObject(removeService));
		bundleCtx.ungetService(reference);
//		thread.stopThread();
		
	}
	
	private Bndpageinfo getServiceBndObject(RegisterEntryPoint service){
		Bndpageinfo bnd = new Bndpageinfo();
		HashMap<String,String> infoMap = service.getRegisterPageInfoMap();
		
		bnd.setBundle_name(infoMap.get("bundle_name"));
		bnd.setBundle_version(infoMap.get("bundle_version"));
		bnd.setClass_name(infoMap.get("class_name"));
		bnd.setEntry_point(infoMap.get("entry_point"));
		bnd.setId(Long.parseLong(infoMap.get("id")));
		
		logger.info("getServiceBndObject map = "+ infoMap);
		logger.info("getServiceBndObject map to bndobj = "+ bnd);
		
		return bnd;
	}
	
	public IRegisterWicketPageBndIdentify getiRegisterWicketPageBndIdentify() {
		return iRegisterWicketPageBndIdentify;
	}

	public void setiRegisterWicketPageBndIdentify(
			IRegisterWicketPageBndIdentify iRegisterWicketPageBndIdentify) {
		this.iRegisterWicketPageBndIdentify = iRegisterWicketPageBndIdentify;
	}


	public static class RegisterThread extends Thread {
		private final Logger log = LoggerFactory.getLogger(RegisterThread.class);	
		private volatile boolean active = true;
		private final RegisterEntryPoint service;

		public RegisterThread(RegisterEntryPoint service) {
			this.service = service;
		}

		public void run() {
			while (active) {
//				System.out.println(service.getQuote());
				log.info("RegisterThread , service = "  + service.getRegisterPageInfoMap());
				try {
					Thread.sleep(5000);
				} catch (Exception e) {
					log.error("Thread interrupted " + e.getMessage());
				}
			}
		}

		public void stopThread() {
			active = false;
		}
	}
	
}