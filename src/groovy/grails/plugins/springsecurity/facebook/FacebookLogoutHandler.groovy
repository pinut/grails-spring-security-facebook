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
	}

	private extractDomain(HttpServletRequest request) {
		UrlHelper.getTLD request.requestURL.toString()
	}

}