package com.lp.service.edifact.schema;

public class SccInfo {
	private String planCommitmentCode;
	private String deliveryInstructionCode;
	private String frequencyCode;
	private String despatchCode;
	private String despatchTimingCode;
	
	public String getPlanCommitmentCode() {
		return planCommitmentCode;
	}
	public void setPlanCommitmentCode(String planCommitmentCode) {
		this.planCommitmentCode = planCommitmentCode;
	}
	public String getDeliveryInstructionCode() {
		return deliveryInstructionCode;
	}
	public void setDeliveryInstructionCode(String deliveryInstructionCode) {
		this.deliveryInstructionCode = deliveryInstructionCode;
	}
	public String getFrequencyCode() {
		return frequencyCode;
	}
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
	public String getDespatchCode() {
		return despatchCode;
	}
	public void setDespatchCode(String despatchCode) {
		this.despatchCode = despatchCode;
	}
	public String getDespatchTimingCode() {
		return despatchTimingCode;
	}
	public void setDespatchTimingCode(String despatchTimingCode) {
		this.despatchTimingCode = despatchTimingCode;
	}
}
