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
 *******************************************************************************/
package com.lp.util;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.PdfFont;
import net.sf.jasperreports.engine.fonts.FontFace;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontInfo;
import net.sf.jasperreports.engine.fonts.FontUtil;
import net.sf.jasperreports.repo.RepositoryUtil;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

@SuppressWarnings("deprecation")
public class HVPDFExporter extends JRPdfExporter {

	protected Font getFont(Map<Attribute, Object> attributes, Locale locale,
			boolean setFontLines) {
		JRFont jrFont = new JRBaseFont(attributes);
		if(jrFont.isPdfEmbedded()) {
			return super.getFont(attributes, locale, setFontLines) ;
		}
		
		Exception initialException = null;

		Color forecolor = (Color) attributes.get(TextAttribute.FOREGROUND);

		// use the same font scale ratio as in
		// JRStyledText.getAwtAttributedString
		float fontSizeScale = 1f;
		Integer scriptStyle = (Integer) attributes
				.get(TextAttribute.SUPERSCRIPT);
		if (scriptStyle != null
				&& (TextAttribute.SUPERSCRIPT_SUB.equals(scriptStyle) || TextAttribute.SUPERSCRIPT_SUPER
						.equals(scriptStyle))) {
			fontSizeScale = 2f / 3;
		}

		Font font = null;
		PdfFont pdfFont = null;


		FontInfo fontInfo = FontUtil.getInstance(jasperReportsContext)
				.getFontInfo(jrFont.getFontName(), locale);
		if (fontInfo == null) {

			
			if(jrFont.getFontName()!=null && jrFont.getFontName().equals("Courier New")){

				if (!jrFont.isBold() && !jrFont.isItalic()) {
					pdfFont = new PdfFont("Courier", "Cp1252", false);
				} else if (jrFont.isBold() && !jrFont.isItalic()) {
					pdfFont = new PdfFont("Courier-Bold", "Cp1252", false);
				} else if (!jrFont.isBold() && jrFont.isItalic()) {
					pdfFont = new PdfFont("Courier-Oblique", "Cp1252", false);
				} else if (jrFont.isBold() && jrFont.isItalic()) {
					pdfFont = new PdfFont("Courier-BoldOblique", "Cp1252", false);
				} 
			} else if(jrFont.getFontName()!=null && jrFont.getFontName().equals("Times New Roman")){
				if (!jrFont.isBold() && !jrFont.isItalic()) {
					pdfFont = new PdfFont("Times", "Cp1252", false);
				} else if (jrFont.isBold() && !jrFont.isItalic()) {
					pdfFont =new PdfFont("Times-Bold", "Cp1252", false);
				} else if (!jrFont.isBold() && jrFont.isItalic()) {
					pdfFont = new PdfFont("Times-Italic", "Cp1252", false);
				} else if (jrFont.isBold() && jrFont.isItalic()) {
					pdfFont = new PdfFont("Times-BoldItalic", "Cp1252", false);
				} 
			} else {

				if(jrFont.isPdfEmbedded()) {
					return super.getFont(attributes, locale, setFontLines) ;
//					pdfFont = new PdfFont(jrFont.getOwnFontName(), jrFont.getPdfEncoding(), true) ;
				} else {
					if (!jrFont.isBold() && !jrFont.isItalic()) {
						pdfFont = new PdfFont("Helvetica", "Cp1252", false);
					} else if (jrFont.isBold() && !jrFont.isItalic()) {
						pdfFont = new PdfFont("Helvetica-Bold", "Cp1252", false);
					} else if (!jrFont.isBold() && jrFont.isItalic()) {
						pdfFont = new PdfFont("Helvetica-Oblique", "Cp1252", false);
					} else if (jrFont.isBold() && jrFont.isItalic()) {
						pdfFont = new PdfFont("Helvetica-BoldOblique", "Cp1252", false);
					} 
				}
			}

		} else {
			// fontName found in font extensions
			FontFamily family = fontInfo.getFontFamily();

			String pdfFontName = null;
			int pdfFontStyle = java.awt.Font.PLAIN;

			FontFace fontFace = fontInfo.getFontFace();
			if (fontFace != null) {
				pdfFontName = fontFace.getPdf();
				pdfFontName = pdfFontName == null ? fontFace.getTtf()
						: pdfFontName;
				pdfFontStyle = fontInfo.getStyle();
			}

			if (pdfFontName == null && jrFont.isBold() && jrFont.isItalic()) {
				fontFace = family.getBoldItalicFace();
				if (fontFace != null) {
					pdfFontName = fontFace.getPdf();
					pdfFontName = pdfFontName == null ? fontFace.getTtf()
							: pdfFontName;
					pdfFontStyle = java.awt.Font.BOLD | java.awt.Font.ITALIC;
				}
			}

			if (pdfFontName == null && jrFont.isBold()) {
				fontFace = family.getBoldFace();
				if (fontFace != null) {
					pdfFontName = fontFace.getPdf();
					pdfFontName = pdfFontName == null ? fontFace.getTtf()
							: pdfFontName;
					pdfFontStyle = java.awt.Font.BOLD;
				}
			}

			if (pdfFontName == null && jrFont.isItalic()) {
				fontFace = family.getItalicFace();
				if (fontFace != null) {
					pdfFontName = fontFace.getPdf();
					pdfFontName = pdfFontName == null ? fontFace.getTtf()
							: pdfFontName;
					pdfFontStyle = java.awt.Font.ITALIC;
				}
			}

			if (pdfFontName == null) {
				fontFace = family.getNormalFace();
				if (fontFace != null) {
					pdfFontName = fontFace.getPdf();
					pdfFontName = pdfFontName == null ? fontFace.getTtf()
							: pdfFontName;
					pdfFontStyle = java.awt.Font.PLAIN;
				}
			}

			if (pdfFontName == null) {
				pdfFontName = jrFont.getPdfFontName();
			}

			pdfFont = new PdfFont(pdfFontName,
					family.getPdfEncoding() == null ? jrFont.getPdfEncoding()
							: family.getPdfEncoding(),
					family.isPdfEmbedded() == null ? jrFont.isPdfEmbedded()
							: family.isPdfEmbedded().booleanValue(),
					jrFont.isBold()
							&& ((pdfFontStyle & java.awt.Font.BOLD) == 0),
					jrFont.isItalic()
							&& ((pdfFontStyle & java.awt.Font.ITALIC) == 0));
		}

		int pdfFontStyle = (pdfFont.isPdfSimulatedBold() ? Font.BOLD : 0)
				| (pdfFont.isPdfSimulatedItalic() ? Font.ITALIC : 0);
		if (setFontLines) {
			pdfFontStyle |= (jrFont.isUnderline() ? Font.UNDERLINE : 0)
					| (jrFont.isStrikeThrough() ? Font.STRIKETHRU : 0);
		}

		try {
			font = FontFactory.getFont(pdfFont.getPdfFontName(),
					pdfFont.getPdfEncoding(), pdfFont.isPdfEmbedded(),
					jrFont.getFontSize() * fontSizeScale, pdfFontStyle,
					forecolor);

			// check if FontFactory didn't find the font
			if (font.getBaseFont() == null
					&& font.getFamily() == Font.UNDEFINED) {
				font = null;
			}
		} catch (Exception e) {
			initialException = e;
		}

		if (font == null) {
//			Set families = FontFactory.getRegisteredFamilies() ;
//			Set fonts = FontFactory.getRegisteredFonts() ;
			
			byte[] bytes = null;

			try {
				bytes = RepositoryUtil.getInstance(jasperReportsContext)
						.getBytesFromLocation(pdfFont.getPdfFontName());
			} catch (JRException e) {
				throw // NOPMD
				new JRRuntimeException("Could not load the following font : "
						+ "\npdfFontName   : " + pdfFont.getPdfFontName()
						+ "\npdfEncoding   : " + pdfFont.getPdfEncoding()
						+ "\nisPdfEmbedded : " + pdfFont.isPdfEmbedded(),
						initialException);
			}

			BaseFont baseFont = null;

			try {
				baseFont = BaseFont.createFont(pdfFont.getPdfFontName(),
						pdfFont.getPdfEncoding(), pdfFont.isPdfEmbedded(),
						true, bytes, null);
			} catch (DocumentException e) {
				throw new JRRuntimeException(e);
			} catch (IOException e) {
				throw new JRRuntimeException(e);
			}

			font = new Font(baseFont, jrFont.getFontSize() * fontSizeScale,
					pdfFontStyle, forecolor);
		}

		return font;
	}


}
