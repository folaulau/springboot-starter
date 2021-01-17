package com.lovemesomecoding.security;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.lovemesomecoding.security.jwt.JwtPayload;
import com.lovemesomecoding.security.jwt.JwtTokenUtils;
import com.lovemesomecoding.utils.ObjMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenUtilsTests {

    @Test
    public void test_generateToken_valid() {
        String jti = "test-jti";
        String uuid = "test-uuid";
        String cud = "test-cud";
        Set<String> aud = new HashSet<String>();
        aud.add("USER");

        JwtPayload payload = new JwtPayload(jti, uuid, cud, aud);

        String token = JwtTokenUtils.generateToken(payload);

        assertThat(token).isNotNull();
        assertThat(token.length()).isGreaterThan(0);
    }

    @Test
    public void test_generateToken_invalid() {

        JwtPayload payload = null;

        String token = JwtTokenUtils.generateToken(payload);

        assertThat(token).isNull();
    }

    @Test
    public void test_getPayloadByToken_valid() {
        String jti = "test-jti";
        String uuid = "test-uuid";
        String cud = "test-cud";
        Set<String> aud = new HashSet<String>();
        aud.add("USER");
        JwtPayload payload = new JwtPayload(jti, uuid, cud, aud);

        log.info("payload={}", ObjMapperUtils.toJson(payload));

        String token = JwtTokenUtils.generateToken(payload);

        log.info("token={}", token);

        assertThat(token).isNotNull();
        assertThat(token.length()).isGreaterThan(0);

        JwtPayload updatedPayload = JwtTokenUtils.getPayloadByToken(token);

        log.info("updatedPayload={}", ObjMapperUtils.toJson(updatedPayload));

        assertThat(updatedPayload).isNotNull();
        assertThat(updatedPayload.getJti()).isEqualTo("test-jti");
        assertThat(updatedPayload.getSub()).isEqualTo("test-uuid");
        assertThat(updatedPayload.getCud()).isEqualTo("test-cud");

        assertThat(updatedPayload.getAud()).isNotNull();
        assertThat(updatedPayload.getUserRolesAsArray()).isNotNull().isEqualTo(new String[]{"USER"});

    }

}
