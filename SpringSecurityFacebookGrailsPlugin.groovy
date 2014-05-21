import org.codehaus.groovy.grails.plugins.springsecurity.SecurityFilterPosition
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import grails.plugins.springsecurity.facebook.*

class SpringSecurityFacebookGrailsPlugin {
	// the plugin version
	def version = "0.1.1"
	// the version or versions of Grails the plugin is designed for
	String grailsVersion = '1.2.2 > *'
	// the other plugins this plugin depends on
	def dependsOn = [springSecurityCore: "1.0 > *"]
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
			"grails-app/views/error.gsp"
	]

	def author = "Patrick Schmidt"
	def authorEmail = "pschmidt@web2come.com"
	def title = "Spring Security Facebook Connect"
	def description = '''\\
The plugin allows you to extend your standard spring security DAO-based authentication
with facebook connect utilizing the facebook JavaScript SDK and the Graph API
while associating the facebook user with a local account.

'''

	// URL to the plugin's documentation
	def documentation = "http://grails.org/plugin/spring-security-facebook"

	def doWithWebDescriptor = { xml ->
		// TODO Implement additions to web.xml (optional), this event occurs before
	}

	def doWithSpring = {

		def conf = SpringSecurityUtils.securityConfig
		if (!conf || !conf.active) {
			return
		}

		println 'Configuring Spring Security Facebook Connect ...'

		SpringSecurityUtils.registerProvider 'facebookAuthProvider'
		SpringSecurityUtils.registerFilter 'facebookFilter', SecurityFilterPosition.PRE_AUTH_FILTER
		SpringSecurityUtils.registerLogoutHandler 'facebookLogoutHandler'

		// Facebook connect implementation
		facebookAuthenticationDetailsSource(org.springframework.security.web.authentication.WebAuthenticationDetailsSource) {
			clazz = FacebookAuthenticationDetails.class
		}

		facebookFilter(FacebookAuthenticationProcessingFilter) {
			authenticationManager = ref("authenticationManager")
			authenticationDetailsSource = ref("facebookAuthenticationDetailsSource")
            authenticationFailureHandler = ref("facebookAuthenticationFailureHandler")
			apiKey = conf.facebook.apiKey
			secretKey = conf.facebook.secretKey
			domains = conf.facebook.domains
		}

		facebookAuthProvider(FacebookAuthenticationProvider) {
			facebookUserDetailsService = ref("facebookUserDetailsService")
		}

		facebookUserDetailsService(FacebookUserDetailsService) {
			userDetailsService = ref("userDetailsService")
			facebookService = ref("facebookService")
		}

		facebookLogoutHandler(FacebookLogoutHandler) {
			apiKey = conf.facebook.apiKey
			domains = conf.facebook.domains
		}

        facebookAuthenticationFailureHandler(FacebookAuthenticationFailureHandler) { bean ->
            bean.parent = ref("authenticationFailureHandler")
        }

	}

	def doWithDynamicMethods = { ctx ->
		// TODO Implement registering dynamic methods to classes (optional)
	}

	def doWithApplicationContext = { applicationContext ->
		// TODO Implement post initialization spring config (optional)
	}

	def onChange = { event ->
		// TODO Implement code that is executed when any artefact that this plugin is
		// watching is modified and reloaded. The event contains: event.source,
		// event.application, event.manager, event.ctx, and event.plugin.
	}

	def onConfigChange = { event ->
		// TODO Implement code that is executed when the project configuration changes.
		// The event is the same as for 'onChange'.
	}
}
