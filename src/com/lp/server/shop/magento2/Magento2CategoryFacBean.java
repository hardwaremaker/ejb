package com.lp.server.shop.magento2;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jfree.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lp.server.artikel.fastlanereader.generated.FLRWebshopShopgruppe;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.artikel.service.WebshopShopgruppeDto;
import com.lp.server.shop.ejbfac.IsChanged;
import com.lp.server.shop.ejbfac.ShopgruppeAlwaysChanged;
import com.lp.server.shop.ejbfac.ShopgruppeHierarchyDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ShopgruppeId;
import com.lp.server.util.WebshopId;
import com.lp.server.util.fastlanereader.FLRSessionFactory;

@Stateless
public class Magento2CategoryFacBean extends Magento2BaseFacBean implements Magento2CategoryFacLocal {
	@PersistenceContext
	private EntityManager em;

	private Set<String> categoryNames;
	private SimpleDateFormat sdfCategory;

	@EJB
	private Magento2ApiFacLocal magento2Api;
	
	private Set<String> getCategories() {
		if (categoryNames == null) {
			categoryNames = new HashSet<String>();
		}
		return categoryNames;
	}

	private boolean addCategoryName(String name) {
		return getCategories().add(name);
	}

	private boolean existsCategoryName(String name) {
		return getCategories().contains(name);
	}


	private Integer getDefaultMagentoCategoryRoot() {
		return new Integer(2);
	}
	
