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

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.beans.factory.InitializingBean
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler

class FacebookLogoutHandler implements LogoutHandler, InitializingBean {

	def apiKey
	def domains

	/**
	 * Check whether all required properties have been set.
	 */
	void afterPropertiesSet() {
		assert apiKey || domains, "Either must be a facebook apiKey or domains set."
	}

	void logout(HttpServletRequest request, HttpServletResponse response,
	            Authentication authentication) {

		def apiKey = this.apiKey

		// evaluate domains
		if (domains) {
			def currentDomain = extractDomain(request)
			apiKey = domains[currentDomain].apiKey
		}

		// try to find facebook cookie
		def cookie = request.cookies.find { it.name == "fbs_${apiKey}" }
		if (!cookie) { return }

		// delete cookie
		def delCookie = new Cookie(cookie.name, null)
		delCookie.maxAge = 0
		delCookie.path = "/"
		response.addCookie(delCookie)

        println "cookie removed"
	}

	private extractDomain(HttpServletRequest request) {
		UrlHelper.getTLD request.requestURL.toString()
	}

}