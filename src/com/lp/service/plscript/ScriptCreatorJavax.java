package com.lp.service.plscript;

import java.util.ArrayList;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public class ScriptCreatorJavax extends ScriptCreator {	
	public void createFormula(Integer classId, Integer formulaId,
            String resultClassName, String formulaBody) throws CannotCompileException, ParameterNotFoundException, NotFoundException, ClassCastException, CharSequenceCompilerException {
		compileWithJavax(classId, formulaId, resultClassName, formulaBody);
		super.createFormula(classId, formulaId, resultClassName, formulaBody);		
    }
	
	private void compileWithJavax(Integer classId, Integer formulaId, String resultClassName, String formulaBody) throws CannotCompileException, ParameterNotFoundException, ClassCastException, CharSequenceCompilerException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		CharSequenceCompiler<BaseScript> cc = new CharSequenceCompiler<BaseScript>(cl, getOptions(cl));
		cc.beCompileOnly();
		DiagnosticCollector<JavaFileObject> diags = new DiagnosticCollector<JavaFileObject>();
		String src =
				"import java.lang.* ;\n" + 
			    "import java.math.BigDecimal;\n" +
			    "import com.lp.service.plscript.ScriptReportLogging;\n" + 
				"public class Formula" + classId + "hex extends " + getSuperclassName() + " {\n" +
				"public " + resultClassName + " " + getFormulaName(formulaId) + 
                "(com.lp.service.plscript.ScriptReportLogging report) {\n" +
                transformBody(formulaBody) + "\n}\n" + "}";
		cc.compile("com.lp.service.plscript.Formula" + classId + "hex", src, diags, BaseScript.class);		
	}
	
	private Iterable<String> getOptions(ClassLoader classLoader) {
		ArrayList<String> opts = new ArrayList<String>();
		return opts;
	}
}