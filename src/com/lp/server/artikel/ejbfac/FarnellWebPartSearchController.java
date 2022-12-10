package com.lp.server.artikel.ejbfac;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.lp.server.artikel.service.PartSearchInvalidParamExc;
import com.lp.server.artikel.service.PartSearchUnexpectedResponseExc;
import com.lp.server.artikel.service.WebPart;
import com.lp.server.artikel.service.WebPartPrice;
import com.lp.server.artikel.service.WebPartQuantityScale;
import com.lp.server.artikel.service.WebPartSearchResult;
import com.lp.server.partner.bl.partsearch.PartSearchFarnellErrorException;
import com.lp.server.partner.service.WeblieferantFarnellDto;
import com.lp.server.schema.element14.productsearch.DefaultFarnellProductSearchParams;
import com.lp.server.schema.element14.productsearch.FarnellDatasheet;
import com.lp.server.schema.element14.productsearch.FarnellError;
import com.lp.server.schema.element14.productsearch.FarnellKeywordSearchParams;
import com.lp.server.schema.element14.productsearch.FarnellKeywordSearchResult;
import com.lp.server.schema.element14.productsearch.FarnellManuPartNumberSearchParams;
import com.lp.server.schema.element14.productsearch.FarnellManufacturerPartNumberSearchResult;
import com.lp.server.schema.element14.productsearch.FarnellPartNumberSearchParams;
import com.lp.server.schema.element14.productsearch.FarnellPartNumberSearchResult;
import com.lp.server.schema.element14.productsearch.FarnellPrice;
import com.lp.server.schema.element14.productsearch.FarnellProduct;
import com.lp.server.schema.element14.productsearch.FarnellStock;
import com.lp.server.schema.element14.productsearch.IFarnellProductSearchParams;
import com.lp.server.schema.element14.productsearch.IFarnellProductSearchResult;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.Helper;

public class FarnellWebPartSearchController implements IWebPartSearchController {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(this.getClass());

	private FarnellApiService apiService;
	private WeblieferantFarnellDto webabfrageFarnellDto;
	
	public FarnellWebPartSearchController(WeblieferantFarnellDto webabfrageFarnellDto) {
		setWebabfrageFarnellDto(webabfrageFarnellDto);
	}
	
	public FarnellApiService getApiService() {
		if (apiService == null) {
			apiService = new FarnellApiService();
		}
		return apiService;
	}
	
	public WeblieferantFarnellDto getWebabfrageFarnellDto() {
		return webabfrageFarnellDto;
	}
	
	public void setWebabfrageFarnellDto(WeblieferantFarnellDto webabfrageFarnellDto) {
		this.webabfrageFarnellDto = webabfrageFarnellDto;
	}
	
	private String getApiBaseUrl() {
		return getWebabfrageFarnellDto().getCUrl();
	}
	
	private String getValueIfNotEmpty(String value) {
		return Helper.isStringEmpty(value) ? null : value;
	}
	
	private void setUserParams(DefaultFarnellProductSearchParams params) {
		if (Helper.isStringEmpty(getWebabfrageFarnellDto().getCCustomerId())
				&& Helper.isStringEmpty(getWebabfrageFarnellDto().getCCustomerKey())) {
			return;
		}
		
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(new SecretKeySpec(getWebabfrageFarnellDto().getCCustomerKey().getBytes(), "HmacSHA1"));
			params.setTimestamp(new Timestamp(System.currentTimeMillis()));
			params.setCustomerId(getWebabfrageFarnellDto().getCCustomerId());
			
			String value = params.getOperationName() + params.getTimestamp();
			byte[] sha1Result = mac.doFinal(value.getBytes());
			params.setSignature(new String(Base64.encodeBase64(sha1Result), "UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException", e);
		} catch (InvalidKeyException e) {
			log.error("InvalidKeyException", e);
		} catch (UnsupportedEncodingException e) {
			log.error("UnsupportedEncodingException", e);
		}
		
	}

	@Override
	public WebPartSearchResult findByPartNumber(String partNumber) throws PartSearchUnexpectedResponseExc {
		FarnellPartNumberSearchParams params = new FarnellPartNumberSearchParams(partNumber, 
				getWebabfrageFarnellDto().getCApiKey(), getWebabfrageFarnellDto().getCStore());
		setUserParams(params);
		return find(partNumber, params, FarnellPartNumberSearchResult.class);
	}

