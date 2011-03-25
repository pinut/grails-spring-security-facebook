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

import grails.converters.JSON
import org.apache.commons.logging.LogFactory

/**
 * Provides helper methods for dealing with facebook connect.
 *
 * @author Patrick Schmidt
 */
class FacebookUtils {

	private static final String GRAPH_URL = "https://graph.facebook.com"
    static log = LogFactory.getLog(FacebookUtils.class)

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
			log.debug("cannot invoke api: ${ex}", ex)
		}
		result
	}

}