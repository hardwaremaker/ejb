package com.lp.service.plscript;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class JavaSourceFromString extends SimpleJavaFileObject {
	private final String srcCode;
	
	public JavaSourceFromString(String name, String sourceCode) {
		super(URI.create("string:///" + name.replace('.', '/') + 
				Kind.SOURCE.extension), Kind.SOURCE);
		this.srcCode = sourceCode;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		return srcCode;
	}
}
