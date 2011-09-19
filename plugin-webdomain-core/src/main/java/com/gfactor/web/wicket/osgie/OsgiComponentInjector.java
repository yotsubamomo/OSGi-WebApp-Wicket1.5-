package com.gfactor.web.wicket.osgie;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.injection.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfactor.osgi.api.export.util.BundleContextInfoUtil;

public class OsgiComponentInjector extends Injector implements
		IComponentInstantiationListener {
    private static final Logger logger = LoggerFactory.getLogger(BundleContextInfoUtil.class);

	private OsgiFieldValueFactory fieldFactory;

	public OsgiComponentInjector() {
		this(true);
		logger.info("OsgiComponentInjector constrcutor.....");
	}

	public OsgiComponentInjector(boolean wrapInProxies) {
		logger.info("OsgiComponentInjector constrcutor.....wrapInProxies = " + wrapInProxies);
		fieldFactory = new OsgiFieldValueFactory(wrapInProxies);
	}

	@Override
	public void inject(Object object) {
		logger.info("OsgiComponentInjector inject.... object = " + object);
		inject(object, fieldFactory);
	}

	public void onInstantiation(Component component) {
		logger.info("OsgiComponentInjector onInstantiation.... component = " + component);
		inject(component);
	}
}