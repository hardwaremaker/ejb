package com.lp.server.artikel.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WebPartQuantityScale implements Serializable {
	private static final long serialVersionUID = -1261562658837972205L;

	private List<WebPartPrice> prices;
	
	public WebPartQuantityScale(List<WebPartPrice> prices) {
		setPrices(prices);
	}

	public List<WebPartPrice> getPrices() {
		if (prices == null) {
			prices = new ArrayList<WebPartPrice>();
		}
		return prices;
	}
	
	public void setPrices(List<WebPartPrice> prices) {
		this.prices = new ArrayList<WebPartPrice>(prices);
		Collections.sort(this.prices, getPriceComparator());
	}
	
	private Comparator<WebPartPrice> getPriceComparator() {
		return new Comparator<WebPartPrice>() {
			public int compare(WebPartPrice price1, WebPartPrice price2) {
				if (price1.getFrom() == null) return 1;
				if (price2.getFrom() == null) return -1;
				
				return price1.getFrom().compareTo(price2.getFrom());
			}
		};
	}
}
