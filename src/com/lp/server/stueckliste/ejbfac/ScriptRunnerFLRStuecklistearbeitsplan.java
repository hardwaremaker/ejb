package com.lp.server.stueckliste.ejbfac;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.server.stueckliste.service.StrukturDatenParamDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.service.plscript.ScriptFormula;
import com.lp.util.Helper;

public class ScriptRunnerFLRStuecklistearbeitsplan extends ScriptRunner {
	private ScriptCompiler compiler;
	
	public ScriptRunnerFLRStuecklistearbeitsplan(ScriptCompiler compiler) {
		super(compiler.getClassId());
		this.compiler = compiler;
	}
	
	public void compileRun(List<FLRStuecklistearbeitsplan> positions,
			StrukturDatenParamDto paramDto) {
		List<ScriptFormula<?>> formulas = createApFormulas(positions);
		compiler.externalCompileRun("aps",
				new ScriptCreatorFLRStuecklistearbeitsplan(), formulas, paramDto.getRuntime());
	}

	public String compile(StuecklistearbeitsplanDto apDto) {
		List<ScriptFormula<?>> formulas = new ArrayList<ScriptFormula<?>>();
		formulas.add(new StuecklistearbeitsplanDtoFormula(apDto.getIId(), apDto.getXFormel()));
		return compiler.compile(formulas, new ScriptCreatorFLRStuecklistearbeitsplan());
	}

	private List<ScriptFormula<?>> createApFormulas(List<FLRStuecklistearbeitsplan> positions) {
		List<ScriptFormula<?>> formulas = new ArrayList<ScriptFormula<?>>();
		for (FLRStuecklistearbeitsplan pos : positions) {
			if(Helper.isStringEmpty(pos.getX_formel())) continue;
			ScriptFormula<?> formula = 
					new FLRStuecklistearbeitsplanFormula(pos.getI_id(), pos.getX_formel(), pos);
			pos.reportLogging = formula.getReportLogger();
			formulas.add(formula);
		}
		return formulas;
	}
}
