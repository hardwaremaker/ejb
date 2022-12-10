package com.lp.server.shop.ejbfac;

import com.lp.server.artikel.service.ShopgruppeDto;

public class ShopgruppeAlwaysChanged implements IsChanged<ShopgruppeDto> {
	public ShopgruppeAlwaysChanged() {
	}
	
	@Override
	public boolean isChanged(ShopgruppeDto dto) {
		return true;
	}
}
