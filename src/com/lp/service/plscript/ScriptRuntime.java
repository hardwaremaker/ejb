package com.lp.service.plscript;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.lp.server.util.ReportSqlExecutor;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

public class ScriptRuntime implements Serializable {
	private static final long serialVersionUID = -3967586404440745107L;
	private static final ILPLogger log = LPLogService.getInstance()
			.getLogger(ScriptRuntime.class);
	
	private Map<String, Object> params;
	private Map<String, Object> vars;
	private ReportSqlExecutor sql;
	private boolean produktStueckliste;
	
    public ScriptRuntime(){
        params = new HashMap<String, Object>();
        vars = new HashMap<String, Object>();
    }

    public Object getParam(String paramName) {
        Object o = params.get(paramName);
        return o;
    }

    public void setParam(String paramName, Object value) {
        params.put(paramName, value);
    }

    public Object getVar(String variableName) {
        Object o = vars.get(variableName);
        return o;
    }

    public void setVar(String variableName, Object value) {
    	vars.put(variableName, value);
    }

    public boolean existsVar(String variableName) {
    	return vars.containsKey(variableName);
    }
    
	public ReportSqlExecutor getSql() {
		return sql;
	}

	public void setSql(ReportSqlExecutor sql) {
		this.sql = sql;
	}
	
	public void beProduktStueckliste() {
		produktStueckliste = true;
	}
	
	public void beGesamtkalkulation() {
		produktStueckliste = false;
	}
	
	public boolean isProduktStueckliste() {
		return produktStueckliste;
	}

	public void halt(String cause) {
		log.error("halting for '"+ cause + "'");
	}
}
