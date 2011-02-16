package grails.plugins.springsecurity.facebook

/**
 * Provides an application hook to get a valid username based on the facebook
 * email address.
 *
 * @author Patrick Schmidt
 */
interface FacebookService {

	/**
	 * Returns a valid username related to the facebook user details.
	 *
	 * @param details the facebook authentication details
	 * @return the alias associated with the email given
	 */
	String getUsername(FacebookAuthenticationDetails details)

	;

}