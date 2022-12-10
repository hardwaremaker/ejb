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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Diese Klasse instanziert alle Objekte eines Wurzelobjekts, bis zur
 * untersten Ebene. Abgeleitete Klassen koennen Datentypen definieren, die
 * ausgelassen werden sollen, sowie Ausnahmen bestimmen.
 * 
 * @author andi
 *
 * @param <T>
 */
public abstract class FieldInstantiator<T> {

	protected Map<String, Integer> excludedDatatypes;
	
	public FieldInstantiator() {
		excludedDatatypes = new HashMap<String, Integer>();
	}

	/**
	 * Instanziert alle Felder des uebergebenen Wurzelelements, die den
	 * Wert null enthalten, bis in die unterste Ebene.
	 * 
	 * @param element Wurzelelement
	 * @return Wurzelelement, mit instanzierten Objekten
	 * @throws Exception
	 */
	public T instantiateNullFields(T element) 
			throws Exception {
		
		instantiateNullFieldsImpl(element);
		
		return element;
	}

	/**
	 * Implementierung der Instanzierung. Rekursive Methode fuer
	 * hierarchische Instanzierung.
	 * 
	 * @param elem aktuell zu pruefendes Objekt
	 * @throws Exception
	 */
	private void instantiateNullFieldsImpl(Object elem) 
			throws Exception {
		
		Method[] methods = elem.getClass().getMethods();

		for(Method theMethod : methods) {
			if(!theMethod.getName().startsWith("get")) continue;
			if(theMethod.getName().startsWith("getClass")) continue;
			if(excludedDatatypes.containsKey(theMethod.getReturnType().getSimpleName())) continue;
			if(meetExceptions(theMethod)) continue;
			
			if(List.class.getSimpleName().equals(theMethod.getReturnType().getSimpleName())) {
				instantiateListElements(elem, theMethod);
				continue;
			}

			instantiateFieldIfNull(elem, theMethod);
			instantiateNullFieldsImpl(theMethod.invoke(elem));
		}
		
	}

	/**
	 * Definition von Ausnahmeregeln.
	 * 
	 * @param theMethod der Getter des aktuellen Feldes
	 * @return true, wenn keine Ausnahme erfuellt wurde
	 */
	protected abstract boolean meetExceptions(Method theMethod);

	/**
	 * Instanziert ein einzelnes Feld mit seinem Default-Konstruktor
	 * 
	 * @param elem Basisobjekt, welches das zu instanzierende Feld beinhaltet
	 * @param theMethod Getter des zu instanzierenden Feldes
	 * @throws Exception 
	 */
	private void instantiateFieldIfNull(Object elem, Method theMethod)
			throws Exception {

		if(theMethod.invoke(elem) == null) {
			String setterName = theMethod.getName().replaceFirst("get", "set");
			Method setterMethod = elem.getClass().getMethod(setterName, theMethod.getReturnType());
			setterMethod.invoke(elem, theMethod.getReturnType().newInstance());
		}
	}

	/**
	 * Veranlasst die Instanziierung aller Elemente einer Liste. Die auszulassenden
	 * Datentypen werden dabei beruecksichtigt.
	 * 
	 * @param elem Basisobjekt, welches die Liste beinhaltet
	 * @param theMethod Getter der Liste
	 * @throws Exception 
	 */
	private void instantiateListElements(Object elem, Method theMethod) throws Exception {
		
		List<Object> list =  (List<Object>) theMethod.invoke(elem);
		Iterator<Object> listIter = list.iterator();
		while(listIter.hasNext()) {
			Object listElement = listIter.next();
			if(excludedDatatypes.containsKey(listElement.getClass().getSimpleName())) continue;
			
			instantiateNullFieldsImpl(listElement);
		}
	}

}
