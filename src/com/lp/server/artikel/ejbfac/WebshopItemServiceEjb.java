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
package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.w3c.dom.Document;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.ArtikelQuery;
import com.lp.server.artikel.ejb.Artikelkommentar;
import com.lp.server.artikel.ejb.Artikelkommentarspr;
import com.lp.server.artikel.ejb.ArtikelkommentarsprQuery;
import com.lp.server.artikel.ejb.Artikelshopgruppe;
import com.lp.server.artikel.ejb.ArtikelshopgruppeQuery;
import com.lp.server.artikel.ejb.Artikelspr;
import com.lp.server.artikel.ejb.ArtikelsprQuery;
import com.lp.server.artikel.ejb.Shopgruppe;
import com.lp.server.artikel.ejb.Shopgruppewebshop;
import com.lp.server.artikel.ejb.ShopgruppewebshopQuery;
import com.lp.server.artikel.ejb.VkPreisfindungEinzelverkaufspreis;
import com.lp.server.artikel.ejb.VkPreisfindungEinzelverkaufspreisQuery;
import com.lp.server.artikel.ejb.VkPreisfindungPreisliste;
import com.lp.server.artikel.ejb.VkPreisfindungPreislisteQuery;
import com.lp.server.artikel.ejb.Vkpfmengenstaffel;
import com.lp.server.artikel.ejb.Webshop;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.BaseRequestResult;
import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Kundesoko;
import com.lp.server.partner.ejb.KundesokoQuery;
import com.lp.server.partner.ejbfac.VkpfmengenstaffelQuery;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WebshopAuthHeader;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperWebshop;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class WebshopItemServiceEjb extends Facade implements WebshopItemServiceFacLocal {
	
	@PersistenceContext
	private EntityManager em;
	
	private TheClientDto webClientDto = null ;
	private Integer cachedWebshopIId = null ;
	private String cachedWebshopName = null ;
	private Boolean cachedSokoPreisEnabled = null ;
	
	private Map<Integer, List<WebshopShopgroupOnly>> cachedShopgroupLists = null ;
	
	public WebshopItemServiceEjb() {
		cachedShopgroupLists = new HashMap<Integer, List<WebshopShopgroupOnly>>() ;
	}

	
	protected void setupSessionParams(WebshopAuthHeader header) {
		resetCaches() ; 
		
		if(null == header) {
			throw new EJBExceptionLP(
				EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN, new IllegalArgumentException()) ;
		}

		if(HelperWebshop.isEmptyString(header.getUser())) {
			throw new EJBExceptionLP(
				EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN, new IllegalArgumentException()) ;
		}
		
		if(HelperWebshop.isEmptyString(header.getPassword())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN, new IllegalArgumentException()) ;			
		}
		
		if(HelperWebshop.isEmptyString(header.getShopName())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN, new IllegalArgumentException()) ;						
		}
		
		if(HelperWebshop.isEmptyString(header.getIsoCountry())) {
			header.setIsoCountry("AT") ;
		}
		if(HelperWebshop.isEmptyString(header.getIsoLanguage())) {
			header.setIsoLanguage("de") ;
		}
		
		try {
			String password = new String(Helper.getMD5Hash((header.getUser() + header.getPassword())
					.toCharArray())) ;
			BenutzerDto benutzerDto = getBenutzerFac().benutzerFindByCBenutzerkennung(header.getUser(), password) ;
			
			if(benutzerDto.getTGueltigbis() != null) {
				Timestamp t = new Timestamp(new Date().getTime()) ;
				if(benutzerDto.getTGueltigbis().before(t)) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG, new IllegalArgumentException()) ;
				}
			}
			
			if(benutzerDto.getBGesperrt() != null && benutzerDto.getBGesperrt() > 0) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BENUTZER_IST_GESPERRT, new IllegalArgumentException()) ;				
			}
			
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(benutzerDto.getMandantCNrDefault(), null) ;
			
			webClientDto = new TheClientDto() ;
			webClientDto.setBenutzername(benutzerDto.getCBenutzerkennung()) ;
			webClientDto.setMandant(mandantDto.getCNr()) ;
			webClientDto.setSMandantenwaehrung(mandantDto.getWaehrungCNr()) ;

			Locale l = new Locale(header.getIsoLanguage(), header.getIsoCountry()) ;
			webClientDto.setLocMandant(l) ;
			webClientDto.setUiLoc(l) ;
			webClientDto.setLocKonzern(l) ;		
			
			cachedWebshopName = header.getShopName().trim() ;
		} catch(RemoteException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BENUTZER_DARF_SICH_BEI_DIESEM_MANDANTEN_NICHT_ANMELDEN, e) ;
		}  
	}
	
	private void resetCaches() {
		webClientDto = null ;
		cachedWebshopIId = null ;
		cachedWebshopName = null ;
		cachedSokoPreisEnabled = null ;
	}
	
	protected Integer getWebshopIId() {
		if(null == webClientDto) return -1 ;
		
		if(null == cachedWebshopIId) {
			try {
				Query query = em.createNamedQuery(Webshop.QueryFindByMandantCNrCBez);
				query.setParameter(1, webClientDto.getMandant());
				query.setParameter(2, cachedWebshopName) ; 

				cachedWebshopIId = ((Webshop) query.getSingleResult()).getIId();
			} catch (NoResultException ex) {
			}
		}

		return cachedWebshopIId  ;
	}
	
	protected Boolean isSokoPreisEnabled() {
		if(null == webClientDto) return false ;
		
		if(null == cachedSokoPreisEnabled) {
			try {
				boolean sokoEnabled = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_KUNDESONDERKONDITIONEN, webClientDto.getMandant()) ;
				if(sokoEnabled) {
					ParametermandantDto paramDto = getParameterFac()
							.getMandantparameter(webClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_KUNDESONDERKONDITIONEN_WEBSHOP_VERWENDEN) ;
					sokoEnabled = paramDto.asBoolean() ;					
				}
				
				cachedSokoPreisEnabled = sokoEnabled ;
			} catch(RemoteException e) {
				cachedSokoPreisEnabled = false ;
			}
		}
		
		return cachedSokoPreisEnabled ;
	}
	
	
	@Override
	public ShopgroupsResult getShopGroupsFindAll(WebshopAuthHeader authHeader) {
		try {
			setupSessionParams(authHeader) ;

			ShopgroupsResult sgResult = new ShopgroupsResult() ;			
			List<Shopgruppewebshop> cl = ShopgruppewebshopQuery.listByWebshopId(em, getWebshopIId()) ;
			for (Shopgruppewebshop shopgruppewebshop : cl) {
				ShopgruppeDto shopGruppeDto = getArtikelFac().shopgruppeFindByPrimaryKey(
						shopgruppewebshop.getShopgruppeIId(), webClientDto) ;
				if(shopGruppeDto.getShopgruppeIId() != null) continue ;
				
				WebshopShopgroup node = buildWebshopgroup(shopGruppeDto) ;
				sgResult.getShopgroups().add(node) ;
			}

			sgResult.setOkay() ;
			return sgResult ;
		} catch(RemoteException ex) {			
			return new ShopgroupsResult(BaseRequestResult.ERROR_RMI_EXCEPTION, ex.getMessage()) ;			
		} catch(EJBExceptionLP ex) {
			return new ShopgroupsResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}
	}

	@Override
	public ShopgroupsFlatResult getShopGroupsFlatFindAll(WebshopAuthHeader header) {
		try {
			setupSessionParams(header) ;
			
			ShopgroupsFlatResult sgResult = new ShopgroupsFlatResult() ;

			List<Shopgruppewebshop> cl = ShopgruppewebshopQuery.listByWebshopId(em, getWebshopIId()) ;
			Map<Integer, WebshopShopgroupFlat> allShopgroups = validateHierarchy(cl) ;
			for (WebshopShopgroupFlat flatNode : allShopgroups.values()) {
				sgResult.getShopgroups().add(flatNode) ;				
			}
//			for (Shopgruppewebshop shopgruppewebshop : cl) {
//				ShopgruppeDto shopGruppeDto = getArtikelFac().shopgruppeFindByPrimaryKey(
//						shopgruppewebshop.getShopgruppeIId(), webClientDto) ;
//				
//				WebshopShopgroupFlat node = buildWebshopgroupFlat(shopGruppeDto) ;
//				sgResult.getShopgroups().add(node) ;
//			}
			
			sgResult.setOkay() ;
			return sgResult ;
		} catch(EJBExceptionLP ex) {
			return new ShopgroupsFlatResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;			
		}
	}
	
	private Map<Integer, WebshopShopgroupFlat> validateHierarchy(List<Shopgruppewebshop> cl) {
		Map<Integer, WebshopShopgroupFlat> allGroups = new HashMap<Integer, WebshopShopgroupFlat>() ;

		for (Shopgruppewebshop shopgruppewebshop : cl) {
			ShopgruppeDto shopGruppeDto = getArtikelFac().shopgruppeFindByPrimaryKey(
					shopgruppewebshop.getShopgruppeIId(), webClientDto) ;
			
			WebshopShopgroupFlat node = buildWebshopgroupFlat(shopGruppeDto) ;
			allGroups.put(node.getId(), node) ;
		}
		
		Map<Integer, WebshopShopgroupFlat> returnGroups = new HashMap<Integer, WebshopShopgroupFlat>() ;

		for (Entry<Integer, WebshopShopgroupFlat> entry : allGroups.entrySet()) {
			boolean addEntry = true ;
			if(entry.getValue().getParentId() != null) {
				WebshopShopgroupFlat parent = allGroups.get(entry.getValue().getParentId()) ;
				if(parent == null) {
					WebshopShopgroupFlat webshopShopgroup = entry.getValue() ;
					myLogger.warn(MessageFormat.format(
						"Shopgruppe ''{0}'' ({1}) referenziert unbekannten Vorgaenger ({2})!", 
						new Object[]{webshopShopgroup.getCnr(), webshopShopgroup.getId(), webshopShopgroup.getParentId()})) ; 
					addEntry = false ;
				}
			}
			
			if(addEntry) {
				returnGroups.put(entry.getKey(), entry.getValue()) ;
			}
		}
		
		
//		for (WebshopShopgroupFlat webshopShopgroup : allGroups.values()) {
//			if(webshopShopgroup.getParentId() != null) {
//				WebshopShopgroupFlat parent = allGroups.get(webshopShopgroup.getParentId()) ;
//				if(parent == null) {
////					myLogger.warn("Shopgruppe '" + webshopShopgroup.getCnr() + "' (" +
////							webshopShopgroup.getId() + ") referenziert unbekannten Vorgaenger (" + webshopShopgroup.getParentId() +")") ;
//					
//					myLogger.warn(MessageFormat.format(
//						"Shopgruppe ''{0}'' ({1}) referenziert unbekannten Vorgaenger ({2})!", 
//						new Object[]{webshopShopgroup.getCnr(), webshopShopgroup.getId(), webshopShopgroup.getParentId()})) ; 
//				}
//			}			
//		}
//		return allGroups ;
		return returnGroups ;
	}
	
	@Override
	public ShopgroupsResult getShopGroupsFindAllChanged(WebshopAuthHeader header, String changedDateTime) {
		if(HelperWebshop.isEmptyString(changedDateTime)) {
			return new ShopgroupsResult(BaseRequestResult.ERROR_NULL_PARAMETER, "changedDate == null (or blank/empty)") ;
		}
		Date d = HelperWebshop.parseDateTimeString(changedDateTime) ;
		if(null == d) {
			return new ShopgroupsResult(
				BaseRequestResult.ERROR_NULL_PARAMETER, "Date == null (or illegal format [" + changedDateTime + "])") ;
		}
	
		Timestamp changedStamp = new Timestamp(normalizeDateTime(d).getTime()) ;

		List<ShopgruppeDto> changedShopgruppen = new ArrayList<ShopgruppeDto>() ;
		try {
			setupSessionParams(header) ; 

			ShopgroupsResult sgResult = new ShopgroupsResult() ;
			List<Shopgruppewebshop> cl = ShopgruppewebshopQuery.listByWebshopId(em, getWebshopIId()) ;
			for (Shopgruppewebshop shopgruppewebshop : cl) {
				ShopgruppeDto shopGruppeDto = getArtikelFac().shopgruppeFindByPrimaryKey(
						shopgruppewebshop.getShopgruppeIId(), webClientDto) ;

				// Hat sich die Shopgruppe selbst geaendert
				if(shopGruppeDto.getTAendern().after(changedStamp)) {
					changedShopgruppen.add(shopGruppeDto) ;
					continue ;
				}

				// Oder ein Artikel in dieser Gruppe?
				Collection<Artikel> artikelCl = artikelFindByShopgroupIId(shopGruppeDto.getIId(), changedStamp) ;
				if(artikelCl.size() > 0) {
					changedShopgruppen.add(shopGruppeDto) ;
					continue ;
				}
			}
			
			for (ShopgruppeDto shopgruppeDto : changedShopgruppen) {
				ShopgruppeDto rootShopDto = findRootShopgruppeDto(shopgruppeDto);
				if(rootShopDto != null) {
					
					Object o = CollectionUtils.find(
						sgResult.getShopgroups(), new ShopgruppeIdPredicate(rootShopDto.getIId())); 
					if(o == null) {
						WebshopShopgroup node = buildWebshopgroup(rootShopDto, changedStamp) ;
						sgResult.getShopgroups().add(node) ;
					}
				}
			}
				
			sgResult.setOkay() ;
			return sgResult ;
		} catch(RemoteException ex) {			
			return new ShopgroupsResult(BaseRequestResult.ERROR_RMI_EXCEPTION, ex.getMessage()) ;			
		} catch(EJBExceptionLP ex) {
			return new ShopgroupsResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}
	}

	@Override
	public ShopgroupsFlatResult getShopGroupsFlatFindAllChanged(WebshopAuthHeader header, String changedDateTime) {
		if(HelperWebshop.isEmptyString(changedDateTime)) {
			return new ShopgroupsFlatResult(BaseRequestResult.ERROR_NULL_PARAMETER, "changedDate == null (or blank/empty)") ;
		}
		Date d = HelperWebshop.parseDateTimeString(changedDateTime) ;
		if(null == d) {
			return new ShopgroupsFlatResult(
				BaseRequestResult.ERROR_NULL_PARAMETER, "Date == null (or illegal format [" + changedDateTime + "])") ;
		}
	

		List<ShopgruppeDto> changedShopgruppen = new ArrayList<ShopgruppeDto>() ;
		try {
			setupSessionParams(header) ; 

			Timestamp changedStamp = HelperWebshop.normalizeDateTimeAsTimestamp(d, webClientDto.getLocMandant()) ;

			ShopgroupsFlatResult sgResult = new ShopgroupsFlatResult() ;
			List<Shopgruppewebshop> cl = ShopgruppewebshopQuery.listByWebshopId(em, getWebshopIId()) ;

			for (Shopgruppewebshop shopgruppewebshop : cl) {
				ShopgruppeDto shopGruppeDto = getArtikelFac().shopgruppeFindByPrimaryKey(
						shopgruppewebshop.getShopgruppeIId(), webClientDto) ;

				// Hat sich die Shopgruppe selbst geaendert
				if(shopGruppeDto.getTAendern().after(changedStamp)) {
					changedShopgruppen.add(shopGruppeDto) ;
					continue ;
				}

				// Oder ein Artikel in dieser Gruppe?
				Collection<Artikel> artikelCl = artikelFindByShopgroupIId(shopGruppeDto.getIId(), changedStamp) ;
				if(artikelCl.size() > 0) {
					changedShopgruppen.add(shopGruppeDto) ;
					continue ;
				}
				
				// Oder der Referenzartikel
				if(shopGruppeDto.getArtikelIId() != null) {
					List<BmeCatKommentar> kommentare = getArtikelkommentar(shopGruppeDto.getArtikelIId()) ;
					for (BmeCatKommentar bmecatKommentar : kommentare) {
						if(bmecatKommentar.isChanged(changedStamp)) {
							changedShopgruppen.add(shopGruppeDto) ;
							break ;
						}
					}
				}
			}

			for (ShopgruppeDto shopgruppeDto : changedShopgruppen) {
				WebshopShopgroupFlat node = buildWebshopgroupFlat(shopgruppeDto) ;
				sgResult.getShopgroups().add(node) ;				
			}
			
			sgResult.setOkay() ;
			return sgResult ;
		} catch(RemoteException ex) {
			return new ShopgroupsFlatResult(BaseRequestResult.ERROR_RMI_EXCEPTION, ex.getMessage()) ;			
		} catch(EJBExceptionLP ex) {
			return new ShopgroupsFlatResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}
	}

	
	@Override
	public ShopgroupsResult getShopGroupsFindByCnrChanged(WebshopAuthHeader header, String rootShopgruppe, String changedDateTime) {
		if(HelperWebshop.isEmptyString(rootShopgruppe)) {
			return new ShopgroupsResult(BaseRequestResult.ERROR_NULL_PARAMETER, "name == null (or blank/empty)") ;			
		}
		if(HelperWebshop.isEmptyString(changedDateTime)) {
			return new ShopgroupsResult(BaseRequestResult.ERROR_NULL_PARAMETER, "changedDate == null (or blank/empty)") ;
		}
		Date d = HelperWebshop.parseDateTimeString(changedDateTime) ;
		if(null == d) {
			return new ShopgroupsResult(
				BaseRequestResult.ERROR_NULL_PARAMETER, "Date == null (or illegal format [" + changedDateTime + "])") ;
		}
			
		try {
			setupSessionParams(header) ; 
			Timestamp changedStamp = HelperWebshop.normalizeDateTimeAsTimestamp(d, webClientDto.getLocMandant()) ;

			ShopgruppeDto sgDto = getArtikelFac().shopgruppeFindByCNrMandantOhneExc(rootShopgruppe, webClientDto) ;
			if(null == sgDto) return new ShopgroupsResult(BaseRequestResult.ERROR_NOT_FOUND, rootShopgruppe) ;			
			return getShopGroupsFindIdChangedImpl(header, sgDto.getIId(), changedStamp, rootShopgruppe) ;
		} catch(EJBExceptionLP ex) {
			return new ShopgroupsResult(
				BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}
	}

	@Override
	public ShopgroupsResult getShopGroupsFindByIdChanged(WebshopAuthHeader header, Integer rootShopgruppe, String changedDateTime) {		
		if(HelperWebshop.isEmptyString(changedDateTime)) {
			return new ShopgroupsResult(BaseRequestResult.ERROR_NULL_PARAMETER, "changedDate == null (or blank/empty)") ;
		}
		Date d = HelperWebshop.parseDateString(changedDateTime) ;
		if(null == d) {
			return new ShopgroupsResult(
				BaseRequestResult.ERROR_NULL_PARAMETER, "Date == null (or illegal format [" + changedDateTime + "])") ;
		}

	
		try {
			setupSessionParams(header) ; 
			Timestamp changedStamp = new Timestamp(normalizeDateTime(d).getTime()) ;

			ShopgruppeDto sgDto = getArtikelFac().shopgruppeFindByPrimaryKey(rootShopgruppe, webClientDto) ;		
			return getShopGroupsFindIdChangedImpl(header, sgDto.getIId(), changedStamp, rootShopgruppe.toString()) ;	
		} catch(EJBExceptionLP ex) {
			return new ShopgroupsResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), rootShopgruppe.toString()) ;
		}
	}
	
	private Collection<Shopgruppe> queryChildShopgruppen(Integer parentId) {
		HvTypedQuery<Shopgruppe> query = new HvTypedQuery<Shopgruppe>(
				em.createNamedQuery(Shopgruppe.QueryFindByParentIId)) ;
		query.setParameter("id", parentId) ;
		return query.getResultList() ;		
	}
	
	
	private ShopgroupsResult getShopGroupsFindIdChangedImpl(
		WebshopAuthHeader header, Integer shopgruppeIId, Timestamp changedStamp, String searchKey) {
		
		if(!existsShopgroupIIdForWebshopIId(shopgruppeIId))
			return new ShopgroupsResult(BaseRequestResult.ERROR_NOT_FOUND, searchKey) ;

		try {
			ShopgroupsResult sgResult = new ShopgroupsResult() ;
			List<Shopgruppe> changedShopgruppen = new ArrayList<Shopgruppe>() ;

			Collection<Shopgruppe> childs = queryChildShopgruppen(shopgruppeIId) ;			
			for (Shopgruppe shopgruppe : childs) {
				if(haveChangedWithChildShopgruppen(shopgruppe, changedStamp)) {
					changedShopgruppen.add(shopgruppe) ;
				}
			}
			
			if(childs.size() == 0) {
				Shopgruppe me = em.find(Shopgruppe.class, shopgruppeIId) ;
				if(me != null) {
					changedShopgruppen.add(me) ;
				}
			}
			
			for (Shopgruppe shopgruppe : changedShopgruppen) {
				Object o = CollectionUtils.find(
					sgResult.getShopgroups(), new ShopgruppeIdPredicate(shopgruppeIId)); 
				if(o == null) {
					ShopgruppeDto rootShopDto = getArtikelFac().shopgruppeFindByPrimaryKey(shopgruppe.getIId(), webClientDto) ;
					WebshopShopgroup node = buildWebshopgroup(rootShopDto, changedStamp) ;
					sgResult.getShopgroups().add(node) ;
				}
			}
				
			sgResult.setOkay() ;
			return sgResult ;
		} catch(RemoteException ex) {			
			return new ShopgroupsResult(BaseRequestResult.ERROR_RMI_EXCEPTION, ex.getMessage()) ;						
		} catch(EJBExceptionLP ex) {
			return new ShopgroupsResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), searchKey) ;
		}
	}

	private boolean haveChangedWithChildShopgruppen(Shopgruppe shopgruppe, Timestamp changedStamp) {
		if(shopgruppe.getTAendern().after(changedStamp)) return true ;

		Collection<Artikel> artikelCl = artikelFindByShopgroupIId(shopgruppe.getIId(), changedStamp) ;
		if(artikelCl.size() > 0) return true ;
		
		// Hat eine meiner Kinder eine Aenderung?
		Collection<Shopgruppe> childs = queryChildShopgruppen(shopgruppe.getIId()) ;

		for (Shopgruppe childShopgruppe : childs) {
			boolean foundChangedChild = haveChangedWithChildShopgruppen(childShopgruppe, changedStamp) ;
			if(foundChangedChild) return true ;
		}
		
		return false ;
	}
	
	class ShopgruppeIdPredicate implements Predicate {
		private Integer knownId ;
		
		public ShopgruppeIdPredicate(Integer shopgruppeId) {
			knownId = shopgruppeId ;
		}

		@Override
		public boolean evaluate(Object arg0) {
			if(null == arg0) return false ;
			return ((WebshopShopgroup)arg0).getId().equals(knownId) ; 
		}		
	}
	
	class ShopgruppeFlatIdPredicate implements Predicate {
		private Integer knownId ;
		
		public ShopgruppeFlatIdPredicate(Integer shopgruppeId) {
			knownId = shopgruppeId ;
		}

		@Override
		public boolean evaluate(Object arg0) {
			if(null == arg0) return false ;
			return ((WebshopShopgroupFlat)arg0).getId().equals(knownId) ; 
		}		
	}
	

	private ShopgruppeDto findRootShopgruppeDto(ShopgruppeDto anyShopgruppeDto) throws EJBExceptionLP {
		Integer parentShopIId = anyShopgruppeDto.getShopgruppeIId() ;
		ShopgruppeDto rootShopDto = anyShopgruppeDto ;
		while(parentShopIId != null) {
			Shopgruppe shopgruppe  = em.find(Shopgruppe.class, parentShopIId) ;
			if(null != shopgruppe) {
				parentShopIId = shopgruppe.getShopgruppeIId() ;
				if(null == parentShopIId) {
					rootShopDto = getArtikelFac().shopgruppeFindByPrimaryKey(shopgruppe.getIId(), webClientDto) ;							
				}
			} else {
				parentShopIId = null ;
				rootShopDto = null ;
			}
		}
		
		return rootShopDto;
	}
	
	@Override
	public ShopgroupResult getShopGroupFindByCnr(WebshopAuthHeader header, String name) {
		if(HelperWebshop.isEmptyString(name)) {
			return new ShopgroupResult(BaseRequestResult.ERROR_NULL_PARAMETER, "name == null (or blank/empty)") ;
		}

		setupSessionParams(header) ;
		
		try {
			ShopgruppeDto sgDto = getArtikelFac().shopgruppeFindByCNrMandantOhneExc(name, webClientDto) ;
			if(null == sgDto) return new ShopgroupResult(ShopgroupResult.ERROR_NOT_FOUND, name) ;
			
			ShopgroupResult sgResult = retrieveWebShopgroup(sgDto, name);
			return sgResult;
		} catch(RemoteException ex) {			
			return new ShopgroupResult(BaseRequestResult.ERROR_RMI_EXCEPTION, ex.getMessage()) ;			
		} catch(EJBExceptionLP ex) {
			return new ShopgroupResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;			
		}
	}

	@Override
	public ShopgroupResult getShopGroupFindById(WebshopAuthHeader header, Integer id) {
		if(null == id) {
			return new ShopgroupResult(BaseRequestResult.ERROR_NULL_PARAMETER, "id == null") ;
		}
		
		setupSessionParams(header) ;
		
		try {
			ShopgruppeDto sgDto = getArtikelFac().shopgruppeFindByPrimaryKey(id, webClientDto) ;
			if(null == sgDto) return new ShopgroupResult(ShopgroupResult.ERROR_NOT_FOUND, id.toString()) ;
			
			ShopgroupResult sgResult = retrieveWebShopgroup(sgDto, id.toString()) ;
			return sgResult;
		} catch(RemoteException ex) {			
			return new ShopgroupResult(BaseRequestResult.ERROR_RMI_EXCEPTION, ex.getMessage()) ;			
		} catch(EJBExceptionLP ex) {
			return new ShopgroupResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;			
		}
	}


	@Override
	public WebshopItemsResult getItems(WebshopAuthHeader header) {
		setupSessionParams(header) ;

		WebshopItemsResult itemsResult = new WebshopItemsResult() ;

		try {
			List<Shopgruppewebshop> cl =
					ShopgruppewebshopQuery.listByWebshopId(em, getWebshopIId()) ;
			for (Shopgruppewebshop shopgruppewebshop : cl) {			
				Integer shopgroupIId = shopgruppewebshop.getShopgruppeIId() ;				
				Collection<Artikel> artikelCl = artikelFindByShopgroupIId(shopgroupIId) ;
				for (Artikel artikel : artikelCl) {
					WebshopItemReference itemRef = 
							new WebshopItemReference(artikel.getCNr(), artikel.getIId()) ;
					itemsResult.getItems().add(itemRef) ;
				}				
			}

			itemsResult.setOkay() ;
			
		} catch(EJBExceptionLP e) {
			itemsResult.setRc(BaseRequestResult.ERROR_EJB_EXCEPTION + e.getCode()) ;
			itemsResult.setDescription(e.getMessage()) ;
		}

		return itemsResult ;
	}

	
	private boolean addAllShopgroupItems(
			List<WebshopItemReference> items, Integer shopgroupIId, Timestamp changedStamp) {
		ShopgruppeDto shopGruppeDto = getArtikelFac()
				.shopgruppeFindByPrimaryKey(shopgroupIId, webClientDto) ;

		boolean changed = false ;
		
		// Hat sich die Shopgruppe selbst geaendert
		if(shopGruppeDto.getTAendern().after(changedStamp)) {
			Collection<Artikel> artikelCl = artikelFindByShopgroupIId(shopgroupIId) ;
			for (Artikel artikel : artikelCl) {
				WebshopItemReference itemRef = 
						new WebshopItemReference(artikel.getCNr(), artikel.getIId()) ;
				items.add(itemRef) ;
			}
			
			changed = true ;
		}	
		
		return changed ;
	}

	@Override
	public WebshopItemsResult getItemsChanged(WebshopAuthHeader header, String changedDateTime) {
		if(HelperWebshop.isEmptyString(changedDateTime)) {
			return new WebshopItemsResult(BaseRequestResult.ERROR_NULL_PARAMETER, "changedDateTime == null (or blank/empty)") ;
		}

		setupSessionParams(header) ;

		Date d = HelperWebshop.parseDateTimeString(changedDateTime) ;
		if(null == d) {
			return new WebshopItemsResult (
				BaseRequestResult.ERROR_NULL_PARAMETER, "Date == null (or illegal format [" + changedDateTime + "])") ;
		}
	
		Timestamp changedStamp = HelperWebshop.normalizeDateTimeAsTimestamp(d, webClientDto.getLocMandant()) ;

		Set<Integer> itemIds = new HashSet<Integer>() ;
		
		WebshopItemsResult itemsResult = new WebshopItemsResult() ;
		try {			
			List<Shopgruppewebshop> cl =
					ShopgruppewebshopQuery.listByWebshopId(em, getWebshopIId()) ;
			for (Shopgruppewebshop shopgruppewebshop : cl) {
				Integer shopgroupIId = shopgruppewebshop.getShopgruppeIId() ;
				addAllShopgroupItems(itemsResult.getItems(), shopgroupIId, changedStamp) ;
			}
			
			addChangedArtikelkommentarSpr(changedStamp, itemIds) ;		
			addChangedArtikelSpr(changedStamp, itemIds);		
			addChangedVkpfmengenstaffel(changedStamp, itemIds);
			addChangedVkPreisfindungPreisliste(changedStamp, itemIds);		
			addChangedVkPreisfindungEinzelverkaufspreis(changedStamp, itemIds);
			addChangedArtikel(changedStamp, itemIds) ;
			
			for (Integer anItemId : itemIds) {
				Artikel item = em.find(Artikel.class, anItemId) ;
				WebshopItemReference itemRef = 
						new WebshopItemReference(item.getCNr(), item.getIId()) ;
				itemsResult.getItems().add(itemRef) ;
			}

			itemsResult.setOkay() ;
//		} catch(RemoteException e) {
//			itemsResult.setRc(BaseRequestResult.ERROR_RMI_EXCEPTION) ;
//			itemsResult.setDescription(e.getMessage()) ;			
		} catch(EJBExceptionLP e) {
			itemsResult.setRc(BaseRequestResult.ERROR_EJB_EXCEPTION + e.getCode()) ;
			itemsResult.setDescription(e.getMessage()) ;			
		}
		
		return itemsResult ;
	}

	private void addItemHavingShopgruppe(Set<Integer> itemIds, Integer itemId) {
		Artikel item = em.find(Artikel.class, itemId) ;
		if(item.getShopgruppeIId() != null) {
			itemIds.add(itemId) ;
		}		
	}
	
	private void addChangedArtikelkommentarSpr(Timestamp changedStamp, Set<Integer> itemIds) {
		List<Artikelkommentarspr> kommentare = ArtikelkommentarsprQuery.listByChangedDate(em, changedStamp) ;
		for (Artikelkommentarspr artikelkommentarspr : kommentare) {
			Artikelkommentar artikelkommentar = em.find(Artikelkommentar.class, artikelkommentarspr.getPk().getArtikelkommentarIId()) ;
			addItemHavingShopgruppe(itemIds, artikelkommentar.getArtikelIId()) ;
		}	
	}

	private void addChangedVkPreisfindungEinzelverkaufspreis(
			Timestamp changedStamp, Set<Integer> itemIds) {
		List<VkPreisfindungEinzelverkaufspreis> einzelpreise = 
				VkPreisfindungEinzelverkaufspreisQuery.listByChangedDate(em, webClientDto.getMandant(), changedStamp) ;
		for (VkPreisfindungEinzelverkaufspreis einzelpreis : einzelpreise) {
			addItemHavingShopgruppe(itemIds, einzelpreis.getArtikelIId()) ;
		}
	}


	private void addChangedVkPreisfindungPreisliste(Timestamp changedStamp,
			Set<Integer> itemIds) {
		List<VkPreisfindungPreisliste> preise = VkPreisfindungPreislisteQuery.listByChangedDate(em, changedStamp) ;
		for(VkPreisfindungPreisliste preis : preise) {
			addItemHavingShopgruppe(itemIds, preis.getArtikelIId()) ;
		}
	}


	private void addChangedVkpfmengenstaffel(Timestamp changedStamp,
			Set<Integer> itemIds) {
		List<Vkpfmengenstaffel> mengenstaffeln = VkpfmengenstaffelQuery.listByChangedDate(em, changedStamp) ;
		for (Vkpfmengenstaffel vkpfmengenstaffel : mengenstaffeln) {
			addItemHavingShopgruppe(itemIds, vkpfmengenstaffel.getArtikelIId()) ;
		}
	}


	private void addChangedArtikelSpr(Timestamp changedStamp,
			Set<Integer> itemIds) {
		List<Artikelspr> artikelspr = ArtikelsprQuery.listByChangedDate(em, changedStamp) ;
		for (Artikelspr artikelsprEntry : artikelspr) {
			addItemHavingShopgruppe(itemIds, artikelsprEntry.getPk().getArtikelIId()) ;
		}
	}

	private void addChangedArtikel(Timestamp changedStamp, Set<Integer> itemIds) {
		List<Artikel> artikels = ArtikelQuery.listByMandantDate(em, webClientDto.getMandant(), changedStamp) ;
		for (Artikel artikel : artikels) {
			itemIds.add(artikel.getIId()) ;
		}
	}
	
	@Override
	public WebshopItemResult getItemFindByCnr(WebshopAuthHeader header, String cnr) {
		if(HelperWebshop.isEmptyString(cnr)) {
			return new WebshopItemResult(BaseRequestResult.ERROR_NULL_PARAMETER, "item == null (or blank/empty)") ;						
		}
		
		setupSessionParams(header) ;	
		cnr = cnr.trim();
				
		try {
			ArtikelDto itemDto = getArtikelFac().artikelFindByCNr(cnr, webClientDto) ;

			WebshopItemResult result = getItemFindByIIdImpl(itemDto, cnr) ;
			return result ;
		} catch (NoResultException e) {
			return new WebshopItemResult(WebshopItemResult.ERROR_NOT_FOUND, cnr) ;
		} catch (NonUniqueResultException e) {
			return new WebshopItemResult(WebshopItemResult.ERROR_NOT_FOUND, cnr) ;
		} catch(RemoteException re) {
			return new WebshopItemResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;
		} catch(EJBExceptionLP ex) {
			return new WebshopItemResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}		
	}

	
	@Override
	public WebshopItemResult getItemFindById(WebshopAuthHeader header, Integer id) {
		setupSessionParams(header) ;
				
		try {
			ArtikelDto itemDto = getArtikelFac().artikelFindByPrimaryKey(id, webClientDto) ;
			
			WebshopItemResult result = getItemFindByIIdImpl(itemDto, id.toString()) ;
			return result ;
		} catch (NoResultException e) {
			return new WebshopItemResult(WebshopItemResult.ERROR_NOT_FOUND, id.toString()) ;
		} catch (NonUniqueResultException e) {
			return new WebshopItemResult(WebshopItemResult.ERROR_NOT_FOUND, id.toString()) ;
		} catch(RemoteException re) {
			return new WebshopItemResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;
		} catch(EJBExceptionLP ex) {
			return new WebshopItemResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}		
	}


	private ArtikelkommentarsprDto findLongDescription(List<BmeCatKommentar> kommentare) {
		for (BmeCatKommentar bmecatKommentar : kommentare) {
			ArtikelkommentarDto kommentarDto = bmecatKommentar.getKommentarDto() ;
			if(kommentarDto.getDatenformatCNr().startsWith("text/html") &&
					bmecatKommentar.getKommentarartDto().isWebshop()) {
				if(null != kommentarDto.getArtikelkommentarsprDto()) {
					return kommentarDto.getArtikelkommentarsprDto() ;
				}
			}
		}
		
		return null ;
	}
	
	private ArtikelkommentarsprDto findRemark(List<BmeCatKommentar> kommentare) {
		for (BmeCatKommentar bmecatKommentar : kommentare) {
			ArtikelkommentarDto kommentarDto = bmecatKommentar.getKommentarDto() ;
			if(kommentarDto.getDatenformatCNr().startsWith("text/html") &&
					!bmecatKommentar.getKommentarartDto().isWebshop()) {
				if(null != kommentarDto.getArtikelkommentarsprDto()) {
					return kommentarDto.getArtikelkommentarsprDto() ;
				}
			}
		}
		
		return null ;
	}
	
	private ArtikelkommentarDto findImage(List<BmeCatKommentar> kommentare) {
		for (BmeCatKommentar bmecatKommentar : kommentare) {
			ArtikelkommentarDto kommentarDto = bmecatKommentar.getKommentarDto() ;
			if(kommentarDto.getDatenformatCNr().startsWith("image/")) {
				if(null != kommentarDto.getArtikelkommentarsprDto()) {
					return kommentarDto ;
				}
			}
		}
		
		return null ;
	}
	
	
	private void buildItemImage(WebshopItem webItem, List<BmeCatKommentar> kommentare) {
		ArtikelkommentarDto kommentarDto = findImage(kommentare) ;
		if(null == kommentarDto) return ;
		
		webItem.setImage(kommentarDto.getArtikelkommentarsprDto().getOMedia()) ;
	}
	
	private BigDecimal getMinimumPrice(BigDecimal minimum, VerkaufspreisDto priceDto) {
		if(priceDto != null && priceDto.nettopreis != null) {
			return null == minimum ? priceDto.nettopreis : minimum.min(priceDto.nettopreis) ; 
		}
		
		return minimum;		
	}
	
	private BigDecimal getPriceFromPreisfindung(VkpreisfindungDto vkPreisDto) {
		BigDecimal p = getMinimumPrice(null, vkPreisDto.getVkpStufe3()) ;
//		if(p != null) return p ;

		p = getMinimumPrice(p, vkPreisDto.getVkpStufe2()) ;
//		if(p != null) return p ;

		p = getMinimumPrice(p, vkPreisDto.getVkpStufe1()) ;
//		if(p != null) return p ;

		return getMinimumPrice(p, vkPreisDto.getVkpPreisbasis())  ;
	}
	

	private List<VkpfartikelpreislisteDto> getWebshopPreislisten() throws RemoteException {
		List<VkpfartikelpreislisteDto> webPreislisten = new ArrayList<VkpfartikelpreislisteDto>() ;
		VkpfartikelpreislisteDto[] preislisten = getVkPreisfindungFac()
				.getAlleAktivenPreislisten((Short) (short) 1, webClientDto) ;
		for (VkpfartikelpreislisteDto preislisteDto : preislisten) {
			if(cachedWebshopIId.equals(preislisteDto.getWebshopIId())) {
				webPreislisten.add(preislisteDto) ;
			}
		}
		
		return webPreislisten ;
	}
	
	
	private List<PriceInfo> calculatePricelistPrices(ArtikelDto itemDto,
			Integer preislisteIId, java.sql.Date normalizedSqlDate) throws RemoteException {
		List<PriceInfo> preisInfos = new ArrayList<PriceInfo>() ;
		
		Integer mwstsatzbezIId = itemDto.getMwstsatzbezIId() ;

		VkpfMengenstaffelDto[] staffelnDto = getVkPreisfindungFac()
			.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
					itemDto.getIId(), normalizedSqlDate, 
					preislisteIId, webClientDto) ;
		if(staffelnDto.length > 0) {
			for (VkpfMengenstaffelDto vkpfMengenstaffelDto : staffelnDto) {
				VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindungWeb(
						itemDto.getIId(), null, vkpfMengenstaffelDto.getNMenge(), normalizedSqlDate,
						preislisteIId, 
						mwstsatzbezIId, webClientDto.getSMandantenwaehrung(), webClientDto) ;	
				BigDecimal price = getPriceFromPreisfindung(vkpreisfindungDto) ;
				preisInfos.add(
						new PriceInfo(null, preislisteIId, vkpfMengenstaffelDto.getNMenge(), price)) ;
			}
		} 

		VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindungWeb(
					itemDto.getIId(), null, BigDecimal.ONE, normalizedSqlDate,
					preislisteIId, 
					mwstsatzbezIId, webClientDto.getSMandantenwaehrung(), webClientDto) ;				
		BigDecimal price = getPriceFromPreisfindung(vkpreisfindungDto) ;
		preisInfos.add(
					new PriceInfo(null, preislisteIId, BigDecimal.ONE, price)) ;		
		
		return preisInfos ;
	}
	
	
	private List<PriceInfo> calculateKundeSokoPrices(List<Kundesoko> kundesokos, 
		ArtikelDto itemDto, Integer preislisteIId, java.sql.Date normalizedSqlDate) throws RemoteException {
		
		List<PriceInfo> preisInfos = new ArrayList<PriceInfo>() ;
		
		for (Kundesoko kundesoko : kundesokos) {
			Integer kundeIId = kundesoko.getKundeIId() ;
			Integer mwstsatzbezIId = itemDto.getMwstsatzbezIId() ;
			if(mwstsatzbezIId == null) {
				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, webClientDto) ;
				mwstsatzbezIId = kundeDto.getMwstsatzbezIId() ;
			}

			VkpfMengenstaffelDto[] staffelnDto = getVkPreisfindungFac()
				.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
						itemDto.getIId(), normalizedSqlDate, 
						preislisteIId, webClientDto) ;
			if(staffelnDto.length > 0) {
				for (VkpfMengenstaffelDto vkpfMengenstaffelDto : staffelnDto) {
					VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(
							itemDto.getIId(), kundeIId, vkpfMengenstaffelDto.getNMenge(), normalizedSqlDate,
							preislisteIId, 
							mwstsatzbezIId, webClientDto.getSMandantenwaehrung(), webClientDto) ;	
					BigDecimal price = getPriceFromPreisfindung(vkpreisfindungDto) ;
					preisInfos.add(
							new PriceInfo(kundeIId, preislisteIId, vkpfMengenstaffelDto.getNMenge(), price)) ;
				}
			} 

			VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(
						itemDto.getIId(), kundeIId, BigDecimal.ONE, normalizedSqlDate,
						preislisteIId, 
						mwstsatzbezIId, webClientDto.getSMandantenwaehrung(), webClientDto) ;				
				BigDecimal price = getPriceFromPreisfindung(vkpreisfindungDto) ;
				preisInfos.add(
						new PriceInfo(kundeIId, preislisteIId, BigDecimal.ONE, price)) ;
		}
		
		return preisInfos ;
	}
	
	private List<PriceInfo> calculatePrices(ArtikelDto itemDto, Date theDate) throws RemoteException {
		List<PriceInfo> preisInfos = new ArrayList<PriceInfo>() ;
		
		java.sql.Date normalizedSqlDate = new java.sql.Date(normalizeDate(theDate).getTime()) ;
		
		BigDecimal price = getVkPreisfindungFac().ermittlePreisbasis(
				itemDto.getIId(), normalizedSqlDate,
				null, webClientDto.getSMandantenwaehrung(), webClientDto) ;

		preisInfos.add(new PriceInfo(BigDecimal.ONE, price)) ;
		
		List<VkpfartikelpreislisteDto> webPreislisten = getWebshopPreislisten() ;
		for (VkpfartikelpreislisteDto vkpfartikelpreislisteDto : webPreislisten) {
			if(isSokoPreisEnabled()) {
				List<Kundesoko> kundesokos = KundesokoQuery
						.listByArtikelIIdGueltigkeitsdatum(em, itemDto.getIId(), normalizedSqlDate) ;
				preisInfos.addAll(calculateKundeSokoPrices(
						kundesokos, itemDto, vkpfartikelpreislisteDto.getIId(), normalizedSqlDate)) ;
				
				kundesokos = KundesokoQuery
						.listByArtgruIIdGueltigkeitsdatum(em, itemDto.getArtgruIId(), normalizedSqlDate) ;
				preisInfos.addAll(calculateKundeSokoPrices(
						kundesokos, itemDto, vkpfartikelpreislisteDto.getIId(), normalizedSqlDate)) ;
			}
			
			preisInfos.addAll(calculatePricelistPrices(
					itemDto, vkpfartikelpreislisteDto.getIId(), normalizedSqlDate)) ;
		}
		
		return preisInfos ;
	}
	
	private WebshopItemResult getItemFindByIIdImpl(ArtikelDto itemDto, String searchKey) throws RemoteException {
		WebshopItemResult result = new WebshopItemResult() ;
		WebshopItem webItem = new WebshopItem() ;
		
		if(itemDto.getShopgruppeIId() == null) {
			return new WebshopItemResult(WebshopItemResult.ERROR_NOT_FOUND, searchKey) ;
		}
		if(!existsShopgroupIIdForWebshopIId(itemDto.getShopgruppeIId())) {
			return new WebshopItemResult(WebshopItemResult.ERROR_NOT_FOUND, searchKey) ;
		}

		List<WebshopShopgroupOnly> webshopgroups = retrieveCachedShopgroups(itemDto.getShopgruppeIId()) ;
		
		webItem.setId(itemDto.getIId()) ;
		webItem.setCnr(itemDto.getCNr()) ;
		
		List<Integer> shopgroupIds = getShopgruppeIIds(itemDto);
		webItem.setShopgroupIds(shopgroupIds) ;

		List<BmeCatKommentar> kommentare = getArtikelkommentar(itemDto.getIId()) ;
		buildItemImage(webItem, kommentare) ;

		buildStockQuantities(webItem, itemDto.getIId()) ;
		buildSeoInfo(webItem, itemDto.getIId());
		
		List<PriceInfo> priceInfos = calculatePrices(itemDto, Calendar.getInstance().getTime()) ;

		Integer mwstsatzbezIId = itemDto.getMwstsatzbezIId() ;
		MwstsatzDto satzDto = null ;
		if(null != mwstsatzbezIId) {
			satzDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(mwstsatzbezIId, webClientDto) ;
		}
		
		BmeCatTransformer bmeCatTransformer = new BmeCatTransformer() ;
		Document d = bmeCatTransformer.transform(itemDto, kommentare, 
				webshopgroups, priceInfos, satzDto, webClientDto.getSMandantenwaehrung()) ;
		webItem.setBmeCatInfo(bmeCatTransformer.asString(d)) ;
				
		result.setItem(webItem) ;
		result.setOkay() ;
		return result ;		
	}

	
	private void buildStockQuantities(WebshopItem webItem, Integer itemIId) throws RemoteException {		
		BigDecimal lagerstand = getLagerFac()
				.getLagerstandAllerLagerEinesMandanten(itemIId, false, webClientDto);
		webItem.setQuantityStored(lagerstand) ;
		
		BigDecimal fehlmengen = getFehlmengeFac()
				.getAnzahlFehlmengeEinesArtikels(itemIId, webClientDto);
		BigDecimal reservierungen = getReservierungFac()
				.getAnzahlReservierungen(itemIId, webClientDto);
		BigDecimal verfuegbar = lagerstand.subtract(reservierungen)
				.subtract(fehlmengen);
		webItem.setQuantityAvailable(verfuegbar) ;		
	}


	private List<Integer> getShopgruppeIIds(ArtikelDto itemDto) {
		List<Integer> shopgroupIds = new ArrayList<Integer>() ;
// getBereitsVerwendete... beinhaltet bereits alle Shopgruppen
//		shopgroupIds.add(itemDto.getShopgruppeIId()) ;
		
		Integer[] otherShopgroupIIds = getArtikelFac().getBereitsVerwendeteShopgruppen(itemDto.getIId()) ;
		for (Integer shopgroupIId : otherShopgroupIIds) {
			shopgroupIds.add(shopgroupIId) ;
		}
		return shopgroupIds;
	}

	
	@Override
	public WebshopItemImageResult getItemImage(WebshopAuthHeader header, String itemImageName) {
		if(HelperWebshop.isEmptyString(itemImageName)) {
			return new WebshopItemImageResult(BaseRequestResult.ERROR_NULL_PARAMETER, "itemImageName == null (or blank/empty)") ;						
		}
		
		setupSessionParams(header) ;
		
		WebshopItemImageResult notFoundResult = 
				new WebshopItemImageResult(BaseRequestResult.ERROR_NOT_FOUND, itemImageName) ;

		itemImageName = itemImageName.trim();
		// Format des Namens:
		// iid-dateiname
		int hyphen = itemImageName.indexOf('-') ;
		if(hyphen < 0) return notFoundResult ;
		if(itemImageName.length() <= hyphen) return notFoundResult ;
		
		try {
			Integer kommentarIId = Integer.parseInt(itemImageName.substring(0, hyphen)) ;
			ArtikelkommentarDto kommentarDto = getArtikelkommentarFac().artikelkommentarFindByPrimaryKey(kommentarIId, webClientDto) ;
			if(kommentarDto.getArtikelkommentarsprDto().getCDateiname().equals(itemImageName.substring(hyphen + 1))) {

				WebshopItemImageResult result = new WebshopItemImageResult() ;
				result.setImage(kommentarDto.getArtikelkommentarsprDto().getOMedia()) ; 
				result.setImageName(itemImageName) ;
				result.setMimeType(kommentarDto.getDatenformatCNr().trim()) ;
				result.setOkay() ;
				return result ;
			}			
		} catch(NumberFormatException e) {
			return new WebshopItemImageResult(BaseRequestResult.ERROR_NOT_FOUND, itemImageName) ;
		} catch(RemoteException re) {
			return new WebshopItemImageResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;			
		} catch(EJBExceptionLP ex) {
			return new WebshopItemImageResult(BaseRequestResult.ERROR_EJB_EXCEPTION + ex.getCode(), ex.getMessage()) ;
		}		
		
		return notFoundResult ;
	}
	
	private Date normalizeDate(Date changedDate) {
		Calendar cal = Calendar.getInstance(webClientDto.getLocMandant()) ;
		cal.setTime(changedDate) ;
		cal.set(Calendar.HOUR_OF_DAY, 0) ;
		cal.set(Calendar.MINUTE, 0) ;
		cal.set(Calendar.SECOND, 0) ;
		cal.set(Calendar.MILLISECOND, 0) ;
		return cal.getTime() ;
	}

	private Date normalizeDateTime(Date changedDate) {
		Calendar cal = Calendar.getInstance(webClientDto.getLocMandant()) ;
		cal.setTime(changedDate) ;
		cal.set(Calendar.SECOND, 0) ;
		cal.set(Calendar.MILLISECOND, 0) ;
		return cal.getTime() ;
	}
	

