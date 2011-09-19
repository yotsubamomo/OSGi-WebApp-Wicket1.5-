package com.gfactor.auth.xml;

import java.io.File;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.gfactor.osgi.api.export.util.GetResourceFileUtil;

public class BundleActivator implements ApplicationContextAware{
	
	private ApplicationContext ctx;
	
	@Autowired
	private XmlUserDetailService service;
	
	public void start(){
		System.out.println("stat service = "+service);
		service.loadUserByUsername("yotsuba");
	}
	
	public void stop(){
		
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
		
	}
}
