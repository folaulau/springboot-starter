package com.lovemesomecoding.jwt;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public final class JwtTokenUtils {
	
	private static final String secret = "folau";

	private static final Algorithm ALGORITHM = Algorithm.HMAC256(secret);
	
	private static final String ISSUER = "kaveinga-security-api";
	
	private static final JWTVerifier jwtVerifier = JWT.require(ALGORITHM).withIssuer(ISSUER).build();

	/**
	 * Use JwtPayload to generate a jwt token
	 * @param payload - JwtPayload
	 * @return token - String
	 */
	public static String generateToken(JwtPayload payload) {
		System.out.println("JwtTokenUtils - generate(..)");
		try {
			String token = JWT.create()
					.withClaim("email", payload.getEmail())
					.withClaim("uid", payload.getUid())
					.withClaim("deviceId", payload.getDeviceId())
					.withArrayClaim("roles", payload.getAuthorities().toArray(new String[payload.getAuthorities().size()]))
					.withIssuedAt(new Date())
					.withJWTId(payload.getJti())
					.withIssuer(ISSUER)
					.withNotBefore(payload.getNbf())
					.sign(ALGORITHM);
			return token;
		} catch (JWTCreationException e) {
			System.out.println("JWTCreationException, msg: " + e.getLocalizedMessage());
			return null;
		}
	}

	public static Boolean verify(String token) {
		System.out.println("JwtTokenUtils - verify(..)");
		try {
			 // Reusable verifier instance
			DecodedJWT jwt = jwtVerifier.verify(token);
			if(jwt!=null) {
				return true;
			}else {
				System.out.println("decoded jwt is null. invalid token");
				return false;
			}
		} catch (Exception e) {
			System.out.println("Exception, msg: " + e.getLocalizedMessage());
			return false;
		}
	}
	
	public static JwtPayload getJetPayload(String token) {
		JwtPayload jwtPayload = new JwtPayload();
		
		 // Reusable verifier instance
		DecodedJWT jwt = jwtVerifier.verify(token);
		
		if(jwt!=null) {
			
			jwt = JWT.decode(token);
			
			jwtPayload.setExp(jwt.getExpiresAt());
			jwtPayload.setIss(jwt.getIssuer());
			jwtPayload.setJti(jwt.getId());
			jwtPayload.setIat(jwt.getIssuedAt());
			jwtPayload.setNbf(jwt.getNotBefore());
			
			String email = jwt.getClaim("email").asString();
			jwtPayload.setEmail(email);
			String uid = jwt.getClaim("uid").asString();
			jwtPayload.setUid(uid);
			String deviceId = jwt.getClaim("deviceId").asString();
			jwtPayload.setDeviceId(deviceId);
			
			String[] roles = jwt.getClaim("roles").asArray(String.class);
			jwtPayload.setAuthorities(Arrays.asList(roles));
			
			return jwtPayload;
		}else {
			System.out.println("jwt is null");
		}
		return null;
	}
}
