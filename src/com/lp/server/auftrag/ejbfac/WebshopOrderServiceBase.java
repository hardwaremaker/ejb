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
package com.lp.server.auftrag.ejbfac;

import java.io.StringReader;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.BreakIterator;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJBException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;

import com.lp.server.artikel.ejb.Webshop;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.CreateOrderResult;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.ejb.LieferartQuery;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeAuftrag;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.HeliumSimpleAuthController;
import com.lp.server.system.service.HeliumSimpleAuthException;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WebshopAuthHeader;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public abstract class WebshopOrderServiceBase extends Facade implements WebshopOrderServiceFacLocal {

	private List<ChangeEntry<?>> changedEntries  = new ArrayList<ChangeEntry<?>>() ;	
	private KundeFinder kundeFinder = null ;
	private AuftragFinder auftragFinder = null ;
	private ArtikelFinder artikelFinder = null ;
	private IXmlOrderAdapter orderAdapter = null ;
	
	private String cachedWebshopName = null ;
	private Integer cachedWebshopIId = null ;
	
	private static final String TAG_CDATA_START = "<![CDATA[" ;
	private final static String TAG_CDATA_END = "]]>" ;

	@PersistenceContext
	protected EntityManager em;
	
	
	protected boolean isEmptyString(String string) {
		return null == string || 0 == string.trim().length() ;
	}
	
	protected String emptyString(String string) {
		return null == string ? "" : string.trim() ;
	}

	protected abstract HeliumSimpleAuthController getAuthController() ;
	
	protected  void addChangeEntry(ChangeEntry<?> changedEntry) {
		changedEntries.add(changedEntry) ;
	}

	protected void resetFinders() {
		kundeFinder = null ;
		auftragFinder = null ;
		artikelFinder = null ;
		orderAdapter = null ;
	}
	
	protected KundeFinder getKundeFinder() {
		return kundeFinder ;
	}

	protected void setKundeFinder(KundeFinder newKundeFinder) {
		kundeFinder = newKundeFinder ;
	}
	
	protected AuftragFinder getAuftragFinder() {
		return auftragFinder ;
	}
	
	protected void setAuftragFinder(AuftragFinder newAuftragFinder) {
		auftragFinder = newAuftragFinder ;
	}
	
	
	protected ArtikelFinder getArtikelFinder() {
		return artikelFinder ;
	}

	protected void setArtikelFinder(ArtikelFinder newArtikelFinder) {
		artikelFinder = newArtikelFinder ;
	}
	
	protected IXmlOrderAdapter getOrderAdapter() {
		return orderAdapter ;
	}
	
	protected void setOrderAdapter(IXmlOrderAdapter newOrderAdapter) {
		orderAdapter = newOrderAdapter ;
	}
	
	protected void clearChangeEntries() {
		changedEntries.clear();
	}
	
	protected List<ChangeEntry<?>> getChangeEntries() {
		return changedEntries ;
	}

	protected String getWebshopName() {
		return cachedWebshopName ;
	}
	
	protected Integer getWebshopIId() {
		return cachedWebshopIId ;
	}
	
	protected int getParameterDefaultLieferzeitAuftrag() {
		int defaultLieferzeitAuftrag = 0 ;
		
		try {
			ParametermandantDto parameter = getParameterFac()
				.getMandantparameter(getAuthController().getWebClientDto().getMandant(),
					ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG) ;					
			defaultLieferzeitAuftrag = ((Integer) parameter.getCWertAsObject()).intValue();
		} catch(NumberFormatException e) {
		} catch(RemoteException e) {			
		}
		return defaultLieferzeitAuftrag ;
	}
	

	protected boolean getParameterMitZusammenfassung() {
		boolean mitZusammenfassung = false ;
		
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(
				getAuthController().getWebClientDto().getMandant(),
					ParameterFac.KATEGORIE_ANGEBOT, ParameterFac.PARAMETER_DEFAULT_MIT_ZUSAMMENFASSUNG) ;
			mitZusammenfassung = (Boolean) parameter.getCWertAsObject();
		} catch(RemoteException e) {
		}  
		
		return mitZusammenfassung ;
	}
	
	
	protected Short getParameterKundeAkzeptiertTeillieferung() {
		Short teillieferung = new Short((short)0) ;
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(getAuthController().getWebClientDto().getMandant(), ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_DEFAULT_KUNDE_AKZEPTIERT_TEILLIEFERUNGEN);					
			teillieferung = new Short(parameter.getCWert()) ;		
		} catch(RemoteException e) {
		}
		
		return teillieferung ;
	}
	
	protected boolean isValidEinheitCnr(String cnr) throws RemoteException {
		return null != getSystemFac().einheitFindByPrimaryKeyOhneExc(cnr, getAuthController().getWebClientDto()) ;
	}
	
	
	protected Integer getMwstsatzIIdForTax(BigDecimal taxPercent) {
		MwstsatzDto[] mwstsatzDtos = getMandantFac().mwstsatzfindAllByMandant(
				getAuthController().getWebClientDto().getMandant(), new Timestamp(System.currentTimeMillis()), true) ;
		double d = taxPercent.doubleValue() ;
		for (MwstsatzDto mwstsatzDto : mwstsatzDtos) {
			if(mwstsatzDto.getFMwstsatz().equals(d)) return mwstsatzDto.getIId() ;
		}

		return null ;
	}
	
	protected KundeDto findByLieferantenNr(String lieferantNr) {
		if(null == lieferantNr || 0 == lieferantNr.trim().length()) {
			return null;
		}

		// Ueber die Lieferantennummer suchen
		TheClientDto webClientDto = getAuthController().getWebClientDto() ;
		KundeDto kundeDto = getKundeFac()
			.kundeFindByLieferantenCnrMandantCnrNull(
				lieferantNr.trim(), 
				webClientDto.getMandant()) ;
		return kundeDto ;
	}
	

	protected KundeDto findByUidNummer(String uidNummer) {
		if(null == uidNummer || uidNummer.trim().length() == 0) return null ;
		
		PartnerDto[] partnerDtos = getPartnerFac().partnerFindByUID(uidNummer) ;
		if(partnerDtos.length == 0) {
			addChangeEntry(
					new ChangeEntry<String>(uidNummer, "", 
							"Die UID {0} wurde nicht gefunden.")) ;
			return null ;
		}
		
		if(partnerDtos.length > 1) {
			String kurzbs = "" ; 
			for (PartnerDto partnerDto : partnerDtos) {
				if(kurzbs.length() > 0) {
					kurzbs += ", " ;
				}
				kurzbs += partnerDto.getCKbez() ;
			}
			
			addChangeEntry(
					new ChangeEntry<String>(uidNummer, "", 
							"ACHTUNG! Die UID {0} wird von " + partnerDtos.length + 
							" Partnern (" + kurzbs + ") verwendet. Es wurde keiner gew\u00E4hlt.")) ;
			return null ;
		}

		TheClientDto webClientDto = getAuthController().getWebClientDto() ;
		KundeDto kundeDto = null ;
		try {
			kundeDto = getKundeFac()
				.kundeFindByiIdPartnercNrMandantOhneExc(
						partnerDtos[0].getIId(), webClientDto.getMandant(), webClientDto) ;
		} catch(RemoteException e) {					
		}

		return kundeDto ;
		
//		PartnerDto partnerDto = getPartnerFac().partnerFindByUIDNull(uidNummer) ;
//		if(null == partnerDto) return null ;
//
//		TheClientDto webClientDto = getAuthController().getWebClientDto() ;
//		KundeDto kundeDto = null ;
//		try {
//			kundeDto = getKundeFac()
//				.kundeFindByiIdPartnercNrMandantOhneExc(
//						partnerDto.getIId(), webClientDto.getMandant(), webClientDto) ;
//		} catch(RemoteException e) {					
//		}
//
//		return kundeDto ;
	}
	
	
	protected boolean isValidCurrency(KundeDto kundeDto, String orderCurrency) {
		return kundeDto.getCWaehrung().equalsIgnoreCase(orderCurrency.trim()) ;
	}
	
	
	protected Date getDateFromDateStringWithDefault(String dateString) {
		if(null == dateString || dateString.trim().length() == 0) return Calendar.getInstance().getTime() ;
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			return formatter.parse(dateString) ;
		} catch(ParseException e) {
			System.out.println("Parse Exception for given date '" + dateString + "'. ex=" + e.getMessage()) ;
			return Calendar.getInstance().getTime() ;
		}
	}
	

	
	protected void setDefaultLiefertermine(AuftragDto auftragDto) {		
		Calendar c = Calendar.getInstance() ;
		c.add(Calendar.DAY_OF_MONTH, getParameterDefaultLieferzeitAuftrag());		
		Timestamp t = new Timestamp(c.getTimeInMillis()) ;
		
	    auftragDto.setDLiefertermin(t);
	    auftragDto.setDFinaltermin(t);
	    auftragDto.setBLieferterminUnverbindlich(Helper.boolean2Short(false));		
	}
	
	
	protected AuftragDto buildAuftragDto(KundeDto kundeDto, IXmlOrderAdapter orderAdapter) {
		AuftragDto auftragDto = new AuftragDto() ;
		
		auftragDto.setMandantCNr(getAuthController().getWebClientDto().getMandant()) ;
		String orderDate = getOrderAdapter().getDatum() ;
		auftragDto.setTBelegdatum(new Timestamp(System.currentTimeMillis())) ;
		auftragDto.setDBestelldatum(new Timestamp(getDateFromDateStringWithDefault(orderDate).getTime())) ;
	    auftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_FREI);
	    auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
	    auftragDto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
	    
	    auftragDto.setCBestellnummer(getOrderAdapter().getBestellnummer()) ;			    
	    auftragDto.setCAuftragswaehrung(kundeDto.getCWaehrung());
	    auftragDto.setWaehrungCNr(kundeDto.getCWaehrung()) ;
	    auftragDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(1.0)) ;
	    auftragDto.setLagerIIdAbbuchungslager(kundeDto.getLagerIIdAbbuchungslager());

	    setDefaultLiefertermine(auftragDto) ;
	    auftragDto.setLieferartIId(
	    		getValidLieferart(
	    				kundeDto, 
	    				getOrderAdapter().getLieferart())) ;
		auftragDto.setZahlungszielIId(kundeDto.getZahlungszielIId()) ; 
		auftragDto.setSpediteurIId(kundeDto.getSpediteurIId()) ;
		
		auftragDto.setKundeIIdAuftragsadresse(kundeDto.getIId()) ;
