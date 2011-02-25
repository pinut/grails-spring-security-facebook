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