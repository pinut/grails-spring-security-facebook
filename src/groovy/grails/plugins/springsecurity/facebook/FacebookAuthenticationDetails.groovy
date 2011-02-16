package grails.plugins.springsecurity.facebook

import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest
import org.apache.commons.logging.LogFactory
import org.springframework.security.web.authentication.WebAuthenticationDetails

/**
 * Extends the standard web authentication details by extracting the facebook
 * user's session information and checking for its validity. This information
 * is later evaluated by the FacebookPreAuthenticatedAuthenticationProvider.
 *
 * @author Patrick Schmidt
 */
class FacebookAuthenticationDetails extends WebAuthenticationDetails {

	static log = LogFactory.getLog(FacebookAuthenticationDetails)

	/** Whether the facebook user is connected and granted email rights.  */
	boolean connected = false

	/** The facebook user's data  */
	def uid
	def email
	def firstname
	def lastname
	def birthday
	def gender = -1

	def accessToken

	FacebookAuthenticationDetails(HttpServletRequest request) {
		super(request)

		log.debug "checking facebook connect status ${request.facebook?.apiKey}/${request.facebook?.secretKey}"

		// get facebook cookie
		def cookieValue = request.cookies.find { it.name == "fbs_${request.facebook?.apiKey}" }?.value
		def utils = new FacebookUtils(request.facebook?.apiKey, request.facebook?.secretKey)
		def cookie = utils.getCookie(cookieValue)

		// get user data
		def user = utils.api("/me", cookie.accessToken)

		// check for error
		if (user.error) {
			log.error "an error occured connecting to facebook"
			return
		}

		setUserData(user, cookie)
	}

	def setUserData(user, cookie) {
		uid = cookie.uid
		accessToken = cookie.accessToken
		connected = true
		email = user.email
		firstname = user.first_name
		lastname = user.last_name

		// simple gender supporting en and de
		switch (user.gender) {
			case "male":
			case "m\u00e4nnlich": gender = 0; break
			case "female":
			case "weiblich": gender = 1; break
		}

		// parse birthday
		if (user.birthday) {
			def df = new SimpleDateFormat("mm/dd/yyyy", Locale.US)
			birthday = df.parse(user.birthday)
		}
	}

}
