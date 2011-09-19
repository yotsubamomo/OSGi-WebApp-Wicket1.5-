package com.gfactor.locate;

import java.util.HashMap;

import com.gfactor.pageinfo.jpa.Bndpageinfo;

public interface IPageClassLocateService {
	public Class<?> getPageClass(Bndpageinfo bndobj);
	public Class<?> getPageClass(String bundleName,String bundleVer,String entryPoint);
	public Class<?> getPageClass(HashMap<String,String> inputMap);
}
