package grails.plugins.springsecurity.facebook

/**
 * Simple helper for handling urls.
 *
 * @author Patrick Schmidt
 */
class UrlHelper {

	/**
	 * Returns the top level domain part of an url or "localhost" if its a local url
	 * @param url
	 * @return
	 */
	static getTLD(url) {
		url.replaceAll(
				/^http.?:\/\/(?:(localhost)|[^\/:]+\.([a-z]+)).*$/) { full, local, match -> local ?: match }
	}

}
