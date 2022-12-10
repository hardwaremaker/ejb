package com.lp.server.artikel.ejbfac;

import java.rmi.RemoteException;

import javax.ejb.Stateless;

import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.gs1.GTIN13;
import com.lp.util.gs1.GTIN13Builder;

@Stateless
public class GTINGeneratorFacBean extends Facade implements GTINGeneratorFac {

	private class CounterValue {
		private String prefix;
		private String mandantCnr;
		private Integer count;
		
		public CounterValue(KeyvalueDto keyvalueDto) {
			setFromKeyvalue(keyvalueDto);
		}
		
		public CounterValue(String prefix, String mandantCnr, Integer count) {
			setPrefix(prefix);
			setMandantCnr(mandantCnr);
			setCount(count);
		}
		
		public void setFromKeyvalue(KeyvalueDto keyvalueDto) {
			if (Helper.isStringEmpty(keyvalueDto.getCKey())) return;
			
			String[] strings = keyvalueDto.getCKey().split(",");
			if (strings.length > 0) setPrefix(strings[0]);
			if (strings.length > 1) setMandantCnr(strings[1]);
			
			setCount(Integer.parseInt(keyvalueDto.getCValue()));
		}
		
		public KeyvalueDto asKeyvalueDto() {
			KeyvalueDto keyvalueDto = new KeyvalueDto();
			keyvalueDto.setCKey(getPrefix() + "," + getMandantCnr());
			keyvalueDto.setCValue(getCount().toString());
			keyvalueDto.setCDatentyp("java.lang.Integer");
			
			return keyvalueDto;
		}
		
		public String getPrefix() {
			return prefix;
		}
		
		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
		
		public String getMandantCnr() {
			return mandantCnr;
		}
		
		public void setMandantCnr(String mandantCnr) {
			this.mandantCnr = mandantCnr;
		}
		
		public Integer getCount() {
			return count;
		}
		
		public void setCount(Integer count) {
			this.count = count;
		}
	}
	
	public GTIN13 generiereGTIN13(String companyPrefix, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		validateGTIN13GenerationProperties(companyPrefix, theClientDto);
		
		return generiereGTIN13Impl(companyPrefix, theClientDto.getMandant());
	}
	
	public void setDefaultValueItemReference(String companyPrefix, Integer defaultValue, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		validateGTIN13GenerationProperties(companyPrefix, theClientDto);
		CounterValue counterValue = getCounterValue(companyPrefix, theClientDto.getMandant(), true);
		counterValue.setCount(defaultValue);
		updateCounterValue(counterValue);
	}

	private GTIN13 generiereGTIN13Impl(String companyPrefix, String mandantCnr) throws RemoteException, EJBExceptionLP {
		GTIN13Builder gtin13Builder = new GTIN13Builder();
		Integer itemReference = getNextItemReference(companyPrefix, mandantCnr);
		
		if (companyPrefix.length() == 9 && new Integer(999).compareTo(itemReference) < 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_GTIN13_GENERIERUNG_LAUFENDE_NUMMER_ZU_GROSS, 
					"itemReference (='" + itemReference + "') length exceeded, is greater than 999");
		} else if (companyPrefix.length() == 7 && new Integer(99999).compareTo(itemReference) < 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_GTIN13_GENERIERUNG_LAUFENDE_NUMMER_ZU_GROSS, 
					"itemReference (='" + itemReference + "') length exceeded, is greater than 99999");
		}
		GTIN13 gtin13 = gtin13Builder.build(companyPrefix, itemReference);
		gtin13.validate();
		return gtin13;
	}

	private Integer getNextItemReference(String companyPrefix, String mandantCnr) throws RemoteException, EJBExceptionLP {
		CounterValue counterValue = getCounterValue(companyPrefix, mandantCnr, true);
		counterValue.setCount(counterValue.getCount() + 1);
		updateCounterValue(counterValue);
		
		return counterValue.getCount();
	}
	
	private void updateCounterValue(CounterValue counterValue) throws RemoteException, EJBExceptionLP {
		KeyvalueDto keyvalueDto = counterValue.asKeyvalueDto();
		keyvalueDto.setCGruppe(SystemServicesFac.KEYVALUE_GTIN_ARTIKELNUMMER);
		getSystemServicesFac().updateKeyvalue(keyvalueDto);
	}

	private CounterValue getCounterValue(String companyPrefix, String mandantCnr, boolean startCounterifNotFound) throws RemoteException, EJBExceptionLP {
		KeyvalueDto[] keyvalues = getSystemServicesFac().keyvalueFindyByCGruppe(SystemServicesFac.KEYVALUE_GTIN_ARTIKELNUMMER);
		
		if (keyvalues.length == 0) {
			return startCounterifNotFound ? startNewCounter(companyPrefix, mandantCnr) : null;
		}
		
		for (KeyvalueDto kvDto : keyvalues) {
			CounterValue counterValue = new CounterValue(kvDto);
			if (!mandantCnr.equals(counterValue.getMandantCnr())) continue;
			if (!companyPrefix.equals(counterValue.getPrefix())) continue;
			
			return counterValue;
		}
		
		return startCounterifNotFound ? startNewCounter(companyPrefix, mandantCnr) : null;
	}

	private CounterValue startNewCounter(String companyPrefix, String mandantCnr) throws RemoteException, EJBExceptionLP {
		CounterValue counterValue = new CounterValue(companyPrefix, mandantCnr, 0);
		KeyvalueDto keyvalueDto = counterValue.asKeyvalueDto();
		keyvalueDto.setCGruppe(SystemServicesFac.KEYVALUE_GTIN_ARTIKELNUMMER);
		getSystemServicesFac().createKeyvalue(keyvalueDto);

		return counterValue;
	}

	private void validateGTIN13GenerationProperties(String companyPrefix, TheClientDto theClientDto) {
		Validator.dtoNotNull(theClientDto, "theClientDto");
		Validator.notEmpty(companyPrefix, "companyPrefix");
		Validator.notEmpty(theClientDto.getMandant(), "theClientDto.getMandant()");
		
		if (companyPrefix.length() != 7 && companyPrefix.length() != 9) {
			throw new IllegalArgumentException("companyPrefix '" + companyPrefix + "' has not length of 7 or 9");
		}
	}
	
	public Integer getCurrentValueItemReference(String companyPrefix, String mandantCnr) throws RemoteException, EJBExceptionLP {
		CounterValue counterValue = getCounterValue(companyPrefix, mandantCnr, false);
		return counterValue != null ? counterValue.getCount() : null;
	}
}
