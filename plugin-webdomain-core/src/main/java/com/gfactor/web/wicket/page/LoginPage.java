package com.gfactor.web.wicket.page;
import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfactor.osgi.api.export.iface.IGetMainPageClassService;
import com.gfactor.osgi.api.export.util.BundleContextInfoUtil;
import com.gfactor.web.wicket.context.WebAuthenticatedWebSession;

/**
 *
 * @author mbenrhouma
 */
public final class LoginPage extends WebPage {
    private static final Logger logger = LoggerFactory.getLogger(BundleContextInfoUtil.class);

//	@SpringBean 
//	private IUpdate updatePatch;
	
	@Inject
	private IGetMainPageClassService mainPageService;
	 

    public LoginPage() {
        this(null);
    }
    
    	    
	@SuppressWarnings("deprecation")
	public LoginPage(final PageParameters parameters) {    	
        add(new LoginForm("loginform"));        
//        add(new PageLink("pageLink", new IPageLink() {
//        		public Page getPage() {
//        			logger.info("get page chk updatePatch bean status -> "+updatePatch);
//        			updatePatch.updateFileFromServer();
//        			return new LoginPage();
//        		}
//        		public Class getPageIdentity() {
//        			return LoginPage.class;
//        		}
//        		}));
    }

    class LoginForm extends Form {

        private String username;
        private String password;

        public LoginForm(String id) {
            super(id);
            setModel(new CompoundPropertyModel(this));
            add(new RequiredTextField("username"));
            add(new PasswordTextField("password"));
            add(new FeedbackPanel("feedback"));
        }

        @Override
        protected void onSubmit() {
        	logger.info("mainPageService = "+mainPageService);
        	WebAuthenticatedWebSession session = WebAuthenticatedWebSession.getSpringWicketWebSession();
        	logger.info("userName="+username);
        	logger.info("password="+password);
        	logger.info("WebAuthenticatedWebSession = "+ session);        	

			if (session.signIn(username, password)) {
				logger.info("session signIn....");
				// redirect here
				// info(getString("login.success"));
				if (!continueToOriginalDestination()) {
//					logger.info("HomePage.class = " + HomePage.class);
//					logger.info("osgi get classes " + mainPageService.getPageClazz());
//					setResponsePage(HomePage.class);
					
					Class clazz = mainPageService.getPageClazz();
					
					logger.info("response page classloader = " + clazz.getClassLoader());
//					
//					Class clazz2 = testPageService.getPageClass();
					setResponsePage(clazz);
//					setr
//					setResponsePage(SuccessPage.class);
//					setResponsePage(home.class);
				}
			} else {
				
				logger.warn("login fail");
			}
		}

    }
}
