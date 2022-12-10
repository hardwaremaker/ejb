package com.lp.server.shop.magento2;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.http.HttpResponse;
import org.hibernate.Session;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lp.server.artikel.fastlanereader.generated.FLRShopgruppewebshop;
import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.shop.ejbfac.HttpTalk;
import com.lp.server.shop.ejbfac.IsChanged;
import com.lp.server.shop.ejbfac.ShopgruppeChanged;
import com.lp.server.shop.ejbfac.ShopgruppeHierarchyDto;
import com.lp.server.shop.service.SyncShopFac;
import com.lp.server.shop.service.WebshopConnectionDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.WebshopId;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.Helper;

@Stateless
public class SyncMagento2FacBean extends Facade implements SyncShopFac {
	@PersistenceContext
	private EntityManager em;

	private ObjectMapper jsonMapper;
	
	@Override
	public void ping(WebshopId shopId, TheClientDto theClientDto) {
		getCategory(shopId, 15);
//		buildShopgruppeHierarchy(shopId, theClientDto);
		MagCategory categories = getAllShopCategories(shopId);

		WebshopConnectionDto dto = getConnectionDto(shopId);
		// HttpTalk t = HttpTalk.post("/V1/modules", dto);
		HttpTalk t = HttpTalk.get("/V1/products?searchCriteria", dto).beJson().acceptJson();
		try {
			HttpResponse r = t.ex();
		} catch (IOException e) {
			myLogger.error("IOException:", e);
		} finally {
			t.close();
		}
	}

	
	@org.jboss.ejb3.annotation.TransactionTimeout(20000)
	@Override
	public void pushCustomerGroups(WebshopId shopId, TheClientDto theClientDto) {
		getMagento2ProductFacLocal().pushCustomerGroups(shopId, theClientDto);
	}
	
