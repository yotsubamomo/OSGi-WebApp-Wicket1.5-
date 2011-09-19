package com.gfactor.auth.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gfactor.auth.xml.internal.XmlAcctReader;
import com.gfactor.osgi.api.export.iface.AbstractUserDetailService;
import com.gfactor.osgi.api.export.obj.UserInfoObject;
import com.gfactor.osgi.api.export.util.GetResourceFileUtil;

/**
 * @author momo
 *
 */
public class XmlUserDetailService implements AbstractUserDetailService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	

	private XmlAcctReader xmlReader;
	
	
	public void setXmlReader(XmlAcctReader xmlReader) {
		this.xmlReader = xmlReader;
	}


	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		logger.info("XmlUserDetailService loadUserByUsername = " + username);
		
		UserInfoObject userDetil = xmlReader.getUserDetailFromXML(username);
		
		logger.info("isAccountNonLocked = "+userDetil.isAccountNonLocked());
		logger.info("isAccountNonExpired = "+userDetil.isAccountNonExpired());
		logger.info("isCredentialsNonExpired = "+userDetil.isCredentialsNonExpired());
		logger.info("isEnabled = "+userDetil.isEnabled());
		 
		return userDetil;
	}

}
