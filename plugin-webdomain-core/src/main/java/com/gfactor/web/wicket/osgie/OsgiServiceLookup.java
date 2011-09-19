package com.gfactor.web.wicket.osgie;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.wicket.WicketRuntimeException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfactor.osgi.api.export.util.BundleContextInfoUtil;

public class OsgiServiceLookup {
    private static final Logger logger = LoggerFactory.getLogger(BundleContextInfoUtil.class);

    public static final long DEFAULT_TIMEOUT = 10000;

    public static <T> T getOsgiService(BundleContext bc, String className) {
        return getOsgiService(bc, className, DEFAULT_TIMEOUT, null);
    }

    public static <T> T getOsgiService(BundleContext bc, Class<T> type) {
        return getOsgiService(bc, type, DEFAULT_TIMEOUT);
    }

    public static <T> T getOsgiService(BundleContext bc, Class<T> type, Map<String, String> props) {
        return getOsgiService(bc, type, DEFAULT_TIMEOUT, props);
    }

    public static <T> T getOsgiService(BundleContext bc, Class<T> type, long timeout, Map<String, String> props) {
        return getOsgiService(bc, type.getName(), timeout, props);
    }

    public static <T> T getOsgiService(BundleContext bc, Class<T> type, long timeout) {
        return getOsgiService(bc, type.getName(), timeout, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getOsgiService(BundleContext bc, String className, long timeout, Map<String, String> props) {
    	logger.debug("getosgiservice...");
    	logger.debug("BundleContext = " + bc);
    	logger.debug("className = " + className);
    	logger.debug("props = " + props);
    	
        ServiceTracker tracker = createServiceTracker(bc, className, props);
        
        try {
            tracker.open();
            Object svc = tracker.waitForService(timeout);
            if (svc == null) {
                throw new WicketRuntimeException("gave up waiting for service " + className);
            }
            return (T) svc;
        }
        catch (InterruptedException exc) {
            throw new WicketRuntimeException(exc);
            
        }
        finally {
            tracker.close();
        }
    }
        

    private static ServiceTracker createServiceTracker(BundleContext bc, String className, Map<String, String> props) {
        if (props == null || props.isEmpty()) {
            return new ServiceTracker(bc, className, null);
        }
        
        StringBuilder builder = new StringBuilder("(&(objectClass=");
        builder.append(className);
        builder.append(')');
        for (Entry<String, String> entry : props.entrySet()) {
            builder.append('(');
            builder.append(entry.getKey());
            builder.append('=');
            builder.append(entry.getValue());
            builder.append(')');
        }
        builder.append(')');
        try {
            Filter filter;
            filter = FrameworkUtil.createFilter(builder.toString());
            ServiceTracker tracker = new ServiceTracker(bc, filter, null);
            return tracker;
        }
        catch (InvalidSyntaxException exc) {
        	logger.error("getOsgiService fail .. " + exc);
           throw new WicketRuntimeException(exc);
        }
    }
}
