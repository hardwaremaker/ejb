package com.lp.service.plscript;


public class BaseScript {
    private ScriptRuntime runtime;

    public BaseScript(){
        this(new ScriptRuntime());
    }

    public BaseScript(ScriptRuntime rt) {
        runtime = rt;
    }

    public void setRuntime(ScriptRuntime rt) {
        runtime = rt;
    }

    protected ScriptRuntime rt() {
        return runtime;
    }

    public Object getParam(String paramName) {
        Object o = rt().getParam(paramName);
        return o;
    }
    
    public Object getVar(String variableName) {
    	Object o = rt().getVar(variableName);
    	return o;
    }
    
    public void setVar(String variableName, Object value) {
    	rt().setVar(variableName, value);
    }
    
    public boolean existsVar(String variableName) {
    	return rt().existsVar(variableName);
    }
    
    public boolean isGeneratePartlist() {
    	return rt().isProduktStueckliste();
    }
    
	public void halt(String cause) {
		rt().halt(cause);
	}
}