//		auftragDto.setKundeIIdRechnungsadresse(getKundeIIdRechnungsadresse(kundeDto, getOrderAdapter())) ;
		
//		auftragDto.setKundeIIdRechnungsadresse(getKundeIIdRechnungsadresse(kundeDto, getXmlOrder())) ;
//		auftragDto.setKundeIIdLieferadresse(getKundeIIdLieferadresse(kundeDto, getXmlOrder())) ;
		
		auftragDto.setPersonalIIdVertreter(getAuthController().getWebClientDto().getIDPersonal()) ;

		/// TODO: WAS IST HIER LOS???? KostIID vs. KostenstelleIID
		auftragDto.setKostIId(kundeDto.getKostenstelleIId()) ;
		auftragDto.setKostenstelleIId(kundeDto.getKostenstelleIId()) ;
		
		auftragDto.setWaehrungCNr(kundeDto.getCWaehrung()) ;
		auftragDto.setBTeillieferungMoeglich(kundeDto.getBAkzeptiertteillieferung()) ;
		auftragDto.setBPoenale(Helper.boolean2Short(false)) ;
		auftragDto.setILeihtage(0) ;
		auftragDto.setFVersteckterAufschlag(new Double(0.0)) ;
		auftragDto.setFAllgemeinerRabattsatz(new Double(0.0)) ;
		auftragDto.setFProjektierungsrabattsatz(new Double(0.0)) ;
		auftragDto.setIGarantie(kundeDto.getIGarantieinmonaten()) ;
		auftragDto.setBRoHs(Helper.boolean2Short(false)) ;

		boolean bMitZusammenfassung = getParameterMitZusammenfassung() ;
		auftragDto.setBMitzusammenfassung(Helper.boolean2Short(bMitZusammenfassung));
		
		return auftragDto ;
	}
	
	protected Integer getValidLieferart(KundeDto kundeDto, String lieferart) {
		Lieferart l = null ;
		try {
			Query query = LieferartQuery.byCnrMandantCnr(
					em, lieferart, getAuthController().getWebClientDto().getMandant()) ;
			l = (Lieferart) query.getSingleResult() ;
		} catch(NoResultException e) {
		} catch(NonUniqueResultException e) {
		}

		if(l == null) {
			try {
				LieferartDto lieferartDto = getLocaleFac().lieferartFindByPrimaryKey(kundeDto.getLieferartIId(), getAuthController().getWebClientDto()) ;
				addChangeEntry(
					new ChangeEntry<String>(lieferartDto.getCNr() + " " + lieferartDto.getLieferartsprDto().getCBezeichnung(), lieferart, 
							"Lieferart {1} ist unbekannt. Es wurde stattdessen {0} verwendet.")) ;
			} catch(RemoteException e) {					
			}
		}

		return null == l ? kundeDto.getLieferartIId() : l.getIId() ;			
	}

	
	protected Integer getKundeIIdRechnungsadresse(KundeDto kundeDto, IXmlOrderAdapter orderAdapter) {
		String uidNummer = orderAdapter.getUidNummerFromInvoicePartner() ;
		if(null == uidNummer || 0 == uidNummer.trim().length()) {
			myLogger.info("Es gab keine InvoiceParty, verwende daher die Kundenadresse als Rechnungsadresse") ;
			return kundeDto.getIId() ;
		}
		
		KundeDto invoiceKundeDto = findByUidNummer(uidNummer) ;
		return null == invoiceKundeDto ? kundeDto.getIId() : invoiceKundeDto.getIId() ;
	}
	
	
	
	protected AuftragpositionDto createPositionText(AuftragDto auftragDto, ChangeEntry<?> changeEntry) throws RemoteException {
		AuftragpositionDto positionDto = new AuftragpositionDto() ;
		positionDto.setBelegIId(auftragDto.getIId()) ;
		positionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE) ;
		positionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN) ;
		positionDto.setXTextinhalt(
			MessageFormat.format("Info: " + changeEntry.getMessage(), 
					new Object[]{changeEntry.getExpectedValue(), changeEntry.getPresentendValue()})) ;
		positionDto.setBDrucken(Helper.boolean2Short(true)) ;
		positionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false)) ;
		positionDto.setMwstsatzIId(getMwstsatzIIdForTax(BigDecimal.ZERO)) ;
		positionDto.setBDrucken(Helper.boolean2Short(false)) ;
		positionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false)) ;
		positionDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false)) ;
		
		Integer positionIId = getAuftragpositionFac().createAuftragposition(positionDto, getAuthController().getWebClientDto()) ; 
		return getAuftragpositionFac().auftragpositionFindByPrimaryKeyOhneExc(positionIId) ;
	}
	
	protected void createPositionsForChangeEntries(AuftragDto auftragDto) throws RemoteException {
		for (ChangeEntry<?> changeEntry : getChangeEntries()) {
			createPositionText(auftragDto, changeEntry) ;
		}
		clearChangeEntries() ;
	}


	/**
	 * Die XML-Daten in die passende Datenstruktur verwandeln mit der dann gearbeitet wird.
	 * 
	 * @param xmlOpenTransOrder der XML String der den Auftrag darstellt
	 */
	protected abstract Object unmarshalImpl(String xmlOpenTransOrder) throws SAXException, JAXBException ;

	protected abstract KundeDto createEjbCustomerImpl() ;

	protected abstract AuftragDto createEjbOrderImpl(KundeDto kundeDto) ;
	
	protected abstract void createEjbPositionsImpl(KundeDto kundeDto, AuftragDto auftragDto) ; 

	private void archiveXmlDocument(KundeDto kundeDto, AuftragDto auftragDto, String xmlOpenTransOrder) {
		TheClientDto theClientDto = getAuthController().getWebClientDto() ;
		
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
				theClientDto.getIDPersonal(), theClientDto);

