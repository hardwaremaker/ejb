package com.lp.server.finanz.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SammelSaldoInfoDto implements Serializable {
	private static final long serialVersionUID = -1200385980380536525L;

	private final Map<KontoUvaVariante, SaldoInfoDto[]> salden;
	
	public SammelSaldoInfoDto() {
		salden = new HashMap<KontoUvaVariante, SaldoInfoDto[]>();
	}
	
	/**
	 * Einen neuen Saldo hinzufuegen</br>
	 * <p>Eine Konto hat eine Uva-Variante, da der Sammelsaldo mehrere
	 * Konten beinhaltet, koennten Konten auf unterschiedlichen Konto-Uvavarianten
	 * sein. Der Saldo existiert sozusagen pro Uva-Variante.</p>
	 * 
	 * @param saldo (des Kontos) der zum Sammelsaldo addiert werden soll
	 */
	public void add(SaldoInfoDto[] saldo) {
		SaldoInfoDto[] old = salden.get(saldo[0].getUva());
		if(old == null) {
			salden.put(saldo[0].getUva(), saldo);
		} else {
			old[0].addSaldo(saldo[0].getSaldo());
			if (old[0].getSatzDto() == null) {
				old[0].setSatzDto(saldo[0].getSatzDto());
			}
			old[1].addSaldo(saldo[1].getSaldo());
			if (old[1].getSatzDto() == null) {
				old[1].setSatzDto(saldo[1].getSatzDto());
			}
		}
	}
	
	/**
	 * Den Saldo aller Konten negieren
	 */
	public void negateSaldo() {
		for (Entry<KontoUvaVariante, SaldoInfoDto[]> element : 
			total().entrySet()) {
			SaldoInfoDto saldo[] = element.getValue();
			saldo[0].negateSaldo();
			saldo[1].negateSaldo();
		}		
	}
	
	public void addSammelSaldo(SammelSaldoInfoDto sammelSaldo) {
		for (Entry<KontoUvaVariante, SaldoInfoDto[]> element :
			sammelSaldo.total().entrySet()) {
			add(element.getValue());
		}
	}
	
	public Set<Entry<KontoUvaVariante, SaldoInfoDto[]>>entrySet() {
		return salden.entrySet();
	}
	
	public Map<KontoUvaVariante, SaldoInfoDto[]> total() {
		return salden;
	}
}
