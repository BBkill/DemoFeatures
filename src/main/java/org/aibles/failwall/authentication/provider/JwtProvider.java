package org.aibles.failwall.authentication.provider;

import io.jsonwebtoken.*;
import org.aibles.failwall.authentication.payload.UserPrincipalService;
import org.aibles.failwall.exception.FailWallBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtProvider {

    private final UserPrincipalService userDetailsService;

    @Autowired
    public JwtProvider(UserPrincipalService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private final static String JWT_HEADER = "Authorization";

    private final static String JWT_SECRET_KEY = "aibles";

    private final static long EXPIRATION_TIME_OF_JWT = 604800;

    private static long JWT_LIFE_TIME_MILLISECONDS = 604800000;

    private final static String JWT_PREFIX= "Bearer";

    public String generateToken(String email){
        Claims claims = Jwts.claims().setSubject(email);
        Date now  = new Date();
        Date expirationDate = new Date (now.getTime() + JWT_LIFE_TIME_MILLISECONDS);
        return Jwts.builder().setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (JwtException e){
            throw new FailWallBusinessException("JWT token is expired", HttpStatus.UNAUTHORIZED);
        }catch (IllegalArgumentException e){
            throw new FailWallBusinessException("JWT token is invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(JWT_HEADER);
        if (token != null && token.startsWith(JWT_PREFIX)){
            return token.substring(7);
        }
        return null;
    }

}