//		String sPath = JCRDocFac.HELIUMV_NODE + "/"
//				+ LocaleFac.BELEGART_AUFTRAG.trim() + "/"
//				+ LocaleFac.BELEGART_AUFTRAG.trim() + "/"
//				+ auftragDto.getCNr().replace("/", ".");

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocPath dp = new DocPath(new DocNodeAuftrag(auftragDto)).add(new DocNodeFile("Import_Clevercure.xml")) ;
		jcrDocDto.setDocPath(dp) ;
		jcrDocDto.setbData(xmlOpenTransOrder.getBytes());
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(partnerDto.getIId());
		
		jcrDocDto.setlPartner(kundeDto.getPartnerIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
//		jcrDocDto.setlVersion(0);
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(auftragDto.getCNr().replace("/", "."));
		jcrDocDto.setsFilename(auftragDto.getCNr().replace("/", "."));
//		jcrDocDto.setDocPath(new DocPath(sPath + "/Import Clevercure "
//				+ auftragDto.getIId()));
		jcrDocDto.setsMIME(".xml");
		jcrDocDto.setsName("Import Clevercure" + auftragDto.getIId());
		jcrDocDto.setsRow(auftragDto.getIId().toString());
		jcrDocDto.setsTable("AUFTRAG");
		String sSchlagworte = "Import Clevercure XML Orderresponse";
		jcrDocDto.setsSchlagworte(sSchlagworte);
		getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto, theClientDto);
	}
	
	
	private String stripCData(String xmlOpenTransOrder) {
		if(xmlOpenTransOrder.startsWith(TAG_CDATA_START) && xmlOpenTransOrder.endsWith(TAG_CDATA_END)) {
			return xmlOpenTransOrder.substring(
					TAG_CDATA_START.length(), xmlOpenTransOrder.length() - TAG_CDATA_END.length()) ;
		}

		return xmlOpenTransOrder ;
	}
	
	@Override
	public CreateOrderResult createOrder(WebshopAuthHeader header, String xmlOpenTransOrder) {
		if(isEmptyString(xmlOpenTransOrder)) {
			myLogger.info("Leere Zeichenkette als Auftrag erhalten.") ;
			return new CreateOrderResult(CreateOrderResult.ERROR_EMPTY_ORDER) ;
		}

		xmlOpenTransOrder = stripCData(xmlOpenTransOrder) ;

		try {
			getAuthController().setupSessionParams(header) ;
			validateWebshop(header) ;
			
			resetFinders() ;

			if(null == unmarshalImpl(xmlOpenTransOrder)) {
				myLogger.error("Der xml-Auftrag mit der Gr\u00F6\u00DFe von (" + 
						xmlOpenTransOrder.length() + ") Bytes konnte nicht umgewandelt werden") ;
				return new CreateOrderResult(CreateOrderResult.ERROR_UNMARSHALLING) ;
			}
			
			KundeDto kundeDto = createEjbCustomerImpl() ;
			if(null == kundeDto) {
				myLogger.error("Der Kunde wurde nicht gefunden") ;
				return new CreateOrderResult(CreateOrderResult.ERROR_CUSTOMER_NOT_FOUND);
			}
			
			AuftragDto auftragDto = createEjbOrderImpl(kundeDto) ;
			if(auftragDto == null) {
				myLogger.error("Der Auftrag konnte nicht angelegt werden!") ;
				return new CreateOrderResult(CreateOrderResult.ERROR_ORDER_NOT_CREATED);
			}
			
			createEjbPositionsImpl(kundeDto, auftragDto) ;

			archiveXmlDocument(kundeDto, auftragDto, xmlOpenTransOrder) ;
			
			CreateOrderResult result = new CreateOrderResult(CreateOrderResult.OKAY) ;
			result.setOrderNumber(auftragDto.getCNr()) ;
			return result ;
		} catch(HeliumSimpleAuthException e) {
			myLogger.error("HeliumSimpleAuthException (" + e.getErrorCode() + ")") ;
			return new CreateOrderResult(CreateOrderResult.ERROR_AUTHENTIFICATION) ;
		} catch (JAXBException e) {
			myLogger.error("JAXBException:", e) ;
			return new CreateOrderResult(CreateOrderResult.ERROR_JAXB_EXCEPTION, e.getMessage()) ;
		} catch (SAXException e) {
			myLogger.error("SAXException:", e) ;			
			return new CreateOrderResult(CreateOrderResult.ERROR_SAX_EXCEPTION, e.getMessage()) ;
//		} catch(RemoteException e) {
//			return new CreateOrderResult(CreateOrderResult.ERROR_RMI_EXCEPTION) ;
		} catch(EJBException e) {
			myLogger.error("EJBException:", e) ;
			return new CreateOrderResult(CreateOrderResult.ERROR_RMI_EXCEPTION, e.getMessage()) ;
		} catch(EJBExceptionLP e) {
			myLogger.error("EJBExceptionLP:", e) ;
			return new CreateOrderResult(CreateOrderResult.ERROR_EJB_EXCEPTION + e.getCode(), e.getCause().getMessage()) ;			
		} catch(Exception e) {
			myLogger.error("Exception:", e) ;
			return new CreateOrderResult(CreateOrderResult.ERROR_RMI_EXCEPTION, e.getCause().getMessage()) ;						
		}
	}
	
	private void validateWebshop(WebshopAuthHeader header) throws HeliumSimpleAuthException {
		if(isEmptyString(header.getShopName())) {
			throw new HeliumSimpleAuthException(
				EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN) ;
		}

		cachedWebshopName = header.getShopName() ;
		
		try {
			Query query = em.createNamedQuery(Webshop.QueryFindByMandantCNrCBez);
			query.setParameter(1, getAuthController().getWebClientDto().getMandant());
			query.setParameter(2, cachedWebshopName) ; 

			cachedWebshopIId = ((Webshop) query.getSingleResult()).getIId();
		} catch (NoResultException ex) {
			throw new HeliumSimpleAuthException(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN) ;
		}
	}
	
	public static <T> T unmarshal(String xsdSchema, String xmlContent,
			Class<T> clss) throws JAXBException, SAXException {

		// SchemaFactory schemaFactory = SchemaFactory
		// .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// Schema schema = (xsdSchema == null || xsdSchema.trim().length() == 0)
		// ? null
		// : schemaFactory.newSchema(new File(xsdSchema));

		Schema schema = null;
		JAXBContext jaxbContext = JAXBContext.newInstance(clss.getPackage().getName());
		return unmarshal(jaxbContext, schema, xmlContent, clss);
	}	
		
	public static <T> T unmarshal(JAXBContext jaxbContext, Schema schema,
			String xmlContent, Class<T> clss) throws JAXBException {
		// Unmarshaller ist nicht multithreadingsicher:
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(schema);
		unmarshaller.setEventHandler(new ValidationCollector()) ;
		Object o = unmarshaller.unmarshal(new StreamSource(new StringReader(xmlContent))) ;
		if(o instanceof JAXBElement<?>) {
			return clss.cast(((JAXBElement) o).getValue()) ;
		}
		return clss.cast(o);
	}

	/**
	 * Wrap multi-line strings (and get the individual lines).
	 * 
	 * @param original
	 *            the original string to wrap
	 * @param width
	 *            the maximum width of lines
	 * @param breakIterator
	 *            breaks original to chars, words, sentences, depending on what
	 *            instance you provide.
	 * @param removeNewLines
	 *            if <code>true</code>, any newlines in the original string are
	 *            ignored
	 * @return the lines after wrapping
	 */
	public static List<String> wrapStringToArray(String original, int width,
			BreakIterator breakIterator, boolean removeNewLines) {
		if (original.length() == 0)
			return Arrays.asList(original);

		String[] workingSet;

		// substitute original newlines with spaces,
		// remove newlines from head and tail
		if (removeNewLines) {
			original = original.trim();
			original = original.replace('\n', ' ');
			workingSet = new String[] { original };
		} else {
			StringTokenizer tokens = new StringTokenizer(original, "\n"); // NOI18N
			int len = tokens.countTokens();
			workingSet = new String[len];

			for (int i = 0; i < len; i++)
				workingSet[i] = tokens.nextToken();
		}

		if (width < 1)
			width = 1;

		if (original.length() <= width)
			return Arrays.asList(workingSet);

		widthcheck: {
			boolean ok = true;

			for (int i = 0; i < workingSet.length; i++) {
				ok = ok && (workingSet[i].length() < width);

				if (!ok)
					break widthcheck;
			}

			return Arrays.asList(workingSet);
		}

		ArrayList<String> lines = new ArrayList<String>();

		int lineStart = 0; // the position of start of currently processed line
							// in
		// the original string

		for (int i = 0; i < workingSet.length; i++) {
			if (workingSet[i].length() < width)
				lines.add(workingSet[i]);
			else {
				breakIterator.setText(workingSet[i]);

				int nextStart = breakIterator.next();
				int prevStart = 0;

				do {
					while (((nextStart - lineStart) < width)
							&& (nextStart != BreakIterator.DONE)) {
						prevStart = nextStart;
						nextStart = breakIterator.next();
					}

					if (nextStart == BreakIterator.DONE)
						nextStart = prevStart = workingSet[i].length();

					if (prevStart == 0)
						prevStart = nextStart;

					lines.add(workingSet[i].substring(lineStart, prevStart));

					lineStart = prevStart;
					prevStart = 0;
				} while (lineStart < workingSet[i].length());

				lineStart = 0;
			}
		}

		return lines;
	}
}

class ValidationCollector implements ValidationEventHandler {

	private List<ValidationEvent> events = new ArrayList<ValidationEvent>() ;

	@Override
	public boolean handleEvent(ValidationEvent event) {
		events.add(event) ;
		return true ;
	}
}

