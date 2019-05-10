package com.lovemesomecoding.config;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovemesomecoding.utils.ObjectUtils;

@Configuration
public class GlobalConfig {

	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = ObjectUtils.getObjectMapper();
		// Date and Time Format
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US));
		return objectMapper;
	}

}