//	private List<ArtikelkommentarDto> getArtikelkommentar(Integer itemIId) throws RemoteException {
//		List<ArtikelkommentarDto> kommentare = new ArrayList<ArtikelkommentarDto>() ;
//		
//		ArtikelkommentarDto[] kommentarDtos = getArtikelkommentarFac()
//				.artikelkommentarFindByArtikelIId(itemIId, webClientDto) ;
//		
//		for (ArtikelkommentarDto artikelkommentarDto : kommentarDtos) {
//			ArtikelkommentarartDto kommentarArtDto = getArtikelkommentarFac().
//				artikelkommentarartFindByPrimaryKey(artikelkommentarDto.getArtikelkommentarartIId(), webClientDto) ;
//			if(kommentarArtDto.isWebshop()) {
//				if(isKommentarForWebshop(artikelkommentarDto)) {
//					kommentare.add(artikelkommentarDto) ;
// 				}
//			}
//		}
//		
//		return kommentare ;
//	}
//	
	private List<BmeCatKommentar> getArtikelkommentar(Integer itemIId) throws RemoteException {
		List<BmeCatKommentar> kommentare = new ArrayList<BmeCatKommentar>() ;
		
		ArtikelkommentarDto[] kommentarDtos = getArtikelkommentarFac()
				.artikelkommentarFindByArtikelIId(itemIId, webClientDto) ;
		
		for (ArtikelkommentarDto artikelkommentarDto : kommentarDtos) {
			ArtikelkommentarartDto kommentarArtDto = getArtikelkommentarFac().
				artikelkommentarartFindByPrimaryKey(artikelkommentarDto.getArtikelkommentarartIId(), webClientDto) ;
			if(kommentarArtDto.isWebshop()) {
				if(isKommentarForWebshop(artikelkommentarDto)) {
					BmeCatKommentar c = new BmeCatKommentar(artikelkommentarDto, kommentarArtDto) ;
					kommentare.add(c) ;
					continue ;
 				}
			}
			
			if("WEBDescription".equals(kommentarArtDto.getCNr())) {
				kommentare.add(new BmeCatKommentar(artikelkommentarDto, kommentarArtDto)) ;
			}
		}
		
		return kommentare ;
	}
	
	
	private ISeoInfo getArtikelkommentarSeo(ISeoInfo seoInfo, Integer itemIId) throws RemoteException {
		ArtikelkommentarDto[] kommentarDtos = getArtikelkommentarFac()
				.artikelkommentarFindByArtikelIId(itemIId, webClientDto) ;
		
		boolean hasSeoInfo = false ;
		for (ArtikelkommentarDto artikelkommentarDto : kommentarDtos) {
			ArtikelkommentarartDto kommentarArtDto = getArtikelkommentarFac().
				artikelkommentarartFindByPrimaryKey(artikelkommentarDto.getArtikelkommentarartIId(), webClientDto) ;
			hasSeoInfo |= setSeoInfo(seoInfo, kommentarArtDto, artikelkommentarDto) ;
		}
		
		return hasSeoInfo ? seoInfo : null ;		
	}
	
	private boolean setSeoInfo(ISeoInfo seoInfo, ArtikelkommentarartDto kommentarArtDto, ArtikelkommentarDto kommentarDto) {
		if(kommentarArtDto.getCNr().startsWith("SEO") && "text/html".equals(kommentarDto.getDatenformatCNr().trim())) {
			if("SEOTitle".equals(kommentarArtDto.getCNr())) {
				seoInfo.setTitle(HelperWebshop.unescapeHtml(kommentarDto.getArtikelkommentarsprDto().getXKommentar())) ;
				return true ;
			}		
			if("SEOKeywords".equals(kommentarArtDto.getCNr())) {
				seoInfo.setKeywords(HelperWebshop.unescapeHtml(kommentarDto.getArtikelkommentarsprDto().getXKommentar()));					
				return true ;
			}
			if("SEODescription".equals(kommentarArtDto.getCNr())) {
				seoInfo.setDescription(HelperWebshop.unescapeHtml(kommentarDto.getArtikelkommentarsprDto().getXKommentar())) ;					
				return true ;
			}
		}
		
		return false ;
	}
	
	
	/** 
	 * Ein Kommentar ist dann f&uuml;r den Webshop, wenn es sich um einen Text oder ein Bild handelt
	 * @param artikelkommentarDto
	 * @return true wenn der angegebene Artikelkommentar f&uuml;r den Webshop geeignet ist
	 */
	private boolean isKommentarForWebshop(ArtikelkommentarDto artikelkommentarDto) {
		if(null == artikelkommentarDto) return false ;
		
		String datenformat = artikelkommentarDto.getDatenformatCNr() ;
		if(null == datenformat) return false ;
		
		if(datenformat.startsWith("text/html")) return true ;
		if(datenformat.startsWith("image/")) return true ;
		
		return false ;
	}
	
	
	protected List<WebshopShopgroupOnly> retrieveCachedShopgroups(Integer shopgruppeBlatt) {
		List<WebshopShopgroupOnly> l = cachedShopgroupLists.get(shopgruppeBlatt) ;
		if(l != null) return l ;
		
		l = retrieveShopgroups(shopgruppeBlatt) ;
		cachedShopgroupLists.put(shopgruppeBlatt, l) ;

		return l ;
	}

	protected List<WebshopShopgroupOnly> retrieveShopgroups(Integer shopgruppeBlatt) {
		List<WebshopShopgroupOnly> shopgruppen = new ArrayList<WebshopShopgroupOnly>() ;
		while(shopgruppeBlatt != null) {
			if(!existsShopgroupIIdForWebshopIId(shopgruppeBlatt)) break ;

			try {
				// Kann keine Exception brauchen
				Shopgruppe sg = em.find(Shopgruppe.class, shopgruppeBlatt) ;
				if(null == sg) break ; 

				ShopgruppeDto sgDto = getArtikelFac().shopgruppeFindByPrimaryKey(shopgruppeBlatt, webClientDto) ;				
				WebshopShopgroupOnly g = transformToWebshopgroupOnly(sgDto) ;
				shopgruppen.add(0, g) ;
				
				shopgruppeBlatt = sg.getShopgruppeIId() ;
			} catch(EJBExceptionLP ex) {
				break ;
			}			
		}
	
		return shopgruppen ;
	}

	protected ShopgroupResult retrieveWebShopgroup(ShopgruppeDto sgDto, String key) throws RemoteException {
		if(!existsShopgroupIIdForWebshopIId(sgDto.getIId()))
			return new ShopgroupResult(ShopgroupResult.ERROR_NOT_FOUND, key) ;
		
		WebshopShopgroup node = buildWebshopgroup(sgDto);
		
		ShopgroupResult sgResult = new ShopgroupResult() ;
		sgResult.setWebshopGroup(node) ;
		sgResult.setOkay() ;
		return sgResult;
	}

	
	protected boolean existsShopgroupIIdForWebshopIId(Integer shopgruppeIId) {
		Query allowedForWebshopQuery = em
				.createNamedQuery(Shopgruppewebshop.QueryFindByShopgruppeIIdWebshopIId) ;
		allowedForWebshopQuery.setParameter("shopgruppeIId", shopgruppeIId) ;
		allowedForWebshopQuery.setParameter("webshopIId", getWebshopIId()) ;
		
		try {
			allowedForWebshopQuery.getSingleResult() ;
			return true ;
		} catch(NoResultException e) {						
		}
		
		return false ;
	}

	private WebshopShopgroup transformToWebshopgroup(ShopgruppeDto shopGruppeDto) {
		WebshopShopgroup webShopgroup = new WebshopShopgroup() ;
		webShopgroup.setId(shopGruppeDto.getIId()) ;
		webShopgroup.setCnr(shopGruppeDto.getCNr()) ;
		webShopgroup.setName(shopGruppeDto.getBezeichnung()) ;
		webShopgroup.setSortValue(shopGruppeDto.getISort()) ;
		return webShopgroup ;
	}

	private WebshopShopgroupOnly transformToWebshopgroupOnly(ShopgruppeDto shopGruppe) {
		WebshopShopgroupOnly webShopgroup = new WebshopShopgroupOnly() ;
		webShopgroup.setId(shopGruppe.getIId()) ;
		webShopgroup.setCnr(shopGruppe.getCNr()) ;
		webShopgroup.setName(shopGruppe.getBezeichnung()) ;
		webShopgroup.setSortValue(shopGruppe.getISort()) ; 
		return webShopgroup ;
	}
	
	private WebshopShopgroupOnly transformToWebshopgroup(Shopgruppe shopGruppe) {
		WebshopShopgroupOnly webShopgroup = new WebshopShopgroupOnly() ;
		webShopgroup.setId(shopGruppe.getIId()) ;
		webShopgroup.setCnr(shopGruppe.getCNr()) ;
		webShopgroup.setName(shopGruppe.getCNr()) ;
		webShopgroup.setSortValue(shopGruppe.getISort()) ; 
		return webShopgroup ;
	}
	

	private WebshopShopgroupFlat transformToWebshopgroupFlat(ShopgruppeDto shopGruppe) {
		WebshopShopgroupFlat node = new WebshopShopgroupFlat() ;
		node.setId(shopGruppe.getIId()) ;
		node.setBez(shopGruppe.getBezeichnung()) ;
		node.setCnr(shopGruppe.getCNr()) ;
		node.setParentId(shopGruppe.getShopgruppeIId()) ;
		node.setSortValue(shopGruppe.getISort()) ;
		return node ;	
	}

	protected WebshopShopgroupFlat buildWebshopgroupFlat(ShopgruppeDto shopGruppe) {
		return transformToWebshopgroupFlat(shopGruppe) ;
	}

	protected WebshopShopgroup buildWebshopgroup(ShopgruppeDto sgDto) throws RemoteException {
		Collection<Shopgruppe> childs = queryChildShopgruppen(sgDto.getIId());
		
		List<WebshopShopgroupOnly> webChilds = new ArrayList<WebshopShopgroupOnly>() ;
		for (Shopgruppe childShopgruppe : childs) {			
			if(existsShopgroupIIdForWebshopIId(childShopgruppe.getIId())) {
				webChilds.add(transformToWebshopgroup(childShopgruppe));
			}
		}
		
		WebshopShopgroup node = transformToWebshopgroup(sgDto) ;
		node.setChildShopgroup(webChilds) ;

		buildItemReferenceInfo(node, sgDto.getArtikelIId()) ;
		buildSeoInfo(node, sgDto.getArtikelIId()) ;
		
		List<WebshopItemReference> items = retrieveItemsInShopgroup(webClientDto.getMandant(), sgDto.getIId()) ;
		node.setItems(items) ;
		
		return node;
	}

	
	protected WebshopShopgroup buildWebshopgroup(ShopgruppeDto sgDto, Timestamp changedStamp) throws RemoteException {
		Collection<Shopgruppe> childs = queryChildShopgruppen(sgDto.getIId());

		List<Shopgruppe> changedShopgruppen = new ArrayList<Shopgruppe>() ;
		for (Shopgruppe childShopgruppe : childs) {
			if(childShopgruppe.getTAendern().after(changedStamp)) {
				changedShopgruppen.add(childShopgruppe) ;
				continue ;
			}

			Collection<Artikel> artikelCl = artikelFindByShopgroupIId(childShopgruppe.getIId(), changedStamp) ;
			if(artikelCl.size() > 0) {
				changedShopgruppen.add(childShopgruppe) ;
				continue ;				
			}
		}
		
		List<WebshopShopgroupOnly> webChilds = new ArrayList<WebshopShopgroupOnly>() ;
		for (Shopgruppe childShopgruppe : changedShopgruppen) {
			if(existsShopgroupIIdForWebshopIId(childShopgruppe.getIId())) {
				webChilds.add(transformToWebshopgroup(childShopgruppe));
			}
		}
		
		WebshopShopgroup node = transformToWebshopgroup(sgDto) ;
		node.setChildShopgroup(webChilds) ;

		buildItemReferenceInfo(node, sgDto.getArtikelIId()) ;
		buildSeoInfo(node, sgDto.getArtikelIId()) ;
		
		List<WebshopItemReference> items = retrieveItemsInShopgroup(webClientDto.getMandant(), sgDto.getIId(), changedStamp) ;
		node.setItems(items) ;
		
		return node;
	}	
	
	
	/**
	 * Stellt die Kurzinfo f&uuml;r einen Artikel zusammen (CNR, IID)
	 * @param artikelIId ist die IID des gesuchten Artikels
	 */
