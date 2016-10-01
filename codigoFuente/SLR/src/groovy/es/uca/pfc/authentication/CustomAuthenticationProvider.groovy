package es.uca.pfc.authentication;

import mendeley.pfc.services.MendeleyService
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
import grails.plugin.springsecurity.userdetails.GrailsUser
import es.uca.pfc.User;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	def springSecurityService 
	def mendeleyToolService
	
	@Override
	public Authentication authenticate(Authentication customAuth) throws AuthenticationException {
		
		User.withTransaction { status ->
			User user = User.findByUsername(customAuth.principal)
			
			if(user)
			{
				MendeleyService mendeleyService = ldapAuthMethodHere(customAuth)
				if(mendeleyService != null)
				{
					def jUsername = customAuth.principal.toString()
					def jPassword = customAuth.credentials.toString()
					def jPasswordEnc = mendeleyToolService.encodePasswordMendeley(jPassword)
					user.password = jPassword
					user.save(failOnError: true, flush: true)
					user.userMendeley.email_mend = jUsername
					user.userMendeley.pass_mend = jPasswordEnc
					user.userMendeley.access_token = mendeleyService.getTokenResponse().getAccessToken()
					user.userMendeley.token_type = mendeleyService.getTokenResponse().getTokenType()
					user.userMendeley.expires_in = mendeleyService.getTokenResponse().getExpiresIn()
					user.userMendeley.refresh_token = mendeleyService.getTokenResponse().getRefreshToken()
					user.userMendeley.save(failOnError: true, flush: true)
					
					Collection<GrantedAuthority> authorities = user.authorities.collect { new GrantedAuthorityImpl(it.authority) };
					def userDetails = new GrailsUser(user.username, user.password, true, true, true,  true, authorities, user.id);
					def token = new UsernamePasswordAuthenticationToken(userDetails, user.password, userDetails.authorities);
					token.details = customAuth.details;
					return token
				}
				else
				{
					throw new BadCredentialsException("Log in failed - identity could not be verified");
		  		}
			}
			else
			{
				return null;
			}
		}
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

	public MendeleyService ldapAuthMethodHere(Authentication customAuth) {
		MendeleyService mendeleyService = mendeleyToolService.getTokenResponseMendeley(customAuth.principal.toString(), customAuth.credentials.toString());
		return mendeleyService;
	}
}
