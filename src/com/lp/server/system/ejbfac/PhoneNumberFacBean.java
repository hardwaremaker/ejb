package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Singleton;
import javax.interceptor.Interceptors;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.TelefonSuchergebnisDto;
import com.lp.server.system.service.KundenTelefonSuchergebnisDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VerfuegbareHostsDto;
import com.lp.server.util.bean.ClientBean;
import com.lp.server.util.bean.ISupplier;
import com.lp.server.util.bean.KundeBean;
import com.lp.server.util.bean.Lazy;
import com.lp.server.util.bean.LieferantBean;
import com.lp.server.util.bean.LogonBean;
import com.lp.server.util.bean.MandantBean;
import com.lp.server.util.bean.PartnerBean;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.server.util.logger.MethodLogger;
import com.lp.util.Helper;

@Singleton
@Interceptors(MethodLogger.class)
public class PhoneNumberFacBean implements PhoneNumberFacLocal {
	private final ILPLogger log = LPLogService.getInstance().getLogger(PhoneNumberFacBean.class);
	
	private final PartnerBean partnerBean = new PartnerBean();
	private final LogonBean logonBean = new LogonBean();
	private final ClientBean clientBean = new ClientBean();
	private final LieferantBean lieferantBean = new LieferantBean();
	private final KundeBean kundeBean = new KundeBean();
	private final MandantBean mandantBean = new MandantBean();
	
	private final Lazy<TheClientDto> theClientDto = new Lazy<TheClientDto>();
	
	private final ISupplier<TheClientDto> clientSupplier = new ISupplier<TheClientDto>() {
		@Override
		public TheClientDto get() {
			Locale l = mandantBean.get().getLocaleDesHauptmandanten();
			return logonBean.get().logonIntern(l, null);
		}
	};
	
	private TheClientDto clientDto() {
		return theClientDto.getOrCompute(clientSupplier);
	}
	
/*
	private void halt() {
		try {
			Thread.sleep(122);
		} catch(InterruptedException e) {
			log.error("Interrupted", e);
		}		
	}
*/
	
	@Override
	public List<KundenTelefonSuchergebnisDto> search(String phonenumber, boolean isModified) {
//		halt();
		List<TelefonSuchergebnisDto> ejbResults = partnerBean.get()
				.findeTelefonnummer(phonenumber, isModified, clientDto());
		return transform(phonenumber, ejbResults);
	}

	private List<KundenTelefonSuchergebnisDto> transform(
			String phonenumber, List<TelefonSuchergebnisDto> phoneEntries) {
		List<KundenTelefonSuchergebnisDto> entries = new ArrayList<KundenTelefonSuchergebnisDto>();
		
		try {
			for (TelefonSuchergebnisDto entryDto : phoneEntries) {
				KundenTelefonSuchergebnisDto entry = new KundenTelefonSuchergebnisDto(entryDto, clientDto());
				prepareKunden(entry);
				prepareLieferanten(entry);
				entries.add(entry);
			}			
		} catch(RemoteException e) {
			log.error("RemoteException", e);
		}
		
		log.info("Found " + entries.size() + " for '" + phonenumber + "'.");
		return entries;
	}
	
	private void prepareKunden(KundenTelefonSuchergebnisDto entry) throws RemoteException {
		List<KundenTelefonSuchergebnisDto.IdMandant> kunden = new ArrayList<KundenTelefonSuchergebnisDto.IdMandant>();
		KundeDto[] dtos = kundeBean.get().kundeFindByPartnerIId(entry.getPartnerIId(), clientDto());
		for (KundeDto kundeDto : dtos) {
			if(Helper.isTrue(kundeDto.getBVersteckterlieferant())) continue;

			kunden.add(entry.new IdMandant(kundeDto.getIId(), kundeDto.getMandantCNr()));
		}
		
		entry.setKunden(kunden);
	}
	
	private void prepareLieferanten(KundenTelefonSuchergebnisDto entry) throws RemoteException {
		List<KundenTelefonSuchergebnisDto.IdMandant> lieferanten = new ArrayList<KundenTelefonSuchergebnisDto.IdMandant>();
		LieferantDto[] dtos = lieferantBean.get()
				.lieferantFindByPartnerIId(entry.getPartnerIId(), clientDto());
		for (LieferantDto lieferantDto : dtos) {
			if(Helper.isTrue(lieferantDto.getBVersteckt())) continue;
			lieferanten.add(entry.new IdMandant(
					lieferantDto.getIId(), lieferantDto.getMandantCNr()));
		}
		
		entry.setLieferanten(lieferanten);
	}
	
	@Override
	public List<VerfuegbareHostsDto> getClientList(String hostName) {
		return clientBean.get()
				.getVerfuegbareBenutzer(hostName, clientDto());
	}

}