//	protected WebshopItemReference retrieveItemReferenceInfo(Integer artikelIId) {
//		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, webClientDto) ;
//		WebshopItemReference itemRef = new WebshopItemReference(artikelDto.getCNr(), artikelDto.getIId()) ;
//		
//		return itemRef;
//	}

	protected void buildItemReferenceInfo(WebshopShopgroup shopgroup, Integer artikelIId) throws RemoteException {
		if(artikelIId == null) return ;
		
		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, webClientDto) ;
		if(artikelDto.getShopgruppeIId() != null) {
			WebshopItemReference itemRef = new WebshopItemReference(artikelDto.getCNr(), artikelDto.getIId()) ;
			shopgroup.setReferenceItem(itemRef) ;
		}
		
		WebshopItemReferenceInfo refInfo = new WebshopItemReferenceInfo() ;
		shopgroup.setReferenceItemInfo(refInfo) ;

		// Kurzbeschreibung
		ArtikelsprDto sprDto = artikelDto.getArtikelsprDto() ;
		refInfo.setShortDescription(
				null != sprDto ? HelperWebshop.getEmptyStringForNull(sprDto.getCBez()) : artikelDto.getCNr()) ;

		List<BmeCatKommentar> kommentare = getArtikelkommentar(artikelDto.getIId()) ;
		
		ArtikelkommentarsprDto descKommentarDto = findLongDescription(kommentare) ;
		if(descKommentarDto != null) {
			refInfo.setLongDescription(HelperWebshop.unescapeHtml(descKommentarDto.getXKommentar())) ;
		}
		
		ArtikelkommentarsprDto remarkKommentarDto = findRemark(kommentare) ;
		if(remarkKommentarDto != null) {
			refInfo.setRemark(HelperWebshop.unescapeHtml(remarkKommentarDto.getXKommentar())) ;
		}
		
		ArtikelkommentarDto kommentarDto = findImage(kommentare) ;
		if(kommentarDto != null) {
			refInfo.setImage(kommentarDto.getArtikelkommentarsprDto().getOMedia()) ;
			
			WebshopItemMimeInfo mimeInfo = new WebshopItemMimeInfo() ;
			refInfo.setMimeInfo(mimeInfo) ;
			mimeInfo.setMimeSource(kommentarDto.getArtikelkommentarsprDto().getCDateiname()) ;
			mimeInfo.setMimeType(kommentarDto.getDatenformatCNr()) ;
		}	
	}
	
	protected void buildSeoInfo(WebshopShopgroup shopgroup, Integer artikelIId) throws RemoteException {
		if(artikelIId == null) return ;

		// Kompatibilitaet bewahren
		ISeoInfo seoInfo = getArtikelkommentarSeo(new WebshopSeoInfo(), artikelIId) ;
		shopgroup.setSeoInfo((WebshopSeoInfo) seoInfo) ;
	}

	protected void buildSeoInfo(WebshopItem shopItem, Integer artikelIId) throws RemoteException {
		if(artikelIId == null) return ;

		// Kompatibilitaet bewahren
		ISeoInfo seoInfo = getArtikelkommentarSeo(new WebshopSeoInfo(), artikelIId) ;
		shopItem.setSeoInfo((WebshopSeoInfo) seoInfo) ;
	}
	
	private Collection<Artikel> artikelFindByShopgroupIId(Integer shopgruppeIId, Timestamp changedStamp) {
		return ArtikelQuery.listByMandantShopgruppeIIdDate(em, webClientDto.getMandant(), shopgruppeIId, changedStamp) ;
	}
	
	private Collection<Artikel> artikelFindByShopgroupIId(Integer shopgruppeIId) {
		return ArtikelQuery.listByMandantShopgruppeIId(em, webClientDto.getMandant(), shopgruppeIId) ;
	}
	
	/**
	 * Eine Liste Artikel in der gegebenen Shopgruppe liefern
	 * @param mandant
	 * @param shopgroupIId
	 * @return Liste der Artikel
	 */
	protected List<WebshopItemReference> retrieveItemsInShopgroup(String mandant, Integer shopgroupIId) {
		List<WebshopItemReference> webItems = new ArrayList<WebshopItemReference>() ;
		
		Collection<Artikel> items = ArtikelQuery.listByMandantShopgruppeIId(em, mandant, shopgroupIId) ;
		for (Artikel artikel : items) {
			webItems.add(new WebshopItemReference(artikel.getCNr(), artikel.getIId())) ;
		}
		
		List<Artikelshopgruppe> artikelShopgruppen = ArtikelshopgruppeQuery.listByShopgruppeIId(em, shopgroupIId) ;
		for (Artikelshopgruppe artikelshopgruppe : artikelShopgruppen) {
			Artikel item = em.find(Artikel.class, artikelshopgruppe.getArtikelIId()) ;
			webItems.add(new WebshopItemReference(item.getCNr(), item.getIId())) ;
		}
		return webItems ;
	}
	

	protected List<WebshopItemReference> retrieveItemsInShopgroup(String mandant, Integer shopgroupIId, Timestamp changedStamp) {
		List<WebshopItemReference> webItems = new ArrayList<WebshopItemReference>() ;
		
		Collection<Artikel> items = ArtikelQuery.listByMandantShopgruppeIIdDate(em, mandant, shopgroupIId, changedStamp) ;
		for (Artikel artikel : items) {
			WebshopItemReference itemRef = new WebshopItemReference(artikel.getCNr(), artikel.getIId()) ;
			webItems.add(itemRef) ;
		}

		List<Artikelshopgruppe> artikelShopgruppen = ArtikelshopgruppeQuery.listByShopgruppeIIdDate(em, shopgroupIId, changedStamp) ;
		for (Artikelshopgruppe artikelshopgruppe : artikelShopgruppen) {
			Artikel item = em.find(Artikel.class, artikelshopgruppe.getArtikelIId()) ;
			webItems.add(new WebshopItemReference(item.getCNr(), item.getIId())) ;
		}
		
		return webItems ;
	}
}
