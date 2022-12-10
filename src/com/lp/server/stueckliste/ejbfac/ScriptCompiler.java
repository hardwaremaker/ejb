package com.lp.server.stueckliste.ejbfac;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.apache.commons.codec.digest.DigestUtils;

import com.lp.server.stueckliste.service.StklparameterDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.collection.IInject;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.plscript.CharSequenceCompilerException;
import com.lp.service.plscript.IScriptCreator;
import com.lp.service.plscript.ParameterNotFoundException;
import com.lp.service.plscript.ScriptFormula;
import com.lp.service.plscript.ScriptInstance;
import com.lp.service.plscript.ScriptRuntime;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import javassist.CannotCompileException;
import javassist.NotFoundException;


public class ScriptCompiler {
	private final static ILPLogger log = LPLogService
			.getInstance().getLogger(ScriptCompiler.class);
	
	private Integer stuecklisteId;
	private StklparameterDto[] stklParams;
	
	public ScriptCompiler(Integer stuecklisteId, StklparameterDto[] params) {
		this.stuecklisteId = stuecklisteId;
		this.stklParams = params;
	}

	public Integer getClassId() {
		return stuecklisteId;
	}
	
	public String compile(List<ScriptFormula<?>> formulas, IScriptCreator scriptCreator) {
		String md5hex = getMd5("frm", formulas);
		if(md5hex == null) return null;
		
		IScriptCreator sc = getDefaultScriptCreator(md5hex, scriptCreator);
		compileImpl(sc, md5hex, formulas);
		return md5hex;
	}
	
	private void addStklParams(IScriptCreator sc) {
		for (StklparameterDto paramDto : stklParams) {
			try {
				sc.addParamInfo(paramDto.getCNr(), paramDto.getCTyp());				
			} catch(CannotCompileException e) {
				throw EJBExcFactory.scriptParameterBereitsBekannt(e, paramDto);
			}
		}
	}
	
	private void compileImpl(IScriptCreator sc, String md5hex, 
			List<ScriptFormula<?>> formulas) {
		if(!sc.exists(stuecklisteId, md5hex)) {
			addStklParams(sc);				
			
			for (ScriptFormula<?> formula : formulas) {
				if (Helper.isStringEmpty(formula.getSrc()))
					continue;

				try {
					sc.createFormula(stuecklisteId, formula.getId(), 
							formula.getResultType(), formula.getSrc());
					
				} catch(CharSequenceCompilerException e) {
					log.error("CannotCompileException", e);
					StringBuffer sb = new StringBuffer();
					for (Diagnostic<? extends JavaFileObject> diag : e.getDiagnostics().getDiagnostics()) {
						sb.append(diag.toString());
					}
					throw EJBExcFactory
						.scriptCompileFehlerFormel(new CannotCompileException(sb.toString()), stuecklisteId, formula);
					
				} catch(CannotCompileException e) {
					log.error("CannotCompileException", e);
					throw EJBExcFactory
						.scriptCompileFehlerFormel(e, stuecklisteId, formula);
				} catch(NotFoundException e) {
					log.error("NotFoundException", e);
					throw EJBExcFactory
						.scriptCompileFehlerKlasseNochNichtErzeugt(
								e, stuecklisteId, formula);
				} catch(ParameterNotFoundException e) {
					log.error("ParameterNotFoundException", e);
					throw EJBExcFactory
						.scriptParameterUnbekannt(e, stuecklisteId, formula);
				}
			}				
		}			
	}
	
	
	private void runImpl(IScriptCreator sc, List<ScriptFormula<?>> formulas, ScriptRuntime runtime) {
		try {
			ScriptInstance si = sc.instance(stuecklisteId, runtime);
			for (ScriptFormula<?> formula : formulas) {
				try {
					Object o = sc.evaluate(si, formula);
					formula.storeResult(o);
				} catch(NoSuchMethodException e) {
					throw EJBExcFactory.scriptMethodeNichtGefunden(e, stuecklisteId, formula.getId());					
				} catch(InvocationTargetException e) {
					if(e.getTargetException() instanceof EJBExceptionLP) {
						throw (EJBExceptionLP) e.getTargetException();
					} else {
						throw EJBExcFactory.scriptMethodeNichtAufrufbar(e, stuecklisteId, formula.getId());						
					}
				}
			}									
		} catch(CannotCompileException e) {
			throw EJBExcFactory.scriptCompileFehlerKlasse(e, stuecklisteId);
		} catch(ClassNotFoundException e) {
			throw EJBExcFactory.scriptInstanceKlasseNichtGefunden(e, stuecklisteId);
		} catch(InstantiationException e) {
			throw EJBExcFactory.scriptInstanceNichtMoeglich(e, stuecklisteId);
		} catch(IllegalAccessException e) {
			throw EJBExcFactory.scriptInstanceFehlerhafterZugriff(e, stuecklisteId);			
		} catch(IOException e) {
			throw EJBExcFactory.scriptKlasseNichtErzeugbar(e, stuecklisteId);
		} catch(VerifyError e) {
			throw EJBExcFactory.scriptInstanceVerifyError(e, stuecklisteId);
		}
	}
	
		
	private void compileRunImpl(IScriptCreator sc, 
			List<ScriptFormula<?>> formulas, ScriptRuntime runtime) {
		compileImpl(sc, sc.getMd5Hex(), formulas);
		runImpl(sc, formulas, runtime);	
	}
	
	public void externalCompileRun(String prefix, IScriptCreator sc,
			List<ScriptFormula<?>> formulas, ScriptRuntime runtime) {
		String md5hex = getMd5(prefix, formulas);
		if(md5hex == null) return;
	
		IScriptCreator dsc = getDefaultScriptCreator(md5hex, sc);
		compileRunImpl(dsc, formulas, runtime);		
	}
	
	
	public String getMd5(String prefix, List<ScriptFormula<?>> formulas) {
		StringBuffer sb = getSourceCode(formulas);
		if(sb.length() == 0) return null;

		sb.append(getParamCode());
		
		String md5hex = DigestUtils.md5Hex(prefix +
				stuecklisteId.toString() + ":" + sb.toString());
		return md5hex;
	}

	private StringBuffer getSourceCode(List<ScriptFormula<?>> positions) {
		return CollectionTools.inject(new StringBuffer(),
				positions, new IInject<ScriptFormula<?>, StringBuffer>() {
			public StringBuffer inject(StringBuffer src, ScriptFormula<?> each) {
				if(Helper.isStringEmpty(each.getSrc())) return src;
				src.append(each.getSrc());
				return src;
			};
		});
	}

	private StringBuffer getParamCode() {
		StringBuffer sb = new StringBuffer();
		for (StklparameterDto stklparamDto : stklParams) {
			sb.append("{" + stklparamDto.getCTyp() + " " + stklparamDto.getCNr() + "}");
		}
		return sb;
	}
	
	private IScriptCreator getDefaultScriptCreator(String md5hex, IScriptCreator scriptCreator) {
		IScriptCreator sc = new DebuggingScriptCreator(scriptCreator);
		sc.setMd5Hex(md5hex);
		return sc;
	}
}