	@org.jboss.ejb3.annotation.TransactionTimeout(20000)
	@Override
	public void pushItems(WebshopId shopId, TheClientDto theClientDto) {
		getMagento2ProductFacLocal().pushItems(shopId, theClientDto);
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(20000)
	@Override
	public void pushCategories(WebshopId shopId, TheClientDto theClientDto) {
		try {
			List<ShopgruppeHierarchyDto> shopgroupRoots =
					buildShopgruppeHierarchy(shopId, theClientDto);
			getMagento2CategoryFacLocal()
				.traverseHierarchy(shopId, shopgroupRoots, theClientDto);
		} catch(RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}
	
	
	@org.jboss.ejb3.annotation.TransactionTimeout(20000)
	@Override
	public void pushTaxClasses(WebshopId shopId, TheClientDto theClientDto) {
		getMagento2ProductFacLocal().pushTaxClasses(shopId, theClientDto);
	}
	
	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
	@Override
	public void fetchOrders(WebshopId shopId, TheClientDto theClientDto) {
		getMagento2OrderFacLocal().fetchOrders(shopId, theClientDto);
	}
	
	@org.jboss.ejb3.annotation.TransactionTimeout(20000)
	@Override
	public void pushChangedItems(WebshopId shopId, Timestamp tLastChanged, TheClientDto theClientDto) {
		getMagento2ProductFacLocal().pushChangedItems(shopId, tLastChanged, theClientDto);
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(2000)
	@Override
	public void pushChangedCategories(WebshopId shopId, Timestamp tLastChanged, TheClientDto theClientDto) {
		try {
			List<ShopgruppeHierarchyDto> shopgroupRoots =
					buildShopgruppeHierarchy(shopId, theClientDto);
			IsChanged<ShopgruppeDto> evalChanged = new ShopgruppeChanged(tLastChanged);
			getMagento2CategoryFacLocal().pushChangedCategories(shopId, 
					shopgroupRoots, evalChanged, theClientDto);			
		} catch(RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}
	
	
	private WebshopConnectionDto getConnectionDto(WebshopId shopId) {
		WebshopConnectionDto dto = getArtikelFac().webshopConnectionFindByPrimaryKey(shopId);
		String url = dto.getUrl() + "rest";
		dto.setUrl(url);
		// if(url.endsWith("/")) {
		// dto.setUrl(url.substring(0, url.length() - 1));
		// }
		return dto;
	}

	protected ObjectMapper getJsonMapper() {
		if (jsonMapper == null) {
			jsonMapper = new ObjectMapper();
			jsonMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			jsonMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			jsonMapper.setDateFormat(sdf);

		}
		return jsonMapper;
	}

	public MagCategory getAllShopCategories(WebshopId shopId) {
		WebshopConnectionDto dto = getConnectionDto(shopId);
		// HttpTalk t = HttpTalk.post("/V1/modules", dto);
		HttpTalk t = HttpTalk.get("/V1/categories?all", dto).beJson().acceptJson();
		try {
			HttpResponse r = t.ex();
			MagCategory categories = getJsonMapper().readValue(r.getEntity().getContent(), MagCategory.class);
			System.out.println(categories.getName());
			return categories;
		} catch (JsonParseException e) {
			myLogger.error("JsonParse", e);
		} catch (JsonMappingException e) {
			myLogger.error("JsonParse", e);
		} catch (IOException e) {
			myLogger.error("IOException:", e);
		} finally {
			t.close();
		}

		return null;
	}

	private MagCategory getCategory(WebshopId shopId, Integer categoryId) {
		WebshopConnectionDto dto = getConnectionDto(shopId);
		HttpTalk t = HttpTalk.get("/V1/categories/" + categoryId.toString(), dto).acceptJson();
		try {
			HttpResponse r = t.ex();
			MagCategory categories = getJsonMapper().readValue(r.getEntity().getContent(), MagCategory.class);
			myLogger.warn("Category " + categoryId + " id:" + categories.getId() + ", name '" + categories.getName()
					+ "', path '" + categories.getPath() + "'.");
			return categories;
		} catch (JsonParseException e) {
			myLogger.error("JsonParse", e);
		} catch (JsonMappingException e) {
			myLogger.error("JsonParse", e);
		} catch (IOException e) {
			myLogger.error("IOException:", e);
		} finally {
			t.close();
		}

		return null;
	}

	protected List<ShopgruppeHierarchyDto> buildShopgruppeHierarchy(WebshopId shopId, TheClientDto theClientDto) {
		Validator.notNull(shopId, "shopId");

		List<ShopgruppeHierarchyDto> shopgroups = getRootShopgruppen(shopId, null, theClientDto);
		for (ShopgruppeHierarchyDto shopgruppeHierarchyDto : shopgroups) {
			resolve(shopId, shopgruppeHierarchyDto, theClientDto);
		}

		return shopgroups;
	}

	private ShopgruppeHierarchyDto resolve(WebshopId shopId, ShopgruppeHierarchyDto hierarchyDto,
			TheClientDto theClientDto) {
		ShopgruppeDto sgDto = hierarchyDto.getShopgruppeDto();
		List<ShopgruppeHierarchyDto> groups = getRootShopgruppen(shopId, sgDto.getIId(), theClientDto);
		for (ShopgruppeHierarchyDto shopgruppeHierarchyDto : groups) {
			ShopgruppeHierarchyDto child = resolve(shopId, shopgruppeHierarchyDto, theClientDto);
			hierarchyDto.addChild(child);
		}
		return hierarchyDto;
	}


	private List<ShopgruppeHierarchyDto> getRootShopgruppen(WebshopId shopId, Integer parentId,
			TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "FROM FLRShopgruppewebshop AS sgw" + " WHERE sgw.webshop_i_id = " + shopId.id()
				+ (parentId == null ? " AND sgw.flrshopgruppe.flrshopgruppe is NULL"
						: " AND sgw.flrshopgruppe.flrshopgruppe.i_id =" + parentId)
				+ " ORDER BY sgw.flrshopgruppe.i_sort";

		session.enableFilter("filterLocale").setParameter("paramLocale", Helper.locale2String(theClientDto.getLocUi()));

		org.hibernate.Query query = session.createQuery(sQuery);
		List<FLRShopgruppewebshop> resultList = query.list();

		List<ShopgruppeHierarchyDto> shopgroups = new ArrayList<ShopgruppeHierarchyDto>();
		for (FLRShopgruppewebshop flrWebshop : resultList) {
			ShopgruppeDto sgDto = getArtikelFac().shopgruppeFindByPrimaryKey(flrWebshop.getShopgruppe_i_id(),
					theClientDto);
			shopgroups.add(new ShopgruppeHierarchyDto(sgDto));
		}

		session.close();
		return shopgroups;
	}
}
