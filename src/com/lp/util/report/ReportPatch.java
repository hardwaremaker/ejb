/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.util.report;

import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.JRTemplatePrintText;
import net.sf.jasperreports.engine.fill.JRTemplateText;

/**
 * Jasper Version 5.5.x Patches
 * <p>Jasper Version 5 verwendet nun "SansSerif" als Standard-Font. Das f&uuml;hrt dann
 * zu Problemen, wenn der Server und der Client nicht die gleichen Fonts haben, bzw. diese
 * kennen. Dann nimmt der Server anderere Font-Informationen zur Berechnung, als der 
 * Client. Folge sind abgeschnittene Texte und der gleichen.</p>
 * <p>Mit Jasper 5 werden nun auch "Tabs" unterst&uuml;tzt. Damit k&ouml;nnen wir die
 * Tabs aus dem LpEditor an den Jasper senden. Damit wir uns die Tab-Definition sowohl
 * im LpEditor als auch im Report sparen, setzen wir alle 72px einen Tab.</p>
 * 
 * @author Gerold
 */
public class ReportPatch {
	private static final String DEFAULT_JASPER_FONT = "SansSerif" ;
	private static final String DEFAULT_HELIUM_FONT = "Arial" ;
	
	private JasperPrint jasperPrint ;
	
	public ReportPatch(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint ;
	}
	
	public void apply() {
		apply(jasperPrint) ;
	}
	
	public void apply(JasperPrint jasperPrint) {
		if(jasperPrint == null) throw new IllegalArgumentException("jasperPrint") ;
		
		for (JRPrintPage page : jasperPrint.getPages()) {
			for(JRPrintElement element : page.getElements()) {
				if(element instanceof JRTemplatePrintText) {
					applyTo((JRTemplatePrintText) element) ;
				}
			}
		}
	}
	
	protected void applyTo(JRTemplatePrintText textField) {
		applyTabs(textField) ;
		applyDefaultFont(textField);
	}
	
	protected void applyTabs(JRTemplatePrintText textField) {
		JRParagraph paragraph = textField.getParagraph() ;
		int tabstopWidth = paragraph.getTabStopWidth() ;
		if(0 == tabstopWidth || 40 == tabstopWidth) {
			textField.getParagraph().setTabStopWidth(72);						
		}		
	}
	
	protected void applyDefaultFont(JRTemplatePrintText textField) {
		String f = textField.getFontName() ;
		if(DEFAULT_JASPER_FONT.equals(f)) {
			JRTemplateText templateText = (JRTemplateText)textField.getTemplate() ;
			templateText.setFontName(DEFAULT_HELIUM_FONT);
		}		
	}
}

