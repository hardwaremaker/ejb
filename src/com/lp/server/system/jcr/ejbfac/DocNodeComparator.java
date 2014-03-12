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
package com.lp.server.system.jcr.ejbfac;

import java.util.Comparator;
import java.util.List;

import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeJCR;
import com.lp.server.system.jcr.service.docnode.DocNodeSymbolicLink;
import com.lp.server.system.jcr.service.docnode.DocNodeVersion;

public class DocNodeComparator implements Comparator<DocNodeBase> {

	private int compare(DocNodeJCR node1, DocNodeJCR node2) {
		if(node1.getBelegart().equals(DocNodeBase.BELEGART_PARTNER)
			||node1.getBelegart().equals(DocNodeBase.BELEGART_LIEFERANT)
					||node1.getBelegart().equals(DocNodeBase.BELEGART_KUNDE))
			return node1.asVisualPath().compareToIgnoreCase(node2.asVisualPath());
		
		return node2.asVisualPath().compareToIgnoreCase(node1.asVisualPath());
	}
	
	private int compare(DocNodeVersion node1, DocNodeVersion node2) {
		Long l1 = node1.getlVersion();
		Long l2 = node2.getlVersion();

		if (!l1.equals(l2)) {
			if (l1 == -1)
				return -1;
			if (l2 == -1)
				return 1;
			return l2.compareTo(l1);
		}

		Long z1 = node1.getJCRDocDto().getlZeitpunkt();
		Long z2 = node2.getJCRDocDto().getlZeitpunkt();
		return z2.compareTo(z1);
	}
	
	private int compare(DocNodeSymbolicLink node1, DocNodeSymbolicLink node2) {
		List<DocNodeBase> path1 = node1.getViewPath().asDocNodeList();
		List<DocNodeBase> path2 = node2.getViewPath().asDocNodeList();
		for(int i = 0; i < path1.size() && i < path2.size(); i++) {
			int c = compare(path1.get(i), path2.get(i));
			if(c != 0)
				return c;
		}
		return 0;
	}
	
	@Override
	public int compare(DocNodeBase o1, DocNodeBase o2) {
		if(o1.getNodeType() == DocNodeBase.SYMBOLIC_LINK && o2.getNodeType() == DocNodeBase.SYMBOLIC_LINK) {
			return compare((DocNodeSymbolicLink)o1, (DocNodeSymbolicLink)o2);
		}
		if (o1.getNodeType() == DocNodeBase.JCRDOC
				&& o2.getNodeType() == DocNodeBase.JCRDOC) {
			return compare((DocNodeJCR)o1, (DocNodeJCR)o2);
		} else if (o1.getNodeType() == DocNodeBase.JCRDOC) {
			return 1;
		} else if (o2.getNodeType() == DocNodeBase.JCRDOC) {
			return -1;
		}
		if (o1.getNodeType() == DocNodeBase.VERSION
				&& o2.getNodeType() == DocNodeBase.VERSION) {
			return compare((DocNodeVersion) o1, (DocNodeVersion) o2);
		}

		return o1.asVisualPath().trim().compareToIgnoreCase(o2.asVisualPath().trim());
	}

}
