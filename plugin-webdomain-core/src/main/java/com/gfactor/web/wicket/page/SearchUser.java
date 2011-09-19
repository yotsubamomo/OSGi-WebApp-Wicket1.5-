package com.gfactor.web.wicket.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

@AuthorizeInstantiation("ROLE_USER")
public class SearchUser extends WebPage {
	private String userName;
	
	public SearchUser(){		
		add(new Label("userNameMessage",new PropertyModel(this,"userName")));
	}
 
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
