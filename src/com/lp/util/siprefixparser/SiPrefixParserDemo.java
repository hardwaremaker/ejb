/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.util.siprefixparser;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class SiPrefixParserDemo extends JFrame {
	
	private static final long serialVersionUID = 5444152935811211143L;
	private JTextField input = new JTextField();
	private JTextField output = new JTextField();
	private SiPrefixParserAlternatives parser = new SiPrefixParserAlternatives();

	public SiPrefixParserDemo() {
		setSize(300, 100);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c = getContentPane();
		c.setLayout(new GridBagLayout());

		parser.setUnits("R", "F");
		parser.setMicroAlternative("u");
		
		input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
//				List<BigDecimalSI> bd = parser.parse(input.getText());
//				StringBuffer sb = new StringBuffer();
//				for(BigDecimalSI si : bd) {
//					sb.append(si.toSIString() + "; ");
//				}
//				output.setText(sb.toString());
				BigDecimalSI bd = parser.parseFirst(input.getText());
				output.setText(bd == null? "" : bd.toSIString());
			}
		});

		c.add(input,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 0,
						GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
		c.add(output,
				new GridBagConstraints(1, 0, 1, 1, 1.0, 0,
						GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new SiPrefixParserDemo();
	}
}
