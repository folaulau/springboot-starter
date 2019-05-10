package com.lovemesomecoding.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ConversionUtils {

	public static String toDateString(Date date) {
		return new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a").format(date);
	}
	
	public static String toJsonString(Object object) {
		try {
			return ObjectUtils.getObjectMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			System.out.println("ConversionUtils - toJsonString - JsonProcessingException - Msg: "+e.getLocalizedMessage());
			return "{}";
		}
	}
	
	public static <E> List<E> getListFromSet(Set<E> set) {
		return new ArrayList<E>(set);
	}

	public static <E> Set<E> getSetFromList(List<E> list) {
		return new HashSet<E>(list);
	}
	
	public static <T> T map(Object object, Class<T> clazz) {
		try {
			return ObjectUtils.getObjectMapper().convertValue(object, clazz);
		} catch (Exception e) {
			System.out.println("ObjectUtil - printJson - JsonProcessingException - Msg: " + e.getLocalizedMessage());
			return null;
		}

	}

	public static File convert(MultipartFile multipartFile) {
		File convFile = new File(multipartFile.getOriginalFilename());
	    try {
	    	convFile.createNewFile(); 
		    FileOutputStream fos = new FileOutputStream(convFile); 
		    fos.write(multipartFile.getBytes());
		    fos.close(); 
		    return convFile;
		} catch (Exception e) {
			System.out.println("Exception, msg: "+e.getMessage());
			return null;
		}
	}
	
	public static Map<String, Object> convertJsonStringToMap(String jsonObj){
		try {

			ObjectMapper mapper = new ObjectMapper();

			Map<String, Object> map = new HashMap<String, Object>();

			map = mapper.readValue(jsonObj, new TypeReference<Map<String, Object>>(){});
			
			return map;

		} catch (JsonGenerationException e) {
			//System.out.println("JsonGenerationException, msg: "+e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			//System.out.println("JsonMappingException, msg: "+e.getLocalizedMessage());
		} catch (IOException e) {
			//System.out.println("IOException, msg: "+e.getLocalizedMessage());
		}
		return null;
	}
}
