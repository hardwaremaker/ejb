package com.lp.server.system.ejbfac;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.lp.server.system.service.HttpProxyConfig;
import com.lp.server.system.service.HttpRequestConfig;
import com.lp.server.system.service.ServerDruckerFac;
import com.lp.server.util.HvHttpHeader;
import com.lp.server.util.HvHttpHeaderTransformer;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.service.HttpStatusCodeException;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JasperPrint;

public class HvPrinterProxy implements HvPrinter {
	private final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());

	private String proxyValue;
	private HttpProxyConfig proxyConfig;
	private String printername;
	
	public HvPrinterProxy(String proxyValue) {
		this.proxyValue = proxyValue;
		initProperties(proxyValue);
	}

	private void initProperties(String value) {
		if (value.startsWith(ServerDruckerFac.PROXY_PROTOCOL_PREFIX))
			value = value.substring(ServerDruckerFac.PROXY_PROTOCOL_PREFIX.length());
		
		int idxSlash = value.indexOf("/");
		if (idxSlash < 0) return;
		
		proxyConfig = new HttpProxyConfig(value.substring(0, idxSlash));
		printername = value.substring(idxSlash + 1);
	}
	
	
	public HttpProxyConfig getProxyConfig() {
		return proxyConfig;
	}
	
	private boolean hasValidProperties() {
		if (getProxyConfig() == null || Helper.isStringEmpty(printername)) {
			myLogger.warn("No valid properties, HvPrinterProxy was initialized with value = '" + proxyValue + "'");
			return false;
		}		
		
		if (!getProxyConfig().isDefined()) {
			myLogger.warn("Proxy config is not defined, HvPrinterProxy was initialized with value = '" + proxyValue + "'");
			return false;
		}
		
		return true;
	}
	
	private HttpRequestConfig createRequestConfig() {
		HttpRequestConfig requestConfig = new HttpRequestConfig(getProxyConfig(), "/print");
		requestConfig.addParameter("printer", printername);
		return requestConfig;
	}
	
	@Override
	public boolean exists() {
		return hasValidProperties() && existsImpl();
	}
	
	@Override
	public void print(JasperPrintLP print) {
		if (print == null) return;
		
		try {
			httpPost(createRequestConfig(), print.getPrint());
		} catch (HttpStatusCodeException e) {
		} catch (IOException e) {
		}
	}
	
	@Override
	public void print(JasperPrintLP[] prints) {
		if (prints == null) {
			myLogger.warn("Parameter 'prints' is null");
			return;
		}
		
		JasperPrintLP totalPrint = prints[0];
		for (int i = 1; i < prints.length; i++) {
			totalPrint = Helper.addReport2Report(totalPrint, prints[i].getPrint());
		}
		
		print(totalPrint);
	}

	private boolean existsImpl() {
		if (!hasValidProperties()) return false;
		
		try {
			httpGet(createRequestConfig());
			return true;
		} catch (HttpStatusCodeException e) {
		} catch (IOException e) {
		}
		return false;
	}

	private void httpPost(HttpRequestConfig requestConfig, JasperPrint jasperPrint) throws HttpStatusCodeException, IOException {
		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost httpPost = new HttpPost("http://" + requestConfig.getUri());
			final ByteArrayOutputStream baos = new ByteArrayOutputStream(100000) ;
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(jasperPrint);
			oos.close();
			ByteArrayEntity entity = new ByteArrayEntity(baos.toByteArray());
			httpPost.setEntity(entity);
			HttpResponse response = client.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				myLogger.info("HTTP POST to " + requestConfig.getUri() + " succeeded with status=" + status);
			} else {
				myLogger.error("HTTP POST to "  + requestConfig.getUri() + " did not succeed with status code '" + status);
				HvHttpHeaderTransformer headerTransformer = new HvHttpHeaderTransformer();
				HvHttpHeader hvHeader = headerTransformer.process(response);
				throw new HttpStatusCodeException(requestConfig, status, response.getStatusLine().getReasonPhrase(), hvHeader);
			}
		} catch (IOException e) {
			myLogger.error("Error during HTTP POST execution to " + requestConfig.getUri(), e);
			throw e;
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	private void httpGet(HttpRequestConfig requestConfig) throws HttpStatusCodeException, IOException {
		HttpClient client = new DefaultHttpClient();
		try {
			HttpGet httpGet = new HttpGet("http://" + requestConfig.getUri());
			HttpResponse response = client.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				myLogger.info("HTTP GET to " + requestConfig.getUri() + " succeeded with status=" + status);
			} else {
				myLogger.error("HTTP GET to "  + requestConfig.getUri() + " did not succeed with status code '" + status);
				HvHttpHeaderTransformer headerTransformer = new HvHttpHeaderTransformer();
				HvHttpHeader hvHeader = headerTransformer.process(response);
				throw new HttpStatusCodeException(requestConfig, status, response.getStatusLine().getReasonPhrase(), hvHeader);
			}
		} catch (IOException e) {
			myLogger.error("Error during HTTP POST execution to " + requestConfig.getUri(), e);
			throw e;
		} finally {
			client.getConnectionManager().shutdown();
		}
	}
}
