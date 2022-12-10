package com.lp.server.system.service;

import javax.ejb.Remote;

@Remote
public interface AutoMonatsabrechnungversandFac {

	public AutoMonatsabrechnungversandDto autoMonatsabrechnungversandFindByMandantCNr(String mandantCNr);

	public void createAutoMonatsabrechnungversand(AutoMonatsabrechnungversandDto dto);

	public AutoMonatsabrechnungversandDto autoMonatsabrechnungversandFindByPrimaryKey(Integer id);
	
	public void removeAutoMonatsabrechnungversand(Integer id);
	
	public void removeAutoMonatsabrechnungversand(
			AutoMonatsabrechnungversandDto dto);
	public void updateAutoMonatsabrechnungversand(
			AutoMonatsabrechnungversandDto dto);
	public void updateAutoMonatsabrechnungversands(
			AutoMonatsabrechnungversandDto[] dtos);
	
	
	public void createAutoMonatsabrechnungversandAbteilungen(
			AutoMonatsabrechnungversandDto dto);
	public AutoMonatsabrechnungversandDto autoMonatsabrechnungversandAbteilungenfindByMandantCNr(
			String mandantCNr);
	
	public void updateAutoMonatsabrechnungversandAbteilungen(
			AutoMonatsabrechnungversandDto dto);
	
}
