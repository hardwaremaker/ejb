package com.lp.server.shop.magento2;

import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;

import com.lp.server.artikel.service.WebshopDto;
import com.lp.server.shop.ejbfac.HttpTalk;

public class HttpTalkMagento extends HttpTalk {
	protected HttpTalkMagento(HttpUriRequest request) {
		super(request);
	}
	
	public static HttpTalk post(String resource, WebshopDto connectionDto) {
		HttpPost post = new HttpPost(connectionDto.getCUrl() + resource);
		post.addHeader("Authorization", "Bearer " + connectionDto.getCPassword());
		return new HttpTalkMagento(post);
	}
	
	public static HttpTalk put(String resource, WebshopDto connectionDto) throws URISyntaxException {
		URIBuilder builder = new URIBuilder(connectionDto.getCUrl() + resource);
		HttpPut put = new HttpPut(builder.build());
		put.addHeader("Authorization", "Bearer " + connectionDto.getCPassword());
		return new HttpTalkMagento(put);
	}
	
	public static HttpTalk get(String resource, WebshopDto connectionDto) {
		HttpGet get = new HttpGet(connectionDto.getCUrl() + resource);
		get.addHeader("Authorization", "Bearer " + connectionDto.getCPassword());
		return new HttpTalkMagento(get);
	}

	public static HttpTalk get(String resource, Map<String, String> queryParams, WebshopDto connectionDto) throws URISyntaxException {
		URIBuilder builder = new URIBuilder(connectionDto.getCUrl() + resource);
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			builder.setParameter(entry.getKey(), entry.getValue());
		}
		HttpGet get = new HttpGet(builder.build());
		get.addHeader("Authorization", "Bearer " + connectionDto.getCPassword());
		return new HttpTalkMagento(get);
	}
	
	public static HttpTalk delete(String resource, WebshopDto connectionDto) {
		HttpDelete delete = new HttpDelete(connectionDto.getCUrl() + resource);
		delete.addHeader("Authorization", "Bearer " + connectionDto.getCPassword());
		return new HttpTalkMagento(delete);
	}
}
