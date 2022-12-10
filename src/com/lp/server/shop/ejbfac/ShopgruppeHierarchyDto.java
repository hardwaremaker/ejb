package com.lp.server.shop.ejbfac;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.ShopgruppeDto;

public class ShopgruppeHierarchyDto {
	private ShopgruppeDto shopgruppeDto;
	private List<ShopgruppeHierarchyDto> childs;

	public ShopgruppeHierarchyDto(ShopgruppeDto sgDto) {
		setShopgruppeDto(sgDto);
	}

	public ShopgruppeDto getShopgruppeDto() {
		return shopgruppeDto;
	}

	public void setShopgruppeDto(ShopgruppeDto shopgruppeDto) {
		this.shopgruppeDto = shopgruppeDto;
		this.childs = new ArrayList<ShopgruppeHierarchyDto>();
	}

	public List<ShopgruppeHierarchyDto> getChilds() {
		return childs;
	}

	public void setChilds(List<ShopgruppeHierarchyDto> childs) {
		this.childs = childs;
	}

	public void addChild(ShopgruppeHierarchyDto child) {
		childs.add(child);
	}
}