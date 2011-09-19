/**
 * 
 */
package com.gfactor.pageinfo.service.internal.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gfactor.pageinfo.dao.RegWicketPageBndDao;
import com.gfactor.pageinfo.jpa.Bndpageinfo;
import com.gfactor.pageinfo.service.IGetPageObjectInfoService;

/**
 * @author momo
 *
 */
public class GetPageObjectInfoServiceImpl implements IGetPageObjectInfoService {
	
	@Autowired
	private RegWicketPageBndDao regWicketPageBndDao;
	
	@Override
	@Transactional
	public Bndpageinfo getBndPageInfo(String bndName, String bndVer,
			String entryPoint) {
		System.out.println("GetPageObjectInfoServiceImpl getBndPageInfo start ...");
		Bndpageinfo bndPageInfo = regWicketPageBndDao.getBndPageInfo(bndName, bndVer, entryPoint);
		System.out.println("GetPageObjectInfoServiceImpl return  =" + bndPageInfo);
		
		return bndPageInfo;
	}

}
