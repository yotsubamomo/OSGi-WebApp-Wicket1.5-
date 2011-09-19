package com.gfactor.page.html.internal.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class NonePage extends WebPage {
	public NonePage(){
		System.out.println("none page start.....");
		System.out.println("this.getclass = "+this.getClass());
		ResourceReference resource = new PackageResourceReference(NonePage.class,"med.gif");
		System.out.println("resource = "+resource);
		
		add(new Image("med_images",resource)); 
		//add(new Image("img", new ResourceReference(MyPage1.class,"4992433.jpeg")));

	}  
	
}
