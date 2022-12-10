package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.util.BelegAdapter;
import com.lp.server.util.BelegZahlungAdapter;

public class SepaImportResult implements ISepaImportResult, Serializable {

	private static final long serialVersionUID = 1L;

	private SepaZahlung payment;
	private Boolean totalMatch;
	private Boolean apply;
	private List<BelegAdapter> foundItems;
	private List<BelegZahlungAdapter> selectedPaymentItems;
	private List<BuchungKompakt> manualBookings;
	private Integer selectedIndex;
	private Boolean completed;
	private Boolean editedByUser;
	private Boolean ignore;
	
	public SepaImportResult() {
		totalMatch = Boolean.FALSE;
		apply = Boolean.FALSE;
		completed = Boolean.FALSE;
		editedByUser = Boolean.FALSE;
		ignore = Boolean.FALSE;
	}
	
	@Override
	public SepaZahlung getPayment() {
		return payment;
	}

	@Override
	public void setPayment(SepaZahlung payment) {
		this.payment = payment;
	}

	@Override
	public void setTotalMatch(Boolean totalMatch) {
		this.totalMatch = totalMatch;
	}

	@Override
	public Boolean getTotalMatch() {
		return totalMatch;
	}

	@Override
	public void setFoundItems(List<BelegAdapter> items) {
		foundItems = items;
	}

	@Override
	public List<BelegAdapter> getFoundItems() {
		return foundItems;
	}

	@Override
	public Boolean applyPayment() {
		return apply;
	}

	@Override
	public void setApplyPayment(Boolean apply) {
		this.apply = apply;
	}

	@Override
	public BigDecimal getRemainingAmount() {
		if (getSelectedBeleg() == null || getSelectedBeleg().getOffenerBetrag() == null) return null;
		
		return getSelectedBeleg().getOffenerBetrag().subtract(getPayment().getBetrag().getWert());
	}

	@Override
	public String getToolTipText() {
		StringBuilder builder = new StringBuilder("<html>Manuelle Zahlungen");
		for (BelegZahlungAdapter beleg : getManualPayments()) {
			builder.append("<br />");
			builder.append(beleg.getNBetragfw());
		}
		builder.append("</html>");
		return builder.toString();
	}

	@Override
	public void setManualPayments(List<BelegZahlungAdapter> items) {
		selectedPaymentItems = items;
	}

	@Override
	public List<BelegZahlungAdapter> getManualPayments() {
		return selectedPaymentItems != null ? selectedPaymentItems : new ArrayList<BelegZahlungAdapter>();
	}

	@Override
	public void setSelectedIndex(Integer index) {
		selectedIndex = index;
	}

	@Override
	public Integer getSelectedIndex() {
		return selectedIndex;
	}

	@Override
	public BelegAdapter getSelectedBeleg() {
		if (selectedIndex == null || selectedIndex < 0) return null;
		
		return foundItems.get(selectedIndex);
	}

	@Override
	public void setCompletedForSelectedBeleg(Boolean completed) {
		this.completed = completed;
	}

	@Override
	public Boolean completeSelectedBeleg() {
		return completed;
	}

	@Override
	public void setEditedByUser(Boolean edited) {
		editedByUser = edited;
	}

	@Override
	public Boolean isEditedByUser() {
		return editedByUser;
	}

	@Override
	public void setManualBooking(List<BuchungKompakt> bookings) {
		manualBookings = bookings;
	}
	
	@Override
	public List<BuchungKompakt> getManualBookings() {
		if (manualBookings == null) {
			manualBookings = new ArrayList<BuchungKompakt>();
		}
		return manualBookings;
	}

	@Override
	public Boolean hasManualPaymentsOrBookings() {
		return !getManualBookings().isEmpty() || !getManualPayments().isEmpty();
	}	

	@Override
	public Boolean ignorePayment() {
		return ignore;
	}
	
	@Override
	public void setIgnorePayment(Boolean ignore) {
		this.ignore = ignore;
	}
}
