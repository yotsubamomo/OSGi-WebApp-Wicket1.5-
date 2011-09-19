package com.gfactor.locate.internal;

import org.springframework.beans.factory.annotation.Autowired;

import com.gfactor.locate.IPageClassLocateService;

public class BundleActivator {
	
	@Autowired
	private IPageClassLocateService pageLocateServiceS;
	
	public void start(){ 
		System.out.println("===== > start page locate service chk ..." + pageLocateServiceS);
		Class<?> clazz = pageLocateServiceS.getPageClass("plugin-webdomain-mainpage","1.0.0","mains");
		System.out.println("BundleActivator get = "+ clazz);
	}  
 	
	public void stop(){
		
	}
	
}
