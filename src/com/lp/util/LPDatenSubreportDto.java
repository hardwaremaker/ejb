package com.lp.util;

import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public abstract class LPDatenSubreportDto<T> extends LPDatenSubreport {
	private static final long serialVersionUID = 4058997173036076151L;
	private final List<T> rows;
	
	protected LPDatenSubreportDto(List<T> rows) {
		this.rows = rows;
	}

	@Override
	public Object getFieldValue(JRField field) throws JRException {
		String fieldname = field.getName().trim();
		return getEntityValue(rows.get(index), fieldname);
	}

	public boolean next() throws JRException {
		if(++index >= rows.size()){
			index = -1;
			return false;
		} else {
			return true;
		}		
	}
	
	protected abstract Object getEntityValue(T dto, String fieldname); 
} 
