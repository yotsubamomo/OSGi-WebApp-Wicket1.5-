package com.gfactor.web.wicket.osgie;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.proxy.IProxyTargetLocator;
import org.osgi.framework.BundleContext;

public class OsgiServiceProxyTargetLocator implements IProxyTargetLocator {

    private static final long serialVersionUID = 1L;
    private String className;
//    private BundleContext context;
    
    public OsgiServiceProxyTargetLocator(String className) {
        this.className = className;
    }
    
//    public OsgiServiceProxyTargetLocator(String className,BundleContext ctx) {
//        this.className = className;
//        this.context = ctx;
//    }
    
    
    @Override
    public Object locateProxyTarget() {
    	
        WebApplication application = (WebApplication) Application.get();
        BundleContext contexts = (BundleContext) application.getServletContext().getAttribute("osgi-bundlecontext");
//        System.out.println("locateProxyTarget contexts = "+ contexts);
//    	System.out.println("locateProxyTarget.... , context ="+ this.context);
        return OsgiServiceLookup.getOsgiService(contexts, className);
    }
}
