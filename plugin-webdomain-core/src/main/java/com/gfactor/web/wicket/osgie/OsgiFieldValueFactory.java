package com.gfactor.web.wicket.osgie;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.proxy.LazyInitProxyFactory;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfactor.osgi.api.export.util.BundleContextInfoUtil;

public class OsgiFieldValueFactory implements IFieldValueFactory {
    private static final Logger logger = LoggerFactory.getLogger(BundleContextInfoUtil.class);

    private final boolean wrapInProxies;
//    private BundleContext context;
    /**
     * @param contextLocator
     *            spring context locator
     */
    public OsgiFieldValueFactory(BundleContext context) {
        this(true);
    }

    /**
     * @param contextLocator
     *            spring context locator
     * @param wrapInProxies
     *            whether or not wicket should wrap dependencies with specialized proxies that can
     *            be safely serialized. in most cases this should be set to true.
     */
    public OsgiFieldValueFactory(boolean wrapInProxies) {
        this.wrapInProxies = wrapInProxies;
    }
    
//    public OsgiFieldValueFactory(boolean wrapInProxies,BundleContext ctx) {
//        this.wrapInProxies = wrapInProxies;
//        this.context = ctx;
//    }
    /**
     * @see org.apache.wicket.injection.IFieldValueFactory#getFieldValue(java.lang.reflect.Field,
     *      java.lang.Object)
     */
    public Object getFieldValue(Field field, Object fieldOwner) {
    	logger.debug("get field value ..");
    	logger.debug("field =" + field);
    	logger.debug("fieldOwner = "+ fieldOwner);
        if (field.isAnnotationPresent(Inject.class)) {
            OsgiServiceProxyTargetLocator locator = new OsgiServiceProxyTargetLocator(field.getType().getName());


            final Object target;
            if (wrapInProxies) {
                target = LazyInitProxyFactory.createProxy(field.getType(), locator);
            }
            else {
                target = locator.locateProxyTarget();
            }

            return target;
        }
        return null;
    }

    /**
     * @see org.apache.wicket.injection.IFieldValueFactory#supportsField(java.lang.reflect.Field)
     */
    public boolean supportsField(Field field) {
        return field.isAnnotationPresent(Inject.class);
    }
}
