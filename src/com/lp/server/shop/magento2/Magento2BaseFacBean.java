package com.lp.server.shop.magento2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarartDto;
import com.lp.server.artikel.service.WebshopDto;
import com.lp.server.auftrag.ejbfac.ChangeEntry;
import com.lp.server.shop.ejbfac.HttpTalk;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperWebshop;
import com.lp.server.util.WebshopId;
import com.lp.util.Helper;

public class Magento2BaseFacBean extends Facade {
	private ObjectMapper jsonMapper;
	private List<ChangeEntry<?>> changedEntries  = new ArrayList<ChangeEntry<?>>() ;	
	private ArtikelkommentarartCache kommentarartCache;

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
	
	protected boolean isEmptyString(String string) {
		return null == string || 0 == string.trim().length() ;
	}
	
	protected String emptyString(String string) {
		return null == string ? "" : string.trim() ;
	}
	
	protected  void addChangeEntry(ChangeEntry<?> changedEntry) {
		changedEntries.add(changedEntry) ;
	}
	
	protected void clearChangeEntries() {
		changedEntries.clear();
	}
	
	protected List<ChangeEntry<?>> getChangeEntries() {
		return changedEntries ;
	}
	
	protected <T> T getJson(WebshopId shopId, String resource, Class<T> resultClass) {
		WebshopDto dto = getConnectionDto(shopId);
		HttpTalk t = HttpTalkMagento.get(resource, dto).acceptJson();
		
		try {
			HttpResponse r = t.ex();
			if (r.getStatusLine().getStatusCode() == 200) {
				return (T) getJsonMapper().readValue(r.getEntity().getContent(), resultClass);
			} else {
				logErrorStream("GET ERROR: ", r.getEntity().getContent());
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
				logErrorStream("GET ERROR: ", r.getEntity().getContent());
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
				logErrorStream("PUT REQUEST ERROR: ",
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
				logErrorStream("POST REQUEST ERROR: ", ((HttpPost) t.getRequest()).getEntity().getContent());
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
				logErrorStream("DELETE RESPONSE BODY ERROR: ", r.getEntity().getContent());
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
	
	protected IMagCustomAttribute setTextAttributes(
		IMagCustomAttribute entity, ArtikelDto itemDto,
		ArtikelkommentarDto[] commentsDto, TheClientDto theClientDto) {
		
		String s = find(commentsDto, "WEBDescription", "text/html", theClientDto);
		if (s == null) {
			s = find(commentsDto, "VK-Bild", "text/html", theClientDto);
		}
		if (s != null) {
			entity.addAttribute("description", s.trim());
		} else {
			entity.addAttribute("description",
					itemDto.getCBezAusSpr() == null ? itemDto.getCNr() : itemDto.getCBezAusSpr().trim());
		}
		if ((s = find(commentsDto, "SEOTitle", "text/html", theClientDto)) != null) {
			entity.addAttribute("meta_title", HelperWebshop.unescapeHtml(s).trim());
		}
		if ((s = find(commentsDto, "SEOKeywords", "text/html", theClientDto)) != null) {
			entity.addAttribute("meta_keyword", HelperWebshop.unescapeHtml(s).trim());
		}
		if ((s = find(commentsDto, "SEODescription", "text/html", theClientDto)) != null) {
			entity.addAttribute("meta_description", HelperWebshop.unescapeHtml(s).trim());
		}
		
		return entity;
	}
	
	private String find(ArtikelkommentarDto[] commentsDto, String kennung, String mimeType, TheClientDto theClientDto) {
		for (ArtikelkommentarDto kommentarDto : commentsDto) {
			ArtikelkommentarartDto artDto = getKommentarartCache(theClientDto)
					.getValueOfKey(kommentarDto.getArtikelkommentarartIId());
			if (!Helper.isTrue(artDto.getBWebshop()))
				continue;

			if (kennung.equals(artDto.getCNr()) && mimeType.equals(kommentarDto.getDatenformatCNr().trim())) {
				return kommentarDto.getArtikelkommentarsprDto().getXKommentar();
			}
		}

		return null;
	}
	
	protected ArtikelkommentarartCache getKommentarartCache(TheClientDto theClientDto) {
		if (kommentarartCache == null) {
			kommentarartCache = new ArtikelkommentarartCache(theClientDto);
		}
		return kommentarartCache;
	}

	class ArtikelkommentarartCache extends HvCreatingCachingProvider<Integer, ArtikelkommentarartDto> {

		private TheClientDto theClientDto;

		public ArtikelkommentarartCache(TheClientDto theClientDto) {
			this.theClientDto = theClientDto;
		}

		@Override
		protected ArtikelkommentarartDto provideValue(Integer key, Integer transformedKey) {
			try {
				return getArtikelkommentarFac().artikelkommentarartFindByPrimaryKey(key, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			return null;
		}
	}
}