	@Override
	public WebPartSearchResult findByManufacturerPartNumber(String manuPartNumber) throws PartSearchUnexpectedResponseExc {
		FarnellManuPartNumberSearchParams params = new FarnellManuPartNumberSearchParams(manuPartNumber, 
				getWebabfrageFarnellDto().getCApiKey(), getWebabfrageFarnellDto().getCStore());
		setUserParams(params);
		return find(manuPartNumber, params, FarnellManufacturerPartNumberSearchResult.class);
	}

	@Override
	public WebPartSearchResult findByKeyword(String keyword) throws PartSearchUnexpectedResponseExc {
		FarnellKeywordSearchParams params = new FarnellKeywordSearchParams(keyword, 
				getWebabfrageFarnellDto().getCApiKey(), getWebabfrageFarnellDto().getCStore());
		setUserParams(params);
		return find(keyword, params, FarnellKeywordSearchResult.class);
	}

	private <T extends IFarnellProductSearchResult> WebPartSearchResult find(String value, IFarnellProductSearchParams params, Class<T> resultClass) throws PartSearchUnexpectedResponseExc {
		WebPartSearchResult searchResult = new WebPartSearchResult();
		searchResult.setSearchValue(value);
		searchResult.setExecuted(true);
		try {
			T result = getApiService().search(getApiBaseUrl(), params, resultClass);
			return transform(searchResult, result);
		} catch (PartSearchUnexpectedResponseExc e) {
			e.setSearchValue(value);
			throwPartSearchExcIfNecessary(e);
			return searchResult;
		}
	}
	
	private void throwPartSearchExcIfNecessary(PartSearchUnexpectedResponseExc e) throws PartSearchUnexpectedResponseExc {
		if (e instanceof PartSearchFarnellErrorException) {
			FarnellError farnellError = ((PartSearchFarnellErrorException)e).getFarnellError();
			FarnellErrorCode errorCode = getErrorCode(farnellError);
			if (Helper.isOneOf(errorCode, FarnellErrorCode.INVALID_TIMESTAMP, FarnellErrorCode.INVALID_PASSWORD, FarnellErrorCode.SIGNATURE_DESC_EXC,
					FarnellErrorCode.INVALID_CUSTOMER_DETAILS, FarnellErrorCode.INVALID_TIMESTAMP_FORMAT, FarnellErrorCode.INVALID_LOCALE)) {
				PartSearchInvalidParamExc exc = new PartSearchInvalidParamExc(e);
				exc.setErrorCode(errorCode.getValue());
				exc.setErrorMsg(farnellError.getSearchException().getExceptionString());
				throw exc;
			}
			
			if (Helper.isOneOf(errorCode, FarnellErrorCode.INVALID_SKU, FarnellErrorCode.COULDNOT_QUERY_FOR_KEYWORD, 
					FarnellErrorCode.COULDNOT_QUERY_FOR_KEYWORD2, FarnellErrorCode.INVALID_MANU_PARTNUMBER)) {
				return;
//				PartSearchPartNotFoundExc exc = new PartSearchPartNotFoundExc(e);
//				return exc;
			}
			PartSearchUnexpectedResponseExc exc = new PartSearchUnexpectedResponseExc(e);
			throw exc;
		}
		throw e;
	}

	private FarnellErrorCode getErrorCode(FarnellError farnellError) {
		return farnellError != null && farnellError.getExceptionCode() != null ? FarnellErrorCode.fromString(farnellError.getExceptionCode()) : FarnellErrorCode.UNKNOWN;
	}
	
	private WebPartSearchResult transform(WebPartSearchResult transformed, IFarnellProductSearchResult farnellResult) {
		if (farnellResult == null || farnellResult.getSearchResult() == null
				|| farnellResult.getSearchResult().getProducts() == null) {
			return transformed;
		}
		
		for (FarnellProduct farnellProduct : farnellResult.getSearchResult().getProducts()) {
			transformed.getParts().add(transformFarnellProduct(farnellProduct));
		}
		return transformed;
	}

