package com.gfactor.web.wicket.context;
/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AbstractAuthenticationManager;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.Assert;


/**
* Iterates an {@link Authentication} request through a list of {@link AuthenticationProvider}s.
*
* <p>
* <tt>AuthenticationProvider</tt>s are usually tried in order until one provides a non-null response.
* A non-null response indicates the provider had authority to decide on the authentication request and no further
* providers are tried.
* If a subsequent provider successfully authenticates the request, the earlier authentication exception is disregarded
* and the successful authentication will be used. If no subsequent provider provides a non-null response, or a new
* <code>AuthenticationException</code>, the last <code>AuthenticationException</code> received will be used.
* If no provider returns a non-null response, or indicates it can even process an <code>Authentication</code>,
* the <code>ProviderManager</code> will throw a <code>ProviderNotFoundException</code>.
* A parent {@code AuthenticationManager} can also be set, and this will also be tried if none of the configured
* providers can perform the authentication. This is intended to support namespace configuration options though and
* is not a feature that should normally be required.
* <p>
* The exception to this process is when a provider throws an {@link AccountStatusException}, in which case no
* further providers in the list will be queried.
*
* Post-authentication, the credentials will be cleared from the returned {@code Authentication} object, if it
* implements the {@link CredentialsContainer} interface. This behaviour can be controlled by modifying the
* {@link #setEraseCredentialsAfterAuthentication(boolean) eraseCredentialsAfterAuthentication} property.
*
* <h2>Event Publishing</h2>
* <p>
* Authentication event publishing is delegated to the configured {@link AuthenticationEventPublisher} which defaults
* to a null implementation which doesn't publish events, so if you are configuring the bean yourself you must inject
* a publisher bean if you want to receive events. The standard implementation is {@link DefaultAuthenticationEventPublisher}
* which maps common exceptions to events (in the case of authentication failure) and publishes an
* {@link org.springframework.security.authentication.event.AuthenticationSuccessEvent AuthenticationSuccessEvent} if
* authentication succeeds. If you are using the namespace then an instance of this bean will be used automatically by
* the <tt>&lt;http&gt;</tt> configuration, so you will receive events from the web part of your application automatically.
* <p>
* Note that the implementation also publishes authentication failure events when it obtains an authentication result
* (or an exception) from the "parent" {@code AuthenticationManager} if one has been set. So in this situation, the
* parent should not generally be configured to publish events or there will be duplicates.
*
*
* @author Ben Alex
* @author Luke Taylor
*
* @see DefaultAuthenticationEventPublisher
*/
public class ProviderManager extends AbstractAuthenticationManager implements MessageSourceAware, InitializingBean {
   //~ Static fields/initializers =====================================================================================

   private static final Log logger = LogFactory.getLog(ProviderManager.class);

   //~ Instance fields ================================================================================================

   private AuthenticationEventPublisher eventPublisher = new NullEventPublisher();
   private List<AuthenticationProvider> providers = Collections.emptyList();
   protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
   private AuthenticationManager parent;
   private boolean eraseCredentialsAfterAuthentication = false;

   //~ Methods ========================================================================================================

   public void afterPropertiesSet() throws Exception {
       if (parent == null && providers.isEmpty()) {
           throw new IllegalArgumentException("A parent AuthenticationManager or a list " +
                   "of AuthenticationProviders is required");
       }
   }

