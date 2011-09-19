/**
 * 
 */
package com.gfactor.page.main;

import java.util.HashMap;

import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfactor.locate.IPageClassLocateService;
import com.gfactor.osgi.api.export.iface.IGetOutPageInfoService;


/**
 * @author momo
 *
 */ 
public class HomePage extends WebPage {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	

	@Inject
	private IPageClassLocateService pageClassLocateService;
	
	@Inject
	private IGetOutPageInfoService getMainPagetOutService;


	public HomePage() {		
		super();
//		this.getApplication().addComponentInstantiationListener
		
		logger.info("start HomePage(),....");	
		logger.info("classloader = "+ this.getClass().getClassLoader());
		logger.info("IPageClassLocateService ... = " +pageClassLocateService);
		logger.info("IGetOutPageInfoService ... = " +getMainPagetOutService);
		logger.info("classname getName =" +this.getClass().getName());
		logger.info("classname toString =" +this.getClass().toString());
		
		HashMap<String,String> map = getMainPagetOutService.getOutPageInfoMap(this.getClass().getName(), "homeout1");
		logger.info("map = "+ map);
		
		
		Class<?> pageClass = pageClassLocateService.getPageClass(map.get("bundleSymbolicName"),map.get("version"),map.get("entry_point"));
		logger.info("HomePage pageClass = "+ pageClass);
//		Class<?> pageClass = pageClassLocateService.getPageClass("plugin-webdomain-page-html", "1.0.0", "imagePage1");
		
		
//        final Dialog dialog = new Dialog("dialog");
//        add(dialog); 
//        
//        Button button = new Button("open-dialog");
//        button.add(new WiQueryEventBehavior(new Event(MouseEvent.DBLCLICK) {
//                
//                        @Override
//                        public JsScope callback() {
//                                return JsScope.quickScope(dialog.open().render());
//                        }
//                
//                }));
//        add(button);
//        add(new Link("pageLink", pageClass));
		 add(new BookmarkablePageLink("pageLink", pageClass));
    }


}
