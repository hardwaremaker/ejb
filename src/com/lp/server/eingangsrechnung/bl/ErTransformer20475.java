package com.lp.server.eingangsrechnung.bl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.eingangsrechnung.service.ErImportError20475;
import com.lp.server.eingangsrechnung.service.ErImportItem20475;
import com.lp.server.eingangsrechnung.service.ErImportItemList20475;
import com.lp.server.eingangsrechnung.service.ErImportError20475.Severity;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpDefaultBelegnummerFormat;
import com.lp.server.util.DefaultFieldnameIndexer;
import com.lp.server.util.IFieldnameIndexer;
import com.lp.util.Helper;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ErTransformer20475 {
	
	public class Fieldnames {
		static final String ER_NR = "ER-Nr";
		static final String LIEFERANT = "Lieferant";
		static final String NAME = "Name";
		static final String EINGANGSDATUM = "Eingangs-Datum";
		static final String RECHNUNGSNR = "Rechnungs-Nr";
		static final String RECHNUNGSDATUM = "Rechnungs-Datum";
		static final String BESTELLNR = "Bestell-Nr.";
		static final String RECHNUNGSBETRAG = "Rechnungs-Betrag";
		static final String USTBETRAG = "USt-Betrag-1";
		static final String USTCODE = "USt-Cd";
		static final String WAEHRUNG = "Wrg";
		static final String KONTO = "Konto";
	}

	private IFieldnameIndexer fieldIndex = new DefaultFieldnameIndexer(new HashMap<String, Integer>());
	private Integer startRow = 4;
	private String dateFormat = "dd.MM.yyyy";
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
	private LpDefaultBelegnummerFormat belegnummerFormat;
	
	public ErTransformer20475(LpDefaultBelegnummerFormat belegnummerFormat) {
		this.belegnummerFormat = belegnummerFormat;
	}

	public ErImportItemList20475 transformXls(byte[] xlsBytes) throws BiffException, IOException {
		List<ErImportItem20475> list = new ArrayList<ErImportItem20475>();
		Sheet xlsSheet = getSheet(xlsBytes);
		
		if (startRow.compareTo(xlsSheet.getRows()) >= 0) {
			throw new IOException("Die Xls-Datei hat zu wenig Zeilen mit Daten (Definierte Startzeile = " + startRow + ", Zeilen Datei = " + xlsSheet.getRows() + ")");
		}
		
		buildFieldIndexer(xlsSheet.getRow(startRow - 1));
		for (int row = startRow; row < xlsSheet.getRows(); row++) {
			Cell[] cells = xlsSheet.getRow(row);
			ErImportItem20475 erItem = transformLine(cells);
			erItem.setLinenumber(row + 1);
			
			list.add(erItem);
		}
		
		return new ErImportItemList20475(list);
	}

	private ErImportItem20475 transformLine(Cell[] cells) {
		ErImportItem20475 er = new ErImportItem20475();
		setCnr(er, cells);
		er.setCreditor(getTrimmed(cells, Fieldnames.LIEFERANT));
		er.setCreditorName(getTrimmed(cells, Fieldnames.NAME));
		er.setDate(getDate(cells, Fieldnames.RECHNUNGSDATUM, dateFormat, er));
		er.setExpirationDate(getDate(cells, Fieldnames.EINGANGSDATUM, dateFormat, er));
		er.setCnrSupplier(getTrimmed(cells, Fieldnames.RECHNUNGSNR));
		er.setAmount(getBigDecimal(cells, Fieldnames.RECHNUNGSBETRAG, er));
		er.setOrderNumber(getTrimmed(cells, Fieldnames.BESTELLNR));
		er.setTaxAmount(getBigDecimal(cells, Fieldnames.USTBETRAG, er));
		er.setTaxCode(getTrimmed(cells, Fieldnames.USTCODE));
		er.setAccountNumber(getTrimmed(cells, Fieldnames.KONTO));
		er.setCurrency(getTrimmed(cells, Fieldnames.WAEHRUNG));
		
		return er;
	}

	private void setCnr(ErImportItem20475 er, Cell[] cells) {
		String cnr = getTrimmed(cells, Fieldnames.ER_NR);
		if (Helper.isStringEmpty(cnr))
			return; 
		
		if (cnr.length() <= belegnummerFormat.getStellenGeschaeftsjahr()) {
			// TODO add Error
			return;
		}
		
		try {
			Integer stellenGj = belegnummerFormat.getStellenGeschaeftsjahr();
			Integer gj = Integer.parseInt(cnr.substring(0, stellenGj));
			Integer lfdNummer = Integer.parseInt(cnr.substring(stellenGj));
			
			er.setCnr(belegnummerFormat.format(new LpBelegnummer(gj, null, lfdNummer)));
			er.setErNumber(Integer.parseInt(cnr));
		} catch (NumberFormatException exc) {
			// TODO add Error
		}
	}

	private Date getDate(Cell[] cells, String fieldname, final String dateFormat, ErImportItem20475 erItem) {
		final Cell cell = cells[fieldIndex.getIndex(fieldname)];
		if (cell == null || Helper.isStringEmpty(cell.getContents())) return null;
		
		try {
			if (CellType.DATE.equals(cell.getType())) {
				return new Date(((jxl.DateCell) cell).getDate().getTime());
			}
			dateFormatter.applyPattern(dateFormat);
			java.util.Date date = (java.util.Date) dateFormatter.parse(cell.getContents().trim());
			return new Date(date.getTime());
		} catch (ParseException ex) {
			erItem.addError(new ErImportError20475(Severity.ERROR, 
					"Feldinhalt '" + cell.getContents() + "' aus Feld '" + fieldname + "' ist kein g\u00fcltiges Datum."));
			return null;
		}
	}

	private String getString(Cell[] cells, Integer index) {
		Cell cell = cells[index];
		return cell.getContents();
	}
	
	private String getTrimmed(Cell[] cells, String fieldname) {
		String content = getString(cells, fieldIndex.getIndex(fieldname));
		return content != null ? content.trim() : content;
	}

	protected BigDecimal getBigDecimal(Cell[] cells, String fieldname, ErImportItem20475 erItem) {
		Cell cell = cells[fieldIndex.getIndex(fieldname)];
		if (cell == null || Helper.isStringEmpty(cell.getContents())) return null;
		try {
			BigDecimal bd = Helper.toBigDecimal(cell.getContents());
			return bd;
		} catch (NumberFormatException ex) {
			erItem.addError(new ErImportError20475(Severity.ERROR, 
					"Feldinhalt '" + cell.getContents() + "' aus Feld '" + fieldname + "' ist keine g\u00fcltige Zahl."));
			return null;
		}
	}

	private Sheet getSheet(byte[] xlsDatei) throws BiffException, IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);
		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");

		Workbook workbook = Workbook.getWorkbook(is, ws);

		return workbook.getSheet(0);
	}

	private void buildFieldIndexer(Cell[] xlsFields) {
		Map<String, Integer> map = new HashMap<String, Integer>() ;
		
		for (int i = 0; i < xlsFields.length; i++) {
			String s =  xlsFields[i].getContents();
			if(s.length() == 0) continue ;
			
			s = s.replaceAll("\\s+", "");
			map.put(s, i) ;
		}
		
		fieldIndex.setMap(map) ;
	}
}
