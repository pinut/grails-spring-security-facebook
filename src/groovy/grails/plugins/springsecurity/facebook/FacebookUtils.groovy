package grails.plugins.springsecurity.facebook

import grails.converters.JSON

/**
 * Provides helper methods for dealing with facebook connect.
 *
 * @author Patrick Schmidt
 */
class FacebookUtils {

	private static final String GRAPH_URL = "https://graph.facebook.com"

	String apiKey
	String secretKey

	FacebookUtils(String apiKey, String secretKey) {
		this.apiKey = apiKey
		this.secretKey = secretKey
	}

	FacebookCookie getCookie(String cookieValue) {
		def params = [apiKey: apiKey, secretKey: secretKey]
		cookieValue.tokenize("&").each { pair ->
			def m = (pair =~ /([a-z_]+)=(.*)/)
			params[m[0][1]] = m[0][2]
		}
		new FacebookCookie(params)
	}

	def api(String path, String token, Map params = null) {
		if (!path.startsWith("/")) {
			path = "/${path}"
		}
		def url = params ?
			"${GRAPH_URL}${path}".toURL() :
			"${GRAPH_URL}${path}?access_token=${token}".toURL()
		def result = [:]
		try {
			if (!params) {
				result = JSON.parse(url.text)
			} else {
				def conn = url.openConnection()
				conn.doOutput = true
				conn.doInput = true

				def paramString = "access_token=$token&" + params.collect { k, v ->
					"$k=${v?.encodeAsURL()}"
				}.join("&")

				conn.outputStream << paramString
				result = JSON.parse(conn.inputStream.text)
			}
		} catch (Exception ex) {
			ex.printStackTrace()
		}
		result
	}

}