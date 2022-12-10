package com.lp.service.plscript;

import java.util.Set;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public class CharSequenceCompilerException extends Exception {
	private static final long serialVersionUID = 2545228954012724160L;

	private final Set<String> classNames;
	private final DiagnosticCollector<JavaFileObject> diagnostics;
	
	public CharSequenceCompilerException(String message,
			Set<String> classNames, DiagnosticCollector<JavaFileObject> diagnostics) {
		super(message);
		this.classNames = classNames;
		this.diagnostics = diagnostics;
	}
	
	public CharSequenceCompilerException(
			Set<String> classNames, Exception ex, DiagnosticCollector<JavaFileObject> diagnostics) {
		super(ex);
		this.classNames = classNames;
		this.diagnostics = diagnostics;
	}
	
	public Set<String> getClassNames() {
		return classNames;
	}
	
	public DiagnosticCollector<JavaFileObject> getDiagnostics() {
		return diagnostics;
	}
}
