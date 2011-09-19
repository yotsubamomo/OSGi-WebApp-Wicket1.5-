package com.gfactor.pageinfo.service;

import com.gfactor.pageinfo.jpa.Bndpageinfo;


public interface IGetPageObjectInfoService {
	public Bndpageinfo getBndPageInfo(String bndName,String bndVer,String entryPoint);
}
