package com.gfactor.osgi.api.export.iface;

import java.util.HashMap;

public interface IGetOutPageInfoService {
	public HashMap<String,String> getOutPageInfoMap(String className,String outPage);
}
