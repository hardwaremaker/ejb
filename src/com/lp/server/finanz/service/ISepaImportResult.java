package com.lp.server.finanz.service;

import java.math.BigDecimal;
import java.util.List;

import com.lp.server.util.BelegAdapter;
import com.lp.server.util.BelegZahlungAdapter;

public interface ISepaImportResult {

	/**
	 * Zahlungsinfo
	 * 
	 * @return Zahlungsobjekt
	 */
	public SepaZahlung getPayment();
	
	public void setPayment(SepaZahlung payment);
	
	/**
	 * Eindeutiger Treffer?
	 * 
	 * @param totalMatch
	 */
	public void setTotalMatch(Boolean totalMatch);
	
	public Boolean getTotalMatch();
	
	/**
	 * Gefundene Treffer
	 * 
	 * @param items
	 */
	public void setFoundItems(List<BelegAdapter> items);
	
	public List<BelegAdapter> getFoundItems();
	
	public void setSelectedIndex(Integer index);
	
	public Integer getSelectedIndex();
	
	public BelegAdapter getSelectedBeleg();
	
	
	/**
	 * Ausgewaehlte, getaetigte Zahlungen
	 * 
	 * @param items
	 */
	public void setManualPayments(List<BelegZahlungAdapter> items);
	
	public List<BelegZahlungAdapter> getManualPayments();
	
	public void setManualBooking(List<BuchungKompakt> bookings);
	
	public List<BuchungKompakt> getManualBookings();
	
	public Boolean hasManualPaymentsOrBookings();
	
	
	public void setCompletedForSelectedBeleg(Boolean completed);
	
	public Boolean completeSelectedBeleg();
	
	/**
	 * Restbetrag
	 * 
	 * @return Restbetrag
	 */
	public BigDecimal getRemainingAmount();
	
	/**
	 * Zahlung uebernehmen?
	 * 
	 * @return true, wenn die Zahlung zu uebernehmen ist
	 */
	public Boolean applyPayment();
	
	public void setApplyPayment(Boolean apply);
	

	public String getToolTipText();
	
	public void setEditedByUser(Boolean edited);
	
	public Boolean isEditedByUser();

	/**
	 * Soll die Zahlung ignoriert werden?
	 * 
	 * @return true, wenn die Zahlung nicht verbucht werden soll
	 */
	public Boolean ignorePayment();
	
	public void setIgnorePayment(Boolean ignore);
}
