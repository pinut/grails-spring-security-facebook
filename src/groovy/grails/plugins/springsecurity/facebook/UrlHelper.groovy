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
