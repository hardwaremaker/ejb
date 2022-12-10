package com.lp.server.system.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.partner.service.TelefonSuchergebnisDto;

public class KundenTelefonSuchergebnisDto extends TelefonSuchergebnisDto {
	private static final long serialVersionUID = -7918795890919911797L;

	private List<IdMandant> kunden;
	private List<IdMandant> lieferanten;
	
	public KundenTelefonSuchergebnisDto(TelefonSuchergebnisDto phoneDto, 
			TheClientDto theClientDto) throws RemoteException {
		super(phoneDto.getPartnerIId(), phoneDto.getAnsprechpartnerIId(), phoneDto.getPartner(), phoneDto.getAnsprechpartner(),phoneDto.isVersteckt());
		prepareKunden(theClientDto);
		prepareLieferanten(theClientDto);
	}
	
	private void prepareKunden(TheClientDto theClientDto) throws RemoteException {
		kunden = new ArrayList<IdMandant>();
/*		
		KundeDto[] dtos = kundeFac().kundeFindByPartnerIId(getPartnerIId(), theClientDto);
		for (KundeDto kundeDto : dtos) {
			if(Helper.isTrue(kundeDto.getBVersteckterlieferant())) continue;

			kunden.add(new IdMandant(kundeDto.getIId(), kundeDto.getMandantCNr()));
		}
*/		
	}
	
	private void prepareLieferanten(TheClientDto theClientDto) throws RemoteException {
		lieferanten = new ArrayList<IdMandant>();
/*		
		LieferantDto[] dtos = lieferantFac()
				.lieferantFindByPartnerIId(getPartnerIId(), theClientDto);
		for (LieferantDto lieferantDto : dtos) {
			if(Helper.isTrue(lieferantDto.getBVersteckt())) continue;
			lieferanten.add(new IdMandant(lieferantDto.getIId(), lieferantDto.getMandantCNr()));
		}
*/		
	}
	
	public List<IdMandant> getKunden() {
		return kunden;
	}

	public void setKunden(List<IdMandant> kunden) {
		this.kunden = kunden;
	}

	public List<IdMandant> getLieferanten() {
		return lieferanten;
	}

	public void setLieferanten(List<IdMandant> lieferanten) {
		this.lieferanten = lieferanten;
	}
	
	
	public class IdMandant {
		private Integer entityId;
		private String mandantCnr;
		
		public IdMandant(Integer entityId, String mandantCnr) {
			this.setEntityId(entityId);
			this.setMandantCnr(mandantCnr);
		}

		public Integer getEntityId() {
			return entityId;
		}

		public void setEntityId(Integer entityId) {
			this.entityId = entityId;
		}

		public String getMandantCnr() {
			return mandantCnr;
		}

		public void setMandantCnr(String mandantCnr) {
			this.mandantCnr = mandantCnr;
		}
	}
}
