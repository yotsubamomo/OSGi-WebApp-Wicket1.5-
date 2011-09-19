/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gfactor.web.wicket.loader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Application;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.application.DefaultClassResolver;
import org.apache.wicket.application.IClassResolver;
import org.apache.wicket.util.string.Strings;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.osgi.context.BundleContextAware;

import com.gfactor.osgi.api.export.util.BundleContextInfoUtil;

/** 
 * @author mkalina
 * 
 */
public class OsgiClassResolver implements IClassResolver,BundleContextAware {

	private DefaultClassResolver wrappedClassResolver;
	private BundleContext bundleCtx;
    private static final Logger logger = LoggerFactory.getLogger(BundleContextInfoUtil.class);

	public OsgiClassResolver() {
		this.wrappedClassResolver = new DefaultClassResolver();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.wicket.application.IClassResolver#getResources(java.lang.String)
	 */
	public Iterator<URL> getResources(String name) {
		logger.info("getResources = " + name);
		HashSet<URL> loadedFiles = new HashSet<URL>();
		try {
			// Try the classloader for the wicket jar/bundle
			Enumeration<URL> resources = Application.class.getClassLoader().getResources(name);
			loadResources(resources, loadedFiles);
			
			logger.info("resources 1 = "  + resources);
			logger.info("loadedFiles = "  + loadedFiles);
			
			// Try the classloader for the user's application jar/bundle
			resources = Application.get().getClass().getClassLoader().getResources(name);
			logger.info("resources 2 = "  + resources);
			
			loadResources(resources, loadedFiles);

			// Try the context class loader
			resources = Thread.currentThread().getContextClassLoader().getResources(name);
			logger.info("resources 3 = "  + resources);
			loadResources(resources, loadedFiles);
		} catch (IOException e) {
			throw new WicketRuntimeException(e);
		}

		return loadedFiles.iterator();
	}
	
	/**
	*
	* @param resources
	* @param loadedFiles
	*/
	private void loadResources(Enumeration<URL> resources, Set<URL> loadedFiles) {
		if (resources != null) {
			while (resources.hasMoreElements()) {
				final URL url = resources.nextElement();
				if (!loadedFiles.contains(url)) {
					logger.info("loadResources add = > "+ url);
					loadedFiles.add(url);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.apache.wicket.application.IClassResolver#resolveClass(java.lang.String)
	 */ 
	public Class<?> resolveClass(String classname)
			throws ClassNotFoundException {
		logger.info("resolveClass = " + classname);
		Class<?> clazz = null;

		try {
			ClassLoader loader = Application.get().getClass().getClassLoader();
			logger.info("clazz2 loader = "+ loader);
			Class<?> clazz2 = Class.forName(classname, false, loader);
			logger.info("clazz2 = "+ clazz2);
			logger.info("");
			clazz = this.wrappedClassResolver.resolveClass(classname);
			logger.info("clazz = "+ clazz);
		} catch (ClassNotFoundException e) {

			// not found in parent classloader? look through the bundles...
			logger.info("resolveClass for bundles");
			logger.info("bundle context = "+ bundleCtx);
				
			Bundle[] bundles = bundleCtx.getBundles();
			logger.info("bundles = "+bundles);
			if (bundles != null && bundles.length > 0) {
				logger.info("bundles[] length = "+bundles.length);
				for (Bundle bundle : bundles) {
//					logger.info("   -> bundle id= = "+ bundle.getBundleId());
					if (bundle.getState() != Bundle.ACTIVE
							|| !this.classIsExportedByBundle(classname, bundle))
						continue;

					try {
						clazz = bundle.loadClass(classname);
						if (clazz != null)
							break;
					} catch (ClassNotFoundException ex) {
						; // ignore and try next bundle..
					}
				}
			}

		}

		if (clazz == null)
			throw new ClassNotFoundException(classname);

		return clazz;
	}

	private boolean classIsExportedByBundle(String classname, Bundle bundle) {
		List<String> exportedPackages = this.getExportedPackages(bundle);
		return exportedPackages.contains(Strings.beforeLast(classname, '.'));
	}

	private List<String> getExportedPackages(Bundle bundle) {
		String exportedString = (String) bundle.getHeaders().get(
				"Export-Package");
		if (Strings.isEmpty(exportedString))
			return Collections.emptyList();

		String[] splitted = Strings.split(exportedString, ',');
		if (splitted == null || splitted.length == 0)
			return Collections.emptyList();

		List<String> packages = new ArrayList<String>();
		for (String s : splitted) {
			String pkg = null;
			if (s.contains(";"))
				pkg = Strings.beforeFirst(s, ';').trim();
			else
				pkg = s.trim();

			if (pkg != null && pkg.length() > 0)
				packages.add(pkg);
		}

		return packages;
	}

	@Override
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleCtx = bundleContext;
		
	}
}
