package grails.plugins.springsecurity.facebook

import org.apache.commons.logging.LogFactory

/**
 * Represents the facebook authentication cookie's fields.
 *
 * @author Patrick Schmidt
 */
class FacebookCookie {

	static log = LogFactory.getLog(FacebookCookie.class)

	String access_token
	String expires
	String secret
	String session_key
	String sig
	String uid

	String apiKey
	String secretKey

	String getAccessToken() {
		access_token
	}

	String getSessionKey() {
		session_key
	}

	long getUid() {
		uid.toLong()
	}

	long getExpires() {
		expires.toLong()
	}

	/**
	 * Checks this token against the application secret to determine validity.
	 *
	 * @return whether this cookie is valid or not
	 */
	boolean isValid() {
		log.debug "attempting to validate cookie"
//		println "apiKey = $apiKey \nsecretKey = $secretKey"
		def payload = "access_token=${access_token}expires=${expires}secret=${secret}session_key=${session_key}uid=${uid}"
//		println "payload = $payload"
//		println "testing " + "${payload}${secretKey}".encodeAsMD5() + " == " + sig
		def encodedPayload = "${payload}${secretKey}".encodeAsMD5()
		def valid = encodedPayload == sig
		if (!valid) {
			log.warn """payload could not be verified, will ignore for now
						- encoded payload: $encodedPayload
						-       signature: $sig"""
		}

		// TODO :: get the validation working
		true
	}

}