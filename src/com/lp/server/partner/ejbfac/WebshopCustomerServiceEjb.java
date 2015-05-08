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
package com.lp.server.partner.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;

import com.lp.server.artikel.ejb.Webshop;
import com.lp.server.artikel.service.BaseRequestResult;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.partner.ejb.BmeCatTransformerKunde;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.HeliumSimpleAuthController;
import com.lp.server.system.service.HeliumSimpleAuthException;
import com.lp.server.system.service.WebshopAuthHeader;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperWebshop;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class WebshopCustomerServiceEjb extends Facade implements
		WebshopCustomerServiceFacLocal {

	@PersistenceContext
	private EntityManager em;
	
	private HeliumSimpleAuthController authController = null ; 
	private String cachedWebshopName = null ;
	private Integer cachedWebshopId = null ;
	private Map<Integer, String> cachedPricelists = null ;
	
	protected HeliumSimpleAuthController getAuthController() {
		if(null == authController) {
			authController = new HeliumSimpleAuthController(getBenutzerFac(), getMandantFac()) ;
			cachedWebshopName = null ;
			cachedWebshopId = null ;
			cachedPricelists = null ;
		}

		return authController ;
	}	
	
	private void setupSessionParams(WebshopAuthHeader header) throws HeliumSimpleAuthException {
		getAuthController().setupSessionParams(header) ;
		cachedWebshopName = header.getShopName() ;
		cachedWebshopId = null ;
	}
	
	private Integer getWebshopIId() {
		if(null == getAuthController().getWebClientDto()) return -1 ;
		if(null == cachedWebshopName) return -1 ;		
		if(null == cachedWebshopId) {
			try {
				HvTypedQuery<Webshop> query = new HvTypedQuery<Webshop>(
						em.createNamedQuery(Webshop.QueryFindByMandantCNrCBez)) ;
				query.setParameter(1, getAuthController().getWebClientDto().getMandant());
				query.setParameter(2, cachedWebshopName) ; 

				cachedWebshopId = query.getSingleResult().getIId();
			} catch (NoResultException ex) {
			}
		}

		return cachedWebshopId  ;
	}	
	
	private Map<Integer, String> getPricelistCache() {
		if(null == cachedPricelists) {
			cachedPricelists = new HashMap<Integer, String>() ;
		}
		return cachedPricelists ;
	}
	
	private String getPricelistName(Integer pricelistId) {
		if(!getPricelistCache().containsKey(pricelistId)) {
			try {
				VkpfartikelpreislisteDto vkpfartikelpreislisteDto = null;
				vkpfartikelpreislisteDto = getVkPreisfindungFac().vkpfartikelpreislisteFindByPrimaryKey(pricelistId);
				
				getPricelistCache().put(pricelistId, vkpfartikelpreislisteDto.getCNr()) ;
			} catch(RemoteException e) {
			}
		}
		
		return getPricelistCache().get(pricelistId) ;
	}
	
	private void putPricelistIntoCache(Integer pricelistId) {
		getPricelistName(pricelistId) ;
	}
	
	@Override
	public WebshopCustomersResult getCustomers(WebshopAuthHeader header) {
		WebshopCustomersResult result = new WebshopCustomersResult() ;
		
		List<FLRKunde> flrCustomers = new ArrayList<FLRKunde>() ;
		Session session = null;
		try {
			setupSessionParams(header) ;

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			String sLocUI = Helper.locale2String(authController.getWebClientDto().getLocUi());
			session.enableFilter("filterLocale")
					.setParameter("paramLocale", sLocUI);

			String queryString = MessageFormat.format("select kunde FROM FLRKunde as kunde " +
					"left outer join kunde.flrpartner.partner_paselektion_set as paset " +
					"left outer join paset.flrselektion as s " +
					"WHERE s.b_webshop = 1 AND " +
					" kunde.mandant_c_nr = ''{0}'' " +
					" ORDER BY kunde.i_id", new Object[] {authController.getWebClientDto().getMandant()});
			
			Query query = session.createQuery(queryString);
			flrCustomers = query.list();
			addCustomerReferences(flrCustomers, result) ;
		
			result.setOkay() ;			
			return result ;
		} catch(HeliumSimpleAuthException e) {
			myLogger.error("HeliumSimpleAuthException (" + e.getErrorCode() + ")") ;
			return new WebshopCustomersResult(WebshopCustomersResult.ERROR_AUTHENTIFICATION) ;
		} catch(Exception e) {
			myLogger.error("Error (" + e.getMessage() + ")") ;
			return new WebshopCustomersResult(WebshopCustomersResult.ERROR_EJB_EXCEPTION) ;	
		} finally {
			HelperWebshop.closeFLRSession(session) ;		
		}
	}

	
	public WebshopCustomersResult getCustomersChangedOld(
			WebshopAuthHeader header, String changedDateTime) {
		WebshopCustomersResult result = new WebshopCustomersResult() ;
		
		if(HelperWebshop.isEmptyString(changedDateTime)) {
			return new WebshopCustomersResult(BaseRequestResult.ERROR_NULL_PARAMETER, "changedDateTime == null (or blank/empty)") ;
		}

		Date d = HelperWebshop.parseDateTimeString(changedDateTime) ;
		if(null == d) {
			return new WebshopCustomersResult(
				BaseRequestResult.ERROR_NULL_PARAMETER, "Date has illegal format [" + changedDateTime + "])") ;
		}
		
		try {
			setupSessionParams(header) ;

			Timestamp changedTimestamp = HelperWebshop.normalizeDateTimeAsTimestamp(
					d, getAuthController().getWebClientDto().getLocMandant()) ;
				
			List<KundeDto> customers = getKundeFac()
					.kundeFindByMandantCnr(authController.getWebClientDto()) ;
			for (KundeDto customer : customers) {
				boolean changed = customer.getTAendern().after(changedTimestamp) ;
				
				if(!changed) {
					Partner p = em.find(Partner.class, customer.getPartnerIId()) ;
					changed = p.getTAendern().after(changedTimestamp) ;
				}
				
				if(changed) {
					result.getCustomers().add(createReference(customer)) ;					
				}
			}
			
			result.setOkay() ;			
			return result ;
		} catch(HeliumSimpleAuthException e) {
			myLogger.error("HeliumSimpleAuthException (" + e.getErrorCode() + ")") ;
			return new WebshopCustomersResult(WebshopCustomersResult.ERROR_AUTHENTIFICATION) ;
		}
	}

	@Override
	public WebshopCustomersResult getCustomersChanged(
			WebshopAuthHeader header, String changedDateTime) {
		WebshopCustomersResult result = new WebshopCustomersResult() ;
		
		if(HelperWebshop.isEmptyString(changedDateTime)) {
			return new WebshopCustomersResult(BaseRequestResult.ERROR_NULL_PARAMETER, "changedDateTime == null (or blank/empty)") ;
		}

		Date d = HelperWebshop.parseDateTimeString(changedDateTime) ;
		if(null == d) {
			return new WebshopCustomersResult(
				BaseRequestResult.ERROR_NULL_PARAMETER, "Date has illegal format [" + changedDateTime + "])") ;
		}
		
		List<FLRKunde> flrCustomers = new ArrayList<FLRKunde>() ;
		Session session = null;

		try {
			setupSessionParams(header) ;

			Timestamp changedTimestamp = HelperWebshop.normalizeDateTimeAsTimestamp(
					d, getAuthController().getWebClientDto().getLocMandant()) ;

			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			String sLocUI = Helper.locale2String(authController.getWebClientDto().getLocUi());
			session.enableFilter("filterLocale")
					.setParameter("paramLocale", sLocUI);

			String ts = Helper.formatTimestampWithSlashes(changedTimestamp) ;
			String queryString = MessageFormat.format("select kunde FROM FLRKunde as kunde " +
					"left outer join kunde.flrpartner.partner_paselektion_set as paset " +
					"left outer join paset.flrselektion as s " +
					"WHERE s.b_webshop = 1 " +
					" AND kunde.mandant_c_nr = ''{0}''"+
					" AND (kunde.t_aendern > ''{1}'' OR kunde.flrpartner.t_aendern > ''{2}'') " +
					" ORDER BY kunde.i_id", new Object[]{authController.getWebClientDto().getMandant(), ts, ts}) ;

			Query query = session.createQuery(queryString);
			flrCustomers = query.list();
			addCustomerReferences(flrCustomers, result) ;
			
			result.setOkay() ;						
			return result ;
		} catch(HeliumSimpleAuthException e) {
			myLogger.error("HeliumSimpleAuthException (" + e.getErrorCode() + ")") ;
			return new WebshopCustomersResult(WebshopCustomersResult.ERROR_AUTHENTIFICATION) ;
		} catch(Exception e) {
			myLogger.error("Error (" + e.getMessage() + ")") ;
			return new WebshopCustomersResult(WebshopCustomersResult.ERROR_EJB_EXCEPTION) ;		
		} finally {
			HelperWebshop.closeFLRSession(session) ;		
		}
	}
	
	private WebshopCustomerReference createReference(KundeDto kundeDto) {
		return new WebshopCustomerReference(
			kundeDto.getPartnerDto().getCKbez(), kundeDto.getIId(), kundeDto.getCIdExtern()) ;
	}
	
	private void addCustomerReferences(List<FLRKunde> flrCustomers, WebshopCustomersResult result) {
		Integer lastCustomerId = null ;
		for (FLRKunde flrCustomer : flrCustomers) {
			if(flrCustomer.getI_id().equals(lastCustomerId)) continue ; // JOIN liefert mehrere Saetze pro KundeIId
			lastCustomerId = flrCustomer.getI_id() ;
			
			// Keine Adressen/Kunden ohne E-Mail Adresse
			if(HelperWebshop.isEmptyString(flrCustomer.getFlrpartner().getC_email())) continue ;
			
			result.getCustomers().add(
				new WebshopCustomerReference(
					flrCustomer.getFlrpartner().getC_kbez(), flrCustomer.getI_id(), flrCustomer.getC_id_extern())) ;
		}
	}
	
	@Override
	public WebshopCustomerResult getCustomerById(WebshopAuthHeader header, Integer id) {
		if(null == id) {
			return new WebshopCustomerResult(BaseRequestResult.ERROR_NULL_PARAMETER, "id == null") ;
		}
		
		WebshopCustomerResult result = new WebshopCustomerResult() ;
		try {
			setupSessionParams(header) ;

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKeyOhneExc(
					id, getAuthController().getWebClientDto()) ;
			if(kundeDto != null) {
				AnsprechpartnerDto[] kontaktDtos = getAnsprechpartnerFac()
						.ansprechpartnerFindByPartnerIId(kundeDto.getPartnerIId(), getAuthController().getWebClientDto()) ;
				for (AnsprechpartnerDto ansprechpartnerDto : kontaktDtos) {
					ansprechpartnerDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(ansprechpartnerDto.getPartnerIIdAnsprechpartner(), getAuthController().getWebClientDto())) ;
				}
				
				putPricelistIntoCache(kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste()) ;
				BmeCatTransformerKunde transformer = new BmeCatTransformerKunde(getPricelistCache()) ;
				Document d = transformer.transform(kundeDto, kontaktDtos) ;
				result.setBmecatParties(transformer.asString(d)) ;
				result.setOkay() ; 
			} else {
				result.setRc(BaseRequestResult.ERROR_NOT_FOUND) ;
				result.setDescription(id.toString()) ;
			}
		} catch(HeliumSimpleAuthException e) {
			myLogger.error("HeliumSimpleAuthException (" + e.getErrorCode() + ")") ;
			return new WebshopCustomerResult(WebshopCustomerResult.ERROR_AUTHENTIFICATION) ;			
		} catch(EJBExceptionLP e) {
			return new WebshopCustomerResult(WebshopCustomerResult.ERROR_EJB_EXCEPTION + e.getCode()) ;			
		} catch(RemoteException e) {
			return new WebshopCustomerResult(WebshopCustomerResult.ERROR_RMI_EXCEPTION) ;						
		}
		
		return result ;
	}

	@Override
	public WebshopPricelistResult getPricelistById(WebshopAuthHeader header, Integer id) {
		if(null == id) {
			return new WebshopPricelistResult(BaseRequestResult.ERROR_NULL_PARAMETER, "id == null") ;
		}

		WebshopPricelistResult result = new WebshopPricelistResult() ;
		try {
			setupSessionParams(header) ;

			VkpfartikelpreislisteDto vkpfartikelpreislisteDto = null;
			vkpfartikelpreislisteDto = getVkPreisfindungFac().vkpfartikelpreislisteFindByPrimaryKey(id);
			if(!getWebshopIId().equals(vkpfartikelpreislisteDto.getWebshopIId())) {
				return new WebshopPricelistResult(BaseRequestResult.ERROR_NOT_FOUND) ;
			}

			if(!vkpfartikelpreislisteDto.isPreislisteaktiv()) {
				return new WebshopPricelistResult(BaseRequestResult.ERROR_NOT_FOUND) ;
			}

			result.setPricelist(new WebshopPricelist(id, vkpfartikelpreislisteDto.getCNr())) ;
			result.setOkay() ;
		} catch(HeliumSimpleAuthException e) {
			myLogger.error("HeliumSimpleAuthException (" + e.getErrorCode() + ")") ;
			return new WebshopPricelistResult(WebshopPricelistResult.ERROR_AUTHENTIFICATION) ;			
		} catch(RemoteException e) {
			return new WebshopPricelistResult(WebshopPricelistResult.ERROR_RMI_EXCEPTION) ;			
			
		} catch(EJBExceptionLP e) {
			return new WebshopPricelistResult(WebshopPricelistResult.ERROR_EJB_EXCEPTION + e.getCode()) ;			
		}
		
		return result ;
	}
	
	@Override
	public WebshopPricelistsResult getPricelists(WebshopAuthHeader header) {
		WebshopPricelistsResult result = new WebshopPricelistsResult() ;
		try {
			setupSessionParams(header) ;

			VkpfartikelpreislisteDto dtos[] = getVkPreisfindungFac().vkpfartikelpreislisteFindByMandantCNr(
					getAuthController().getWebClientDto().getMandant()) ;

			List<WebshopPricelist> pricelists = new ArrayList<WebshopPricelist>() ;
			for (VkpfartikelpreislisteDto dto : dtos) {
				if(dto.isPreislisteaktiv() && getWebshopIId().equals(dto.getWebshopIId())) {
					pricelists.add(new WebshopPricelist(dto.getIId(), dto.getCNr())) ;
				}
			}
			result.setPricelists(pricelists) ;
			result.setOkay() ;
		} catch(HeliumSimpleAuthException e) {
			myLogger.error("HeliumSimpleAuthException (" + e.getErrorCode() + ")") ;
			return new WebshopPricelistsResult(WebshopPricelistResult.ERROR_AUTHENTIFICATION) ;			
		} catch(RemoteException e) {
			return new WebshopPricelistsResult(WebshopPricelistResult.ERROR_RMI_EXCEPTION) ;						
		} catch(EJBExceptionLP e) {
			return new WebshopPricelistsResult(WebshopPricelistResult.ERROR_EJB_EXCEPTION + e.getCode()) ;			
		} catch(Exception e) {
			return new WebshopPricelistsResult(WebshopPricelistResult.ERROR_NOT_INITIALIZED, e.getMessage()) ;						
		}
		
		return result ;
	}
}
