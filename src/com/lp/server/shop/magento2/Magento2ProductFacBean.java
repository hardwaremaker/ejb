package com.lp.server.shop.magento2;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.ArtikelQuery;
import com.lp.server.artikel.ejb.Shopgruppewebshop;
import com.lp.server.artikel.ejb.ShopgruppewebshopQuery;
import com.lp.server.artikel.ejb.WebshopShopgruppe;
import com.lp.server.artikel.ejb.WebshopShopgruppeQuery;
import com.lp.server.artikel.ejbfac.PriceInfo;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.artikel.service.WebshopArtikelDto;
import com.lp.server.artikel.service.WebshopArtikelPreislisteDto;
import com.lp.server.artikel.service.WebshopItemReference;
import com.lp.server.artikel.service.WebshopItemsResult;
import com.lp.server.artikel.service.WebshopMwstsatzbezDto;
import com.lp.server.artikel.service.WebshopShopgruppeDto;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.MwstsatzbezId;
import com.lp.server.util.PreislisteId;
import com.lp.server.util.ShopgruppeId;
import com.lp.server.util.WebshopId;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.collection.ISelect;
import com.lp.util.Helper;

@Stateless
public class Magento2ProductFacBean extends Magento2BaseFacBean implements Magento2ProductFacLocal {
	@PersistenceContext
	private EntityManager em;
	private Set<String> productNames;
	private WebshopArtikelpreislisteCache webshopPreislistenCache;

	@EJB
	private Magento2ApiFacLocal magento2Api;
	
	private Set<String> getProductNames() {
		if (productNames == null) {
			productNames = new HashSet<String>();
		}
		return productNames;
	}

	private boolean addProductName(String name) {
		return getProductNames().add(buildUrlKey(name));
	}

	private boolean existsProductName(String name) {
		return getProductNames().contains(buildUrlKey(name));
	}

