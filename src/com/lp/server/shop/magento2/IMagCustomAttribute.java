package com.lp.server.shop.magento2;

public interface IMagCustomAttribute {
	void addAttribute(String key, Object value);
	Object getAttribute(String key);
	void removeAttribute(String key);
}
