package com.lp.server.stueckliste.ejbfac;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.service.plscript.IScriptCreator;
import com.lp.service.plscript.ScriptFormula;
import com.lp.service.plscript.ScriptInstance;
import com.lp.service.plscript.ScriptRuntime;

import javassist.CannotCompileException;

public class ScriptRunner {
	private Integer classId;
	
	public ScriptRunner(Integer stuecklisteId) {
		this.classId = stuecklisteId;
	}

	
	public void run(IScriptCreator sc, List<ScriptFormula<?>> formulas, ScriptRuntime runtime) {
		try {
			ScriptInstance si = sc.instance(classId, runtime);
			for (ScriptFormula<?> formula : formulas) {
				try {
					Object o = sc.evaluate(si, formula);
					formula.storeResult(o);
				} catch(NoSuchMethodException e) {
					throw EJBExcFactory.scriptMethodeNichtGefunden(e, classId, formula.getId());					
				} catch(InvocationTargetException e) {
					throw EJBExcFactory.scriptMethodeNichtAufrufbar(e, classId, formula.getId());
				}
			}									
		} catch(CannotCompileException e) {
			throw EJBExcFactory.scriptCompileFehlerKlasse(e, classId);
		} catch(ClassNotFoundException e) {
			throw EJBExcFactory.scriptInstanceKlasseNichtGefunden(e, classId);
		} catch(InstantiationException e) {
			throw EJBExcFactory.scriptInstanceNichtMoeglich(e, classId);
		} catch(IllegalAccessException e) {
			throw EJBExcFactory.scriptInstanceFehlerhafterZugriff(e, classId);			
		} catch(IOException e) {
			throw EJBExcFactory.scriptKlasseNichtErzeugbar(e, classId);
		} catch(VerifyError e) {
			throw EJBExcFactory.scriptInstanceVerifyError(e, classId);
		}
	}
}