   /**
    * Attempts to authenticate the passed {@link Authentication} object.
    * <p>
    * The list of {@link AuthenticationProvider}s will be successively tried until an
    * <code>AuthenticationProvider</code> indicates it is  capable of authenticating the type of
    * <code>Authentication</code> object passed. Authentication will then be attempted with that
    * <code>AuthenticationProvider</code>.
    * <p>
    * If more than one <code>AuthenticationProvider</code> supports the passed <code>Authentication</code>
    * object, only the first <code>AuthenticationProvider</code> tried will determine the result. No subsequent
    * <code>AuthenticationProvider</code>s will be tried.
    *
    * @param authentication the authentication request object.
    *
    * @return a fully authenticated object including credentials.
    *
    * @throws AuthenticationException if authentication fails.
    */
   public Authentication doAuthentication(Authentication authentication) throws AuthenticationException {
       Class<? extends Authentication> toTest = authentication.getClass();
       AuthenticationException lastException = null;
       Authentication result = null;

       for (AuthenticationProvider provider : getProviders()) {
           if (!provider.supports(toTest)) {
               continue;
           }

           logger.debug("Authentication attempt using " + provider.getClass().getName());

           try {
               result = provider.authenticate(authentication);

               if (result != null) {
                   copyDetails(authentication, result);
                   break;
               }
           } catch (AccountStatusException e) {
               // SEC-546: Avoid polling additional providers if auth failure is due to invalid account status
               eventPublisher.publishAuthenticationFailure(e, authentication);
               throw e;
           } catch (AuthenticationException e) {
               lastException = e;
           }
       }

       if (result == null && parent != null) {
           // Allow the parent to try.
           try {
               result = parent.authenticate(authentication);
           } catch (ProviderNotFoundException e) {
               // ignore as we will throw below if no other exception occurred prior to calling parent and the parent
               // may throw ProviderNotFound even though a provider in the child already handled the request
           } catch (AuthenticationException e) {
               lastException = e;
           }
       }

       if (result != null) {
           if (eraseCredentialsAfterAuthentication && (result instanceof CredentialsContainer)) {
               // Authentication is complete. Remove credentials and other secret data from authentication
               ((CredentialsContainer)result).eraseCredentials();
           }

           eventPublisher.publishAuthenticationSuccess(result);
           return result;
       }

       // Parent was null, or didn't authenticate (or throw an exception).

       if (lastException == null) {
           lastException = new ProviderNotFoundException(messages.getMessage("ProviderManager.providerNotFound",
                       new Object[] {toTest.getName()}, "No AuthenticationProvider found for {0}"));
       }

       eventPublisher.publishAuthenticationFailure(lastException, authentication);

       throw lastException;
   }

   /**
    * Copies the authentication details from a source Authentication object to a destination one, provided the
    * latter does not already have one set.
    *
    * @param source source authentication
    * @param dest the destination authentication object
    */
   private void copyDetails(Authentication source, Authentication dest) {
       if ((dest instanceof AbstractAuthenticationToken) && (dest.getDetails() == null)) {
           AbstractAuthenticationToken token = (AbstractAuthenticationToken) dest;

           token.setDetails(source.getDetails());
       }
   }

   public List<AuthenticationProvider> getProviders() {
       return providers;
   }

   public void setMessageSource(MessageSource messageSource) {
       this.messages = new MessageSourceAccessor(messageSource);
   }

   public void setParent(AuthenticationManager parent) {
       this.parent = parent;
   }

   public void setAuthenticationEventPublisher(AuthenticationEventPublisher eventPublisher) {
       Assert.notNull(eventPublisher, "AuthenticationEventPublisher cannot be null");
       this.eventPublisher = eventPublisher;
   }

   /**
    * If set to, a resulting {@code Authentication} which implements the {@code CredentialsContainer} interface
    * will have its {@link CredentialsContainer#eraseCredentials() eraseCredentials} method called before it is returned
    * from the {@code authenticate()} method.
    *
    * @param eraseSecretData set to {@literal false} to retain the credentials data in memory.
    * Defaults to {@literal true}.
    */
   public void setEraseCredentialsAfterAuthentication(boolean eraseSecretData) {
       this.eraseCredentialsAfterAuthentication = eraseSecretData;
   }

   public boolean isEraseCredentialsAfterAuthentication() {
       return eraseCredentialsAfterAuthentication;
   }

   /**
    * Sets the {@link AuthenticationProvider} objects to be used for authentication.
    *
    * @param providers the list of authentication providers which will be used to process authentication requests.
    *
    * @throws IllegalArgumentException if the list is empty or null, or any of the elements in the list is not an
    * AuthenticationProvider instance.
    */
   @SuppressWarnings("unchecked")
   public void setProviders(List providers) {
       Assert.notNull(providers, "Providers list cannot be null");
       for(Object currentObject : providers) {
           Assert.isInstanceOf(AuthenticationProvider.class, currentObject, "Can only provide AuthenticationProvider instances");
       }

       this.providers = providers;
   }

   private static final class NullEventPublisher implements AuthenticationEventPublisher {
       public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {}
       public void publishAuthenticationSuccess(Authentication authentication) {}
   }
}
