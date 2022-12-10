package com.lp.server.shop.magento2;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.artikel.service.WebshopArtikelDto;
import com.lp.server.artikel.service.WebshopKundeDto;
import com.lp.server.auftrag.ejbfac.ChangeEntry;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.partner.ejb.Selektion;
import com.lp.server.partner.ejb.SelektionQuery;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.shop.magento2.Magento2ApiFacLocal.OrderStatus;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.ejb.LieferartQuery;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.KundeId;
import com.lp.server.util.WebshopId;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class Magento2OrderFacBean extends Magento2BaseFacBean implements Magento2OrderFacLocal {
	@PersistenceContext
	private EntityManager em;
	private int nachkommastellenPreiseVK = -1 ;
	private Timestamp earliestDeliveryTime ;
	private Timestamp latestDeliveryTime ;

	@EJB
	private Magento2ApiFacLocal magento2Api;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void fetchOrder(WebshopId shopId, Integer orderId, TheClientDto theClientDto) throws RemoteException {
		try {
			MagOrder order = magento2Api.getOrder(shopId, orderId);
			myLogger.info("external order id = " + order.getEntityId() + " for incrementId '" + order.getIncrementId() + "'.");
			
			clearChangeEntries();

			MagCustomer account = magento2Api.getAccount(shopId, order.getCustomerId());
			if(account == null) {
				myLogger.error("Die Account-Daten des Auftrags '" +
						order.getIncrementId() + "' konnten nicht ermittelt werden.");
				return;
			}
			
			Integer defaultShippingAddrId = null;
			Integer defaultBillingAddrId = null;
			for (MagAddress address : account.getAddresses()) {
				if(address.isDefaultBilling() ) {
					defaultBillingAddrId = address.getId();
				}
				if(address.isDefaultShipping()) {
					defaultShippingAddrId = address.getId();
				}
			}
			if(defaultBillingAddrId == null && defaultShippingAddrId != null) {
				defaultBillingAddrId = defaultShippingAddrId;
			}
			if(defaultShippingAddrId == null && defaultBillingAddrId != null) {
				defaultShippingAddrId = defaultBillingAddrId;
			}
			KundeDto orderKundeDto = syncOrderCustomer(shopId, 
				defaultBillingAddrId, order.getBillingAddress(), theClientDto);
			if(orderKundeDto == null) {
				return;
			}

			KundeDto deliveryKundeDto = orderKundeDto;
			if(order.getExtensionAttributes().getShippingAssignments() != null) {
				for (MagShippingAssignment shippingAssignment : order.getExtensionAttributes().getShippingAssignments()) {
					MagShipping shipping = shippingAssignment.getShipping();
					deliveryKundeDto = syncOrderCustomer(shopId,
							defaultShippingAddrId, shipping.getAddress(), theClientDto);
					break;
				}
			}
			
			syncOrder(shopId, order, orderKundeDto, deliveryKundeDto, theClientDto);
		} catch(JsonProcessingException e) {
			myLogger.error("JsonProcessingException", e);
		} catch(URISyntaxException e) {
			myLogger.error("URISyntaxException", e);			
		} catch (UnsupportedEncodingException e) {
			myLogger.error("UnsupportedEncodingException", e);			
		}
	}

	private void syncOrder(WebshopId shopId, 
		MagOrder order, KundeDto orderKundeDto,
		KundeDto deliveryKundeDto, TheClientDto theClientDto) throws RemoteException, URISyntaxException, JsonProcessingException, UnsupportedEncodingException {

		AuftragDto[] auftragDtos = findAuftrag(shopId, order, orderKundeDto, theClientDto);
		if(auftragDtos != null && auftragDtos.length > 0) {
			myLogger.warn("Die Shop-Bestellnummer '" + order.getIncrementId() + "' wird bereits im Auftrag '"
					+ auftragDtos[0].getCNr() + "' verwendet. Kein weiterer Auftrag angelegt.");
			
			MagUpdateOrderStatus newStatus = buildOrderStatus(order, 
					OrderStatus.ERP_PROCESSING, theClientDto);
			magento2Api.putOrderStatus(shopId, newStatus);
			return;
		}

		String auftragsWaehrung = orderKundeDto.getCWaehrung();
		if(!auftragsWaehrung.equals(order.getOrderCurrencyCode())) {
			addChangeEntry(new ChangeEntry<String>(
				orderKundeDto.getCWaehrung(), order.getOrderCurrencyCode(), 
				"Die Auftragsw\u00E4hrung {1} entspricht nicht der Kundenw\u00E4hrung {0}. Es wird die Kundenw\u00E4hrung {0} verwendet")) ;
		}
			
		AuftragDto auftragDto = buildAuftragDto(shopId, orderKundeDto, deliveryKundeDto, order, theClientDto);
		Integer iid = getAuftragFac().createAuftrag(auftragDto, theClientDto) ;	
		auftragDto = getAuftragFac().auftragFindByPrimaryKey(iid) ;
		
		createPositions(shopId, auftragDto, orderKundeDto, order, theClientDto);
		MagUpdateOrderStatus newStatus = buildOrderStatus(order, 
				OrderStatus.ERP_PROCESSING, theClientDto);
		magento2Api.putOrderStatus(shopId, newStatus);
	}

	private void createPositions(WebshopId shopId, AuftragDto auftragDto, KundeDto kundeDto, MagOrder order, TheClientDto theClientDto) throws RemoteException {
		createPositionsForChangeEntries(auftragDto, theClientDto);
		resetDeliveryTimestamps() ;
				
		for (MagOrderItem orderItem : order.getItems()) {		
			String lineItemId = orderItem.getItemId().toString();
			ArtikelDto artikelDto = findArtikel(shopId, orderItem, theClientDto);
			if(artikelDto == null) {
				ChangeEntry<?> changeEntry = new ChangeEntry<String>(lineItemId, "", 
						"Fuer die Auftragszeile {0} gibt es keine Produktdaten!") ;
				addChangeEntry(changeEntry) ;			
			} else {
				createPositionIdent(kundeDto, auftragDto, artikelDto, orderItem, theClientDto) ;
			}
			
			createPositionsForChangeEntries(auftragDto, theClientDto) ;					
		}
				
		auftragDto.setDLiefertermin(getEarliestDeliveryTime(theClientDto)) ;
		auftragDto.setDFinaltermin(getLatestDeliveryTime(theClientDto)) ;
		
		getAuftragFac().updateAuftragOhneWeitereAktion(auftragDto, theClientDto) ;				
	}

	private Timestamp getEarliestDeliveryTime(TheClientDto theClientDto) {
		if(earliestDeliveryTime != null) return earliestDeliveryTime ;			
		return getNextDayAsTimestamp(theClientDto);
	}

	private Timestamp getLatestDeliveryTime(TheClientDto theClientDto) {
		if(latestDeliveryTime != null) return latestDeliveryTime ;
		return getNextDayAsTimestamp(theClientDto);
	}

	private Timestamp getNextDayAsTimestamp(TheClientDto theClientDto) {
		Calendar cal = Calendar.getInstance(theClientDto.getLocMandant()) ;
		cal.add(Calendar.DAY_OF_MONTH, 1) ;
		return new Timestamp(cal.getTimeInMillis()) ;
	}

	private ArtikelDto findArtikel(WebshopId shopId, MagOrderItem orderItem, TheClientDto theClientDto) {
		if(orderItem.getProductId() == null) {
			addChangeEntry(new ChangeEntry<String>(
					"", "", "Die ProductId der Webshop-Position ist nicht gesetzt")) ;				
			return null;
		}
		
		WebshopArtikelDto wsaDto = getArtikelFac()
			.webshopArtikelFindByShopExternalIdNull(shopId, orderItem.getProductId().toString());
		if(wsaDto == null) {
			addChangeEntry(new ChangeEntry<String>(
					"", orderItem.getProductId().toString(),
					"Die ProductId der Webshop-Position ist unbekannt (SKU " + orderItem.getSku() + ")."));
			return null;
		}
	
		ArtikelDto aDto = getArtikelFac()
				.artikelFindByPrimaryKeySmallOhneExc(wsaDto.getArtikelIId(), theClientDto);
		if(aDto == null) {
			addChangeEntry(new ChangeEntry<String>(
					"", wsaDto.getArtikelIId().toString(), "Die HELIUM V Artikel-Id vom Webshop ist unbekannt"));
			return null;
		}
		if(!aDto.getCNr().equals(orderItem.getSku())) {
			addChangeEntry(new ChangeEntry<String>(
					aDto.getCNr(), orderItem.getSku(), "Die HELIUM V Artikel-Nr '{0}' und die Artikel-Nr vom Webshop '{1}' sind nicht identisch")) ;						
			return null;
		}
		
		return aDto;
	}
	
	private AuftragpositionDto createPositionIdent(KundeDto kundeDto, 
			AuftragDto auftragDto, ArtikelDto itemDto, MagOrderItem orderItem, TheClientDto theClientDto) throws RemoteException {
		AuftragpositionDto positionDto = new AuftragpositionDto() ;
		positionDto.setBelegIId((auftragDto.getIId())) ;
		positionDto.setArtikelIId(itemDto.getIId()) ;
		positionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT) ;
		
		BigDecimal xmlQuantity = orderItem.getQtyOrdered() ;
		if(xmlQuantity == null || xmlQuantity.signum() <= 0) {
			xmlQuantity = BigDecimal.ONE ;
		}
		positionDto.setNMenge(xmlQuantity) ;
		positionDto.setNOffeneMenge(xmlQuantity) ;
		positionDto.setAuftragpositionstatusCNr(
				AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN) ;
		
		String einheit = itemDto.getEinheitCNr() ;		
		positionDto.setEinheitCNr(einheit) ;

		// Es wird immer der Mehrwertsteuersatz vom Kunden verwendet
		// Hintergrund: Im Shop bestellen neben Endkunden auch Haendler
		// auch Endkunden und Mini-Haendler in Deutschland die eben Mwst zahlen muessen
		itemDto.setMwstsatzbezIId(kundeDto.getMwstsatzbezIId()) ;
		
		BigDecimal vkPrice = getPrice(kundeDto, auftragDto, itemDto, positionDto.getNMenge(), theClientDto) ;
		BigDecimal xmlPrice = getXmlPrice(positionDto.getNMenge(), orderItem) ;
		
		if(xmlPrice != null && vkPrice.compareTo(xmlPrice) != 0) {
			addChangeEntry(new ChangeEntry<BigDecimal>(vkPrice, xmlPrice, 
					"Der angegebene Preis {1} entspricht nicht dem hinterlegtem Preis {0}. Es wird der Shop-Preis {1} verwendet.")) ;				
			vkPrice = xmlPrice ;
		}
			
		positionDto.setNEinzelpreis(vkPrice);
		positionDto.setNNettoeinzelpreis(vkPrice);
		positionDto.setNBruttoeinzelpreis(vkPrice);
		positionDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		positionDto.setBDrucken(Helper.getShortTrue());
		positionDto.setBNettopreisuebersteuert(Helper.getShortFalse());
		positionDto.setFRabattsatz(new Double(0.0));
		positionDto.setFZusatzrabattsatz(new Double(0.0));
		positionDto.setNRabattbetrag(BigDecimal.ZERO);
		positionDto.setNMaterialzuschlag(BigDecimal.ZERO);
		positionDto.setMwstsatzIId(itemDto.getMwstsatzbezIId());
		updateMwstBetraege(positionDto, theClientDto);
		
		positionDto.setTUebersteuerbarerLiefertermin(getTimestamp()) ;
		
		Integer positionIId = getAuftragpositionFac().createAuftragposition(positionDto, theClientDto) ; 
		return getAuftragpositionFac().auftragpositionFindByPrimaryKeyOhneExc(positionIId) ;
	}

	private void updateMwstBetraege(AuftragpositionDto positionDto, TheClientDto theClientDto) throws RemoteException {
		BigDecimal mwstBetrag = calculateMwstbetrag(positionDto.getMwstsatzIId(), 
				positionDto.getNNettoeinzelpreis(), theClientDto) ;
		positionDto.setNMwstbetrag(mwstBetrag);
		positionDto.setNBruttoeinzelpreis(positionDto.getNNettoeinzelpreis().add(mwstBetrag));			
	}

	private BigDecimal calculateMwstbetrag(Integer mwstsatzId, BigDecimal nettoeinzelpreis, TheClientDto theClientDto) throws RemoteException {
		MwstsatzDto mwstSatzDto = getMandantFac()
				.mwstsatzFindByPrimaryKey(mwstsatzId, theClientDto);
		BigDecimal mwstBetrag = Helper
				.getProzentWert(nettoeinzelpreis, new BigDecimal(
						mwstSatzDto.getFMwstsatz()), getIUINachkommastellenPreiseVK(theClientDto)) ;
		return mwstBetrag ;
	}
	
	private BigDecimal getXmlPrice(BigDecimal quantity, MagOrderItem orderItem) {
		return orderItem.getBasePrice();
	}

	private BigDecimal getPrice(KundeDto kundeDto, AuftragDto auftragDto,
			ArtikelDto itemDto, BigDecimal quantity, TheClientDto theClientDto) {
		java.sql.Date d = new java.sql.Date(auftragDto.getDBestelldatum().getTime()) ;
		
		VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(
				itemDto.getIId(), kundeDto.getIId(), quantity, d,
				kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(), 
				itemDto.getMwstsatzbezIId(), auftragDto.getCAuftragswaehrung(), theClientDto) ;

		BigDecimal p = getPriceFromPreisfindung(vkpreisfindungDto) ;
		return p ;
	}

	private BigDecimal getPriceFromPreisfindung(VkpreisfindungDto vkPreisDto) {
		BigDecimal p = getMinimumPrice(null, vkPreisDto.getVkpStufe3()) ;
		if(p != null) return p ;

		p = getMinimumPrice(null, vkPreisDto.getVkpStufe2()) ;
		if(p != null) return p ;

		p = getMinimumPrice(null, vkPreisDto.getVkpStufe1()) ;
		if(p != null) return p ;

		return getMinimumPrice(null, vkPreisDto.getVkpPreisbasis())  ;
	}
	
	private BigDecimal getMinimumPrice(BigDecimal minimum, VerkaufspreisDto priceDto) {
		if(priceDto != null && priceDto.nettopreis != null) {
			return null == minimum ? priceDto.nettopreis : minimum.min(priceDto.nettopreis) ; 
		}
		
		return minimum;		
	}
	
	private void createPositionsForChangeEntries(AuftragDto auftragDto, TheClientDto theClientDto) throws RemoteException {
		for (ChangeEntry<?> changeEntry : getChangeEntries()) {
			createPositionText(auftragDto, changeEntry, theClientDto) ;
		}
		clearChangeEntries() ;		
	}
	
	private AuftragpositionDto createPositionText(AuftragDto auftragDto,
			ChangeEntry<?> changeEntry, TheClientDto theClientDto) throws RemoteException {
		AuftragpositionDto positionDto = new AuftragpositionDto() ;
		positionDto.setBelegIId(auftragDto.getIId()) ;
		positionDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_TEXTEINGABE) ;
		positionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN) ;
		positionDto.setXTextinhalt(
			MessageFormat.format("Info: " + changeEntry.getMessage(), 
					new Object[]{changeEntry.getExpectedValue(), changeEntry.getPresentendValue()})) ;
		positionDto.setBDrucken(Helper.getShortTrue()) ;
		positionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false)) ;
		positionDto.setMwstsatzIId(getMwstsatzIIdForTax(BigDecimal.ZERO, theClientDto)) ;
		positionDto.setBDrucken(Helper.getShortFalse()) ;
		positionDto.setBNettopreisuebersteuert(Helper.getShortFalse()) ;
		positionDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse()) ;
		
		Integer positionIId = getAuftragpositionFac().createAuftragposition(positionDto, theClientDto) ; 
		return getAuftragpositionFac().auftragpositionFindByPrimaryKeyOhneExc(positionIId) ;
	}

	private void resetDeliveryTimestamps() {
		earliestDeliveryTime = null ;
		latestDeliveryTime = null ;			
	}
	

	private Integer getMwstsatzIIdForTax(BigDecimal taxPercent, TheClientDto theClientDto) {
		MwstsatzDto[] mwstsatzDtos = getMandantFac().mwstsatzfindAllByMandant(
				theClientDto.getMandant(), getTimestamp(), true) ;
		double d = taxPercent.doubleValue() ;
		for (MwstsatzDto mwstsatzDto : mwstsatzDtos) {
			if(mwstsatzDto.getFMwstsatz().equals(d)) return mwstsatzDto.getIId() ;
		}

		return null ;
	}

	private int getIUINachkommastellenPreiseVK(TheClientDto theClientDto) throws RemoteException {
		if (nachkommastellenPreiseVK < 0) {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK);
			nachkommastellenPreiseVK = ((Integer) parameter.getCWertAsObject())
					.intValue();
		}
		return nachkommastellenPreiseVK;
	}

	private boolean getParameterMitZusammenfassung(TheClientDto theClientDto) {
		boolean mitZusammenfassung = false ;
		
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_ANGEBOT,
				ParameterFac.PARAMETER_DEFAULT_MIT_ZUSAMMENFASSUNG) ;
			mitZusammenfassung = (Boolean) parameter.getCWertAsObject();
		} catch(RemoteException e) {
		}  
		
		return mitZusammenfassung ;
	}

	private int getParameterDefaultLieferzeitAuftrag(TheClientDto theClientDto) {
		int defaultLieferzeitAuftrag = 0 ;
		
		try {
			ParametermandantDto parameter = getParameterFac()
				.getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_AUFTRAG, ParameterFac.PARAMETER_DEFAULT_LIEFERZEIT_AUFTRAG) ;					
			defaultLieferzeitAuftrag = ((Integer) parameter.getCWertAsObject()).intValue();
		} catch(NumberFormatException e) {
		} catch(RemoteException e) {			
		}
		return defaultLieferzeitAuftrag ;
	}
	
	private void setDefaultLiefertermine(AuftragDto auftragDto, TheClientDto theClientDto) {		
		Calendar c = Calendar.getInstance() ;
		c.add(Calendar.DAY_OF_MONTH, getParameterDefaultLieferzeitAuftrag(theClientDto));		
		Timestamp t = new Timestamp(c.getTimeInMillis()) ;
		
	    auftragDto.setDLiefertermin(t);
	    auftragDto.setDFinaltermin(t);
	    auftragDto.setBLieferterminUnverbindlich(Helper.boolean2Short(false));		
	}

	private Integer getValidZahlungsziel(KundeDto kundeDto,
			String zahlungsziel, TheClientDto theClientDto) {
		ZahlungszielDto zzDto = getMandantFac()
				.zahlungszielFindByCBezMandantNull(zahlungsziel,
						theClientDto.getMandant(), theClientDto);
		if(zzDto == null) {
			try {
				zzDto = getMandantFac().zahlungszielFindByPrimaryKey(
					kundeDto.getZahlungszielIId(), theClientDto);
				addChangeEntry(
						new ChangeEntry<String>(zzDto.getCBez() + " " + zzDto.getZahlungszielsprDto().getCBezeichnung(),
								zahlungsziel, 
								"Zahlungsziel {1} ist unbekannt. Es wurde stattdessen {0} verwendet.")) ;
			} catch(RemoteException e) {				
			}
		}
		
		return zzDto == null ? kundeDto.getZahlungszielIId() : zzDto.getIId();
	}
	
	private Integer getValidLieferart(KundeDto kundeDto, 
			String lieferart, TheClientDto theClientDto) {
		Lieferart l = null ;
		try {
			Query query = LieferartQuery.byCnrMandantCnr(
					em, lieferart, theClientDto.getMandant()) ;
			l = (Lieferart) query.getSingleResult() ;
		} catch(NoResultException e) {
		} catch(NonUniqueResultException e) {
		}

		if(l == null) {
			try {
				LieferartDto lieferartDto = getLocaleFac()
						.lieferartFindByPrimaryKey(kundeDto.getLieferartIId(), theClientDto) ;
				addChangeEntry(
					new ChangeEntry<String>(lieferartDto.getCNr() + " " + lieferartDto.getLieferartsprDto().getCBezeichnung(), lieferart, 
							"Lieferart {1} ist unbekannt. Es wurde stattdessen {0} verwendet.")) ;
			} catch(RemoteException e) {					
			}
		}

		return null == l ? kundeDto.getLieferartIId() : l.getIId() ;			
	}


	protected AuftragDto buildAuftragDto(WebshopId shopId, 
			KundeDto kundeDto, KundeDto lieferKundeDto, MagOrder order, TheClientDto theClientDto) {
		AuftragDto auftragDto = new AuftragDto() ;
		
		auftragDto.setMandantCNr(theClientDto.getMandant()) ;
		auftragDto.setTBelegdatum(new Timestamp(System.currentTimeMillis())) ;
		auftragDto.setDBestelldatum(order.getUpdatedAt()) ;
	    auftragDto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_FREI);
	    auftragDto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
	    auftragDto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
	    
	    auftragDto.setCBestellnummer(buildBestellnummer(shopId, order)) ;			    
	    auftragDto.setCAuftragswaehrung(kundeDto.getCWaehrung());
	    auftragDto.setWaehrungCNr(kundeDto.getCWaehrung()) ;
	    auftragDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(1.0)) ;
	    auftragDto.setLagerIIdAbbuchungslager(kundeDto.getLagerIIdAbbuchungslager());

	    setDefaultLiefertermine(auftragDto, theClientDto) ;
	    auftragDto.setLieferartIId(
	    		getValidLieferart(kundeDto, 
	    				order.getShippingDescription(), theClientDto));
	    String payment = order.getPayment() != null ? order.getPayment().getMethod() : null;
	    if(payment != null) {
			auftragDto.setZahlungszielIId(
					getValidZahlungsziel(kundeDto,
							order.getPayment().getMethod(), theClientDto)) ; 	    	
	    } else {
	    	auftragDto.setZahlungszielIId(kundeDto.getZahlungszielIId());
	    }
	    
		auftragDto.setSpediteurIId(kundeDto.getSpediteurIId()) ;
		
		auftragDto.setKundeIIdAuftragsadresse(kundeDto.getIId());
		auftragDto.setKundeIIdRechnungsadresse(kundeDto.getIId());
		auftragDto.setKundeIIdLieferadresse(lieferKundeDto.getIId());
				
		auftragDto.setPersonalIIdVertreter(theClientDto.getIDPersonal()) ;

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

		boolean bMitZusammenfassung = getParameterMitZusammenfassung(theClientDto) ;
		auftragDto.setBMitzusammenfassung(Helper.boolean2Short(bMitZusammenfassung));
		
		return auftragDto ;
	}

	private KundeDto syncOrderCustomer(WebshopId shopId,
			Integer defaultAddressId, MagOrderAddress billing,
			TheClientDto theClientDto) throws RemoteException {
		Integer customerId = billing.getCustomerAddressId();
		if(customerId == null) {
			customerId = defaultAddressId;
		}

		WebshopKundeDto wskDto = getArtikelFac()
				.webshopKundeFindByShopExternalIdNull(shopId, customerId.toString());
		KundeDto kundeDto = null;
		if(wskDto == null) {
			kundeDto = findOrCreateKundeDto(shopId, billing, theClientDto);
		} else {
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					wskDto.getKundeIId(), theClientDto);
			if(!verifyPartner(kundeDto.getPartnerDto(), billing, theClientDto)) {
				PartnerDto partnerDto = createPartner(billing, theClientDto);
				kundeDto = createKundeDtoFromPartnerDto(
						partnerDto, theClientDto);
				wskDto.setKundeId(new KundeId(kundeDto.getIId()));
				getArtikelFac().updateWebshopKunde(wskDto);
			}
		}
		
