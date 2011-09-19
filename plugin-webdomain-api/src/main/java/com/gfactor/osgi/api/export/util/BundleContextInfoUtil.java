package com.gfactor.osgi.api.export.util;

import java.util.HashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BundleContextInfoUtil {
    private static final Logger logger = LoggerFactory.getLogger(BundleContextInfoUtil.class);
    
    
    /**
     * Get HashMap with bundle information , 
     * key as below:
     * [bundle_name] for bundle symbolicName, 
     * [bundle_version] for bundle version string,
     * [id] for bundle active id,
     * @param bundleCtx
     * @return HashMap<String,String>
     */
	public static HashMap<String,String> getBundleInfoMap(BundleContext bundleCtx){
		logger.info("getBundleInfoMap , bundleCtx = " + bundleCtx);
		HashMap<String,String> resultMap = new HashMap<String,String>();
		Bundle bundle = bundleCtx.getBundle();
		
		resultMap.put("bundle_name", bundle.getSymbolicName());
		resultMap.put("bundle_version", bundle.getVersion().toString());
//		resultMap.put("entry_point", bundle.getSymbolicName());
//		resultMap.put("class_name", bundle.getSymbolicName());
		resultMap.put("id",Long.toString(bundle.getBundleId()));
		
		logger.info("getBundleInfoMap , resultMap = " + resultMap);
		return resultMap;
	}
	
	 /**
     * Get HashMap with bundle information , 
     * key as below: 
     * [bundle_name] for bundle symbolicName, 
     * [bundle_version] for bundle version string,
     * [entry_point] for bundle page "in" entrypoint, 
     * [class_name] class name with fully package name for bundle page entrypoint identify.,
     * [id] for bundle active id,
     * @param bundleCtx
     * @return HashMap<String,String>
     */
	public static HashMap<String, String> getBundleInfoMapWithClassAndEntrypoint(
			BundleContext bundleCtx, String className, String entryPoint) {
		
		logger.info("getBundleInfoMapWithClassAndEntrypoint , bundleCtx = " + bundleCtx);
		
		
		HashMap<String,String> resultMap = new HashMap<String,String>();
		Bundle bundle = bundleCtx.getBundle();
		
		resultMap.put("bundle_name", bundle.getSymbolicName());
		resultMap.put("bundle_version", bundle.getVersion().toString());
		resultMap.put("entry_point",entryPoint);
		resultMap.put("class_name", className);
		resultMap.put("id",Long.toString(bundle.getBundleId()));		

		
		logger.info("getBundleInfoMapWithClassAndEntrypoint , resultMap = " + resultMap);
		return resultMap;
	}
	
	/**
	 * Get bundle SymbolicName from given bundlecontext
	 * @param bundleCtx
	 * @return String (bundle SymbolicName)
	 */
	public static String getBundleSymbolicName(BundleContext bundleCtx){
		Bundle bundle = bundleCtx.getBundle();
		return bundle.getSymbolicName();
	}
	
	/**
	 * Get bundle Version string from given bundlecontext
	 * @param bundleCtx
	 * @return String (bundle version string)
	 */
	public static String getBundleVersionString(BundleContext bundleCtx){
		Bundle bundle = bundleCtx.getBundle();
		return bundle.getVersion().toString();
	}
	
	/**
	 * Get bundle id string from given bundlecontext
	 * @param bundleCtx
	 * @return String (bundle id)
	 */
	public static long getBundleLongId(BundleContext bundleCtx){
		Bundle bundle = bundleCtx.getBundle();
		return bundle.getBundleId();
	}
	
	
}
