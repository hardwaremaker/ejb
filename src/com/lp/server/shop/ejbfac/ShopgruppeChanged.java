package com.lp.server.shop.ejbfac;

import java.sql.Timestamp;

import com.lp.server.artikel.service.ShopgruppeDto;

public class ShopgruppeChanged implements IsChanged<ShopgruppeDto> {
	private Timestamp lastChanged;
	
	public ShopgruppeChanged(Timestamp lastChanged) {
		this.lastChanged = lastChanged;
	}
	
	@Override
	public boolean isChanged(ShopgruppeDto dto) {
		if(lastChanged == null) return true;

		return dto.getTAendern().after(lastChanged);
	}
}