//		KundeDto kundeDto = wskDto == null
//				? findOrCreateKundeDto(shopId, billing, theClientDto)
//				: getKundeFac().kundeFindByPrimaryKey(wskDto.getKundeIId(), theClientDto);
		if(kundeDto != null && wskDto == null) {
			wskDto = new WebshopKundeDto();
			wskDto.setExternalId(customerId.toString());
			wskDto.setKundeId(new KundeId(kundeDto.getIId()));
			wskDto.setWebshopId(shopId);
			getArtikelFac().createWebshopKunde(wskDto);
		}

		return kundeDto;
	}

	private String buildBestellnummer(WebshopId shopId, MagOrder order) {
		return shopId.id().toString() + "_" + order.getIncrementId();
	}
	
	private AuftragDto[] findAuftrag(WebshopId shopId,
		MagOrder order, KundeDto kundeDto, TheClientDto theClientDto) {		
		AuftragDto[] auftragDtos = null ;
		
		try {
			String bestellnr = buildBestellnummer(shopId, order);
			auftragDtos = getAuftragFac().auftragFindByMandantCnrKundeIIdBestellnummerOhneExc(
					kundeDto.getIId(), theClientDto.getMandant(), bestellnr) ;
		} catch(RemoteException e) {				
		}
		
		return auftragDtos ;
	}

	private PartnerDto findPartnerDto(WebshopId shopId, MagOrderAddress address, TheClientDto theClientDto) throws RemoteException {
		PartnerDto[] partnerDtos = getPartnerFac().partnerFindByEmail(address.getEmail());
		PartnerDto foundDto = null;
		for (PartnerDto partnerDto : partnerDtos) {
			if(verifyPartner(partnerDto, address, theClientDto)) {
				foundDto = partnerDto;
				break;
			}
		}
		
		return foundDto;
	}
	
	private boolean verifyPartner(PartnerDto partnerDto, MagOrderAddress address, TheClientDto theClientDto) throws RemoteException {
		if(!address.getEmail().trim().equals(partnerDto.getCEmail())) return false;
		
		if(Helper.isStringEmpty(address.getCompany())) {
			// Ist eine "Privatadresse"
			if(!acceptPersonalInfo(partnerDto, address)) return false;
			if(!acceptCountryInfo(partnerDto, address)) return false;
		} else {
			if(!acceptCompanyInfo(partnerDto, address)) return false;
			if(!acceptCountryInfo(partnerDto, address)) return false;
		} 
		
		return true;
	}
	
	private KundeDto findOrCreateKundeDto(WebshopId shopId,
			MagOrderAddress address, TheClientDto theClientDto) throws RemoteException {
		PartnerDto foundDto = findPartnerDto(shopId, address, theClientDto);	
		if(foundDto == null) {
			foundDto = createPartner(address, theClientDto);
		} else {
			KundeDto kundeDto = getKundeFac()
					.kundeFindByiIdPartnercNrMandantOhneExc(
							foundDto.getIId(), 
							theClientDto.getMandant(), theClientDto);
			if(kundeDto != null) {
				return kundeDto;
			}
		}
		
		if(foundDto == null) {
			return null;
		}
		
		KundeDto kundeDto = createKundeDtoFromPartnerDto(foundDto, theClientDto);
		return kundeDto;
	}
	
	private PartnerDto createPartner(MagOrderAddress address, TheClientDto theClientDto) throws RemoteException {
		PartnerDto pDto = new PartnerDto() ;
		pDto.setCName1nachnamefirmazeile1(emptyString(address.getLastname())) ;
		pDto.setCName2vornamefirmazeile2(emptyString(address.getFirstname()));
		pDto.setCKbez(emptyString(address.getLastname())) ;
		pDto.setCStrasse(emptyString(address.getStreet().get(0))) ;
		pDto.setCTelefon(emptyString(address.getTelephone())) ;
		pDto.setCFax("") ;
		pDto.setCEmail(emptyString(address.getEmail())) ;
		pDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE) ;
		pDto.setBVersteckt(false) ;
		pDto.setLocaleCNrKommunikation(theClientDto.getLocMandantAsString()) ;

		LandplzortDto lpoDto = createLandplzortIfNeeded(address, theClientDto);
		if(lpoDto == null) {
			return null;
		}
		pDto.setLandplzortDto(lpoDto) ;
		pDto.setLandplzortIId(lpoDto.getIId()) ;
		pDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		pDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		Integer partnerId = getPartnerFac().createPartner(pDto, theClientDto);
		return getPartnerFac().partnerFindByPrimaryKey(partnerId, theClientDto);
	}
	
	private LandplzortDto createLandplzortIfNeeded(MagOrderAddress address, TheClientDto theClientDto) throws RemoteException {
		String lkz = emptyString(address.getCountryId());
		if(isEmptyString(lkz)) {
			MandantDto mandantDto = null ;

			try {
				mandantDto = getMandantFac().mandantFindByPrimaryKey(
							theClientDto.getMandant(), theClientDto);
			} catch(RemoteException e) {
				myLogger.error("Kann Mandant " + theClientDto.getMandant() + " nicht ermitteln:" + e.getMessage()) ;
				return null ;
			}
	
			lkz = mandantDto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz() ;
		}

		if(isEmptyString(address.getCity())) {
			myLogger.error("Es wurde keine Stadt angegeben!");
			return null;
		}
		
		String city = emptyString(address.getCity());		
		String plz = emptyString(address.getPostcode());
		
		Integer landIId = null ;		
		LandDto landDto = getSystemFac().landFindByLkz(lkz) ;
		if(landDto == null) {
			landDto = new LandDto() ;
			landDto.setCLkz(lkz) ;
			landDto.setCName(lkz) ;
			landDto.setILaengeuidnummer(0) ;
			landDto.setBSepa(Helper.getShortFalse()) ;
			landIId = getSystemFac().createLand(landDto, theClientDto) ;
			landDto = getSystemFac().landFindByPrimaryKey(landIId) ;
			
//			addChangeEntry(new ChangeEntry<String>("", lkz, "Das Land {1} wurde mit Standardwerten neu angelegt!")) ;
		} else {
			landIId = landDto.getIID() ;
		}
		
		OrtDto ortDto = getSystemFac().ortFindByNameOhneExc(city) ;
		Integer ortIId ;
		if(ortDto == null) {
			ortDto = new OrtDto() ;
			ortDto.setCName(city) ;
			ortIId = getSystemFac().createOrt(ortDto, theClientDto) ;
			ortDto = getSystemFac().ortFindByPrimaryKey(ortIId) ;
		} else {
			ortIId = ortDto.getIId() ;
		}
		
		LandplzortDto lpoDto = getSystemFac().landplzortFindByLandOrtPlzOhneExc(
				landDto.getIID(), ortDto.getIId(), plz) ; 
		
		if(lpoDto != null) return lpoDto ;
		
		lpoDto = new LandplzortDto() ;
		lpoDto.setIlandID(landIId) ;
		lpoDto.setLandDto(landDto) ;
		lpoDto.setCPlz(plz) ;
		lpoDto.setOrtDto(ortDto) ;
		lpoDto.setOrtIId(ortIId) ;
		Integer lpoIId = getSystemFac().createLandplzort(lpoDto, theClientDto) ;
		return getSystemFac().landplzortFindByPrimaryKey(lpoIId) ;
	}

	private KundeDto createKundeDtoFromPartnerDto(PartnerDto partnerDto, TheClientDto theClientDto) {
		MandantDto mandantDto = null ;
		try {
			mandantDto = getMandantFac().mandantFindByPrimaryKey(
						theClientDto.getMandant(), theClientDto);
		} catch(RemoteException e) {
			myLogger.error("Kann Mandant " + theClientDto.getMandant() + " nicht ermitteln: " + e.getMessage()) ;
			return null ;
		}
		
		KundeDto kundeDto = new KundeDto() ;
		kundeDto.setPartnerDto(partnerDto) ;
		kundeDto.setMandantCNr(theClientDto.getMandant()) ;
		kundeDto.setCWaehrung(mandantDto.getWaehrungCNr()) ;
		kundeDto.setMwstsatzbezIId(mandantDto.getMwstsatzbezIIdStandardinlandmwstsatz()) ;
		kundeDto.setVkpfArtikelpreislisteIIdStdpreisliste(mandantDto.getVkpfArtikelpreislisteIId()) ;
		kundeDto.setLieferartIId(mandantDto.getLieferartIIdKunde()) ;
		kundeDto.setSpediteurIId(mandantDto.getSpediteurIIdKunde()) ;
		kundeDto.setZahlungszielIId(mandantDto.getZahlungszielIIdKunde()) ;
		kundeDto.setBMindermengenzuschlag(Helper.boolean2Short(false));
		kundeDto.setBMonatsrechnung(Helper.boolean2Short(false));
		kundeDto.setBPreiseanlsandrucken(Helper.boolean2Short(false));
		kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));
		kundeDto.setBDistributor(Helper.boolean2Short(false));
		kundeDto.setBIstreempfaenger(Helper.boolean2Short(false));
		kundeDto.setbIstinteressent(Helper.boolean2Short(false));
		kundeDto.setBSammelrechnung(Helper.boolean2Short(false));
		kundeDto.setBRechnungsdruckmitrabatt(Helper.boolean2Short(false));

		// Default 'Akzeptiert Teillieferung'
		kundeDto.setBAkzeptiertteillieferung(
				getParameterKundeAkzeptiertTeillieferung(theClientDto));

		Integer personalId = theClientDto.getIDPersonal() ;
		kundeDto.setPersonaliIdProvisionsempfaenger(personalId) ;
		kundeDto.setKostenstelleIId(mandantDto.getIIdKostenstelle());

		kundeDto.setPersonalAnlegenIID(personalId) ;
		kundeDto.setPersonalAendernIID(personalId) ;
		Timestamp t = getTimestamp() ;
		kundeDto.setTAnlegen(t) ;
		kundeDto.setTAendern(t) ;
		
		try {
			Integer kundeId = getKundeFac().createKunde(kundeDto, theClientDto) ;
			kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeId, theClientDto) ;
			
			createPartnerSelektionen(kundeDto, theClientDto) ;
		} catch(RemoteException e) {
			myLogger.error("Kann kunden nicht anlegen: " + e.getMessage()) ;
			return null ;
		}
		
		return kundeDto ;
	}
	
	private void createPartnerSelektionen(KundeDto kundeDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		List<Selektion> selektions = SelektionQuery
				.listByMandantCNrForWebshop(em, theClientDto.getMandant()) ;
		for (Selektion selektion : selektions) {
			PASelektionDto paSelektionDto = new PASelektionDto() ;
			paSelektionDto.setSelektionIId(selektion.getIId()) ;
			paSelektionDto.setPartnerIId(kundeDto.getPartnerIId()) ;
			getPartnerFac().createPASelektion(paSelektionDto, theClientDto) ;				
		}			
	}
	
	protected Short getParameterKundeAkzeptiertTeillieferung(TheClientDto theClientDto) {
		Short teillieferung = new Short((short)0) ;
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_DEFAULT_KUNDE_AKZEPTIERT_TEILLIEFERUNGEN);					
			teillieferung = new Short(parameter.getCWert()) ;		
		} catch(RemoteException e) {
		}
		
		return teillieferung ;
	}
		
	private boolean acceptPersonalInfo(PartnerDto partnerDto, MagOrderAddress address) {
		return partnerDto.getCName1nachnamefirmazeile1().equals(address.getLastname()) &
			partnerDto.getCName2vornamefirmazeile2().equals(address.getFirstname()) &
			partnerDto.getCStrasse().equals(address.getStreet().get(0));
	}
	
	private boolean acceptCompanyInfo(PartnerDto partnerDto, MagOrderAddress address) {
		return partnerDto.getCName1nachnamefirmazeile1().equals(address.getCompany()) &
				partnerDto.getCName2vornamefirmazeile2().equals(address.getFirstname()) &
				partnerDto.getCStrasse().equals(address.getStreet().get(0));
		
	}
	
	private boolean acceptCountryInfo(PartnerDto partnerDto, MagOrderAddress address) {
		LandplzortDto lpoDto = partnerDto.getLandplzortDto();
		if(lpoDto == null || lpoDto.getLandDto() == null
				|| lpoDto.getOrtDto() == null) return false;
		
		if(!lpoDto.getLandDto().getCLkz().equals(address.getCountryId())) return false;

		// PLZ optional, wenn vorhanden, dann muss sie uebereinstimmen
		if(!Helper.isStringEmpty(lpoDto.getCPlz())) {
			if(!lpoDto.getCPlz().equals(address.getPostcode())) return false;
		}
		
		if(!lpoDto.getOrtDto().getCName().equals(address.getCity())) return false;
		
		return true;
	}
	
	@Override
	public void fetchOrders(WebshopId shopId, TheClientDto theClientDto) {
		try {
			MagSearchOrdersResult result = magento2Api.searchPendingOrders(shopId);
			if(result != null && result.getTotalCount() > 0) {
				for (MagOrder order : result.getItems()) {
					try {
						getMagento2OrderFacLocal().fetchOrder(shopId, order.getEntityId(), theClientDto);						
					} catch(RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}
			}			
		} catch(JsonProcessingException e) {
			myLogger.error("JsonProcessingException", e);
		} catch(URISyntaxException e) {
			myLogger.error("URISyntaxException", e);			
		}
	}

	
//	private MagSearchOrdersResult searchPendingOrders(WebshopId shopId, TheClientDto theClientDto) throws JsonProcessingException, URISyntaxException {
//		Map<String, String> q = new HashMap<String, String>();
//		q.put("searchCriteria[filter_groups][0][filters][0][field]", "status");
//		q.put("searchCriteria[filter_groups][0][filters][0][value]", OrderStatus.PENDING);
//		q.put("searchCriteria[filter_groups][0][filters][0][condition_type]", "eq");
//		return getJson(shopId,"/V1/orders", q, MagSearchOrdersResult.class);
//		
//		WebshopConnectionDto dto = getConnectionDto(shopId);
//		HttpTalk t = null;
//			t = HttpTalk.get("/V1/orders", new HashMap<String, String>() {{
//				put("searchCriteria[filter_groups][0][filters][0][field]", "status");
//				put("searchCriteria[filter_groups][0][filters][0][value]", OrderStatus.PENDING);
//				put("searchCriteria[filter_groups][0][filters][0][condition_type]", "eq");
//				}}, dto).acceptJson();
//		
//		try {
//			HttpResponse r = t.ex();
//			if (r.getStatusLine().getStatusCode() == 200) {
//				MagSearchOrdersResult p = getJsonMapper().readValue(r.getEntity().getContent(), MagSearchOrdersResult.class);
//				return p;
//			} else {
//				logErrorStream("GET RESPONSE ERROR: ", r.getEntity().getContent());
//			}
//		} catch (JsonParseException e) {
//			myLogger.error("JsonParse", e);
//		} catch (JsonMappingException e) {
//			myLogger.error("JsonParse", e);
//		} catch (IOException e) {
//			myLogger.error("IOException:", e);
//		} finally {
//			t.close();
//		}
//
//		return null;
//	}
	
//	private MagOrder getOrder(WebshopId shopId, Integer orderId, TheClientDto theClientDto) throws JsonProcessingException, URISyntaxException {
//		return getJson(shopId, "/V1/orders/" + orderId.toString(), MagOrder.class);
//		
//		HttpTalk t = null;
//			t = HttpTalk.get("/V1/orders/" + orderId.toString(), dto)
//					.acceptJson();
//		
//		try {
//			HttpResponse r = t.ex();
//			if (r.getStatusLine().getStatusCode() == 200) {
//				MagOrder p = getJsonMapper().readValue(r.getEntity().getContent(), MagOrder.class);
//				return p;
//			} else {
//				logErrorStream("GET RESPONSE ERROR: ", r.getEntity().getContent());
//			}
//		} catch (JsonParseException e) {
//			myLogger.error("JsonParse", e);
//		} catch (JsonMappingException e) {
//			myLogger.error("JsonParse", e);
//		} catch (IOException e) {
//			myLogger.error("IOException:", e);
//		} finally {
//			t.close();
//		}
//
//		return null;
//	}
	

	/**
	 * Den Status des Magento-Orders setzen.
	 * 
	 * Es wird dabei die Increment-Id aus dem MagOrder verwendet. Gibt man keine an, 
	 * wird eine neue generiert. Damit wuerde der Backend-User nie mehr wieder die
	 * eigentliche Bestellung(nummer) finden. 
	 * 
	 * @param order
	 * @param newStatus
	 * @param theClientDto
	 * @return
	 */
	private MagUpdateOrderStatus buildOrderStatus(MagOrder order, String newStatus, TheClientDto theClientDto) {
		MagUpdateOrderStatus status = new MagUpdateOrderStatus();
		status.setEntityId(order.getEntityId());
		status.setIncrementId(order.getIncrementId());
		status.setStatus(newStatus);
		return status;
	}
	
//	private MagOrder putOrderStatus(WebshopId shopId, MagUpdateOrderStatus status, TheClientDto theClientDto) throws JsonProcessingException, URISyntaxException, UnsupportedEncodingException {
//		MagPutOrderStatus putStatus = new MagPutOrderStatus(status);
//		return putJson(shopId, "/V1/orders/create", putStatus, MagOrder.class);
//		WebshopConnectionDto dto = getConnectionDto(shopId);
//		HttpTalk t = null;
//		try {
//			MagPutOrderStatus putStatus = new MagPutOrderStatus(status);
//			String json = getJsonMapper().writeValueAsString(putStatus);		
//			t = HttpTalk.put("/V1/orders/create", dto)
//						.addJson(json)
//						.acceptJson();			
//			myLogger.info("Puting " + json);
//		} catch(UnsupportedEncodingException e) {
//			myLogger.error("UnsupportedEncodingException", e);
//			return null;
//		}
//		
//		try {
//			HttpResponse r = t.ex();
//			if (r.getStatusLine().getStatusCode() == 200) {
//				MagOrder p = getJsonMapper().readValue(r.getEntity().getContent(), MagOrder.class);
//				return p;
//			} else {
//				logErrorStream("GET RESPONSE ERROR: ", r.getEntity().getContent());
//			}
//		} catch (JsonParseException e) {
//			myLogger.error("JsonParse", e);
//		} catch (JsonMappingException e) {
//			myLogger.error("JsonParse", e);
//		} catch (IOException e) {
//			myLogger.error("IOException:", e);
//		} finally {
//			t.close();
//		}
//
//		return null;
//	}
}
