package com.lp.server.fertigung.ejbfac;

import java.awt.image.BufferedImage;

import com.lp.server.fertigung.service.ScriptStuecklisteGraphicServiceDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPScriptEngine;

public class StuecklisteCreateGraphicScript extends LPScriptEngine {
	
	private ScriptStuecklisteGraphicServiceDto serviceDto ;
	private String scriptName ;
	
	public StuecklisteCreateGraphicScript(ScriptStuecklisteGraphicServiceDto dto,
			String scriptName, SystemFac systemFac, TheClientDto theClientDto) {
		super(systemFac, theClientDto) ;
		this.serviceDto = dto ;
		this.scriptName = scriptName ;
		beOptionalScript();
		// beMandatoryScript();
	}
	
	public BufferedImage getValue() {
		put("@serviceDto", serviceDto) ;

		try {
			Object receiver = runEmbeddedLPScript() ;			
			BufferedImage image = getEngine().callMethod(receiver, "printAction", BufferedImage.class) ;
			return image ;
		} catch(Throwable t) {
			t.printStackTrace(); 
		}

		return null ;
	}
	
	public String getSvg() {
		put("@serviceDto", serviceDto) ;

		try {
			Object receiver = runEmbeddedLPScript() ;
			if(isProcessed()) {
				String s = getEngine().callMethod(receiver, "printAction", String.class) ;
				return s ;
			}
		} catch(Throwable t) {
			t.printStackTrace(); 
		}

		return null ;
	}
		
	@Override
	protected String getModule() {
		return StuecklisteReportFac.REPORT_MODUL ;
	}
	
	@Override
	protected String getScriptName() {
		return scriptName ;
	}	
}
