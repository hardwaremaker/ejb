package com.lp.service.plscript;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

public interface IScriptCreator {
	void setMd5Hex(String md5Hex);
	String getMd5Hex();
	
	CtClass createScript(Integer classId) throws NotFoundException, CannotCompileException;

	void addParamInfo(String paramName, String typeInfo) throws CannotCompileException;

	void createFormula(Integer classId, Integer formulaId, String resultClassName, String formulaBody)
			throws CannotCompileException, NotFoundException, ParameterNotFoundException, ClassCastException, CharSequenceCompilerException;

	byte[] compile(Integer classId) throws IOException, CannotCompileException, NotFoundException;

	ScriptInstance instance(Integer classId) throws IOException, CannotCompileException, NotFoundException,
			InstantiationException, IllegalAccessException;

	ScriptInstance instance(Integer classId, ScriptRuntime rt) throws IOException, CannotCompileException,
			InstantiationException, IllegalAccessException, ClassNotFoundException;

	<T> T evaluate(ScriptInstance scriptInstance, Integer formulaId) throws InstantiationException,
			IllegalAccessException, NoSuchMethodException, InvocationTargetException, CannotCompileException;

	<T> T evaluate(ScriptInstance scriptInstance, ScriptFormula<?> formula) throws InstantiationException,
	IllegalAccessException, NoSuchMethodException, InvocationTargetException, CannotCompileException;

	boolean exists(Integer classId);

	boolean exists(Integer classId, String md5Hex);
}
