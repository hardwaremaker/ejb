package com.lp.server.artikel.ejbfac;

import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;

import com.lp.server.shop.ejbfac.HttpTalk;

public class HttpTalkFarnell extends HttpTalk {

	public HttpTalkFarnell(HttpUriRequest request) {
		super(request);
	}

	public static HttpTalkFarnell get(String baseUrl, Map<String, String> queryParams) throws URISyntaxException {
		URIBuilder builder = new URIBuilder(baseUrl);
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			builder.setParameter(entry.getKey(), entry.getValue());
		}
		String uri = builder.build().toString();
		HttpGet get = new HttpGet(uri);
		return new HttpTalkFarnell(get);
	}
}
