package grails.plugins.springsecurity.facebook

import org.codehaus.groovy.grails.plugins.springsecurity.AjaxAwareAuthenticationFailureHandler;
import org.codehaus.groovy.grails.plugins.springsecurity.ReflectionUtils

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * Created by IntelliJ IDEA.
 * User: piotrek
 * Date: 25.03.11
 * Time: 09:56
 */
class FacebookAuthenticationFailureHandler extends AjaxAwareAuthenticationFailureHandler {

	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

		if (!isSuccessfulLoginUnknownUser(exception)) {
			super.onAuthenticationFailure(request, response, exception);
			return;
		}

        saveException(request, exception)

		String createAccountUri = (String)ReflectionUtils.getConfigProperty("facebook.registration.createAccountUri");
		getRedirectStrategy().sendRedirect(request, response, createAccountUri);
	}

	private boolean isSuccessfulLoginUnknownUser(AuthenticationException exception) {
		if (exception instanceof FacebookNotYetRegisteredUserException) {
			return true
		}
        return false

	}

}
