package com.gfactor.auth.xml.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import com.gfactor.osgi.api.export.obj.UserInfoObject;
import com.gfactor.osgi.api.export.util.GetResourceFileUtil;

public class XmlAcctReader implements ApplicationContextAware {
	@Autowired
	private ApplicationContext ctx;
//	private ContextResourceLoader ctxRsLoader;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	
	
//	public void setCtxRsLoader(ContextResourceLoader ctxRsLoader) throws IOException {
//		logger.info("XmlAcctReader setCtxRsLoader ctxRsLoader = "+ ctxRsLoader);
//		this.ctxRsLoader = ctxRsLoader;		
//	}
	
	
	public UserInfoObject getUserDetailFromXML(String username){
		UserInfoObject userDetail = null;
		try{
			
//			File xmlFile = ctxRsLoader.getResourceFile("/META-INF/UserXml/UserDetail.xml");
			File xmlFile = GetResourceFileUtil.getFileByApplicationContext("classpath:/META-INF/UserXml/UserDetail.xml", this.ctx);
			logger.info("getUserDetailFromXML xmlFile ="+ xmlFile);
//			File xmlFile = new File("D:/EclipseJava-Project/plugin-webdomain-auth-xml/src/main/resources/META-INF/UserXml/UserDetail.xml");
			Document docJDOM=null;
			SAXBuilder bSAX = new SAXBuilder(false);
			docJDOM = bSAX.build(xmlFile);
			Element elmtRoot = docJDOM.getRootElement();
			List<Element> allChildren = elmtRoot.getChildren();			
			
			logger.info("allChildren size = " +allChildren.size());
			
			for (int i = 0; i < allChildren.size(); i++) {
//				List<Element> children = allChildren.get(i).getChildren();
//				System.out.println(children.get(0).getText());
				if(username.equals(allChildren.get(i).getChild("Username").getText())){
					List<Element> role = allChildren.get(i).getChild("GrantedAuthorities").getChildren();
					List<GrantedAuthority> grant = new ArrayList<GrantedAuthority>();					
					for (int j = 0; j < role.size(); j++) {
						if(role.get(j).getText() != null && role.get(j).getText().length() >0){
							grant.add(new GrantedAuthorityImpl(role.get(j).getText()));
						}
					}
					final String passwd = allChildren.get(i).getChild("Password").getText();
					final boolean enabled = getBooleanValue(allChildren.get(i).getChild("Enable").getText());
					final boolean accountNonExpired = getBooleanValue(allChildren.get(i).getChild("AccountNonExpired").getText());
					final boolean credentialsNonExpired = getBooleanValue(allChildren.get(i).getChild("CredentialsNonExpired").getText());
					final boolean accountNonLocked = getBooleanValue(allChildren.get(i).getChild("AccountNonLocked").getText());
					final String mail = allChildren.get(i).getChild("Mail").getText();
					
					userDetail = new UserInfoObject(username,passwd , enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grant);					
					userDetail.setMail(mail);
					
				}else{
					//do something...
				}
				
				
			}
			logger.info(userDetail.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return userDetail;
	}
	
	private boolean getBooleanValue(String value){
		return value.equals("true") ? true : false;
	}
	
	public static void main(String[] args) {
		XmlAcctReader reader = new XmlAcctReader();
		reader.getUserDetailFromXML("yotsuba");
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
		
	}
	
}
