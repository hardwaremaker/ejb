package com.lp.server.shop.magento2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lp.server.artikel.service.WebshopArtikelDto;
import com.lp.server.artikel.service.WebshopDto;
import com.lp.server.shop.ejbfac.HttpTalk;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.WebshopId;

@Stateless
public class Magento2Api extends Facade implements Magento2ApiFacLocal {
	private ObjectMapper jsonMapper;

	protected void logWarnStream(String message, InputStream stream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String line = null;
		while ((line = br.readLine()) != null) {
			myLogger.warn(message + line);
		}
		br.close();
	}

	protected void logErrorStream(String message, InputStream stream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String line = null;
		while ((line = br.readLine()) != null) {
			myLogger.error(message + line);
		}
		br.close();
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

	protected WebshopDto getConnectionDto(WebshopId shopId) {
		WebshopDto dto = getArtikelFac().webshopFindByPrimaryKey(shopId.id());
		return dto;
	}

	protected <T> T getJson(WebshopId shopId, String resource, Class<T> resultClass) {
		WebshopDto dto = getConnectionDto(shopId);
		HttpTalk t = HttpTalkMagento.get(resource, dto).acceptJson();
		
		try {
			HttpResponse r = t.ex();
			if (r.getStatusLine().getStatusCode() == 200) {
				return (T) getJsonMapper().readValue(r.getEntity().getContent(), resultClass);
			} else {
				logErrorStream("GET ERROR (" + resource + "): ", r.getEntity().getContent());
			}
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
	
	protected <T> T getJson(WebshopId shopId, String resource, Map<String,String> parameters, Class<T> resultClass) throws URISyntaxException {
		WebshopDto dto = getConnectionDto(shopId);
		HttpTalk t = HttpTalkMagento.get(resource, parameters, dto).acceptJson();
		
		try {
			HttpResponse r = t.ex();
			if (r.getStatusLine().getStatusCode() == 200) {
				return (T) getJsonMapper().readValue(r.getEntity().getContent(), resultClass);
			} else {
				logErrorStream("GET ERROR (" + resource + ", queryparams): ", r.getEntity().getContent());
			}
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
	
	protected <T> T putJson(WebshopId shopId, 
		String resource, Object post, Class<T> resultClass) throws JsonProcessingException, URISyntaxException, UnsupportedEncodingException {
		WebshopDto dto = getConnectionDto(shopId);
		String json = getJsonMapper().writeValueAsString(post);
		HttpTalk t = HttpTalkMagento.put(resource, dto).addJson(json).acceptJson();
		
		try {
			HttpResponse r = t.ex();
			if (r.getStatusLine().getStatusCode() == 200) {
				return (T) getJsonMapper().readValue(r.getEntity().getContent(), resultClass);
			} else {
				logErrorStream("PUT REQUEST ERROR (" + resource + "): ",
						((HttpPut) t.getRequest()).getEntity().getContent());
				logErrorStream("PUT RESPONSE BODY ERROR: ", r.getEntity().getContent());
			}
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
	
	protected <T> T postJson(WebshopId shopId, String resource, Object post, Class<T> resultingClass)
			throws JsonProcessingException, URISyntaxException, UnsupportedEncodingException {
		WebshopDto dto = getConnectionDto(shopId);
		String json = getJsonMapper().writeValueAsString(post);
		HttpTalk t = HttpTalk.post(resource, dto).addJson(json).acceptJson();

		try {
			HttpResponse r = t.ex();
			if (r.getStatusLine().getStatusCode() == 200) {
				return (T) getJsonMapper().readValue(r.getEntity().getContent(), resultingClass);
			} else {
				logErrorStream("POST REQUEST ERROR (" + resource + "): ", ((HttpPost) t.getRequest()).getEntity().getContent());
				logErrorStream("POST RESPONSE BODY ERROR: ", r.getEntity().getContent());
			}
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

	protected <T> T deleteJson(WebshopId shopId, String resource, Class<T> resultClass) {
		WebshopDto dto = getConnectionDto(shopId);
		HttpTalk t = HttpTalkMagento.delete(resource, dto).acceptJson();

		try {
			HttpResponse r = t.ex();
			if (r.getStatusLine().getStatusCode() == 200) {
				return (T) getJsonMapper().readValue(r.getEntity().getContent(), resultClass);
			} else {
				logErrorStream("DELETE RESPONSE BODY ERROR (" + resource + "): ", r.getEntity().getContent());
			}
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
	
	@Override
	public boolean removeCategoryId(WebshopId shopId, Integer categoryId) {
		String answer = deleteJson(shopId, "/V1/categories/" + categoryId.toString(), String.class);
		if(answer == null) return false;
		return "true".equals(answer.trim().toLowerCase());
	}

	@Override
	public MagCategory getCategory(WebshopId shopId, Integer categoryId) throws JsonProcessingException {
		return getJson(shopId, "/V1/categories/" 
				+ categoryId.toString(), MagCategory.class);
	}
 
	@Override
	public MagCategory putCategory(WebshopId shopId, MagCategory category) throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
		MagPostCategory postCategory = new MagPostCategory(category);
		return putJson(shopId, "/V1/categories/" + category.getId().toString(), postCategory, MagCategory.class);
	}

	@Override
	public MagCategory postCategory(WebshopId shopId, MagCategory category) throws UnsupportedEncodingException, JsonProcessingException, URISyntaxException {
		MagPostCategory postCategory = new MagPostCategory(category);
		return postJson(shopId, "/V1/categories", postCategory, MagCategory.class);
	}

	@Override
	public List<MagCategory> searchCategoriesByName(WebshopId shopId, String categoryName) throws JsonProcessingException, URISyntaxException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("searchCriteria[filter_groups][0][filters][0][field]", "name");
		parameters.put("searchCriteria[filter_groups][0][filters][0][value]", categoryName);
		parameters.put("searchCriteria[filter_groups][0][filters][0][condition_type]", "eq");
		MagSearchCategoryResult result = getJson(shopId, "/V1/categories/list", parameters, MagSearchCategoryResult.class);
		return result != null ? result.getItems() : new ArrayList<MagCategory>();
	}
	
	@Override
	public MagTaxClass postTaxClass(WebshopId shopId, MagTaxClass taxClass) throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
		MagPostTaxClass postTaxClass = new MagPostTaxClass(taxClass);
		return postJson(shopId, "/V1/taxClasses", postTaxClass, MagTaxClass.class);
	}

	@Override
	public MagCustomerGroup postCustomerGroup(WebshopId shopId, MagCustomerGroup customerGroup)
			throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
		MagPostCustomerGroup postCustomerGroup = new MagPostCustomerGroup(customerGroup);
		return postJson(shopId, "/V1/customerGroups", postCustomerGroup, MagCustomerGroup.class);
	}
	
	@Override
	public MagSearchProductResult searchProductByExternalId(WebshopId shopId, final WebshopArtikelDto waDto) throws JsonProcessingException, URISyntaxException {
		Map<String, String> q = new HashMap<String, String>();
		q.put("searchCriteria[filter_groups][0][filters][0][field]", "entity_id");
		q.put("searchCriteria[filter_groups][0][filters][0][value]", waDto.getExternalId());
		q.put("searchCriteria[filter_groups][0][filters][0][condition_type]", "eq");
		return getJson(shopId, "/V1/products", q, MagSearchProductResult.class);
	}

	@Override
	public MagProduct getProduct(WebshopId shopId, String sku, TheClientDto theClientDto)
			throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
		return getJson(shopId, "/V1/products/" + URLEncoder.encode(sku, "UTF-8"), MagProduct.class);
	}

	@Override
	public MagProduct postProduct(WebshopId shopId, MagProduct product) throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
		MagPostProduct postProduct = new MagPostProduct(product);
		return postJson(shopId, "/V1/products",  postProduct, MagProduct.class);
	}

	@Override
	public MagProduct putProduct(WebshopId shopId, MagProduct product) throws JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
		MagPostProduct postProduct = new MagPostProduct(product);
		return putJson(shopId,
				"/V1/products/" + URLEncoder.encode(product.getSku(), "UTF-8"), postProduct, MagProduct.class);
	}

	@Override
	public MagCustomer getAccount(WebshopId shopId, Integer accountId) {
		return getJson(shopId, "/V1/customers/" + accountId.toString(), MagCustomer.class);
	}
	
	@Override
	public MagSearchOrdersResult searchPendingOrders(WebshopId shopId) throws JsonProcessingException, URISyntaxException {
		Map<String, String> q = new HashMap<String, String>();
		q.put("searchCriteria[filter_groups][0][filters][0][field]", "status");
		q.put("searchCriteria[filter_groups][0][filters][0][value]", OrderStatus.PENDING);
		q.put("searchCriteria[filter_groups][0][filters][0][condition_type]", "eq");
		return getJson(shopId,"/V1/orders", q, MagSearchOrdersResult.class);
	}
	
	@Override
	public MagOrder getOrder(WebshopId shopId, Integer orderId) throws JsonProcessingException, URISyntaxException {
		return getJson(shopId, "/V1/orders/" + orderId.toString(), MagOrder.class);
	}
	
	@Override
	public MagOrder putOrderStatus(WebshopId shopId, MagUpdateOrderStatus status) throws JsonProcessingException, URISyntaxException, UnsupportedEncodingException {
		MagPutOrderStatus putStatus = new MagPutOrderStatus(status);
		return putJson(shopId, "/V1/orders/create", putStatus, MagOrder.class);
	}
}
