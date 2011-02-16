package grails.plugins.springsecurity.facebook

import org.springframework.web.context.request.RequestContextHolder as RCH

import javax.servlet.http.HttpServletRequest
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

/**
 * Provides facebook specific tags to initialize and use the facebook JS API.
 * This tab library makes use of the UI-Performance and jQuery plugin.
 * Adjust it to your needs, if you don't use one of these plugins.
 *
 * @author Patrick Schmidt
 */
class FacebookTagLib {

	static returnObjectForTags = ['facebookCookie']

	/**
	 * Place this tag on any page (or the layout) to initialize facebooks
	 * JS api. You need to set the config key api.facebook.apiKey
	 *
	 * @param initOnLoad whether to initialize the facebook JavaScript SDK after load
	 */
	def facebookInit = { attrs, body ->
		def initOnLoad = "${attrs.remove('initOnLoad') ?: 'false'}"?.toBoolean()
		def facebook = SpringSecurityUtils.securityConfig?.facebook

		def apiKey = facebook?.apiKey
		def secretKey = facebook?.secretKey

		// evaluate domains
		if (facebook?.domains) {
			def currentDomain = extractDomain(request)
			apiKey = facebook.domains[currentDomain].apiKey
			secretKey = facebook.domains[currentDomain].secretKey
		}

		assert apiKey && secretKey, "Please set facebook.apiKey and facebook.secretKey in your Config.groovy"

		def locale = RCH.currentRequestAttributes()?.locale ?: Locale.US

		def scripts = new StringBuilder()
		scripts << """
			<script type="text/javascript">
			  \$(function() {
				  var fbInit = function() {
	    """
		if (initOnLoad) {
			scripts << """
				      FB.init({
					      appId  : '${apiKey}',
					      status : true, // check login status
					      cookie : true, // enable cookies to allow the server to access the session
					      xfbml  : false  // parse XFBML
					  });
					  window.facebookInitialized = true;
			"""
		}
		scripts << """
				  };
				  \$('<div/>').attr("id", "fb-root").appendTo('body');
				  \$.getScript('http://connect.facebook.net/${locale}/all.js', fbInit);
			  });
			</script>
		"""

		// By convention actions which name ends in "Ajax" are assumed to render templates
		// thus relevant scripts are not subject to ui performance dependant javascript handling
		if (actionName ==~ /.*?Ajax/) {
			out << scripts.toString()
		} else {
			p.dependantJavascript javascript: scripts.toString()
		}
	}

	/**
	 * Returns the current FacebookCookie.
	 */
	def facebookCookie = { attrs, value ->
		// get facebook cookie
		def facebook = SpringSecurityUtils.securityConfig?.facebook

		def apiKey = facebook?.apiKey
		def secretKey = facebook?.secretKey

		// evaluate domains
		if (facebook?.domains) {
			def currentDomain = extractDomain(request)
			apiKey = facebook.domains[currentDomain].apiKey
			secretKey = facebook.domains[currentDomain].secretKey
		}

		assert apiKey && secretKey, "Please set facebook.apiKey and facebook.secretKey in your Config.groovy"
		def cookieValue = request.cookies.find { it.name == "fbs_${apiKey}" }?.value
		if (!cookieValue) {
			log.warn "No facebook cookie found, not connected yet?"
			return
		}
		def utils = new FacebookUtils(apiKey, secretKey)
		utils.getCookie(cookieValue)
	}

	def facebookApiKey = { attrs, body ->
		def facebook = SpringSecurityUtils.securityConfig?.facebook

		def apiKey = facebook?.apiKey
		def secretKey = facebook?.secretKey

		// evaluate domains
		if (facebook?.domains) {
			def currentDomain = extractDomain(request)
			apiKey = facebook.domains[currentDomain].apiKey
			secretKey = facebook.domains[currentDomain].secretKey
		}

		assert apiKey && secretKey, "Please set facebook.apiKey and facebook.secretKey in your Config.groovy"

		out << apiKey
	}

	private extractDomain(HttpServletRequest request) {
		UrlHelper.getTLD request.requestURL.toString()
	}

}
