package com.lp.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonWriter {

	private Map<String, Object> jsonMap;
	
	public JsonWriter() {
		jsonMap = new HashMap<String, Object>();
	}
	
	private Map<String, Object> getJsonMap() {
		return jsonMap;
	}
	
	public void putString(String key, String value) {
		getJsonMap().put(key, value);
	}
	
	public void putInteger(String key, Integer value) {
		getJsonMap().put(key, value);
	}
	
	public void putBoolean(String key, Boolean value) {
		getJsonMap().put(key, value);
	}
	
	public <T> void putList(String key, List<T> value) {
		getJsonMap().put(key, value);
	}
	
	public String getJsonString() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.writeValueAsString(getJsonMap());
	}
}