	@Override
	public void pushTaxClasses(WebshopId shopId, TheClientDto theClientDto) {
		MwstsatzbezDto[] bezDtos = getMandantFac().mwstsatzbezFindAllByMandantAsDto(theClientDto.getMandant(),
				theClientDto);
		for (MwstsatzbezDto mwstsatzbezDto : bezDtos) {
			if (!mwstsatzbezDto.getBHandeingabe()) {
				getMagento2ProductFacLocal().pushTaxClass(shopId, mwstsatzbezDto, theClientDto);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void pushTaxClass(WebshopId shopId, MwstsatzbezDto satzbezDto, TheClientDto theClientDto) {
		MwstsatzbezId bezId = new MwstsatzbezId(satzbezDto.getIId());
		WebshopMwstsatzbezDto wsbzeDto = getArtikelFac().webshopMwstsatzbezFindByShopMwstsatzbezNoExc(shopId, bezId);
		if (wsbzeDto == null) {
			MagTaxClass taxClass = createTaxClass(satzbezDto, shopId, theClientDto);
			if (taxClass == null) {
				return;
			}

			try {
				MagTaxClass resultTaxClass = magento2Api.postTaxClass(shopId, taxClass);
				if (resultTaxClass != null) {
					wsbzeDto = new WebshopMwstsatzbezDto();
					wsbzeDto.setExternalId(resultTaxClass.getClassId().toString());
					wsbzeDto.setWebshopId(shopId);
					wsbzeDto.setMwstsatzbezId(bezId);
					getArtikelFac().createWebshopMwstsatzbez(wsbzeDto);

					myLogger.warn("TAXCLASS SYNCED '" + resultTaxClass.getClassName() + "', id:" + bezId.id()
							+ ", externalId:" + wsbzeDto.getExternalId());
				} else {
					myLogger.error("Couldn't post taxclass " + satzbezDto.getCBezeichnung() + ", id:"
							+ satzbezDto.getIId() + ".");
				}
			} catch (JsonProcessingException e) {
				myLogger.error("JsonProcessing", e);
			} catch (UnsupportedEncodingException e) {
				myLogger.error("UnsupportedEncodingException", e);
			} catch (URISyntaxException e) {
				myLogger.error("URISyntaxException", e);
			}
		} else {

		}
	}

	private MagTaxClass createTaxClass(MwstsatzbezDto satzbezDto, WebshopId shopId, TheClientDto theClientDto) {
		MagTaxClass taxClass = new MagTaxClass();
		taxClass.setClassName("HV_" + satzbezDto.getCBezeichnung());
		taxClass.setClassType("PRODUCT");
		return taxClass;
	}


	@Override
	public void pushCustomerGroups(WebshopId shopId, TheClientDto theClientDto) {
		try {
			List<VkpfartikelpreislisteDto> entries = getWebshopPreislisten(shopId, theClientDto);
			for (VkpfartikelpreislisteDto preislisteDto : entries) {
				getMagento2ProductFacLocal().pushCustomerGroup(shopId, preislisteDto, theClientDto);
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	private List<VkpfartikelpreislisteDto> getWebshopPreislisten(WebshopId shopId, TheClientDto theClientDto)
			throws RemoteException {
		List<VkpfartikelpreislisteDto> entries = new ArrayList<VkpfartikelpreislisteDto>();
		VkpfartikelpreislisteDto[] dtos = getVkPreisfindungFac().getAlleAktivenPreislisten(Helper.getShortTrue(),
				theClientDto);
		for (VkpfartikelpreislisteDto preislisteDto : dtos) {
			if (shopId.id().equals(preislisteDto.getWebshopIId())) {
				entries.add(preislisteDto);
			}
		}

		return entries;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void pushCustomerGroup(WebshopId shopId, VkpfartikelpreislisteDto preislisteDto, TheClientDto theClientDto) {
		PreislisteId preislisteId = new PreislisteId(preislisteDto.getIId());
		WebshopArtikelPreislisteDto wpDto = getArtikelFac().webshopPreislisteFindByShopPreislisteNoExc(shopId,
				preislisteId);
		if (wpDto == null) {
			MagCustomerGroup customerGroup = createCustomerGroup(preislisteDto, shopId, theClientDto);
			if (customerGroup == null) {
				return;
			}

			try {
				MagCustomerGroup resultGroup = magento2Api.postCustomerGroup(shopId, customerGroup);
				if (resultGroup != null) {
					wpDto = new WebshopArtikelPreislisteDto();
					wpDto.setExternalId(resultGroup.getId().toString());
					wpDto.setWebshopId(shopId);
					wpDto.setPreislisteId(preislisteId);
					getArtikelFac().createWebshopPreisliste(wpDto);

					myLogger.warn("CUSTOMERGROUP SYNCED '" + resultGroup.getCode() + "', id:" + preislisteId.id()
							+ ", externalId:" + wpDto.getExternalId());
				} else {
					myLogger.error("Couldn't post customerGroup " + preislisteDto.getCNr() + ", id:"
							+ preislisteDto.getIId() + ".");
				}
			} catch (JsonProcessingException e) {
				myLogger.error("JsonProcessing", e);
			} catch (UnsupportedEncodingException e) {
				myLogger.error("UnsupportedEncodingException", e);
			} catch (URISyntaxException e) {
				myLogger.error("URISyntaxException", e);
			}
		}
	}

	private MagCustomerGroup createCustomerGroup(VkpfartikelpreislisteDto preislisteDto, WebshopId shopId,
			TheClientDto theClientDto) {
		MagCustomerGroup cg = new MagCustomerGroup();
		cg.setCode(preislisteDto.getCNr());
		return cg;
	}


	@Override
	public void pushItems(WebshopId shopId, TheClientDto theClientDto) {
		try {
			List<Shopgruppewebshop> groups = ShopgruppewebshopQuery.listByWebshopId(em, shopId.id());
			List<VkpfartikelpreislisteDto> pricelists = getWebshopPreislisten(shopId, theClientDto);

			for (Shopgruppewebshop group : groups) {
				List<Artikel> items = ArtikelQuery.listByMandantShopgruppeIId(em, theClientDto.getMandant(),
						group.getShopgruppeIId());

				for (Artikel item : items) {
					getMagento2ProductFacLocal().pushItem(shopId, pricelists, new ArtikelId(item.getIId()),
							theClientDto);
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	@Override
	public void pushChangedItems(WebshopId shopId, 
			Timestamp lastChanged, TheClientDto theClientDto) {
		if (lastChanged == null) {
			lastChanged = new Timestamp(0l);
			lastChanged = new Timestamp(118, 0, 1, 0, 0, 0, 0);
		}
		
		try {
			List<VkpfartikelpreislisteDto> pricelists = 
					getWebshopPreislisten(shopId, theClientDto);

			WebshopItemsResult result = getWebshopItemServiceFac()
					.getItemsRestChanged(theClientDto, shopId, lastChanged);
			Collection<WebshopItemReference> itemsInShop = selectItemsInWebshop(
					result.getItems(), shopId, theClientDto);
			
			Collection<WebshopItemReference> itemsChangedByShopgruppe = 
					findItemsChangedByShopgruppen(shopId, lastChanged, theClientDto);
			Collection<WebshopItemReference> acceptedItems =
					mergeItemsChanged(
						itemsInShop, itemsChangedByShopgruppe);
			for (WebshopItemReference each : acceptedItems) {
				getMagento2ProductFacLocal().pushItem(shopId,
						pricelists, new ArtikelId(each.getId()), theClientDto);
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	private Collection<WebshopItemReference> selectItemsInWebshop(
			Collection<WebshopItemReference> items, final WebshopId shopId, TheClientDto theClientDto) {
		return CollectionTools.select(items, new ISelect<WebshopItemReference>() {
			@Override
			public boolean select(WebshopItemReference element) {
				Artikel a = em.find(Artikel.class, element.getId());
				if(a.getShopgruppeIId() == null) return false;
				WebshopShopgruppe wssg = WebshopShopgruppeQuery.findByShopIdShopgruppeId(em,
						shopId, new ShopgruppeId(a.getShopgruppeIId()));
				return wssg != null;
			}
		});
	}
	
	private Collection<WebshopItemReference> findItemsChangedByShopgruppen(
			WebshopId shopId, Timestamp lastChanged, TheClientDto theClientDto) {
		List<WebshopShopgruppe> changedShopgruppen = 
				WebshopShopgruppeQuery
					.listByShopIdChangedDate(em, shopId, lastChanged);
		Collection<WebshopItemReference> items = new ArrayList<WebshopItemReference>();
		for (WebshopShopgruppe wssg : changedShopgruppen) {
			Collection<Artikel> artikelCl = artikelFindByShopgroupIId(
					wssg.getShopgruppeIId(), theClientDto) ;
			for (Artikel artikel : artikelCl) {
				WebshopItemReference itemRef = 
						new WebshopItemReference(artikel.getCNr(), artikel.getIId()) ;
				items.add(itemRef) ;
			}				
		}

		return items;
	}
	
	private Collection<WebshopItemReference> mergeItemsChanged(
			Collection<WebshopItemReference> m1, Collection<WebshopItemReference> m2) {
		for (WebshopItemReference ref : m2) {
			if(!m1.contains(ref)) {
				m1.add(ref);
			}
		}
		
		return m1;
	}

	private Collection<Artikel> artikelFindByShopgroupIId(Integer shopgruppeIId, TheClientDto theClientDto) {
		return ArtikelQuery.listByMandantShopgruppeIId(
				em, theClientDto.getMandant(), shopgruppeIId) ;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void pushItem(WebshopId shopId, List<VkpfartikelpreislisteDto> pricelists, ArtikelId itemId,
			final TheClientDto theClientDto) throws RemoteException {
		ArtikelDto itemDto = getArtikelFac().artikelFindByPrimaryKey(itemId.id(), theClientDto);
		ArtikelkommentarDto[] commentsDto = getArtikelkommentarFac().artikelkommentarFindByArtikelIId(itemId.id(),
				theClientDto);
		webshopPreislistenCache = new WebshopArtikelpreislisteCache(shopId);

		WebshopArtikelDto waDto = getArtikelFac().webshopArtikelFindByShopArtikelNoExc(shopId, itemId);
		if (waDto == null) {
			try {
				MagProduct product = pushItemNew(waDto, itemId, itemDto, commentsDto, pricelists, shopId, theClientDto);
			} catch (JsonProcessingException e) {
				myLogger.error("JsonProcessing", e);
			} catch (UnsupportedEncodingException e) {
				myLogger.error("UnsupportedEncodingException", e);
			} catch (URISyntaxException e) {
				myLogger.error("URISyntaxException", e);
			}
		} else {
			try {
				pushItemUpdate(waDto, itemId, itemDto, commentsDto, pricelists, shopId, theClientDto);
			} catch (JsonProcessingException e) {
				myLogger.error("JsonProcessing", e);
			} catch (URISyntaxException e) {
				myLogger.error("URISyntaxError", e);
			} catch (UnsupportedEncodingException e) {
				myLogger.error("UnsupportedEncodingException", e);
			}
		}
	}

	private MagProduct pushItemUpdate(WebshopArtikelDto waDto, ArtikelId itemId, ArtikelDto itemDto,
			ArtikelkommentarDto[] commentsDto, List<VkpfartikelpreislisteDto> pricelists, WebshopId shopId,
			TheClientDto theClientDto)
			throws RemoteException, UnsupportedEncodingException, URISyntaxException, JsonProcessingException {
		MagSearchProductResult products = magento2Api.searchProductByExternalId(shopId, waDto);
		if (products != null && products.getTotalCount() == 1) {
			MagProduct p = products.getItems().get(0);
			if (itemDto.getCNr().equals(p.getSku())) {
				p = magento2Api.getProduct(shopId, itemDto.getCNr(), theClientDto);
				p = updateProduct(p, shopId, itemDto, commentsDto, pricelists, theClientDto);

				p = magento2Api.putProduct(shopId, p);
				myLogger.warn("UPDATED PRODUCT SYNCED '" + p.getSku() + "', id:" + itemId.id() + ", externalId:"
						+ waDto.getExternalId());
				return p;
			} else {

			}
		}

		return null;
	}

	private MagProduct pushItemNew(WebshopArtikelDto waDto, ArtikelId itemId, ArtikelDto itemDto,
			ArtikelkommentarDto[] commentsDto, List<VkpfartikelpreislisteDto> preislisten, WebshopId shopId, TheClientDto theClientDto)
			throws RemoteException, JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
		MagProduct product = createProduct(itemDto, commentsDto, preislisten, shopId, theClientDto);
		if (product == null) {
			return null;
		}

		MagProduct resultProduct = magento2Api.postProduct(shopId, product);
		if (resultProduct != null) {
			waDto = new WebshopArtikelDto();
			waDto.setExternalId(resultProduct.getId().toString());
			waDto.setWebshopId(shopId);
			waDto.setArtikelId(itemId);
			getArtikelFac().createWebshopArtikel(waDto);

			addProductName(product.getName());

			myLogger.warn("NEW PRODUCT SYNCED '" + product.getSku() + "', id:" + itemId.id() + ", externalId:"
					+ waDto.getExternalId());

		} else {
			myLogger.error("Couldn't post product " + itemDto.getCNr() + ", id:" + itemDto.getIId() + ".");
		}

		return product;
	}

	private MagProduct updateProduct(MagProduct product, WebshopId shopId, ArtikelDto itemDto,
			ArtikelkommentarDto[] commentsDto, List<VkpfartikelpreislisteDto> preislisten, TheClientDto theClientDto)
			throws RemoteException {
		setTextAttributes(product, itemDto, commentsDto, theClientDto);
		setImageAttributes(product, itemDto, commentsDto, theClientDto);
		setMwstAttributes(product, shopId, itemDto, theClientDto);
		setPrices(product, itemDto, preislisten, theClientDto);
		setCategoryIdsAttributes(product, shopId, itemDto, theClientDto);
		
		return product;
	}

	private void setPrices(MagProduct product, ArtikelDto itemDto, List<VkpfartikelpreislisteDto> preislisten,
			TheClientDto theClientDto) throws RemoteException {
		List<PriceInfo> priceInfos = calculatePrices(itemDto, preislisten, Calendar.getInstance().getTime(),
				theClientDto);
		List<MagTierPriceEntry> magPrices = new ArrayList<MagTierPriceEntry>();

		for (PriceInfo priceInfo : priceInfos) {
			if (priceInfo.getPreislisteIId() == null) {
				product.setPrice(priceInfo.getPrice());
			} else {
				MagTierPriceEntry tier = new MagTierPriceEntry();
				tier.setQty(priceInfo.getAmount().intValue());
				tier.setValue(priceInfo.getPrice());
				String extId = webshopPreislistenCache.getValueOfKey(priceInfo.getPreislisteIId()).getExternalId();
				tier.setCustomerGroupId(Integer.valueOf(extId));
				magPrices.add(tier);
			}
		}
		product.setTierPrices(magPrices);
	}

	private List<PriceInfo> calculatePrices(ArtikelDto itemDto, List<VkpfartikelpreislisteDto> preislisten,
			Date theDate, TheClientDto theClientDto) throws RemoteException {
		List<PriceInfo> preisInfos = new ArrayList<PriceInfo>();

		java.sql.Date normalizedSqlDate = new java.sql.Date(normalizeDate(theDate, theClientDto).getTime());

		BigDecimal price = getVkPreisfindungFac().ermittlePreisbasis(itemDto.getIId(), normalizedSqlDate, null,
				theClientDto.getSMandantenwaehrung(), theClientDto);

		preisInfos.add(new PriceInfo(BigDecimal.ONE, price));

		for (VkpfartikelpreislisteDto vkpfartikelpreislisteDto : preislisten) {
			// if(isSokoPreisEnabled()) {
			// List<Kundesoko> kundesokos = KundesokoQuery
			// .listByArtikelIIdGueltigkeitsdatum(em, itemDto.getIId(), normalizedSqlDate) ;
			// preisInfos.addAll(calculateKundeSokoPrices(
			// kundesokos, itemDto, vkpfartikelpreislisteDto.getIId(), normalizedSqlDate)) ;
			//
			// kundesokos = KundesokoQuery
			// .listByArtgruIIdGueltigkeitsdatum(em, itemDto.getArtgruIId(),
			// normalizedSqlDate) ;
			// preisInfos.addAll(calculateKundeSokoPrices(
			// kundesokos, itemDto, vkpfartikelpreislisteDto.getIId(), normalizedSqlDate)) ;
			// }

			preisInfos.addAll(calculatePricelistPrices(itemDto, vkpfartikelpreislisteDto.getIId(), normalizedSqlDate,
					theClientDto));
		}

		return preisInfos;
	}

	private Date normalizeDate(Date changedDate, TheClientDto theClientDto) {
		Calendar cal = Calendar.getInstance(theClientDto.getLocMandant());
		cal.setTime(changedDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private List<PriceInfo> calculatePricelistPrices(ArtikelDto itemDto, Integer preislisteIId,
			java.sql.Date normalizedSqlDate, TheClientDto theClientDto) throws RemoteException {
		List<PriceInfo> preisInfos = new ArrayList<PriceInfo>();

		Integer mwstsatzbezIId = itemDto.getMwstsatzbezIId();

		VkpfMengenstaffelDto[] staffelnDto = getVkPreisfindungFac().vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
				itemDto.getIId(), normalizedSqlDate, preislisteIId, theClientDto);
		if (staffelnDto.length > 0) {
			for (VkpfMengenstaffelDto vkpfMengenstaffelDto : staffelnDto) {
				VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindungWeb(itemDto.getIId(),
						null, vkpfMengenstaffelDto.getNMenge(), normalizedSqlDate, preislisteIId, mwstsatzbezIId,
						theClientDto.getSMandantenwaehrung(), theClientDto);
				BigDecimal price = getPriceFromPreisfindung(vkpreisfindungDto);
				preisInfos.add(new PriceInfo(null, preislisteIId, vkpfMengenstaffelDto.getNMenge(), price));
			}
		}

		VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindungWeb(itemDto.getIId(), null,
				BigDecimal.ONE, normalizedSqlDate, preislisteIId, mwstsatzbezIId, theClientDto.getSMandantenwaehrung(),
				theClientDto);
		BigDecimal price = getPriceFromPreisfindung(vkpreisfindungDto);
		preisInfos.add(new PriceInfo(null, preislisteIId, BigDecimal.ONE, price));

		return preisInfos;
	}

	private BigDecimal getPriceFromPreisfindung(VkpreisfindungDto vkPreisDto) {
		BigDecimal p = getMinimumPrice(null, vkPreisDto.getVkpStufe3());
		// if(p != null) return p ;

		p = getMinimumPrice(p, vkPreisDto.getVkpStufe2());
		// if(p != null) return p ;

		p = getMinimumPrice(p, vkPreisDto.getVkpStufe1());
		// if(p != null) return p ;

		return getMinimumPrice(p, vkPreisDto.getVkpPreisbasis());
	}

	private BigDecimal getMinimumPrice(BigDecimal minimum, VerkaufspreisDto priceDto) {
		if (priceDto != null && priceDto.nettopreis != null) {
			return null == minimum ? priceDto.nettopreis : minimum.min(priceDto.nettopreis);
		}

		return minimum;
	}

//	private MagSearchProductResult searchProduct(WebshopId shopId, final WebshopArtikelDto waDto,
//			TheClientDto theClientDto) throws JsonProcessingException, URISyntaxException {
//		Map<String, String> q = new HashMap<String, String>();
//		q.put("searchCriteria[filter_groups][0][filters][0][field]", "entity_id");
//		q.put("searchCriteria[filter_groups][0][filters][0][value]", waDto.getExternalId());
//		q.put("searchCriteria[filter_groups][0][filters][0][condition_type]", "eq");
//		return getJson(shopId, "/V1/products", q, MagSearchProductResult.class);
//	}
//
//	private MagProduct getProduct(WebshopId shopId, String sku, TheClientDto theClientDto)
//			throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
//		return getJson(shopId, "/V1/products/" + URLEncoder.encode(sku, "UTF-8"), MagProduct.class);
//	}
//
//	private MagProduct postProduct(WebshopId shopId, MagProduct product) throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
//		MagPostProduct postProduct = new MagPostProduct(product);
//		return postJson(shopId, "/V1/products",  postProduct, MagProduct.class);
//	}
//
//	private MagProduct putProduct(WebshopId shopId, MagProduct product) throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
//		MagPostProduct postProduct = new MagPostProduct(product);
//		return putJson(shopId,
//				"/V1/products/" + URLEncoder.encode(product.getSku(), "UTF-8"), postProduct, MagProduct.class);
//	}

	private MagProduct createProduct(ArtikelDto itemDto, 
			ArtikelkommentarDto[] commentsDto, 
			List<VkpfartikelpreislisteDto> preislisten, 
			WebshopId shopId, 
			TheClientDto theClientDto) throws RemoteException {
		MagProduct product = new MagProduct();
		product.setSku(itemDto.getCNr().trim());
		String cbezSpr = itemDto.getCBezAusSpr();
		if (cbezSpr == null) {
			cbezSpr = product.getSku();
		} else {
			cbezSpr = cbezSpr.trim();
		}
		product.setName(cbezSpr);
		product.setPrice(new BigDecimal("1.1"));
		product.setTypeId("simple");
		product.setAttributeSetId(4);
		product.setStatus(1);
		product.setVisibility(4);

		product.addAttribute("short_description", cbezSpr);

		setCategoryIdsAttributes(product, shopId, itemDto, theClientDto);

		MagProductExtensionAttributes extensionAttributes = new MagProductExtensionAttributes();
		MagProductStockItem stockItem = new MagProductStockItem();
		stockItem.setInStock(true);
		stockItem.setQty(9999);
		stockItem.setQtyDecimal(false);
		extensionAttributes.setStockItem(stockItem);
		product.setExtensionAttributes(extensionAttributes);

		setTextAttributes(product, itemDto, commentsDto, theClientDto);
		setImageAttributes(product, itemDto, commentsDto, theClientDto);
		setMwstAttributes(product, shopId, itemDto, theClientDto);
		setPrices(product, itemDto, preislisten, theClientDto);

		return normalizeProduct(product, itemDto);
	}

	private MagProduct setCategoryIdsAttributes(MagProduct product, WebshopId shopId, ArtikelDto itemDto, TheClientDto theClientDto) throws RemoteException {
		WebshopShopgruppeDto wsgDto = getArtikelFac()
			.webshopShopgruppeFindByShopShopgruppeNoExc(shopId,
				new ShopgruppeId(itemDto.getShopgruppeIId()));
		if (wsgDto == null) {
			myLogger.error("Artikel '" + itemDto.getCNr() + "' (id:" + itemDto.getIId() + ") hat die ShopgruppenId "
					+ itemDto.getShopgruppeIId()
					+ ", die nicht in der Liste der Shopgruppen fuer den Webshop ist! Artikel ignoriert!");
			return product;
		}

		List<String> categoryIds = new ArrayList<String>();
		categoryIds.add(wsgDto.getExternalId());
		product.addAttribute("category_ids", categoryIds);
		return product;
	}
	
	private MagProduct setMwstAttributes(MagProduct product, WebshopId shopId, ArtikelDto itemDto,
			TheClientDto theClientDto) throws RemoteException {
		Integer mwstsatzBezId = itemDto.getMwstsatzbezIId();
		if (mwstsatzBezId == null) {
			MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mwstsatzBezId = mDto.getMwstsatzbezIIdStandardinlandmwstsatz();
			myLogger.warn("Artikel '" + itemDto.getCNr() + "' (id:" + itemDto.getIId()
					+ ") hat keine MwstsatzBezId, verwendet Mandant Inland");
		}

		if (mwstsatzBezId == null) {
			myLogger.warn("Artikel '" + itemDto.getCNr() + "' (id:" + itemDto.getIId()
					+ ") hat keine MwstsatzBezId, ignoriert.");
			return product;
		}

		WebshopMwstsatzbezDto wsBezDto = getArtikelFac().webshopMwstsatzbezFindByShopMwstsatzbezNoExc(shopId,
				new MwstsatzbezId(mwstsatzBezId));
		if (wsBezDto == null) {
			myLogger.warn("Die MwstsatzbezId " + mwstsatzBezId
					+ " ist im Shop noch nicht bekannt! Artikelsteuersatz nicht gesetzt");
			return product;
		}

		product.addAttribute("tax_class_id", Integer.valueOf(wsBezDto.getExternalId()));
		return product;
	}

	private MagProduct setImageAttributes(MagProduct product, ArtikelDto itemDto, ArtikelkommentarDto[] commentsDto,
			TheClientDto theClientDto) {
		List<ArtikelkommentarDto> images = findImages(commentsDto, theClientDto);
		List<MagProductMediaGalleryEntry> galleryEntries = new ArrayList<MagProductMediaGalleryEntry>();
		List<MagProductMediaGalleryEntry> origEntries = product.getMediaGalleryEntries();
		if (origEntries != null) {
			for (MagProductMediaGalleryEntry ge : origEntries) {
				if (ge.getLabel() == null || !ge.getLabel().startsWith("HV_")) {
					galleryEntries.add(ge);
				}
			}
		}

		for (ArtikelkommentarDto commentDto : images) {
			ArtikelkommentarsprDto sprDto = commentDto.getArtikelkommentarsprDto();
			if (sprDto.getCDateiname() == null || sprDto.getOMedia() == null) {
				continue;
			}

			MagProductMediaGalleryEntry galleryEntry = new MagProductMediaGalleryEntry();
			MediaGalleryContent content = new MediaGalleryContent();
			byte[] media = sprDto.getOMedia();
			content.setBase64EncodedData(new String(Base64.encodeBase64(media)));
			content.setName("HV_" + normalizeFilename(sprDto.getCDateiname()));
			String fileType = "file/unknown";
			String dateiname = sprDto.getCDateiname();
			if (dateiname != null) {
				String mimeType = sprDto.getCDateiname().toLowerCase();
				if (mimeType.endsWith(".jpg")) {
					fileType = "image/jpeg";
				} else if (mimeType.endsWith(".jpeg")) {
					fileType = "image/jpeg";
				} else if (mimeType.endsWith(".png")) {
					fileType = "image/png";
				} else if (mimeType.endsWith(".gif")) {
					fileType = "image/gif";
				}
			} else {
				String mimeType = commentDto.getDatenformatCNr();
				if (mimeType.startsWith("image/jpeg")) {
					fileType = "image/jpeg";
				} else if (mimeType.startsWith("image/png")) {
					fileType = "image/png";
				}
			}
			if ("file/unknown".equals(fileType)) {
				myLogger.error("Couldn't determine filetype");
			}
			content.setType(fileType);
			galleryEntry.setContent(content);
			galleryEntry.setDisabled(false);
			galleryEntry.setLabel("HV_" + sprDto.getCDateiname());
			galleryEntry.setMediaType("image");
			galleryEntry.setPosition(commentDto.getISort());
			List<String> galleryTypes = new ArrayList<String>();
			galleryTypes.add("image");
			galleryTypes.add("thumbnail");
			galleryTypes.add("small_image");
			galleryEntry.setTypes(galleryTypes);
			galleryEntries.add(galleryEntry);
		}

		if (galleryEntries.size() > 0) {
			product.setMediaGalleryEntries(galleryEntries);
		}

		return product;
	}

	private String normalizeFilename(String filename) {
		return filename.replace(" ", "_").replace("(", "").replace(")", "");
	}

	private MagProduct normalizeProduct(MagProduct product, ArtikelDto itemDto) {
		if (existsProductName(product.getName())) {
			product.setName(product.getName() + "-" + itemDto.getCNr());
			myLogger.warn("Productname bereits vorhanden, wurde zu '" + product.getName() + "' abgeaendert!");
		}

		if (product.getName().endsWith("-")) {
			product.setName(product.getName() + Long.toString(getTimestamp().getTime(), 36));
			myLogger.warn("Productname endete mit '-', wurde zu '" + product.getName() + "' abgeaendert!");
		}

		return product;
	}

	private String buildUrlKey(String productName) {
		StringBuffer sb = new StringBuffer();
		char lastChar = 0;
		for (int i = 0; i < productName.length(); i++) {
			char c = Character.toLowerCase(productName.charAt(i));
			if (Character.isLetterOrDigit(c)) {
				sb.append(c);
				lastChar = c;
			} else {
				if (c == '-' || c == ' ' || c == '/') {
					if (lastChar != '-') {
						lastChar = '-';
						sb.append(lastChar);
					}
				}
			}
		}
		return sb.toString();
	}

	private List<ArtikelkommentarDto> findImages(ArtikelkommentarDto[] commentsDto, TheClientDto theClientDto) {
		List<ArtikelkommentarDto> entries = new ArrayList<ArtikelkommentarDto>();

		for (ArtikelkommentarDto kommentarDto : commentsDto) {
			ArtikelkommentarartDto artDto = getKommentarartCache(theClientDto)
					.getValueOfKey(kommentarDto.getArtikelkommentarartIId());
			if (!Helper.isTrue(artDto.getBWebshop()))
				continue;
			if (kommentarDto.getDatenformatCNr().startsWith("image/")
					&& kommentarDto.getArtikelkommentarsprDto() != null) {
				entries.add(kommentarDto);
			}
		}

		return entries;
	}


	class WebshopArtikelpreislisteCache extends HvCreatingCachingProvider<Integer, WebshopArtikelPreislisteDto> {
		private WebshopId shopId;

		public WebshopArtikelpreislisteCache(WebshopId shopId) {
			this.shopId = shopId;
		}

		@Override
		protected WebshopArtikelPreislisteDto provideValue(Integer key, Integer transformedKey) {
			return getArtikelFac().webshopPreislisteFindByShopPreislisteNoExc(shopId, new PreislisteId(key));
		}
	}
}
