/**
 * 
 */
package com.gfactor.page.main.internal;

import com.gfactor.osgi.api.export.iface.IGetMainPageClassService;
import com.gfactor.page.main.HomePage;

/**
 * @author momo
 *
 */ 
public class GetMainPageServiceImpl implements IGetMainPageClassService {
 
	/* (non-Javadoc)
	 * @see com.gfactor.export.IGetPageClassService#getPageClazz()
	 */
	public Class<?> getPageClazz() {		
		return HomePage.class;
	}

}
