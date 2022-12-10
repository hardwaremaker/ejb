package com.lp.server.forecast.service;

import java.io.Serializable;

public class EdiFileInfo implements Serializable {
	private static final long serialVersionUID = -8197802470606387714L;

	private byte[] content;
	private String name;
	
	public EdiFileInfo(byte[] content, String filename) {
		setContent(content);
		setName(filename);
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
