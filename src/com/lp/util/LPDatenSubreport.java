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
package com.lp.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * <p>
 * <b>frame</b><br/>
 * HelperReport Funktionen und Konstanten, die client- und serverseitig
 * verwendbar sind. <br/>
 * Wenn hier etwas ge&auml;ndert wird, hat das Auswirkungen auf bestimmte
 * Reports.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2007-08-21
 * </p>
 * <br/>
 * 
 * @author ck
 * @version 1.0
 */

public class LPDatenSubreport implements net.sf.jasperreports.engine.JRDataSource, Cloneable, Serializable {

	private transient HashMap<String, BufferedImage> images = new LinkedHashMap<String, BufferedImage>();

	private void writeObject(ObjectOutputStream out) throws IOException {
		images = new LinkedHashMap<String, BufferedImage>();
		if (data != null) {
			for (int i = 0; i < data.length; i++) {
				Object[] oZeile = data[i];
				for (int j = 0; j < oZeile.length; j++) {
					if (oZeile[j] instanceof BufferedImage) {
						String key = "{IMG}-" + i + "-" + j;
						images.put(key, (BufferedImage) oZeile[j]);
						oZeile[j] = key;
					}
				}
			}
		}

		out.defaultWriteObject();
		out.writeInt(images.size()); // how many images are serialized?
		Iterator it = images.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(images.get(key), "png", baos);
			byte[] b = baos.toByteArray();
			out.writeInt(b.length);
			out.write(b);
		}
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		final int imageCount = in.readInt();
		if (imageCount > 0) {
			for (int i = 0; i < data.length; i++) {
				Object[] oZeile = data[i];
				for (int j = 0; j < oZeile.length; j++) {
					if (oZeile[j] instanceof String) {
						String s = (String) oZeile[j];
						if (s != null && s.startsWith("{IMG}")) {
							int l = in.readInt();
							byte[] b = new byte[l];
							in.read(b, 0, l);
							data[i][j] = ImageIO.read(new ByteArrayInputStream(b));
						}
					}
				}
			}
		}
	}

	private Object[][] data = null;
	protected int index = -1;
	private String[] fieldnames = null;

	public Object getFieldValue(JRField field) throws JRException {
		String fieldName = field.getName();
		Object value = null;
		for (int i = 0; i < fieldnames.length; i++) {
			if (fieldName.equals(fieldnames[i])) {
				return data[index][i];
			}
		}
		return value;
	}

	public int getAnzahlDatensaetze() {
		if (data != null) {
			return data.length;
		} else {
			return 0;
		}

	}

	public boolean next() throws JRException {
		index++;

		if (index >= data.length) {
			index = -1;
			return false;
		} else {
			return true;
		}
	}

	protected LPDatenSubreport() {
		this.data = null;
		this.fieldnames = null;
	}

	public LPDatenSubreport(Object[][] data, String[] fieldnames) {
		this.data = data;
		this.fieldnames = fieldnames;
	}

	public LPDatenSubreport(ArrayList data, String[] fieldnames) {
		this.data = (Object[][]) data.toArray(new Object[data.size()][fieldnames.length]);

		this.fieldnames = fieldnames;
	}

	protected void setData(Object[][] data) {
		this.data = data;
	}

	public Object clone() {
		LPDatenSubreport o = null;
		try {
			o = (LPDatenSubreport) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		// Referenzen clonen
		o.data = (Object[][]) this.data.clone();
		o.fieldnames = (String[]) this.fieldnames.clone();
		return o;
	}

}
