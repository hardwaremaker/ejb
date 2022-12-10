package com.lp.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorContentDto;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class LPBlockContentSubreport extends LPDatenSubreport {
	private static final long serialVersionUID = 4410977031123347035L;

	enum Fields {
		ContentId("F_ID"),
		Locale("F_LOCALE"),
		Row("F_ROW"),
		ColumnCount("F_COUNT"),
		Columns("F_COLUMNS");
		
		Fields(String value) {
			this.value = value;
		}
		
		public static Fields from(String text) {
			if (text != null) {
				for (Fields status : Fields.values()) {
					if (text.equalsIgnoreCase(status.value)) {
						return status;
					}
				}				
			}
			throw new IllegalArgumentException("No enum value '" + text + "'");
		}
		
		public String getText() {
			return value;
		}
		
		private String value;
	}
	
	private transient final EditorContentDto contentDto;
	private transient final List<EditorBaseBlockDto> blocks;
	
	public LPBlockContentSubreport(EditorContentDto dto) {
		this.contentDto = dto;
		blocks = this.contentDto.getBlockDtos();
		Collections.sort(blocks, new Comparator<EditorBaseBlockDto>() {
			@Override
			public int compare(EditorBaseBlockDto o1, EditorBaseBlockDto o2) {
				if (o1 == null && o2 == null) return 0;
				if (o1 == null) return -1;
				if (o2 == null) return 1;
				
				if (o1.getRow() < o2.getRow()) return -1;
				if (o1.getRow() > o2.getRow()) return 1;
				
				if (o1.getColumn() < o2.getColumn()) return -1;
				if (o1.getColumn() > o2.getColumn()) return 1;
				
				return 0;
			}
		});
	}
	
	@Override
	public boolean next() throws JRException {
		if (++index >= 1) {
			index = -1;
			return false;
		}
		
		return true;
	}
	
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		String fieldname = field.getName().trim();
		if (fieldname.equals(Fields.ContentId.getText())) {
			return contentDto.getId().id();
		} else if (fieldname.equals(Fields.Locale.getText())) {
			return contentDto.getLocale();
		} else if (fieldname.equals(Fields.Row.getText())) {
			return new Integer(index);
		} else if (fieldname.equals(Fields.ColumnCount.getText())) {
			return new Integer(contentDto.getBlockDtos().size());
		} else if (fieldname.equals(Fields.Columns.getText())) {
			return new LPBlockSubreport(blocks);
		}
		
		return "?" + fieldname + "?";
	}
}
