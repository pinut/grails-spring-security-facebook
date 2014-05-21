package grails.plugins.springsecurity.facebook

import org.springframework.security.core.AuthenticationException

/**
 * Created by IntelliJ IDEA.
 * User: piotrek
 * Date: 25.03.11
 * Time: 20:14
 */
public class FacebookNotYetRegisteredUserException extends AuthenticationException {

    FacebookAuthenticationDetails details

    public FacebookNotYetRegisteredUserException(FacebookAuthenticationDetails details) {
        super("Facebook user not registered yet: " + details.username);
        this.details = details
    }
}
