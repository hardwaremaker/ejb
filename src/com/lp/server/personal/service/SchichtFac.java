package com.lp.server.personal.service;

import javax.ejb.Remote;

@Remote
public interface SchichtFac {
	public Integer createSchichtzeit(SchichtzeitDto dto);

	public SchichtzeitDto schichtzeitFindByPrimaryKey(Integer iId);

	public void updateSchichtzeit(SchichtzeitDto dto);

	public void removeSchichtzeit(SchichtzeitDto dto);

	public Integer createSchicht(SchichtDto dto);

	public SchichtDto schichtFindByPrimaryKey(Integer iId);

	public void updateSchicht(SchichtDto dto);

	public void removeSchicht(SchichtDto dto);

	public Integer createSchichtzuschlag(SchichtzuschlagDto dto);

	public SchichtzuschlagDto schichtzuschlagFindByPrimaryKey(Integer iId);

	public void updateSchichtzuschlag(SchichtzuschlagDto dto);

	public void removeSchichtzuschlag(SchichtzuschlagDto dto);

	public SchichtzeitDto[] schichtzeitFindBySchichtIId(Integer schichtIId);
}
