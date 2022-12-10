package com.lp.service.edifact.schema;

public class UnhInfo {
	private String referenceNumber;
	private String type;
	private String version;
	private String release;
	private String controllingAgency;
	private String associationCode;
	private String codeListDirectoryVersion;
	private String subfunction;
	private String accessReference;
	private String sequenceTransfer;
	private String firstLastTransfer;
	private String subsetIdentification;
	private String subsetVersion;
	private String subsetRelease;
	private String subsetControllingAgency;
	private String guidelineIdentification;
	private String guidelineVersion;
	private String guidelineRelease;
	private String guidelineControllingAgency;
	private String scenarioIdentification;
	private String scenarioVersion;
	private String scenarioRelease;
	private String scenarioControllingAgency;
	
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getRelease() {
		return release;
	}
	public void setRelease(String release) {
		this.release = release;
	}
	public String getControllingAgency() {
		return controllingAgency;
	}
	public void setControllingAgency(String controllingAgency) {
		this.controllingAgency = controllingAgency;
	}
	public String getAssociationCode() {
		return associationCode;
	}
	public void setAssociationCode(String associationCode) {
		this.associationCode = associationCode;
	}
	public String getCodeListDirectoryVersion() {
		return codeListDirectoryVersion;
	}
	public void setCodeListDirectoryVersion(String codeListDirectoryVersion) {
		this.codeListDirectoryVersion = codeListDirectoryVersion;
	}
	public String getSubfunction() {
		return subfunction;
	}
	public void setSubfunction(String subfunction) {
		this.subfunction = subfunction;
	}
	public String getAccessReference() {
		return accessReference;
	}
	public void setAccessReference(String accessReference) {
		this.accessReference = accessReference;
	}
	public String getSequenceTransfer() {
		return sequenceTransfer;
	}
	public void setSequenceTransfer(String sequenceTransfer) {
		this.sequenceTransfer = sequenceTransfer;
	}
	public String getFirstLastTransfer() {
		return firstLastTransfer;
	}
	public void setFirstLastTransfer(String firstLastTransfer) {
		this.firstLastTransfer = firstLastTransfer;
	}
	public String getSubsetIdentification() {
		return subsetIdentification;
	}
	public void setSubsetIdentification(String subsetIdentification) {
		this.subsetIdentification = subsetIdentification;
	}
	public String getSubsetVersion() {
		return subsetVersion;
	}
	public void setSubsetVersion(String subsetVersion) {
		this.subsetVersion = subsetVersion;
	}
	public String getSubsetRelease() {
		return subsetRelease;
	}
	public void setSubsetRelease(String subsetRelease) {
		this.subsetRelease = subsetRelease;
	}
	public String getSubsetControllingAgency() {
		return subsetControllingAgency;
	}
	public void setSubsetControllingAgency(String subsetControllingAgency) {
		this.subsetControllingAgency = subsetControllingAgency;
	}
	public String getGuidelineIdentification() {
		return guidelineIdentification;
	}
	public void setGuidelineIdentification(String guidelineIdentification) {
		this.guidelineIdentification = guidelineIdentification;
	}
	public String getGuidelineVersion() {
		return guidelineVersion;
	}
	public void setGuidelineVersion(String guidelineVersion) {
		this.guidelineVersion = guidelineVersion;
	}
	public String getGuidelineRelease() {
		return guidelineRelease;
	}
	public void setGuidelineRelease(String guidelineRelease) {
		this.guidelineRelease = guidelineRelease;
	}
	public String getGuidelineControllingAgency() {
		return guidelineControllingAgency;
	}
	public void setGuidelineControllingAgency(String guidelineControllingAgency) {
		this.guidelineControllingAgency = guidelineControllingAgency;
	}
	public String getScenarioIdentification() {
		return scenarioIdentification;
	}
	public void setScenarioIdentification(String scenarioIdentification) {
		this.scenarioIdentification = scenarioIdentification;
	}
	public String getScenarioVersion() {
		return scenarioVersion;
	}
	public void setScenarioVersion(String scenarioVersion) {
		this.scenarioVersion = scenarioVersion;
	}
	public String getScenarioRelease() {
		return scenarioRelease;
	}
	public void setScenarioRelease(String scenarioRelease) {
		this.scenarioRelease = scenarioRelease;
	}
	public String getScenarioControllingAgency() {
		return scenarioControllingAgency;
	}
	public void setScenarioControllingAgency(String scenarioControllingAgency) {
		this.scenarioControllingAgency = scenarioControllingAgency;
	}

