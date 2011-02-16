package grails.plugins.springsecurity.facebook

import org.springframework.beans.factory.InitializingBean
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.transaction.annotation.Transactional

class FacebookUserDetailsService
implements AuthenticationUserDetailsService, InitializingBean {

	FacebookService facebookService
	UserDetailsService userDetailsService

	/**
	 * Check whether all required properties have been set.
	 */
	void afterPropertiesSet() {
		assert userDetailsService, "A UserDetailsService must be set"
		assert facebookService, "The facebookService must be set"
	}

	@Transactional
	UserDetails loadUserDetails(Authentication token) {
		// find or create UserAccount, delegate to service
		def username = facebookService.getUsername(token.details)
		userDetailsService.loadUserByUsername(username)
	}

}