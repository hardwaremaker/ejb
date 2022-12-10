package com.lp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader implements IReader {
	
	private String string;
	private Map<String, Object> jsonMap;
	
	public JsonReader(String string) {
		this.string = string;
	}
	
	private Map<String, Object> getJsonMap() throws IOException {
		if (jsonMap == null) {
			ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String,Object>>() {
			};
			jsonMap = objectMapper.readValue(string, typeRef);
		}
		
		return jsonMap;
	}
	
	@Override
	public String getString() throws IOException {
		return null;
	}

	@Override
	public Integer getInteger() throws IOException {
		return null;
	}

	@Override
	public Boolean getBoolean() throws IOException {
		return null;
	}

	@Override
	public String getString(String key) throws IOException {
		return (String)getJsonMap().get(key);
	}

	@Override
	public Integer getInteger(String key) throws IOException {
		return (Integer)(getJsonMap().get(key));
	}

	@Override
	public Boolean getBoolean(String key) throws IOException {
		return (Boolean)(getJsonMap().get(key));
	}

	public <T> List<T> getList(String key) throws IOException {
		List<?> list = (List<?>)getJsonMap().get(key);
		List<T> returnList = new ArrayList<T>();
		for (Object element : list) {
			returnList.add((T) element);
		}
		
		return returnList;
	}
}
