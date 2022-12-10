package com.lp.util;

import java.util.List;

import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorImageBlockDto;
import com.lp.server.system.service.EditorTextBlockDto;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class LPBlockSubreport extends LPDatenSubreport {
	private static final long serialVersionUID = 7762533703617475547L;
//	private final ILPLogger log = LPLogService.getInstance().getLogger(LPBlockSubreport.class);

	enum Fields {
		BlockId("F_ID"),
		Col("F_COL"),
		Row("F_ROW"),
		ColumnType("F_CNR"),
		Text("F_TEXT"),
		Image("F_IMAGE");
		
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
	
	private transient final List<EditorBaseBlockDto> blocks;
	
	public LPBlockSubreport(List<EditorBaseBlockDto> blocks) {
		this.blocks = blocks;
	}
	
	@Override
	public boolean next() throws JRException {
		if (++index >= blocks.size()) {
			index = -1;
			return false;
		}
		
		return true;
	}
	
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		String fieldname = field.getName().trim();
		if (fieldname.equals(Fields.BlockId.getText())) {
			return blocks.get(index).getId().id();
		} else if (fieldname.equals(Fields.Col.getText())) {
			return new Integer(blocks.get(index).getColumn());
		} else if (fieldname.equals(Fields.Row.getText())) {
			return new Integer(blocks.get(index).getRow());			
		} else if (fieldname.equals(Fields.ColumnType.getText())) {
			if (blocks.get(index).getClass() == EditorTextBlockDto.class) {
				return "Text";
			} else if (blocks.get(index).getClass() == EditorImageBlockDto.class) {
				return "Image";
			} else {
				throw new EJBExceptionLP(new Exception("Unbekannte Subklasse von EditorBaseBlockDto: " 
						+ blocks.get(index).getClass()
						+ "; Die getFieldValue() Methode muss angepasst werden!"));
			}
		} else if (fieldname.equals(Fields.Text.getText())) {
			if (blocks.get(index).getClass() == EditorTextBlockDto.class) {
				EditorTextBlockDto textDto = (EditorTextBlockDto) blocks.get(index);
				return textDto.getText();
			} else {
				return null;
			}
		} else if (fieldname.equals(Fields.Image.getText())) {
			if (blocks.get(index).getClass() == EditorImageBlockDto.class) {
				EditorImageBlockDto imageDto = (EditorImageBlockDto) blocks.get(index);
//				log.info("Image Id " + imageDto.getId().id() + 
//						", w:" + imageDto.getWidth() + 
//						", h:" + imageDto.getHeight() + ".");
//				BufferedImage bi = imageDto.getImageDto().getBufferedImage();
				return imageDto;
			} else {
				return null;
			}
		}
		
		return "?" + fieldname + "?";
	}
}
