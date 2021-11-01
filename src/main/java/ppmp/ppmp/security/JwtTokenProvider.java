package ppmp.ppmp.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import ppmp.ppmp.domain.User;

@Component
public class JwtTokenProvider {
	// generate token
	public String generateToken(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		Date now = new Date(System.currentTimeMillis());
		Date expirayDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);
		String userId = String.valueOf(user.getId());

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("id", Long.valueOf(userId));
		claims.put("username", user.getUsername());
		claims.put("fullName", user.getFullName());

		return Jwts.builder().setSubject(userId).setClaims(claims).setIssuedAt(now).setExpiration(expirayDate)
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET).compact();

	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			System.out.println("Invalid JWT signature.");
		} catch (MalformedJwtException e) {
			System.out.println("Invalid JWT token.");
		} catch (ExpiredJwtException e) {
			System.out.println("Expired  JWT token.");
		} catch (UnsupportedJwtException e) {
			System.out.println("Unsupported JWT token.");
		} catch (IllegalArgumentException e) {
			System.out.println("JWTclaims string is empty.");
		}
		return false;
	}

	public long getUserIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody();
		String  id = claims.get("id").toString();
		return Long.valueOf(id);
	}

}
