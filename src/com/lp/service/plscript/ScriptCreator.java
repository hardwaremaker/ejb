package com.lp.service.plscript;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class ScriptCreator implements IScriptCreator {
    private ClassPool classPool;
    private Map<String, CtClass> classMap;
    private Map<String, String> paramMap;
    private String md5Hex;
    
    public ScriptCreator() {
        classMap = new HashMap<String, CtClass>();
        paramMap = new HashMap<String, String>();
        md5Hex = "";
    }

    public void setMd5Hex(String md5Hex) {
    	this.md5Hex = md5Hex;
    }
    
    @Override
    public String getMd5Hex() {
    	return md5Hex;
    }
    
    private ClassPool getClassPool() {
        if(classPool == null) {
            classPool = ClassPool.getDefault();
        }
        return classPool;
    }

    protected String getScriptName(Integer classId) {
    	return getScriptName(classId, md5Hex);
    }

    protected String getScriptName(Integer classId, String md5Hex) {
    	return "com.lp.service.plscript.impl.PlScript" + classId.toString() + md5Hex;
    }
    
    protected String getFormulaName(Integer formulaId) {
        return "eval" + formulaId;
    }

    public CtClass createScript(Integer classId) throws NotFoundException, CannotCompileException {
    	String className = getScriptName(classId);
        CtClass cc = null;
        try {
            cc = getClassPool().get(className);
            cc.detach();
        } catch(NotFoundException e) {
        }

        cc = createClass(className);
        classMap.put(className, cc);
        return cc;
    }

    protected CtClass createClass(String className) throws CannotCompileException, NotFoundException {
    	ClassPool pool = getClassPool();
    	pool.importPackage("java.math");
    	CtClass cc = pool.makeClass(className);
        cc.setSuperclass(getClassPool().get(getSuperclassName()));
        return cc;
    }
    
    protected String getSuperclassName() {
    	return "com.lp.service.plscript.BaseScript";	
    }
    
    private CtClass searchClass(Integer classId) throws NotFoundException, CannotCompileException {
        CtClass cc = classMap.get(getScriptName(classId));
        if(cc == null) {
            cc = createScript(classId);
        }
        return cc;
    }

    private String getParamType(String paramName) throws ParameterNotFoundException {
    	String typeInfo = paramMap.get(paramName);
    	if(null == typeInfo) {
    		throw new ParameterNotFoundException(paramName);
    	}
    	return typeInfo.trim();
    }
    
    // $P{REPORT_INFORMATION}
    // => getParam("REPORT_INFORMATION")
    protected String transformBody(String formulaBody) throws ParameterNotFoundException, CannotCompileException {
        String f = formulaBody;
        for(;;) {
            int index = f.indexOf("$P{");
            if(index == -1) break;

            int endIndex = f.indexOf("}", index+2);
            if(endIndex == -1) {
                throw new CannotCompileException("Missing } for $P{ at index " + index);
            }
            String paramName = f.substring(index + 3, endIndex);
            String paramType = getParamType(paramName);
 
            formulaBody = f.substring(0, index)
            		+ "((" + paramType + ")"
                    + "getParam(\"" + f.substring(index + 3, endIndex)
                    + "\"))"
                    + f.substring(endIndex+1);
            f = formulaBody;
        }
        return formulaBody;
    }

    public void createFormula(Integer classId, Integer formulaId,
            String resultClassName, String formulaBody) throws CannotCompileException, ParameterNotFoundException, NotFoundException, ClassCastException, CharSequenceCompilerException {
        CtClass cc = searchClass(classId);

//        CtMethod m = CtNewMethod.make(
//                "public " + resultClassName + " " + getFormulaName(formulaId) + "() {" +
//                        transformBody(formulaBody) +  "}", cc);
        CtMethod m = CtNewMethod.make(
                "public " + resultClassName + " " + getFormulaName(formulaId) + 
                "(com.lp.service.plscript.ScriptReportLogging report) {" +
                        transformBody(formulaBody) +  "}", cc);
        cc.addMethod(m);
    }

    public byte[] compile(Integer classId) throws IOException, CannotCompileException, NotFoundException {
        CtClass cc = searchClass(classId);
        return cc.toBytecode();
    }

    public ScriptInstance instance(Integer classId) throws IOException, CannotCompileException, NotFoundException, InstantiationException,IllegalAccessException {
        CtClass cc = searchClass(classId);
        return new ScriptInstance(cc.toClass());
    }

    public ScriptInstance instance(Integer classId, ScriptRuntime rt) throws IOException, CannotCompileException, InstantiationException,IllegalAccessException, ClassNotFoundException {
    	String className = getScriptName(classId);
    	CtClass cc = classMap.get(className);
    	if(cc == null) {
    		// Noch nicht bekannt, kann aber schon mal erzeugt worden sein
    		try {
    	       	Class c = Class.forName(className);
            	return new ScriptInstance(c, rt);   			
    		} catch(ClassNotFoundException e) {
    		}
    		
    		try {
    			cc = getClassPool().get(className);
    		} catch(NotFoundException e) {
    		}
    	}
        if(cc != null) {
        	return new ScriptInstance(cc.toClass(), rt);
         } else {
        	Class c = Class.forName(className);
        	return new ScriptInstance(c, rt);
        }
 //      CtClass cc = searchClass(classId);
    }

    public <T> T evaluate(ScriptInstance scriptInstance, Integer formulaId)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, CannotCompileException {
        Method m = scriptInstance.getClazz().getMethod(getFormulaName(formulaId));
        return (T) m.invoke(scriptInstance.getInstance(), new Object[]{});
    }
    
    public <T> T evaluate(ScriptInstance scriptInstance, ScriptFormula<?> formula) 
    		 throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, CannotCompileException {
    	scriptInstance.getInstance().setPayloadAny(formula.getPayload());
//    	return evaluate(scriptInstance, formula.getId());
        Method m = scriptInstance.getClazz().getMethod(
        		getFormulaName(formula.getId()), ScriptReportLogging.class);
        return (T) m.invoke(scriptInstance.getInstance(), new Object[]{formula.getReportLogger()});
    }
    
    public boolean exists(Integer classId) {
    	String classname = getScriptName(classId);
       	try {
    		CtClass cc = getClassPool().get(classname);
    		return true;
    	} catch(NotFoundException e) {
    	}
    	
       	try {
          	Class c = Class.forName(classname); 
          	return true;
       	} catch(ClassNotFoundException e) {
       	}
       	
     	return false;
   	
    }
    
    public boolean exists(Integer classId, String md5Hex) {
    	String classname = getScriptName(classId, md5Hex);
    	try {
    		CtClass cc = getClassPool().get(classname);
    		return true;
    	} catch(NotFoundException e) {
    	}
    	
    	return false;
    }
    
    @Override
    public void addParamInfo(String paramName, String typeInfo) throws CannotCompileException {
    	String existParam = paramMap.get(paramName);
    	if(existParam == null) {
    		paramMap.put(paramName, typeInfo);
    	} else {
    		throw new CannotCompileException("Parameter '" + paramName + "' existiert bereits!");
    	}
     }
}