	private WebPart transformFarnellProduct(FarnellProduct farnellProduct) {
		WebPart part = new WebPart();
		part.setPackSize(farnellProduct.getPackSize());
		part.setRequestTime(new Timestamp(System.currentTimeMillis()));
		part.setId(farnellProduct.getId());
		part.setInventory(farnellProduct.getInventory());//getAmountMultiplyingPackSize(part, farnellProduct.getInventory()));
		part.setManufacturerPartNumber(farnellProduct.getTranslatedManufacturerPartNumber());
		part.setStockKeepingUnit(farnellProduct.getStockKeepingUnit());
		part.setMinimumOrderQuantity(farnellProduct.getTranslatedMinimumOrderQuality());
		part.setUnit(farnellProduct.getUnitOfMeasure());
		
		setStockInfo(part, farnellProduct.getStock());
		setPrices(part, farnellProduct.getPrices());
		setUnitPrice(part);
		setWeblink(part, farnellProduct.getDatasheets());
		
		return part;
	}

	private void setWeblink(WebPart part, List<FarnellDatasheet> datasheets) {
		if (datasheets == null || datasheets.isEmpty()) return;
		
		for (FarnellDatasheet sheet : datasheets) {
			if ("T".equals(sheet.getType())) {
				part.setWeblink(sheet.getUrl());
				return;
			}
		}
	}

	private void setUnitPrice(WebPart part) {
		if (part.getQuantityScale().getPrices().isEmpty()) return;
		
		BigDecimal lowestQuantityPrice = part.getQuantityScale().getPrices().get(0).getAmount();
		part.setUnitPrice(lowestQuantityPrice);
	}

	private void setPrices(WebPart part, List<FarnellPrice> farnellPrices) {
		List<WebPartPrice> prices = new ArrayList<WebPartPrice>();
		for (FarnellPrice farnellPrice : farnellPrices) {
			WebPartPrice price = new WebPartPrice();
			price.setAmount(farnellPrice.getCost());//getAmountDividingPackSize(part, farnellPrice.getCost()));
			price.setFrom(farnellPrice.getFrom());//getAmountMultiplyingPackSize(part, farnellPrice.getFrom()));
			price.setTo(farnellPrice.getTo());//getAmountMultiplyingPackSize(part, farnellPrice.getTo()));
			prices.add(price);
		}
		part.setQuantityScale(new WebPartQuantityScale(prices));
	}

	private BigDecimal getAmountDividingPackSize(WebPart part, BigDecimal amount) {
		if (amount == null) 
			return BigDecimal.ZERO;
		
		if (part.getPackSize() == null || part.getPackSize().compareTo(BigDecimal.ONE) == 0) 
			return amount;

		return amount.divide(part.getPackSize(), 6, RoundingMode.HALF_UP);
	}
	
	private BigDecimal getAmountMultiplyingPackSize(WebPart part, BigDecimal amount) {
		if (amount == null) 
			return BigDecimal.ZERO;
		
		if (part.getPackSize() == null || part.getPackSize().compareTo(BigDecimal.ONE) == 0) 
			return amount;

		return amount.multiply(part.getPackSize());
	}
	
	private void setStockInfo(WebPart part, FarnellStock farnellStock) {
		if (farnellStock == null || farnellStock.getLeastLeadTime() == null) return;
		
		part.setLeastLeadTime(farnellStock.getLeastLeadTime().intValue());
	}
	
	private enum FarnellErrorCode {
		INVALID_TIMESTAMP("100001"),
		INTERNAL_FAILURE("100002"),
		INVALID_PASSWORD("100003"),
		INVALID_CUSTOMER_DETAILS("100004"),
		SIGNATURE_DESC_EXC("100005"),
		INVALID_TIMESTAMP_FORMAT("100006"),
		INVALID_LOCALE("100007"),
		INVALID_SKU("200001"),
		COULDNOT_QUERY_FOR_KEYWORD("200002"),
		COULDNOT_QUERY_FOR_KEYWORD2("200003"),
		INVALID_MANU_PARTNUMBER("200004"),
		UNKNOWN("");
		
		private String value;
		
		private FarnellErrorCode(String errorCode) {
			this.value = errorCode;
		}
		
		public String getValue() {
			return value;
		}
		
		public static FarnellErrorCode fromString(String code) {
			if (code != null) {
				for (FarnellErrorCode errorCode : FarnellErrorCode.values()) {
					if (code.equalsIgnoreCase(errorCode.value)) {
						return errorCode;
					}
				}
			}
			return UNKNOWN;
		}
	}
}
