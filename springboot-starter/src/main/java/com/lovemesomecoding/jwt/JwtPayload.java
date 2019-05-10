package com.lovemesomecoding.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lovemesomecoding.user.User;
import com.lovemesomecoding.utils.ObjectUtils;

/**
 * https://tools.ietf.org/html/rfc7519
 * JWT Payload
 * 
 */
@JsonInclude(value = Include.NON_NULL)
public class JwtPayload implements Serializable {

	private static final long serialVersionUID = -1L;
	
	// issuer
	private String iss;
	// id of jwt
	private String jti;
	
	private String email;
	
	private String uid;
	
	private String deviceId;
	
	private List<String> authorities;
	
	// issued at
	private Date iat;
	// expired at
	private Date exp;
	// not before
	private Date nbf;

	public JwtPayload() {
		this(null);
	}
	
	public JwtPayload(String jti) {
		this(null,jti,null,null,null,null,null,null,null);
	}
	
	public JwtPayload(User user, String jti) {
		this(null,jti,user.getEmail(),user.getUid(),user.getAuthorities(),null,null,null,null);
	}

	public JwtPayload(String iss, String jti, String email, String uid, List<String> authorities, String deviceId, Date iat,
			Date exp, Date nbf) {
		super();
		this.iss = iss;
		this.jti = jti;
		this.email = email;
		this.uid = uid;
		this.authorities = authorities;
		this.iat = iat;
		this.exp = exp;
		this.nbf = nbf;
	}

	public String getIss() {
		return iss;
	}

	public void setIss(String iss) {
		this.iss = iss;
	}
	
	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public Date getIat() {
		return iat;
	}

	public void setIat(Date iat) {
		this.iat = iat;
	}

	public Date getExp() {
		return exp;
	}

	public void setExp(Date exp) {
		this.exp = exp;
	}

	public Date getNbf() {
		return nbf;
	}

	public void setNbf(Date nbf) {
		this.nbf = nbf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String toJson() {
        try {
            return ObjectUtils.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println("JwtPayload - toJson - JsonProcessingException, msg: " + e.getLocalizedMessage());
            return "{}";
        }
	}
	
}

