package com.lovemesomecoding.user;

import java.time.Instant;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.Filter;

import org.apache.commons.lang3.time.DateUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.lovemesomecoding.entity.user.User;
import com.lovemesomecoding.entity.user.UserDAO;
import com.lovemesomecoding.entity.user.UserGender;
import com.lovemesomecoding.entity.user.UserMaritalStatus;
import com.lovemesomecoding.entity.user.UserStatus;
import com.lovemesomecoding.security.AuthenticationService;
import com.lovemesomecoding.security.jwt.JwtPayload;
import com.lovemesomecoding.security.jwt.JwtTokenService;
import com.lovemesomecoding.utils.PasswordUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTests {

    private MockMvc               mockMvc;

    @MockBean
    private AuthenticationService authenticationService;
    
    @MockBean
    private JwtTokenService jwtTokenService;

    @Resource
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Filter                springSecurityFilterChain;
    

    @Autowired
    private UserDAO userDAO;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(springSecurityFilterChain).build();

        JwtPayload jwtPayload = new JwtPayload();
        jwtPayload.setSub("user-33cdbbdd-75ed-44e3-8007-db8b7b8c3808");

        Mockito.when(jwtTokenService.getPayloadByToken(Mockito.anyString())).thenReturn(jwtPayload);
        Mockito.when(authenticationService.authorizeRequest(Mockito.anyString(), Mockito.any())).thenReturn(true);
        
        User user = new User();
        user.setId(1L);
        user.setUuid("user-33cdbbdd-75ed-44e3-8007-db8b7b8c3808");
        user.setFirstName("Folau");
        user.setLastName("Kaveinga");
        user.setStatus(UserStatus.ACTIVE);
        user.setDateOfBirth(Date.from(Instant.parse("1986-08-15T07:00:00.00Z")));
        user.setEmail("folaudev@gmail.com");
        user.setPhoneNumber("3109934731");
        user.setPassword(PasswordUtils.hashPassword("Test1234!"));
        user.setGender(UserGender.MALE);
        user.setPasswordExpirationDate(DateUtils.addMonths(new Date(), 12));
        user.setMaritalStatus(UserMaritalStatus.MARRIED);
        user.setAboutMe("I am so cool you dont even know");
        
        userDAO.save(user);
    }

    @Test
    public void test_getUserByUuid_with_null_user_uuid() throws Exception {
        String userUuid = null;

        // @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/users/{uuid}", userUuid)
                .header("token", "token");
       
        ResultActions result = this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(404));
        // @formatter:on
    }
    
    @Test
    public void test_getUserByUuid_with_invalid_user_uuid() throws Exception {
        String userUuid = "test-not-found-uuid";

        // @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/users/{uuid}", userUuid)
                .header("token", "token");
       
        ResultActions result = this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is(400));
    
        result.andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.isA(String.class)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("User not found")));
        // @formatter:on
    }
    
    @Test
    public void test_getUserByUuid_successfully() throws Exception {
        String userUuid = "user-33cdbbdd-75ed-44e3-8007-db8b7b8c3808";

        // @formatter:off
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/users/{uuid}", userUuid)
                .header("token", "token");
       
        ResultActions result = this.mockMvc.perform(request).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    
        result.andExpect(MockMvcResultMatchers.jsonPath("$.uuid", CoreMatchers.isA(String.class)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.uuid", CoreMatchers.is("user-33cdbbdd-75ed-44e3-8007-db8b7b8c3808")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.deleted", CoreMatchers.is(false)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.equalToObject("folaudev@gmail.com")));
        // @formatter:on
    }
}
