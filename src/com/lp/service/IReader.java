package com.lp.service;

import java.io.IOException;
import java.util.List;

public interface IReader {
	
	String getString() throws IOException;
	Integer getInteger() throws IOException;
	Boolean getBoolean() throws IOException;
	
	String getString(String key) throws IOException;
	Integer getInteger(String key) throws IOException;
	Boolean getBoolean(String key) throws IOException;
	<T> List<T> getList(String key) throws IOException;
}
