package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import com.lp.server.angebotstkl.ejb.Webabfrage;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.artikel.ejb.Artikellieferantstaffel;
import com.lp.server.artikel.ejb.ArtikellieferantstaffelQuery;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDtoAssembler;
import com.lp.server.artikel.service.PartSearchUnexpectedResponseExc;
import com.lp.server.artikel.service.WebPart;
import com.lp.server.artikel.service.WebPartPrice;
import com.lp.server.artikel.service.WebPartSearchResult;
import com.lp.server.artikel.service.WebabfrageArtikellieferantSuchparameterDto;
import com.lp.server.artikel.service.WebabfrageArtikellieferantUpdateProperties;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.WeblieferantFarnellDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.ArtikellieferantId;
import com.lp.server.util.Facade;
import com.lp.server.util.LieferantId;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class WebPartSearchService extends Facade implements WebPartSearchServiceFac {

	@Override
	public WebPartSearchResult findNoExc(ArtikelId artikelId, LieferantId lieferantId, TheClientDto theClientDto) {
		WebPartFinder finder = new WebPartFinder(getWebPartSearchController(lieferantId, theClientDto), theClientDto);
		return finder.findNoExc(artikelId, lieferantId);
	}

	@Override
	public WebPartSearchResult find(ArtikelId artikelId, LieferantId lieferantId, TheClientDto theClientDto) {
		WebPartFinder finder = new WebPartFinder(getWebPartSearchController(lieferantId, theClientDto), theClientDto);
		return finder.find(artikelId, lieferantId);
	}
	
	@Override
	public ArtikellieferantDto updateOrCreateArtikellieferantByWebPart(WebabfrageArtikellieferantUpdateProperties updateProperties, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		WebPartUpdater updater = new WebPartUpdater(theClientDto);
		return updater.updateOrCreateArtikellieferantByWebPart(updateProperties);
	}

	private IWebPartSearchController getWebPartSearchController(LieferantId lieferantId, TheClientDto theClientDto) {
		IWebpartnerDto weblieferant = getLieferantFac().weblieferantFindByLieferantIIdOhneExc(lieferantId.id(), theClientDto);
		if (weblieferant == null) {
			LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(lieferantId.id(), theClientDto);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_UNBEKANNTER_WEBLIEFERANT, 
					"Keine Webabfragedaten fuer Lieferant '" + lieferantDto.getPartnerDto().formatName() + "' gefunden.",
					lieferantDto);
		}
		if (weblieferant instanceof WeblieferantFarnellDto) {
			return new FarnellWebPartSearchController((WeblieferantFarnellDto)weblieferant);
		}

		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(lieferantId.id(), theClientDto);
		Webabfrage webabfrage = em.find(Webabfrage.class, weblieferant.getWebabfrageIId());
		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_TYP_NICHT_IMPLEMENTIERT, 
				"Fuer Webabfrage '" + webabfrage.getCBez() + "' ist noch keine Web-Suche implementiert.",
				lieferantDto);
	}
	
	public class WebPartFinder {
		private IWebPartSearchController partSearchCtrl;
		private TheClientDto theClientDto;
		
		public WebPartFinder(IWebPartSearchController partSearchCtrl, TheClientDto theClientDto) {
			this.partSearchCtrl = partSearchCtrl;
			this.theClientDto = theClientDto;
		}
		
		public WebPartSearchResult find(ArtikelId artikelId, LieferantId lieferantId) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelId.id(), theClientDto);
			ArtikellieferantDto artliefDto = getArtikelFac().artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
					artikelId.id(), lieferantId.id(), getDate(), null, theClientDto);
			WebabfrageArtikellieferantSuchparameterDto suchparameter = getParameterFac()
					.getWebabfrageArtikellieferantSuchparameter(theClientDto.getMandant());
			try {
				WebPartSearchResult resultLiefNr = findByArtikelnummerLieferant(
						artliefDto.getCArtikelnrlieferant(), suchparameter);
				if (resultLiefNr.hasSingleResult()) {
					return resultLiefNr;
				}
				
				WebPartSearchResult resultHstNr = findByHerstellerartikelnummer(
						artikelDto.getCArtikelnrhersteller(), suchparameter);
				if (resultHstNr.hasSingleResult()) {
					return resultHstNr;
				}
				
				if (resultLiefNr.hasNoResult() && resultHstNr.hasNoResult()) {
					throw EJBExcFactory.webabfrageArtikellieferantKeinErgebnis(artikelDto, artliefDto, resultLiefNr, resultHstNr);
				} else {
					throw EJBExcFactory.webabfrageArtikellieferantMehrfacheErgebnisse(artikelDto, artliefDto, resultLiefNr, resultHstNr);
				}
			} catch (PartSearchUnexpectedResponseExc exc) {
				throw EJBExcFactory.webabfrageLiefertUnerwarteteResponse(artikelDto, artliefDto, exc);
			}
		}
		
		public WebPartSearchResult findNoExc(ArtikelId artikelId, LieferantId lieferantId) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelId.id(), theClientDto);
			ArtikellieferantDto artliefDto = getArtikelFac().artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
					artikelId.id(), lieferantId.id(), getDate(), null, theClientDto);
			WebabfrageArtikellieferantSuchparameterDto suchparameter = getParameterFac()
					.getWebabfrageArtikellieferantSuchparameter(theClientDto.getMandant());
			
			try {
				WebPartSearchResult result = findByArtikelnummerLieferant(
						artliefDto.getCArtikelnrlieferant(), suchparameter);
				if (result.getNumberOfResults() < 1) {
					result = findByHerstellerartikelnummer(
							artikelDto.getCArtikelnrhersteller(), suchparameter);
				}
				return result;
			} catch (PartSearchUnexpectedResponseExc exc) {
			}
		
			return new WebPartSearchResult();
		}
		
		private WebPartSearchResult findByHerstellerartikelnummer(String artikelnummerHersteller, 
				WebabfrageArtikellieferantSuchparameterDto suchparameter) throws PartSearchUnexpectedResponseExc {
			if (!suchparameter.useHerstellernummer() 
					|| Helper.isStringEmpty(artikelnummerHersteller)) {
				return new WebPartSearchResult();
			}
			
			return partSearchCtrl.findByManufacturerPartNumber(artikelnummerHersteller);
		}

		private WebPartSearchResult findByArtikelnummerLieferant(String artikelnummerLieferant, 
				WebabfrageArtikellieferantSuchparameterDto suchparameter) throws PartSearchUnexpectedResponseExc {
			if (!suchparameter.useLieferantenartikelnummer() 
					|| Helper.isStringEmpty(artikelnummerLieferant)) {
				return new WebPartSearchResult();
			}
			return partSearchCtrl.findByPartNumber(artikelnummerLieferant);
		}
	}
	
	public class WebPartUpdater {
		private TheClientDto theClientDto;
		private Timestamp tToday;
		private Timestamp tYesterday;
		private Integer nachkommastellenEK;

		public WebPartUpdater(TheClientDto theClientDto) {
			this.theClientDto = theClientDto;
		}
		
		public ArtikellieferantDto updateOrCreateArtikellieferantByWebPart(WebabfrageArtikellieferantUpdateProperties updateProperties) throws EJBExceptionLP, RemoteException {
			tToday = Helper.cutTimestamp(updateProperties.getWebPart().getRequestTime());
			tYesterday = Helper.addiereTageZuTimestamp(tToday, -1);
			
			ArtikellieferantDto artliefDto = findArtikellieferant(updateProperties);
			WebPart webPart = updateProperties.getWebPart();
			artliefDto.setTLetzteWebabfrage(webPart.getRequestTime());
			setWebabfrageBestand(artliefDto, webPart);
			setEinzelpreis(artliefDto, webPart);
//			artliefDto.setNVerpackungseinheit(webPart.getPackSize());
			// Mindestbestellmenge derzeit bewusst raus; wenn wieder rein, dann VPE beruecksichtigen
//			artliefDto.setFMindestbestelmenge(webPart.getMinimumOrderQuantity() != null ? webPart.getMinimumOrderQuantity().doubleValue() : null);
			artliefDto.setCArtikelnrlieferant(webPart.getStockKeepingUnit());
			artliefDto.setIWiederbeschaffungszeit(webPart.getLeastLeadTime());
			artliefDto.setCWeblink(webPart.getWeblink());
			
			if (artliefDto.getIId() == null) {
				artliefDto.setTPreisgueltigab(webPart.getRequestTime());
				artliefDto.setFRabatt(0d);
				artliefDto.setNNettopreis(artliefDto.getNEinzelpreis());
				artliefDto.setIId(getArtikelFac().createArtikellieferant(artliefDto, theClientDto));
			} else {
				BigDecimal rabattProzent = artliefDto.getFRabatt() != null ? new BigDecimal(artliefDto.getFRabatt()) : BigDecimal.ZERO;
				BigDecimal rabatt = artliefDto.getNEinzelpreis().multiply(rabattProzent.movePointLeft(2));
				artliefDto.setNNettopreis(artliefDto.getNEinzelpreis().subtract(rabatt));
				getArtikelFac().updateArtikellieferant(artliefDto, theClientDto);
			}
			
			updateOrCreateArtikellieferantstaffelByWebPart(artliefDto, webPart);
			
			return artliefDto;
		}
		
		private void setEinzelpreis(ArtikellieferantDto artliefDto, WebPart webPart) {
			artliefDto.setNEinzelpreis(calcPrice(artliefDto, webPart.getUnitPrice()));
		}

		private void setWebabfrageBestand(ArtikellieferantDto artliefDto, WebPart webPart) {
			BigDecimal mengenfaktor = getVPEFaktor(artliefDto);
			artliefDto.setNWebabfrageBestand(mengenfaktor.multiply(webPart.getInventory()));
		}
		
		private ArtikellieferantDto findArtikellieferant(WebabfrageArtikellieferantUpdateProperties updateProperties) {
			ArtikellieferantDto artliefDto = getArtikelFac().artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
					updateProperties.getArtikelId().id(), updateProperties.getLieferantId().id(), 
					new Date(updateProperties.getWebPart().getRequestTime().getTime()), null, theClientDto);
			BigDecimal calculatedPrice = calcPrice(artliefDto, updateProperties.getWebPart().getUnitPrice());
			if (artliefDto != null) {
				if (artliefDto.getNEinzelpreis() != null && artliefDto.getNEinzelpreis().compareTo(calculatedPrice) == 0
						|| artliefDto.getTPreisgueltigab().equals(Helper.cutTimestamp(updateProperties.getWebPart().getRequestTime()))) {
					return artliefDto;
				}
			} else {
				// TODO naechstaelteren finden??
			}
			
			artliefDto.setIId(null);
			return artliefDto;
		}
		
		private void updateOrCreateArtikellieferantstaffelByWebPart(ArtikellieferantDto artliefDto, WebPart webPart) throws EJBExceptionLP, RemoteException {
			Map<BdKey, List<ArtikellieferantstaffelDto>> hmGueltigeStaffeln = findGueltigeStaffeln(new ArtikellieferantId(artliefDto.getIId()), webPart);
			
			for (WebPartPrice price : webPart.getQuantityScale().getPrices()) {
				BdKey key = new BdKey(calcQuantity(artliefDto, price.getFrom()));
				List<ArtikellieferantstaffelDto> keyList = hmGueltigeStaffeln.get(key);
				if (keyList != null) {
					if (!updateStaffelnEinerMenge(artliefDto, webPart, price, keyList)) {
						createStaffel(artliefDto, webPart, price);
					}
					hmGueltigeStaffeln.remove(key);
				} else {
					createStaffel(artliefDto, webPart, price);
				}
			}
			
			for (List<ArtikellieferantstaffelDto> ungueltigeStaffeln : hmGueltigeStaffeln.values()) {
				for (ArtikellieferantstaffelDto staffel : ungueltigeStaffeln) {
					setUpdateGueltigBis(staffel, tYesterday);
				}
			}
		}

		private void createStaffel(ArtikellieferantDto artliefDto, WebPart webPart, WebPartPrice price) throws RemoteException, EJBExceptionLP {
			ArtikellieferantstaffelDto staffelDto = new ArtikellieferantstaffelDto();
			staffelDto.setArtikellieferantIId(artliefDto.getIId());
			staffelDto.setNMenge(calcQuantity(artliefDto, price.getFrom()));
			staffelDto.setNNettopreis(calcPrice(artliefDto, price.getAmount()));
			staffelDto.setFRabatt(calcRabatt(artliefDto.getNEinzelpreis(), staffelDto.getNNettopreis()).doubleValue());
			staffelDto.setTPreisgueltigab(tToday);
			staffelDto.setBRabattbehalten(Helper.getShortFalse());
			
			getArtikelFac().createArtikellieferantstaffel(staffelDto, theClientDto);
		}
		
		private BigDecimal multiplyByVPEIfNeeded(ArtikellieferantDto artliefDto, BigDecimal value) {
			BigDecimal faktor = getVPEFaktor(artliefDto);
			return value.multiply(faktor);
		}

		private BigDecimal divideByVPEIfNeeded(ArtikellieferantDto artliefDto, BigDecimal value) {
			BigDecimal mengenfaktor = getVPEFaktor(artliefDto);
			return value.divide(mengenfaktor, getNachkommastellenEK(), RoundingMode.HALF_UP);
		}

		private BigDecimal getVPEFaktor(ArtikellieferantDto artliefDto) {
			return !Helper.isStringEmpty(artliefDto.getEinheitCNrVpe()) 
					&& artliefDto.getNVerpackungseinheit() != null ? artliefDto.getNVerpackungseinheit() : BigDecimal.ONE;
		}

		private BigDecimal calcQuantity(ArtikellieferantDto artliefDto, BigDecimal quantity) {
			return multiplyByVPEIfNeeded(artliefDto, quantity);
		}
		
		private BigDecimal calcPrice(ArtikellieferantDto artliefDto, BigDecimal price) {
			return divideByVPEIfNeeded(artliefDto, price);
		}
		
		private BigDecimal calcRabatt(BigDecimal einzelpreis, BigDecimal mengenpreis) {
			BigDecimal bRabatt = BigDecimal.ONE.subtract(mengenpreis.divide(einzelpreis, getNachkommastellenEK(), RoundingMode.HALF_UP));
			return bRabatt.multiply(new BigDecimal(100));
		}
		
		private boolean updateStaffelnEinerMenge(ArtikellieferantDto artliefDto, WebPart webPart, WebPartPrice price, List<ArtikellieferantstaffelDto> staffeln) throws RemoteException, EJBExceptionLP {
			boolean pricePersisted = false;
			for (ArtikellieferantstaffelDto staffelDto : staffeln) {
				if (pricePersisted) {
					setUpdateGueltigBis(staffelDto, tYesterday);
					continue;
				}
				
				BigDecimal quantityScalePrice = calcPrice(artliefDto, price.getAmount());
				if (staffelDto.getNNettopreis().compareTo(quantityScalePrice) == 0) {
					pricePersisted = true;
				} else if (staffelDto.getTPreisgueltigab().equals(tToday)) {
					setUpdatePreis(artliefDto, staffelDto, webPart, quantityScalePrice);
					pricePersisted = true;
				} else {
					setUpdateGueltigBis(staffelDto, tYesterday);
				}
			}
			return pricePersisted;
		}
		
		private void setUpdateGueltigBis(ArtikellieferantstaffelDto staffelDto, Timestamp gueltigBis) throws RemoteException, EJBExceptionLP {
			if (staffelDto.getTPreisgueltigab().after(gueltigBis)) {
				staffelDto.setTPreisgueltigbis(staffelDto.getTPreisgueltigab());
			} else {
				staffelDto.setTPreisgueltigbis(gueltigBis);
			}
			getArtikelFac().updateArtikellieferantstaffel(staffelDto, theClientDto);
		}
		
		private void setUpdatePreis(ArtikellieferantDto artliefDto, ArtikellieferantstaffelDto staffelDto, WebPart webPart, BigDecimal nettopreis) throws RemoteException, EJBExceptionLP {
			staffelDto.setNNettopreis(nettopreis);
			staffelDto.setFRabatt(calcRabatt(artliefDto.getNEinzelpreis(), nettopreis).doubleValue());
			getArtikelFac().updateArtikellieferantstaffel(staffelDto, theClientDto);
		}

		private Map<BdKey, List<ArtikellieferantstaffelDto>> findGueltigeStaffeln(ArtikellieferantId artliefId, WebPart webPart) {
			List<Artikellieferantstaffel> gueltigeStaffeln = ArtikellieferantstaffelQuery.listByArtikellieferantIIdGueltigZuDatum(
					em, artliefId.id(), Helper.extractDate(webPart.getRequestTime()));
			Map<BdKey, List<ArtikellieferantstaffelDto>> hmStaffeln = new HashMap<BdKey, List<ArtikellieferantstaffelDto>>();
			for (Artikellieferantstaffel entity : gueltigeStaffeln) {
				BdKey key = new BdKey(entity.getNMenge());
				List<ArtikellieferantstaffelDto> list = hmStaffeln.get(key);
				if (list == null) {
					list = new ArrayList<ArtikellieferantstaffelDto>();
					hmStaffeln.put(key, list);
				}
				list.add(ArtikellieferantstaffelDtoAssembler.createDto(entity));
			}
			return hmStaffeln;
		}
		
		private int getNachkommastellenEK() {
			if (nachkommastellenEK == null) {
				nachkommastellenEK = getUINachkommastellenPreisEK(theClientDto.getMandant());
			}
			return nachkommastellenEK;
		}
		
		private class BdKey {
			private BigDecimal bd;
			public BdKey(BigDecimal bd) {
				this.bd = bd.setScale(getNachkommastellenEK(), RoundingMode.HALF_UP);
			}
			
			public BigDecimal getBd() {
				return bd;
			}
			
			@Override
			public boolean equals(Object obj) {
				if (!(obj instanceof BdKey)) return false;
				
				return this.bd.compareTo(((BdKey)obj).getBd()) == 0;
			}
			
			@Override
			public int hashCode() {
				return bd.hashCode();
			}
		}
	}
}
