package com.lp.server.stueckliste.ejbfac;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.StrukturDatenParamDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.service.plscript.ScriptFormula;
import com.lp.util.Helper;

public class ScriptRunnerFLRStuecklisteposition extends ScriptRunner {
	private ScriptCompiler compiler;
	
	public ScriptRunnerFLRStuecklisteposition(ScriptCompiler compiler) {
		super(compiler.getClassId());
		this.compiler = compiler;
	}
	
	public void compileRun(List<FLRStuecklisteposition> positions,
			StrukturDatenParamDto paramDto) {
		List<ScriptFormula<?>> formulas = createPosFormulas(positions);
		compiler.externalCompileRun("pos",
				new ScriptCreatorFLRStuecklisteposition(), formulas, paramDto.getRuntime());
	}

	public String compile(StuecklistepositionDto posDto) {
		List<ScriptFormula<?>> formulas = new ArrayList<ScriptFormula<?>>();
		formulas.add(new StuecklistepositionDtoFormula(posDto.getIId(), posDto.getXFormel()));
		return compiler.compile(formulas, new ScriptCreatorFLRStuecklisteposition());
	}

	private List<ScriptFormula<?>> createPosFormulas(List<FLRStuecklisteposition> positions) {
		List<ScriptFormula<?>> formulas = new ArrayList<ScriptFormula<?>>();
		for (FLRStuecklisteposition pos : positions) {
			if(Helper.isStringEmpty(pos.getX_formel())) continue;
			ScriptFormula<?> formula = 
					new FLRStuecklistepositionFormula(pos.getI_id(), pos.getX_formel(), pos);
			pos.reportLogging = formula.getReportLogger();
			formulas.add(formula);
		}
		return formulas;
	}
}
