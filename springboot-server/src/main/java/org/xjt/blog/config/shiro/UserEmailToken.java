package org.xjt.blog.config.shiro;

import lombok.Data;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

@Data
public class UserEmailToken implements HostAuthenticationToken, RememberMeAuthenticationToken {
    private String userEmail;
    private boolean rememberMe;
    private String host;

    public UserEmailToken() {
        this.rememberMe = false;
    }

    public UserEmailToken(String userEmail) {
        this(userEmail, false, null);
    }

    public UserEmailToken(String userEmail, boolean rememberMe) {
        this(userEmail, rememberMe, null);
    }

    public UserEmailToken(String userEmail, boolean rememberMe, String host) {
        this.userEmail = userEmail;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    /**
     * Returns the host name of the client from where the
     * authentication attempt originates or if the Shiro environment cannot or
     * chooses not to resolve the hostname to improve performance, this method
     * returns the String representation of the client's IP address.
     * <p/>
     * When used in web environments, this value is usually the same as the
     * {@code ServletRequest.getRemoteHost()} value.
     *
     * @return the fully qualified name of the client from where the
     * authentication attempt originates or the String representation
     * of the client's IP address is hostname resolution is not
     * available or disabled.
     */
    @Override
    public String getHost() {
        return host;
    }

    /**
     * Returns {@code true} if the submitting user wishes their identity (principal(s)) to be remembered
     * across sessions, {@code false} otherwise.
     *
     * @return {@code true} if the submitting user wishes their identity (principal(s)) to be remembered
     * across sessions, {@code false} otherwise.
     */
    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

    /**
     * Returns the account identity submitted during the authentication process.
     * <p/>
     * <p>Most application authentications are username/password based and have this
     * object represent a username.  If this is the case for your application,
     * take a look at the {@link UsernamePasswordToken UsernamePasswordToken}, as it is probably
     * sufficient for your use.
     * <p/>
     * <p>Ultimately, the object returned is application specific and can represent
     * any account identity (user id, X.509 certificate, etc).
     *
     * @return the account identity submitted during the authentication process.
     * @see UsernamePasswordToken
     */
    @Override
    public Object getPrincipal() {
        return userEmail;
    }

    /**
     * Returns the credentials submitted by the user during the authentication process that verifies
     * the submitted {@link #getPrincipal() account identity}.
     * <p/>
     * <p>Most application authentications are username/password based and have this object
     * represent a submitted password.  If this is the case for your application,
     * take a look at the {@link UsernamePasswordToken UsernamePasswordToken}, as it is probably
     * sufficient for your use.
     * <p/>
     * <p>Ultimately, the credentials Object returned is application specific and can represent
     * any credential mechanism.
     *
     * @return the credential submitted by the user during the authentication process.
     */
    @Override
    public Object getCredentials() {
        return userEmail;
    }


}
