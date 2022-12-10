package com.lp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class DefaultReader implements IReader {
	
	private BufferedReader br;
	
	public DefaultReader(BufferedReader br) {
		this.br = br;
	}

	@Override
	public String getString() throws IOException {
		return br.readLine();
	}

	@Override
	public Integer getInteger() throws IOException {
		return Integer.parseInt(br.readLine());
	}

	@Override
	public Boolean getBoolean() throws IOException {
		return "1".equals(br.readLine());
	}

	@Override
	public String getString(String key) {
		return null;
	}

	@Override
	public Integer getInteger(String key) {
		return null;
	}

	@Override
	public Boolean getBoolean(String key) {
		return null;
	}

	@Override
	public <T> List<T> getList(String key) throws IOException {
		return null;
	}

}
