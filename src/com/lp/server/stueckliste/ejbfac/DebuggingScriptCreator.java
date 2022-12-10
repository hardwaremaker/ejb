package com.lp.server.stueckliste.ejbfac;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.plscript.CharSequenceCompilerException;
import com.lp.service.plscript.IScriptCreator;
import com.lp.service.plscript.ParameterNotFoundException;
import com.lp.service.plscript.ScriptFormula;
import com.lp.service.plscript.ScriptInstance;
import com.lp.service.plscript.ScriptRuntime;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

public class DebuggingScriptCreator implements IScriptCreator {
	private final static ILPLogger log = LPLogService.getInstance()
			.getLogger(DebuggingScriptCreator.class);
	private IScriptCreator delegate;
	
	public DebuggingScriptCreator(IScriptCreator delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void setMd5Hex(String md5Hex) {
		log.warn("Setting md5hex to '" + md5Hex + "'.");
		delegate.setMd5Hex(md5Hex);
	}

	@Override
	public String getMd5Hex() {
		return delegate.getMd5Hex();
	}
	
	@Override
	public CtClass createScript(Integer classId) throws NotFoundException, CannotCompileException {
		log.warn("creating script for classId '" + classId + "'.");
		return delegate.createScript(classId);
	}

	@Override
	public void createFormula(Integer classId, Integer formulaId, String resultClassName, String formulaBody)
			throws CannotCompileException, NotFoundException, ParameterNotFoundException, ClassCastException, CharSequenceCompilerException {
		log.warn("creating formula for classId '" + classId + "/" + formulaId + "'.");
		delegate.createFormula(classId, formulaId, resultClassName, formulaBody);
	}

	@Override
	public byte[] compile(Integer classId) throws IOException, CannotCompileException, NotFoundException {
		log.warn("compiling script for classId '" + classId + "'.");
		return delegate.compile(classId);
	}

	@Override
	public ScriptInstance instance(Integer classId) throws IOException, CannotCompileException, NotFoundException,
			InstantiationException, IllegalAccessException {
		return delegate.instance(classId);
	}

	@Override
	public ScriptInstance instance(Integer classId, ScriptRuntime rt) throws IOException, CannotCompileException,
			InstantiationException, IllegalAccessException, ClassNotFoundException {
		log.warn("instance for classId '" + classId + "'");
		return delegate.instance(classId, rt);
	}

	@Override
	public <T> T evaluate(ScriptInstance scriptInstance, Integer formulaId) throws InstantiationException,
			IllegalAccessException, NoSuchMethodException, InvocationTargetException, CannotCompileException {
		Object o = delegate.evaluate(scriptInstance, formulaId);
		log.warn("result for formulaId '" + formulaId + "' is '" + o.toString() + "'.");
		return (T) o;
	}

	public <T> T evaluate(ScriptInstance scriptInstance, ScriptFormula<?> formula) throws InstantiationException,
	IllegalAccessException, NoSuchMethodException, InvocationTargetException, CannotCompileException {
		Object o = delegate.evaluate(scriptInstance, formula);
		log.warn("result for formulaId '" + formula.getId() + "' is '" + o.toString() + "'.");
		return (T) o;		
	}
	
	@Override
	public boolean exists(Integer classId) {
		boolean b = delegate.exists(classId);
		log.warn("exists script for classId '" + classId + "' is " + b + ".");
		return b;
	}

	@Override
	public boolean exists(Integer classId, String md5Hex) {
		boolean b = delegate.exists(classId, md5Hex);
		log.warn("exists script for classId '" + classId + "_" + md5Hex + " is " + b + ".");
		return b;
	}
	
	@Override
	public void addParamInfo(String paramName, String typeInfo) throws CannotCompileException {
		log.warn("defining parameter '" + typeInfo + " " + paramName + "'.");
		delegate.addParamInfo(paramName, typeInfo);
	}
}
