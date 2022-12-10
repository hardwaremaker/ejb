package com.lp.server.shop.ejbfac;

import com.lp.server.system.service.JobDetailsDto;
import com.lp.server.util.WebshopId;

public class JobDetailsSyncOrderDto extends JobDetailsDto {
	private static final long serialVersionUID = -3786524305788907753L;

	private WebshopId shopId;

	public WebshopId getShopId() {
		return shopId;
	}

	public void setShopId(WebshopId shopId) {
		this.shopId = shopId;
	}
	
	public void setWebshopIId(Integer webshopIId) {
		this.shopId = new WebshopId(webshopIId);
	}
	
	public Integer getWebshopIId() {
		return shopId == null ? null : shopId.id();
	}
}
