package com.lp.server.shop.ejbfac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.lp.server.artikel.service.WebshopDto;
import com.lp.server.shop.service.WebshopConnectionDto;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

public class HttpTalk {
	private final ILPLogger myLogger = 
			LPLogService.getInstance().getLogger(HttpTalk.class);
	private HttpClient client;
	private HttpUriRequest request;
	
	protected HttpTalk(HttpUriRequest request) {
		this.request = request;
	}
	
	public static HttpTalk post(String resource, WebshopDto connectionDto) {
		HttpPost post = new HttpPost(connectionDto.getCUrl() + resource);
		post.addHeader("Authorization", "Bearer " + connectionDto.getCPassword());
		HttpTalk t = new HttpTalk(post);
		return t;
	}
	
	public static HttpTalk put(String resource, WebshopConnectionDto connectionDto) throws URISyntaxException {
		URIBuilder builder = new URIBuilder(connectionDto.getUrl() + resource);

//		HttpPut put = new HttpPut(connectionDto.getUrl() + resource);
		HttpPut put = new HttpPut(builder.build());
		put.addHeader("Authorization", "Bearer " + connectionDto.getPassword());
		HttpTalk t = new HttpTalk(put);
		return t;
	}
	
	public static HttpTalk get(String resource, WebshopConnectionDto connectionDto) {
		HttpGet get = new HttpGet(connectionDto.getUrl() + resource);
		get.addHeader("Authorization", "Bearer " + connectionDto.getPassword());
		HttpTalk t = new HttpTalk(get);
		return t;
	}

	public static HttpTalk get(String resource, Map<String, String> queryParams, WebshopConnectionDto connectionDto) throws URISyntaxException {
		URIBuilder builder = new URIBuilder(connectionDto.getUrl() + resource);
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			builder.setParameter(entry.getKey(), entry.getValue());
		}
		HttpGet get = new HttpGet(builder.build());
		get.addHeader("Authorization", "Bearer " + connectionDto.getPassword());
		HttpTalk t = new HttpTalk(get);
		return t;
	}
	
	public static HttpTalk delete(String resource, WebshopConnectionDto connectionDto) {
		HttpDelete delete = new HttpDelete(connectionDto.getUrl() + resource);
		delete.addHeader("Authorization", "Bearer " + connectionDto.getPassword());
		HttpTalk t = new HttpTalk(delete);
		return t;	
	}
	
	public HttpResponse ex() throws ClientProtocolException, IOException {
		HttpResponse r = getClient().execute(getRequest());
		int status = r.getStatusLine().getStatusCode();
		myLogger.info("HTTP " + getRequest().getMethod() + " " + status);
		if(status >= 200 && status < 300) {
		} else {
			myLogger.error("HTTP " + getRequest().getMethod() + " to " + getRequest().getRequestLine().getUri()
					+ " failed with status code '" + status + "' (" + r.getStatusLine().getReasonPhrase() + ")");
//			BufferedReader br = new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
//			String line =null;
//			while((line = br.readLine()) != null) {
//				myLogger.error(" Body: " + line);
//			}
//			br.close();
		}
		return r;
	}

	public HttpUriRequest getRequest() {
		return request;
	}

	protected void setRequest(HttpUriRequest request) {
		this.request = request;
	}
	

	public HttpTalk addJson(String json) throws UnsupportedEncodingException {
		String method = getRequest().getMethod();
		if("POST".equals(method)) {
			((HttpPost)getRequest()).setEntity(new StringEntity(json));
		} else if("PUT".equals(method)) {
			((HttpPut)getRequest()).setEntity(new StringEntity(json));
		}
		beJson();
		return this;
	}
	
	public HttpTalk beJson() {
		getRequest().addHeader("Content-Type", "application/json;charset=utf-8");
		return this;
	}
	
	public HttpTalk acceptJson() {
		getRequest().addHeader("Accept", "application/json;charset=utf-8");
		return this;
	}
	
	public void close() {
		if(client != null) {
			client.getConnectionManager().shutdown();
		}
	}
	
	private HttpClient getClient() {
		if(client == null) {
			client = new DefaultHttpClient();
		}
		return client;
	}
}
