package com.lp.server.shop.service;

import java.io.Serializable;

import com.lp.server.util.WebshopId;

public class WebshopConnectionDto implements Serializable {
	private static final long serialVersionUID = -6759788792595134822L;

	private WebshopId shopId;
	private String url;
	private String user;
	private String password;


	public WebshopId getShopId() {
		return shopId;
	}

	public void setShopId(WebshopId shopId) {
		this.shopId = shopId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
