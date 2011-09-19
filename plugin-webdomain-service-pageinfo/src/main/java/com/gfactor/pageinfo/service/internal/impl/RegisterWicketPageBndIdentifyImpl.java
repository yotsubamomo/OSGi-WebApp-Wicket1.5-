/**
 * 
 */
package com.gfactor.pageinfo.service.internal.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gfactor.pageinfo.dao.RegWicketPageBndDao;
import com.gfactor.pageinfo.jpa.Bndpageinfo;
import com.gfactor.pageinfo.service.IRegisterWicketPageBndIdentify;

/**
 * @author momo
 *
 */
public class RegisterWicketPageBndIdentifyImpl implements
		IRegisterWicketPageBndIdentify {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	

	@Autowired 
	private RegWicketPageBndDao regWicketPageBndDao;
	
	/* (non-Javadoc)
	 * @see com.gfactor.service.iface.IRegisterWicketPageBndIdentify#registerPageInfo()
	 */
	
	@Transactional
	public boolean registerPageInfo(Bndpageinfo bnd) {
		Bndpageinfo bndObj = regWicketPageBndDao.findByEntry(bnd);
//		System.out.println("find by entry = "+regWicketPageBndDao.findByEntry(bnd));
//		
//		System.out.println("registerPageInfo , bnd = "+bnd);	
//		Bndpageinfo bndObj = regWicketPageBndDao.getBndPageInfo(bnd.getBundle_name(), bnd.getBundle_version(), bnd.getEntry_point());
//		System.out.println("registerPageInfo , bndObj = "+bndObj);
////		List<Bndpageinfo> findObj = regWicketPageBndDao.findBndPageInfoList(bnd.getBundle_name(), bnd.getBundle_version(), bnd.getEntry_point());
////		System.out.println("registerPageInfo , check findObj size = "+findObj.size());
//		  
		if(bndObj==null){
			logger.info("register page info.......");
			Bndpageinfo testobj = new Bndpageinfo();
			testobj.setId(bnd.getId());
			testobj.setBundle_name(bnd.getBundle_name());
			testobj.setBundle_version(bnd.getBundle_version());
			testobj.setClass_name(bnd.getClass_name());
			testobj.setEntry_point(bnd.getEntry_point());
			logger.info("bnd ================> " +testobj);
//			regWicketPageBndDao.saveBndPageInfo(bnd);
			regWicketPageBndDao.saveBndPageInfo(testobj);		
			return true;
		}else{
			logger.info("page info already exists..........");
			return false;
		}

		
		
	}

	
	/* (non-Javadoc)
	 * @see com.gfactor.service.iface.IRegisterWicketPageBndIdentify#unregisterPageInfo()
	 */
	@Transactional
	public boolean unregisterPageInfo(Bndpageinfo bnd) {
//		Bndpageinfo bndObj = regWicketPageBndDao.getBndPageInfo(bnd.getBundle_name(), bnd.getBundle_version(), bnd.getEntry_point());
		Bndpageinfo bndObj = regWicketPageBndDao.findByEntry(bnd);

//		List<Bndpageinfo> findObj = regWicketPageBndDao.findBndPageInfoList(bnd.getBundle_name(), bnd.getBundle_version(), bnd.getEntry_point());
//		System.out.println("unregisterPageInfo , check findObj size = "+findObj.size());
		if(bndObj!=null){
			logger.info("unregister page info....");
			regWicketPageBndDao.delete(bndObj);
			return true;
		}else{
			logger.info("no page info delete , object doesn't exists.");
			return false;
		}
		
		
	}


	@Transactional
	public void clearExpiredPageInfo(Bndpageinfo bnd) {
		logger.info("clearExpiredPageInfo , object = " + bnd.toString());
		regWicketPageBndDao.deleteExpiredBndPageInfo(bnd);
	}

}
