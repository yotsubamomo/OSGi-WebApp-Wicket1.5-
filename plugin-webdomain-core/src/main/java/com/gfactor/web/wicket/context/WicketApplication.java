package com.gfactor.web.wicket.context;


import org.apache.wicket.Page;
import org.apache.wicket.application.IClassResolver;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.gfactor.osgi.api.export.util.BundleContextInfoUtil;
import com.gfactor.web.wicket.loader.OsgiResourceStreamLocator;
import com.gfactor.web.wicket.osgie.OsgiComponentInjector;
import com.gfactor.web.wicket.page.LoginPage;

@Component
public class WicketApplication extends AuthenticatedWebApplication  implements ApplicationContextAware {
	private static final String DEFAULT_ENCODING = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(BundleContextInfoUtil.class);

	@Autowired
    private ApplicationContext applicationContext;
	@Autowired
	private IClassResolver osgiClassResolver;
	
	
//	private BundleContext bundleCtx;	
	static ApplicationContext appCtx;
	
	
	
	
	@Override
    public Class<? extends Page> getHomePage() {
//        return home.class;
		return LoginPage.class;
    }
	
	
	
	protected void init() {
		 logger.info("WicketApplication init........");
		 logger.info("osgiClassResolver = "+ osgiClassResolver);
//		   this.getPageSettings().addComponentResolver(new OsgiExtensionPointResolver(this.bundleCtx));
		   super.init(); 
		  
		  
		   this.getApplicationSettings().setClassResolver(osgiClassResolver);
		   this.getResourceSettings().setResourceStreamLocator(new OsgiResourceStreamLocator());
		   OsgiComponentInjector compInj = new OsgiComponentInjector(true);
		   getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		   getComponentInstantiationListeners().add(compInj);
		   
		   getMarkupSettings().setDefaultMarkupEncoding(DEFAULT_ENCODING);
		   
//		   addComponentInstantiationListener(new SpringComponentInjector(this,applicationContext,true));
//		   addComponentInstantiationListener(compInj);
		   	
//		   registerPageService(this);
	}
	
	@Deprecated
	public void registerPageService(WebApplication app){
//		logger.info("register pageService");
//		logger.info("class = "+ IWicketPageService.class.getName());
//		this.bundleCtx.registerService(IWicketPageService.class.getName(), new WicketPageServiceImpl(this), null);
//		logger.info("register pageService finished");
		
//		this.bundleCtx.registerService(IWicketExtensionService.class.getName(),new WicketExtensionServiceImpl(), null);
	}
	
//	@Override
//	public WiQuerySettings getWiQuerySettings() {
//		 WiQuerySettings settings = new WiQuerySettings();
//         settings.setEnableResourcesMerging(true);         
//         return settings;
//
//	}
//	
	
	
	
//	@Override
//    public String getConfigurationType() {
//        return WebApplication.DEVELOPMENT;
//    }
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		
		 this.applicationContext = applicationContext;
		 appCtx =applicationContext;
	}
	
	public static ApplicationContext getApplicationCtx(){
		return appCtx;
	}
	// public static WicketApplication get() {
	// return (WicketApplication) WebApplication.get();
	// }
	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}



	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		logger.info("get web session class....");
		return WebAuthenticatedWebSession.class;
	}

	
//	@Override
//	public void setBundleContext(BundleContext bundleContext) {
//		this.bundleCtx = bundleContext;		
//	}

	
	
}	
