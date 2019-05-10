/*******************************************************************************
 * @ ObjectUtils.java
 * @ Project: SideCar Health Corporation
 *
 * Copyright (c) 2018 SideCar Health Corporation. - All Rights Reserved
 * El Segundo, California, U.S.A.
 *
 * This software is the confidential and proprietary information of
 * SideCar Health Corporation. ("Confidential Information").
 *
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with SideCar Corporation.
 *
 * Contributors:
 * SideCar Health Corporation. - Software Engineering Team
 ******************************************************************************/
package com.lovemesomecoding.utils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class ObjectUtils {

	public static ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jdk8Module());
		// Serialization
		// Serialization
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, true);
		objectMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
		// Deserialization
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);

		objectMapper.setSerializationInclusion(Include.NON_NULL);

		// Date and Time Format
		objectMapper.setDateFormat(getSidecarDateForm());

		objectMapper.setSerializationInclusion(Include.NON_NULL);
		return objectMapper;
	}
	
	public static DateFormat getSidecarDateForm() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
	}
	
	public static ObjectNode getObjectNode() {
		return getObjectMapper().createObjectNode();
	}
	
	public static ArrayNode getArrayNode() {
		return getObjectMapper().createArrayNode();
	}
	
	public static <T> T map(Object object, Class<T> clazz) {
		try {
			return getObjectMapper().convertValue(object, clazz);
		} catch (Exception e) {
			System.out.println("ObjectUtil - printJson - JsonProcessingException - Msg: " + e.getLocalizedMessage());
			return null;
		}
	}
	
	public static String toJson(Object object) {
		try {
			return ObjectUtils.getObjectMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			System.out.println("JsonProcessingException, msg: "+e.getLocalizedMessage());
			return "{}";
		}
	}
	
	public static <T> T fromJsonString(String json, Class<T> clazz) {
		try {
			return ObjectUtils.getObjectMapper().readValue(json, clazz);
		} catch (Exception e) {
			System.out.println("JsonProcessingException, msg: "+e.getLocalizedMessage());
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<String, String> fromJsonStringToMap(String json) {
		try {
			TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
			};
			return ObjectUtils.getObjectMapper().readValue(json, typeRef);
		} catch (Exception e) {
			System.out.println("JsonProcessingException, msg: "+e.getLocalizedMessage());
			return null;
		}
		
	}
	
    public static List<Field> getNonNullFields(Object thisObject, List<String> ignoredFieldNames) {
        List<Field> returnFields = null;
        if (thisObject != null) {
            Field[] fields = thisObject.getClass().getDeclaredFields();
            returnFields = new ArrayList<Field>();
            for (Field field : fields) {
                boolean accessFlag = field.isAccessible();
                try {

                    if (!accessFlag) {
                        field.setAccessible(true);
                    }
                    if (field.get(thisObject) != null) {
                        if (ignoredFieldNames != null && ignoredFieldNames.contains(field.getName())) {
                            continue;
                        }
                        returnFields.add(field);
                    }
                } catch (Exception e) {
                }
                if (!accessFlag) {
                    field.setAccessible(false);
                }
            }
        }
        return returnFields;
    }
}
