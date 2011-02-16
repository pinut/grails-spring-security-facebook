package grails.plugins.springsecurity.facebook

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.core.Ordered
import org.springframework.security.authentication.AccountStatusUserDetailsChecker
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

class FacebookPreAuthenticatedAuthenticationProvider
implements AuthenticationProvider, InitializingBean, Ordered {

	static log = LogFactory.getLog(FacebookPreAuthenticatedAuthenticationProvider.class)

	AuthenticationUserDetailsService facebookUserDetailsService = null;
	def userDetailsChecker = new AccountStatusUserDetailsChecker()
	def throwExceptionWhenTokenRejected = true

	def order = -1 // default: same as non-ordered

	/**
	 * Check whether all required properties have been set.
	 */
	void afterPropertiesSet() {
		assert facebookUserDetailsService, "An AuthenticationUserDetailsService must be set"
	}

	/**
	 * Authenticate the given PreAuthenticatedAuthenticationToken.
	 * <p>
	 * If the facebook user represented by this token is not connected
	 * a BadCredentials exception will be thrown unless the
	 * throwExceptionWhenTokenRejected property is set to false. In that case
	 * the request will be ignored to allow other providers to authenticate it.
	 */
	Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug "authenticate called"
		if (!supports(authentication.getClass())
				&& !authentication.details
				&& !FacebookAuthenticationDeatils.isAssignableFrom(authentication.details.class)) {
			log.debug "not supported!"
			return null
		}

		log.debug "FacebookPreAuthenticated authentication request: $authentication"

		// check for connection status
		if (!authentication.details.connected) {
			log.debug "Facebook user is not connected."
			if (throwExceptionWhenTokenRejected) {
				throw new BadCredentialsException("Facebook user is not connected.")
			}
			return null
		}

		UserDetails ud = facebookUserDetailsService.loadUserDetails(authentication)
		userDetailsChecker.check(ud)

		PreAuthenticatedAuthenticationToken result =
		new PreAuthenticatedAuthenticationToken(ud, authentication.getCredentials(), ud.getAuthorities());
		result.details = authentication.details

		return result
	}

	/**
	 * Indicate that this provider only supports PreAuthenticatedAuthenticationToken (sub)classes.
	 */
	boolean supports(Class authentication) {
		PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication)
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int i) {
		order = i;
	}

	/**
	 * If true, causes the provider to throw a BadCredentialsException if the presented authentication
	 * request is invalid (contains a null principal or credentials). Otherwise it will just return
	 * null. Defaults to false.
	 */
	public void setThrowExceptionWhenTokenRejected(boolean throwExceptionWhenTokenRejected) {
		this.throwExceptionWhenTokenRejected = throwExceptionWhenTokenRejected;
	}

}
