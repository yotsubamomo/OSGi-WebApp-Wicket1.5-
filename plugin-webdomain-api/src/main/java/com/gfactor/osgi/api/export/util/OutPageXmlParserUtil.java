package com.gfactor.osgi.api.export.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutPageXmlParserUtil {
    private static final Logger logger = LoggerFactory.getLogger(OutPageXmlParserUtil.class);
    
    /**
     * Get outpage information on xml file with className(e.g Test), it will return hashmap
     * Key as below : [bundleSymbolicName],[entry_point],[version]
     * @param fullyClassName
     * @param outPage
     * @param xmlFile
     * @return HashMap<String,String>
     */
    public static HashMap<String,String> getOutPageInfoMapByClassName(String className,String outPage,File xmlFile){
    	HashMap<String,String> resultMap = new HashMap<String,String>();
    	resultMap = checkXmlFileInfo(className,outPage,xmlFile,false);
    	return resultMap;
    }
	
    /**
     * Get outpage information on xml file with fully class name (e.g com.my.Test), it will return hashmap
     * Key as below : [bundleSymbolicName],[entry_point],[version]
     * @param fullyClassName
     * @param outPage
     * @param xmlFile
     * @return HashMap<String,String>
     */
    public static HashMap<String,String> getOutPageInfoMapByFullyClassName(String fullyClassName,String outPage,File xmlFile){
    	HashMap<String,String> resultMap = new HashMap<String,String>();
    	resultMap = checkXmlFileInfo(fullyClassName,outPage,xmlFile,true);
    	return resultMap;
    }
    
	private static HashMap<String, String> checkXmlFileInfo(String className,String outPage,File xmlFile,boolean isFullayClassName) {
		logger.info("checkXmlFileInfo start..");
		
		logger.info("className = " + className);
		logger.info("outPage = " + outPage);
		logger.info("xmlFile = " + xmlFile.exists());
		
		
		HashMap<String, String> resultMap = new HashMap<String, String>();
		Document docJDOM=null;
		SAXBuilder bSAX = new SAXBuilder(false);
		
		try {
			docJDOM = bSAX.build(xmlFile);
		} catch (JDOMException e) {
			logger.error("Jdom parser fail . "  + e);
		} catch (IOException ex) {
			logger.error("checkXml File fail. "+ ex);
		}
		
		
		Element elmtRoot = docJDOM.getRootElement();
		List<Element> allChildren = elmtRoot.getChildren();			
		logger.info("allChildren size = " +allChildren.size());
		
		//parse root element name <outpage></outpage>
		for (int i = 0; i < allChildren.size(); i++) {
			logger.info(allChildren.get(i).getAttributeValue("name"));			
			
			if(classNameExists(allChildren.get(i).getAttributeValue("name"),className,isFullayClassName)){								
				List<Element> children = allChildren.get(i).getChildren();
				logger.info("children size = " + children.size());
				
				for (int j = 0; j < children.size(); j++) {											
					if(outPageExists(children.get(j).getAttributeValue("pagename"),outPage)){
						
						final String entry_point = children.get(j).getChild("entry_point").getText();
						final String bundleSymbolicName = children.get(j).getChild("bundleSymbolicName").getText();
						final String version = children.get(j).getChild("version").getText();
						resultMap.put("entry_point", entry_point);
						resultMap.put("bundleSymbolicName", bundleSymbolicName);
						resultMap.put("version", version);
						break;
					}
				}
								
			}//end of parse root children element			
		}//end of parse root element loop
		
//		System.out.println("OutPageXmlParserUtil resultMap = " + resultMap);
		logger.info("OutPageXmlParserUtil resultMap = " + resultMap);
		return resultMap;
	}


	private static boolean classNameExists(String xmlAttributeName,String className,boolean isFullayClassName) {
		logger.info("classNameExists xmlAttributeName = "+ xmlAttributeName);
		logger.info("classNameExists className = "+ className);
		logger.info("classNameExists isFullyClassName = "+ isFullayClassName);
		if(isFullayClassName) return xmlAttributeName.equals(className);
		
		String tmpName =xmlAttributeName;
		tmpName = tmpName.substring(tmpName.lastIndexOf(".")+1, tmpName.length());
		logger.info("classNameExists , not fully , tmpname = " + tmpName + " ,  className = "+ className);
		return tmpName.equals(className);
	}
    
	private static boolean outPageExists(String xmlAttributeName,String outPage) {
		logger.info("outPageExists = "+ outPage);
		if(xmlAttributeName.equals(outPage)) return true;
		return false;		
	}
	
	
	public static void main(String args[]) {
		File f = new File("OutPageInfo.xml");
		System.out.println("f = "+ f.exists());
//		getOutPageInfoMapByFullyClassName("com.gfactor.service.Home",f,true);
		getOutPageInfoMapByClassName("Home","homeout2",f);
		
	}
	
	
}
