package com.lp.server.util.fastlanereader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.service.PartnerFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

public abstract class FlrFirmaAnsprechpartnerFilterBuilder {
	
	private boolean bSuchenInklusiveKBez;
	private List<String> partnerSuchfelder;

	public FlrFirmaAnsprechpartnerFilterBuilder(boolean bSuchenInklusiveKBez) {
		this.bSuchenInklusiveKBez = bSuchenInklusiveKBez;
	}

	private boolean isBSuchenInklusiveKBez() {
		return bSuchenInklusiveKBez;
	}

	public List<String> getPartnerSuchfelder() {
		return partnerSuchfelder;
	}

	public abstract String getFlrPartner();
	public abstract String getFlrPropertyAnsprechpartnerIId();

	private List<String> getPartnerSuchfelder(String flrPrefix) {
		partnerSuchfelder = new ArrayList<String>();
		partnerSuchfelder.add(flrPrefix + "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
		partnerSuchfelder.add(flrPrefix + "." + PartnerFac.FLR_PARTNER_NAME2VORNAMEFIRMAZEILE2);
		if (isBSuchenInklusiveKBez()) {
			partnerSuchfelder.add(flrPrefix + "." + PartnerFac.FLR_PARTNER_C_KBEZ);
		}
		return partnerSuchfelder;
	}

	private String buildPartnerClause(List<String> items, String searchString, boolean bIgnoreCase, String operator, String andOr) {
		StringBuilder builder = new StringBuilder();
		Iterator<String> iter = items.iterator();
		builder.append(items.size() > 1 ? "(" : "");
		while (iter.hasNext()) {
			String item = iter.next();
			if (bIgnoreCase) {
				builder.append("lower(").append(item).append(")");
			} else {
				builder.append(item);
			}
			builder.append(" ").append(operator).append(" ");
			if (bIgnoreCase) {
				builder.append("lower(").append(searchString).append(")");
			} else {
				builder.append(searchString);
			}
			
			if (iter.hasNext()) {
				builder.append(" ").append(andOr).append(" ");
			}
		}
		builder.append(items.size() > 1 ? ")" : "");
		
		return builder.toString();
	}
	
	private String buildAnsprechpartnerClause(List<String> items) {
		StringBuilder builder = new StringBuilder();
		builder.append(getFlrPropertyAnsprechpartnerIId()).append(" IN (");
		builder.append("SELECT DISTINCT(ap.i_id) FROM FLRAnsprechpartner as ap"
				+ " LEFT JOIN ap.flrpartneransprechpartner as appartner WHERE ");
		Iterator<String> iter = items.iterator();
		builder.append(items.size() > 1 ? "(" : "");
		while (iter.hasNext()) {
			builder.append(buildPartnerClause(getPartnerSuchfelder("appartner"), iter.next(), 
					true, FilterKriterium.OPERATOR_LIKE, FilterKriterium.BOOLOPERATOR_OR));
			if (iter.hasNext()) {
				builder.append(" ").append(FilterKriterium.BOOLOPERATOR_AND).append(" ");
			}
		}
		builder.append(items.size() > 1 ? ")" : "")
			.append(") ");
		return builder.toString();
	}

	/**
	 * Setzt den Filter fuer Suche nach Firma und/oder Ansprechpartner zusammen.
	 * Erstes Vorkommen von "@" trennt Ansprechpartner von der Firma. Ist kein "@" vorhanden, 
	 * wird nur nach der Firma gesucht.<br />
	 * Moegliche Varianten: Mustermann@Firma, Max Mustermann@Firma, Firma, Mustermann@
	 * 
	 * @param filterkriterium
	 * @param where StringBuffer, in dem der erzeugte Filter angehaengt wird
	 */
	public void buildFirmaAnsprechpartnerFilter(FilterKriterium filterkriterium, StringBuffer where) {
		where.append(buildFirmaAnsprechpartnerFilter(filterkriterium));
	}
	
	/**
	 * Setzt den Filter fuer Suche nach Firma und/oder Ansprechpartner zusammen.
	 * Erstes Vorkommen von "@" trennt Ansprechpartner von der Firma. Ist kein "@" vorhanden, 
	 * wird nur nach der Firma gesucht.<br />
	 * Moegliche Varianten: Mustermann@Firma, Max Mustermann@Firma, Firma, Mustermann@
	 * 
	 * @param filterkriterium
	 * @return erzeugten Filter
	 */
	public String buildFirmaAnsprechpartnerFilter(FilterKriterium filterkriterium) {
		StringBuilder builder = new StringBuilder();
		FirmaSearchStringTokenizer tokenizer = new FirmaSearchStringTokenizer(filterkriterium);
		boolean filterAdded = false;
		
		if (tokenizer.hasPartner()) {
			builder.append(" (")
				.append(buildPartnerClause(getPartnerSuchfelder(getFlrPartner()), tokenizer.getStringFirma(), 
					filterkriterium.isBIgnoreCase(), filterkriterium.operator, FilterKriterium.BOOLOPERATOR_OR))
				.append(") ");
			filterAdded = true;
		}
		
		if (tokenizer.hasAnsprechpartner()) {
			if (filterAdded) {
				builder.append(" AND ");
			}
			builder.append(" ")
				.append(buildAnsprechpartnerClause(tokenizer.getStringAnsprechpartner()));
		}
		return builder.toString();
	}
	
	/**
	 * Setzt den Filter fuer die Suche nach der Firma zusammen.
	 * 
	 * @param filterkriterium
	 * @param where StringBuffer, in dem der erzeugte Filter angehaengt wird
	 */
	public void buildFirmaFilter(FilterKriterium filterkriterium, StringBuffer where) {
		where.append(buildFirmaFilter(filterkriterium));
	}
	
	public String buildFirmaFilter(FilterKriterium filterkriterium) {
		StringBuilder builder = new StringBuilder();
		builder.append(" (")
			.append(buildPartnerClause(getPartnerSuchfelder(getFlrPartner()), filterkriterium.value, 
				filterkriterium.isBIgnoreCase(), filterkriterium.operator, FilterKriterium.BOOLOPERATOR_OR))
			.append(") ");
		return builder.toString();
	}
	
	private class FirmaSearchStringTokenizer {
		
		private String stringFirma;
		private List<String> stringAnsprechpartner;
		
		public FirmaSearchStringTokenizer(FilterKriterium filterkriterium) {
			stringAnsprechpartner = new ArrayList<String>();
			
			tokenize(filterkriterium);
		}
		
		private void tokenize(FilterKriterium filterkriterium) {
			String temp = new String(filterkriterium.value);
			temp = removeSingleQuotesAndProzent(temp);
			String[] parts = temp.split("\u0040", 2);
			
			if (parts.length == 1) {
				stringFirma = filterkriterium.value;
				return;
			}
			
			String[] ap = parts[0].split(" ");
			for (String value : ap) {
				stringAnsprechpartner.add(wrapWithSingleQuotesAndProzent(1, value));
			}
			stringFirma = parts[1].isEmpty() || parts[1].trim().isEmpty() ? 
					null : wrapWithSingleQuotesAndProzent(1, parts[1]);
		}
		
		private String removeSingleQuotesAndProzent(String value) {
			return value.replaceAll("^(\'|%)*|(%|\')*$", "");
		}
		
		private String wrapWithSingleQuotesAndProzent(int iWrapWithProzent, String value) {
//			if (iWrapWithProzent == FilterKriteriumDirekt.PROZENT_LEADING) {
//				value = "%" + value;
//			} else if (iWrapWithProzent == FilterKriteriumDirekt.PROZENT_TRAILING) {
//				value = value + "%";
//			} else if (iWrapWithProzent == FilterKriteriumDirekt.PROZENT_BOTH) {
//				value = "%" + value + "%";
//			}
			value = "\'" + value + "%\'"; 
			return value;
		}

		public String getStringFirma() {
			return stringFirma;
		}

		public List<String> getStringAnsprechpartner() {
			return stringAnsprechpartner;
		}
		
		public boolean hasAnsprechpartner() {
			return !getStringAnsprechpartner().isEmpty();
		}
		
		public boolean hasPartner() {
			return getStringFirma() != null;
		}
	}
}
