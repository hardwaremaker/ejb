package com.lp.server.fertigung.service;

import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;

public class ScriptStuecklisteGraphicServiceDto {
	private StuecklistepositionDto[]  stuecklistepositionenIn ;
	private StuecklistearbeitsplanDto[] stuecklistearbeitsplanIn ;
	
	public StuecklistepositionDto[] getStuecklistepositionenIn() {
		return stuecklistepositionenIn;
	}
	public void setStuecklistepositionenIn(StuecklistepositionDto[] stuecklistepositionenIn) {
		this.stuecklistepositionenIn = stuecklistepositionenIn;
	}
	public StuecklistearbeitsplanDto[] getStuecklistearbeitsplanIn() {
		return stuecklistearbeitsplanIn;
	}
	public void setStuecklistearbeitsplanIn(StuecklistearbeitsplanDto[] stuecklistearbeitsplanIn) {
		this.stuecklistearbeitsplanIn = stuecklistearbeitsplanIn;
	}
}