	private SimpleDateFormat getDateTimeFormatter() {
		if(sdfCategory == null) {
			sdfCategory = new SimpleDateFormat("yyyyMMddHHmmss");			
		}
		return sdfCategory;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void traverseHierarchy(WebshopId shopId, List<ShopgruppeHierarchyDto> hierarchyDtos,
			TheClientDto theClientDto) throws RemoteException {
		List<Integer> paths = new ArrayList<Integer>();
		paths.add(0);

		Integer magentoParentId = getDefaultMagentoCategoryRoot();
		for (ShopgruppeHierarchyDto hierarchyDto : hierarchyDtos) {
			getMagento2CategoryFacLocal().traverseHierarchy(magentoParentId, paths, shopId, hierarchyDto, theClientDto);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void traverseHierarchy(Integer magParentId, List<Integer> paths, WebshopId shopId,
			ShopgruppeHierarchyDto hierarchyDto, TheClientDto theClientDto) throws RemoteException {
		Integer nr = paths.get(paths.size() - 1);
		paths.set(paths.size() - 1, ++nr);

		printPath(magParentId, paths, hierarchyDto.getShopgruppeDto().getCNr(),
				hierarchyDto.getShopgruppeDto().getShopgruppesprDto().getCBez());

		ShopgruppeDto sgDto = hierarchyDto.getShopgruppeDto();
		WebshopShopgruppeDto wsDto = getArtikelFac().webshopShopgruppeFindByShopShopgruppeNoExc(shopId,
				new ShopgruppeId(sgDto.getIId()));
		if (wsDto == null) {
			try {
				createMagentoCategory(magParentId, paths, shopId, sgDto, theClientDto);
			} catch(JsonProcessingException e) {
				myLogger.error("JsonProcessingException", e);
			} catch (UnsupportedEncodingException e) {
				myLogger.error("UnsupportedEncodingException", e);
			} catch (URISyntaxException e) {
				myLogger.error("URISyntaxException", e);
			}
		} else {
			magParentId = new Integer(wsDto.getExternalId());
		}

		if (!hierarchyDto.getChilds().isEmpty()) {
			paths.add(0);
			for (ShopgruppeHierarchyDto hDto : hierarchyDto.getChilds()) {
				getMagento2CategoryFacLocal().traverseHierarchy(magParentId, paths, shopId, hDto, theClientDto);
			}
			paths.remove(paths.size() - 1);
		}
	}

	
	@Override
	public void pushChangedCategories(WebshopId shopId,
			List<ShopgruppeHierarchyDto> hierarchyDtos,
			IsChanged<ShopgruppeDto> evaluateChanged, TheClientDto theClientDto) throws RemoteException {
		List<Integer> paths = new ArrayList<Integer>();
		paths.add(0);

		Integer magentoParentId = getDefaultMagentoCategoryRoot();
		for (ShopgruppeHierarchyDto hierarchyDto : hierarchyDtos) {
			ShopgruppeDto sgDto = hierarchyDto.getShopgruppeDto();
			WebshopShopgruppeDto wsDto = getArtikelFac()
					.webshopShopgruppeFindByShopShopgruppeNoExc(shopId,
							new ShopgruppeId(sgDto.getIId()));
			if(wsDto != null) {
				magentoParentId = new Integer(wsDto.getExternalId());
			} else {
				magentoParentId = getDefaultMagentoCategoryRoot();
			}
			
			getMagento2CategoryFacLocal().pushChangedCategories(
					magentoParentId, paths, shopId,
					hierarchyDto, evaluateChanged, theClientDto);
		}
		
		List<WebshopShopgruppeDto> orphanedDtos = listOrphanedShopgruppen(shopId, theClientDto);
		myLogger.warn("we have " + orphanedDtos.size() + " orphaned Shopgruppen.");
		for (WebshopShopgruppeDto wssgDto : orphanedDtos) {
			getMagento2CategoryFacLocal()
				.removeOrphanedCategories(
					shopId, wssgDto, theClientDto);
		}
//		for (WebshopShopgruppeDto wssgDto : orphanedDtos) {
//			getMagento2CategoryFacLocal().pushOrphanedCategory(
//					shopId, wssgDto, theClientDto);
//		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void removeOrphanedCategories(WebshopId shopId, WebshopShopgruppeDto wssgDto, TheClientDto theClientDto) {
		try {
			List<Integer> categories = resolveTreeCategories(
					shopId, new Integer(wssgDto.getExternalId()), theClientDto);
			Collections.reverse(categories);
			for (Integer externalId : categories) {
				if(magento2Api.removeCategoryId(shopId, externalId)) {
					String s = externalId.toString();
					WebshopShopgruppeDto dto = getArtikelFac()
						.webshopShopgruppeFindByShopExternalIdNull(shopId, s);
					if(dto != null) {
						getArtikelFac().removeWebshopShopgruppe(dto.getIId());
					}
				}
			}
			getArtikelFac().removeWebshopShopgruppe(wssgDto.getShopgruppeId());
		} catch(JsonProcessingException e) {
			myLogger.warn("JsonProcessingException", e);
		}
	}
		
	private List<Integer> resolveTreeCategories(WebshopId shopId, Integer rootId, TheClientDto theClientDto) throws JsonProcessingException {
		List<Integer> categories = new ArrayList<Integer>();
		resolveTreeCategoriesImpl(categories, shopId, rootId, theClientDto);
		return categories;
	}
	
	private void resolveTreeCategoriesImpl(List<Integer> categories,
			WebshopId shopId, Integer categoryId, 
			TheClientDto theClientDto) throws JsonProcessingException {
		MagCategory c = magento2Api.getCategory(shopId, categoryId);
		if(c != null) {
			categories.add(c.getId());
			String children = c.getChildren();
			if(children != null) {
				children = children.trim();
				if(!children.isEmpty()) {
					String[] childIds = children.split(",");
					for (String catId : childIds) {
						Integer id = new Integer(catId);
						resolveTreeCategoriesImpl(categories, shopId, id, theClientDto);
					}
				}
			}			
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void pushOrphanedCategory(WebshopId shopId, WebshopShopgruppeDto wssgDto, TheClientDto theClientDto) {
		pushOrphanedCategoryImpl(shopId, wssgDto.getExternalId(), theClientDto);
		getArtikelFac().removeWebshopShopgruppe(wssgDto.getIId());
	}

	private void pushOrphanedCategoryImpl(WebshopId shopId, String externalId, TheClientDto theClientDto) {
		try {
			MagCategory c = magento2Api.getCategory(shopId, new Integer(externalId));
			if(c != null && c.isActive()) {
				String name = c.getName() + "_HV" + getDateTimeFormatter().format(getTimestamp());				
				c.setName(name);
				c.setActive(false);
				c.setIncludeInMenu(false);
				c.setChildren(null);
				magento2Api.putCategory(shopId, c);
				
				String children = c.getChildren();
				if(children != null) {
					children = children.trim();
					if(children.length() > 0) {
						String[] childIds = children.trim().split(",");
						for (String id : childIds) {
							pushOrphanedCategoryImpl(shopId, id, theClientDto);
						}										
					}
				}
			}
		} catch(JsonProcessingException e) { 
			myLogger.error("JsonProcessing on Webshopshopgruppe (External Id:" + externalId + ")", e);
		} catch (UnsupportedEncodingException e) {
			myLogger.error("UnsupportedEncodingException on Webshopshopgruppe (External Id:" + externalId + ")", e);
		} catch (URISyntaxException e) {
			myLogger.error("URISyntaxException on Webshopshopgruppe (External Id:" + externalId + ")", e);
		}
	}
	
	private List<WebshopShopgruppeDto> listOrphanedShopgruppen(WebshopId shopId, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT * FROM WW_WEBSHOPSHOPGRUPPE wssg " +
			" LEFT OUTER JOIN WW_SHOPGRUPPEWEBSHOP sgws ON " + 
			"  sgws.SHOPGRUPPE_I_ID = wssg.SHOPGRUPPE_I_ID AND " +
			"  sgws.WEBSHOP_I_ID = wssg.WEBSHOP_I_ID " +
			" WHERE wssg.webshop_i_id = " + shopId.id().toString() + 
			" AND sgws.i_id IS NULL";
		Query q = session.createSQLQuery(sQuery).addEntity(FLRWebshopShopgruppe.class);
		List<FLRWebshopShopgruppe> resultList = q.list();

		List<WebshopShopgruppeDto> entries = new ArrayList<WebshopShopgruppeDto>();
		for (FLRWebshopShopgruppe flrWebshop : resultList) {
			WebshopShopgruppeDto dto = getArtikelFac()
					.webshopShopgruppeFindByShopShopgruppeNoExc(shopId,
							new ShopgruppeId(flrWebshop.getShopgruppe_i_id()));
			entries.add(dto);
		}

		session.close();
		return entries;		
	}
	
	private Integer createMagentoCategory(Integer magParentId, List<Integer> paths, 
			WebshopId shopId, ShopgruppeDto sgDto, TheClientDto theClientDto) throws RemoteException, JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
		MagCategory category = createCategory(sgDto, shopId, theClientDto);
		category.setParentId(magParentId);
		
		List<MagCategory> searchCategories = magento2Api.searchCategoriesByName(shopId, category.getName());
		if(!searchCategories.isEmpty()) {
			MagCategory knownCategory = searchCategories.get(0);
			String unique = getDateTimeFormatter().format(getTimestamp());
			String name = knownCategory.getName() +
					"_HV" + unique;
			knownCategory.setName(name);
			String urlkey = (String)knownCategory.getAttribute("url_key");
			if(urlkey != null) {
				knownCategory.addAttribute("url_key", urlkey + "hv" + unique);
			}
			String urlpath = (String)knownCategory.getAttribute("url_path");
			if(urlpath != null) {
				knownCategory.addAttribute("url_path", urlpath + "hv" + unique);
			}
			knownCategory.setActive(false);
			knownCategory.setIncludeInMenu(false);
			knownCategory.setChildren(null);
			magento2Api.putCategory(shopId, knownCategory);			
		}
		
		MagCategory resultCategory = magento2Api.postCategory(shopId, category);
		if (resultCategory != null) {
			WebshopShopgruppeDto wsDto = new WebshopShopgruppeDto();
			wsDto.setExternalId(resultCategory.getId().toString());
			wsDto.setPfad(getDottedPath(paths));
			wsDto.setWebshopId(shopId.id());
			wsDto.setShopgruppeId(sgDto.getIId());
 			getArtikelFac().createWebshopShopgruppe(wsDto);

			addCategoryName(category.getName());

			magParentId = resultCategory.getId();
		} else {
			throw new UnsupportedOperationException(
					"Couldnt post category " + sgDto.getCNr() + ", id:" + sgDto.getIId());
		}
		
		return magParentId;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void pushChangedCategories(Integer magParentId,
			List<Integer> paths, WebshopId shopId,
			ShopgruppeHierarchyDto hierarchyDto,
			IsChanged<ShopgruppeDto> evaluateChanged, TheClientDto theClientDto) throws RemoteException {
		Integer nr = paths.get(paths.size() - 1);
		paths.set(paths.size() - 1, ++nr);

		printPath(magParentId, paths, hierarchyDto.getShopgruppeDto().getCNr(),
				hierarchyDto.getShopgruppeDto().getShopgruppesprDto().getCBez());

		ShopgruppeDto sgDto = hierarchyDto.getShopgruppeDto();

		boolean isCreated = false;
		if(evaluateChanged.isChanged(sgDto)) {
			WebshopShopgruppeDto wsDto = getArtikelFac()
					.webshopShopgruppeFindByShopShopgruppeNoExc(shopId,
							new ShopgruppeId(sgDto.getIId()));
			try {
				if(wsDto != null) {
					MagCategory c = magento2Api.getCategory(shopId,
							new Integer(wsDto.getExternalId()));
					if(c != null) {
						if(isSamePath(paths, c, sgDto)) {
							updateCategory(c, sgDto, shopId, theClientDto);
							magento2Api.putCategory(shopId, c);							
						} else {
							removeOrphanedCategories(shopId, wsDto, theClientDto);
							getArtikelFac().removeWebshopShopgruppe(wsDto.getIId());
							wsDto = null;
							if(sgDto.getShopgruppeIId() == null) {
								magParentId = getDefaultMagentoCategoryRoot();
							}
						}
					} else {
						// Es handelt sich um eine "verwaiste" Kategorie
						// Das kann hier zu einem Deadlock wegen REQUIRES_NEW fuehren
						getArtikelFac().removeWebshopShopgruppe(wsDto.getIId());
						myLogger.warn("Verwaiste externe Beziehung shopId:" +
							shopId.toString() + ", ShopgruppeId: " + sgDto.getShopgruppeIId() +
							", externe Id: " + wsDto.getExternalId() + ".");
						wsDto = null;
						if(sgDto.getShopgruppeIId() == null) {
							magParentId = getDefaultMagentoCategoryRoot();
						}
					}
				} 

				if(wsDto == null) {
					magParentId = createMagentoCategory(magParentId,
								paths, shopId, sgDto, theClientDto);
					isCreated = true;
				}
			} catch(JsonProcessingException e) {
				myLogger.error("JsonProcessingException", e);		
			} catch (UnsupportedEncodingException e) {
				myLogger.error("UnsupportedEncodingException", e);		
			} catch (URISyntaxException e) {
				myLogger.error("URISyntaxException", e);		
			}
		}
		
		if (!hierarchyDto.getChilds().isEmpty()) {
			paths.add(0);
			if(isCreated) {
				evaluateChanged = new ShopgruppeAlwaysChanged();
			}
			for (ShopgruppeHierarchyDto hDto : hierarchyDto.getChilds()) {
				try {					
					pushChangedCategories(
						magParentId, paths, shopId, hDto, evaluateChanged, theClientDto);
				} catch(RuntimeException e) {
					// Okay, die eine Shopgruppe hat nicht funktioniert, soll aber
					// keine Auswirkung auf die anderen (parallelen) dazu haben.
					// Verlaesst sich auch auf das RequiresNew Transactionsattribute
					myLogger.error("RuntimeException", e);
				}
			}
			paths.remove(paths.size() - 1);
		}
	}
	
	private boolean isSamePath(List<Integer> paths, MagCategory c, ShopgruppeDto sgDto) {
		String magPath = c.getPath();
		String[] magPaths = magPath.split("/");
		if(magPaths.length - 2 != paths.size()) {
			return false;
		}
		return true;
	}
	
	public void pushChangedCategories0(Integer magParentId,
			List<Integer> paths, WebshopId shopId,
			ShopgruppeHierarchyDto hierarchyDto,
			IsChanged<ShopgruppeDto> evaluateChanged, TheClientDto theClientDto) throws RemoteException {
		Integer nr = paths.get(paths.size() - 1);
		paths.set(paths.size() - 1, ++nr);

		printPath(magParentId, paths, hierarchyDto.getShopgruppeDto().getCNr(),
				hierarchyDto.getShopgruppeDto().getShopgruppesprDto().getCBez());

		ShopgruppeDto sgDto = hierarchyDto.getShopgruppeDto();

		boolean isCreated = false;
		if(evaluateChanged.isChanged(sgDto)) {
			WebshopShopgruppeDto wsDto = getArtikelFac()
					.webshopShopgruppeFindByShopShopgruppeNoExc(shopId,
							new ShopgruppeId(sgDto.getIId()));
			try {
				if(wsDto != null) {
					MagCategory c = magento2Api.getCategory(shopId,
							new Integer(wsDto.getExternalId()));
					if(c != null) {
						updateCategory(c, sgDto, shopId, theClientDto);
						magento2Api.putCategory(shopId, c);
					} else {
						// Es handelt sich um eine "verwaiste" Kategorie
						getArtikelFac().removeWebshopShopgruppe(wsDto.getIId());
						wsDto = null;
					}
				} 

				if (wsDto == null) {
					// Es gibt noch keine Referenz, also die der 
					// Vatergruppe verwenden
					if(sgDto.getShopgruppeIId() != null) {
						wsDto = getArtikelFac()
								.webshopShopgruppeFindByShopShopgruppe(shopId, 
										new ShopgruppeId(sgDto.getShopgruppeIId()));
						magParentId = createMagentoCategory(
								new Integer(wsDto.getExternalId()),
									paths, shopId, sgDto, theClientDto);						
					} else {
						magParentId = createMagentoCategory(
								new Integer(magParentId),
									paths, shopId, sgDto, theClientDto);						
					}
					isCreated = true;
				}			
			} catch(JsonProcessingException e) {
				myLogger.error("JsonProcessingException", e);		
			} catch (UnsupportedEncodingException e) {
				myLogger.error("UnsupportedEncodingException", e);		
			} catch (URISyntaxException e) {
				myLogger.error("URISyntaxException", e);		
			}
		}
		
		if (!hierarchyDto.getChilds().isEmpty()) {
			paths.add(0);
			if(isCreated) {
				evaluateChanged = new ShopgruppeAlwaysChanged();
			}
			for (ShopgruppeHierarchyDto hDto : hierarchyDto.getChilds()) {
				try {					
					getMagento2CategoryFacLocal().pushChangedCategories(
						magParentId, paths, shopId, hDto, evaluateChanged, theClientDto);
				} catch(RuntimeException e) {
					// Okay, die eine Shopgruppe hat nicht funktioniert, soll aber
					// keine Auswirkung auf die anderen (parallelen) dazu haben.
					// Verlaesst sich auch auf das RequiresNew Transactionsattribute
					myLogger.error("RuntimeException", e);
				}
			}
			paths.remove(paths.size() - 1);
		}
	}

	private MagCategory createCategory(ShopgruppeDto sgDto, WebshopId shopId, TheClientDto theClientDto) throws RemoteException {
		MagCategory category = new MagCategory();
		category.setIncludeInMenu(true);
		return updateCategory(category, sgDto, shopId, theClientDto);
	}

	private MagCategory updateCategory(MagCategory category, ShopgruppeDto sgDto, WebshopId shopId, TheClientDto theClientDto) throws RemoteException {
		category.setName(sgDto.getShopgruppesprDto().getCBez());
		category.setPosition(sgDto.getISort());
		category.setActive(true);
//		category.setIncludeInMenu(true);
		
		category.setChildren(null);

		Integer referenceId = sgDto.getArtikelIId();
		if(referenceId != null) {
			ArtikelDto referenceDto = getArtikelFac()
					.artikelFindByPrimaryKey(referenceId, theClientDto);
			ArtikelkommentarDto[] commentsDto = getArtikelkommentarFac()
					.artikelkommentarFindByArtikelIId(referenceId, theClientDto);
			setTextAttributes(category, referenceDto, commentsDto, theClientDto);
		}

		return normalizeCategory(category, sgDto);	
	}
	
	private MagCategory normalizeCategory(MagCategory category, ShopgruppeDto sgDto) {
		if (existsCategoryName(category.getName())) {
			category.setName(category.getName() + "-" + sgDto.getCNr());
			Log.warn("Shopgruppenname bereits vorhanden, wurde zu '" + category.getName() + "' abgeaendert!");
		}

		if (category.getName().endsWith("-")) {
			category.setName(category.getName() + Long.toString(getTimestamp().getTime(), 36));
			Log.warn("Shopgruppenname endete mit '-', wurde zu '" + category.getName() + "' abgeaendert!");
		}

		return category;
	}

	
	private void printPath(Integer magParentId, List<Integer> paths, String cnr, String cbez) {
		StringBuffer sb = new StringBuffer(getDottedPath(paths));
		sb.append(" <" + cbez + "(" + cnr + ")>");
		myLogger.warn("Shopgruppe: " + sb.toString() + " -> " + magParentId.toString());
	}

	private String getDottedPath(List<Integer> paths) {
		return StringUtils.join(paths.iterator(), ".");
	}
}