	public String getKeyInfo() {
		return "|" + getType() + "|" + getVersion() + "|" + getRelease() + "|.";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessReference == null) ? 0 : accessReference.hashCode());
		result = prime * result
				+ ((associationCode == null) ? 0 : associationCode.hashCode());
		result = prime
				* result
				+ ((codeListDirectoryVersion == null) ? 0
						: codeListDirectoryVersion.hashCode());
		result = prime
				* result
				+ ((controllingAgency == null) ? 0 : controllingAgency
						.hashCode());
		result = prime
				* result
				+ ((firstLastTransfer == null) ? 0 : firstLastTransfer
						.hashCode());
		result = prime
				* result
				+ ((guidelineControllingAgency == null) ? 0
						: guidelineControllingAgency.hashCode());
		result = prime
				* result
				+ ((guidelineIdentification == null) ? 0
						: guidelineIdentification.hashCode());
		result = prime
				* result
				+ ((guidelineRelease == null) ? 0 : guidelineRelease.hashCode());
		result = prime
				* result
				+ ((guidelineVersion == null) ? 0 : guidelineVersion.hashCode());
		result = prime * result
				+ ((referenceNumber == null) ? 0 : referenceNumber.hashCode());
		result = prime * result + ((release == null) ? 0 : release.hashCode());
		result = prime
				* result
				+ ((scenarioControllingAgency == null) ? 0
						: scenarioControllingAgency.hashCode());
		result = prime
				* result
				+ ((scenarioIdentification == null) ? 0
						: scenarioIdentification.hashCode());
		result = prime * result
				+ ((scenarioRelease == null) ? 0 : scenarioRelease.hashCode());
		result = prime * result
				+ ((scenarioVersion == null) ? 0 : scenarioVersion.hashCode());
		result = prime
				* result
				+ ((sequenceTransfer == null) ? 0 : sequenceTransfer.hashCode());
		result = prime * result
				+ ((subfunction == null) ? 0 : subfunction.hashCode());
		result = prime
				* result
				+ ((subsetControllingAgency == null) ? 0
						: subsetControllingAgency.hashCode());
		result = prime
				* result
				+ ((subsetIdentification == null) ? 0 : subsetIdentification
						.hashCode());
		result = prime * result
				+ ((subsetRelease == null) ? 0 : subsetRelease.hashCode());
		result = prime * result
				+ ((subsetVersion == null) ? 0 : subsetVersion.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnhInfo other = (UnhInfo) obj;
		if (accessReference == null) {
			if (other.accessReference != null)
				return false;
		} else if (!accessReference.equals(other.accessReference))
			return false;
		if (associationCode == null) {
			if (other.associationCode != null)
				return false;
		} else if (!associationCode.equals(other.associationCode))
			return false;
		if (codeListDirectoryVersion == null) {
			if (other.codeListDirectoryVersion != null)
				return false;
		} else if (!codeListDirectoryVersion
				.equals(other.codeListDirectoryVersion))
			return false;
		if (controllingAgency == null) {
			if (other.controllingAgency != null)
				return false;
		} else if (!controllingAgency.equals(other.controllingAgency))
			return false;
		if (firstLastTransfer == null) {
			if (other.firstLastTransfer != null)
				return false;
		} else if (!firstLastTransfer.equals(other.firstLastTransfer))
			return false;
		if (guidelineControllingAgency == null) {
			if (other.guidelineControllingAgency != null)
				return false;
		} else if (!guidelineControllingAgency
				.equals(other.guidelineControllingAgency))
			return false;
		if (guidelineIdentification == null) {
			if (other.guidelineIdentification != null)
				return false;
		} else if (!guidelineIdentification
				.equals(other.guidelineIdentification))
			return false;
		if (guidelineRelease == null) {
			if (other.guidelineRelease != null)
				return false;
		} else if (!guidelineRelease.equals(other.guidelineRelease))
			return false;
		if (guidelineVersion == null) {
			if (other.guidelineVersion != null)
				return false;
		} else if (!guidelineVersion.equals(other.guidelineVersion))
			return false;
		if (referenceNumber == null) {
			if (other.referenceNumber != null)
				return false;
		} else if (!referenceNumber.equals(other.referenceNumber))
			return false;
		if (release == null) {
			if (other.release != null)
				return false;
		} else if (!release.equals(other.release))
			return false;
		if (scenarioControllingAgency == null) {
			if (other.scenarioControllingAgency != null)
				return false;
		} else if (!scenarioControllingAgency
				.equals(other.scenarioControllingAgency))
			return false;
		if (scenarioIdentification == null) {
			if (other.scenarioIdentification != null)
				return false;
		} else if (!scenarioIdentification.equals(other.scenarioIdentification))
			return false;
		if (scenarioRelease == null) {
			if (other.scenarioRelease != null)
				return false;
		} else if (!scenarioRelease.equals(other.scenarioRelease))
			return false;
		if (scenarioVersion == null) {
			if (other.scenarioVersion != null)
				return false;
		} else if (!scenarioVersion.equals(other.scenarioVersion))
			return false;
		if (sequenceTransfer == null) {
			if (other.sequenceTransfer != null)
				return false;
		} else if (!sequenceTransfer.equals(other.sequenceTransfer))
			return false;
		if (subfunction == null) {
			if (other.subfunction != null)
				return false;
		} else if (!subfunction.equals(other.subfunction))
			return false;
		if (subsetControllingAgency == null) {
			if (other.subsetControllingAgency != null)
				return false;
		} else if (!subsetControllingAgency
				.equals(other.subsetControllingAgency))
			return false;
		if (subsetIdentification == null) {
			if (other.subsetIdentification != null)
				return false;
		} else if (!subsetIdentification.equals(other.subsetIdentification))
			return false;
		if (subsetRelease == null) {
			if (other.subsetRelease != null)
				return false;
		} else if (!subsetRelease.equals(other.subsetRelease))
			return false;
		if (subsetVersion == null) {
			if (other.subsetVersion != null)
				return false;
		} else if (!subsetVersion.equals(other.subsetVersion))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
}
