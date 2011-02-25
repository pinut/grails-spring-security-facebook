/*
 * Copyright 2011 Patrick Schmidt
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