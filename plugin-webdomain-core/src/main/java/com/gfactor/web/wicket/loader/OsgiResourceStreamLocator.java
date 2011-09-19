package com.gfactor.web.wicket.loader;

import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.locator.ResourceStreamLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfactor.osgi.api.export.util.BundleContextInfoUtil;

public class OsgiResourceStreamLocator extends ResourceStreamLocator {
    private static final Logger logger = LoggerFactory.getLogger(BundleContextInfoUtil.class);

	/**
	 * Construct.
	 */
	public OsgiResourceStreamLocator() {
	}

	/**
	 * Construct.
	 * 
	 * @param finder
	 */
	public OsgiResourceStreamLocator(final IResourceFinder finder) {
		super(finder);
	}

	/**
	 * 
	 * @see org.apache.wicket.util.resource.locator.ResourceStreamLocator#locate(java.lang.Class,
	 *      java.lang.String)
	 */
	@Override
	public IResourceStream locate(final Class<?> clazz, final String path) {
		logger.info("IResourceStream = " + clazz + ", path-" + path);
		return super.locate(clazz, "/" + path);
	}
}
