package com.gfactor.pageinfo.service;

import com.gfactor.pageinfo.jpa.Bndpageinfo;

public interface IRegisterWicketPageBndIdentify {
	public void clearExpiredPageInfo(Bndpageinfo bnd);
	public boolean registerPageInfo(Bndpageinfo bnd);
	public boolean unregisterPageInfo(Bndpageinfo bnd);
	
}
  