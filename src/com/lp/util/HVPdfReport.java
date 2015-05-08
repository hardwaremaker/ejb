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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

import javax.naming.InitialContext;

import com.lp.server.system.service.SystemFac;

public class HVPdfReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int HOCHFORMAT = 0;
	public static int QUERFORMAT = 1;

	private int resolution = 150;

	private int ausrichtung = HOCHFORMAT;

	private int drehungInGrad = 0;

	boolean drehenNurWennNoetig = true;

	private byte[] pdf = null;

	public HVPdfReport(byte[] pdf) {
		this.pdf = pdf;
	}

	public HVPdfReport(String pdfFile) throws Throwable {
		File file = new File(pdfFile);
		byte[] b = new byte[(int) file.length()];
		FileInputStream fileInputStream = new FileInputStream(file);
		fileInputStream.read(b);
		fileInputStream.close();
		this.pdf = b;
	}

	public HVPdfReport setDPI(int resolution) {
		this.resolution = resolution;
		return this;
	}

	public HVPdfReport setAusrichtung(int ausrichtung) {
		this.ausrichtung = ausrichtung;
		return this;
	}

	public HVPdfReport setDrehenNurWennNoetig(boolean drehenNurWennNoetig) {
		this.drehenNurWennNoetig = drehenNurWennNoetig;
		return this;
	}

	public HVPdfReport setDrehungInGrad(int drehungInGrad) {
		this.drehungInGrad = drehungInGrad;
		return this;
	}

	public LPDatenSubreport getSubreport() throws Throwable {
		SystemFac systemFac = (SystemFac) new InitialContext()
				.lookup("lpserver/SystemFacBean/remote");

		byte[][] bilder = systemFac.konvertierePDFFileInEinzelneBilder(pdf,
				resolution);

		if (bilder != null) {

			String[] fieldnames = new String[] { "SeiteAlsImage", };
			Object[][] dataSubKD = new Object[bilder.length][1];
			for (int i = 0; i < bilder.length; i++) {

				BufferedImage bi = Helper.byteArrayToImage(bilder[i]);

				if (drehungInGrad == 0) {

					if (drehenNurWennNoetig == true) {

						int width = bi.getWidth();
						int height = bi.getHeight();

						if (ausrichtung == HOCHFORMAT && width > height
								|| ausrichtung == QUERFORMAT && height > width) {

							BufferedImage returnImage = new BufferedImage(
									height, width, BufferedImage.TYPE_INT_RGB);

							for (int x = 0; x < width; x++) {
								for (int y = 0; y < height; y++) {
									returnImage.setRGB(y, width - x - 1,
											bi.getRGB(x, y));
								}
							}
							bi = returnImage;
						}
					}

				} else {
					bi = HelperReport.bildDrehen(bi, drehungInGrad);
				}

				dataSubKD[i][0] = bi;
			}
			return new LPDatenSubreport(dataSubKD, fieldnames);
		} else {
			return null;
		}

	}

}
