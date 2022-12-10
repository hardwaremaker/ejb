package com.lp.server.system.mail.service;

import java.io.Serializable;

import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.VersandauftragDto;

public class MailTestMessage implements Serializable {
	private static final long serialVersionUID = -875588506643941149L;

	private VersandauftragDto versandauftragDto;
	private LPMailDto mailDto;
	private boolean addMailProperties = false;
	
	public MailTestMessage() {
	}
	
	public VersandauftragDto getVersandauftragDto() {
		if (versandauftragDto == null) {
			versandauftragDto = new VersandauftragDto();
		}
		return versandauftragDto;
	}
	
	public LPMailDto getMailDto() {
		return mailDto;
	}
	public void setMailDto(LPMailDto mailDto) {
		this.mailDto = mailDto;
	}
	
	public String getFrom() {
		return getVersandauftragDto().getCAbsenderadresse();
	}
	public MailTestMessage from(String from) {
		getVersandauftragDto().setCAbsenderadresse(from);
		return this;
	}
	
	public String getTo() {
		return getVersandauftragDto().getCEmpfaenger();
	}
	public MailTestMessage to(String to) {
		getVersandauftragDto().setCEmpfaenger(to);
		return this;
	}
	
	public String getSubject() {
		return getVersandauftragDto().getCBetreff();
	}
	public MailTestMessage subject(String subject) {
		getVersandauftragDto().setCBetreff(subject);
		return this;
	}
	
	public String getMessage() {
		return getVersandauftragDto().getCText();
	}
	public MailTestMessage message(String message) {
		getVersandauftragDto().setCText(message);
		return this;
	}
	
	public Integer getPersonalIIdFrom() {
		return getVersandauftragDto().getPersonalIId();
	}
	public MailTestMessage personalFrom(PersonalDto personalDto) {
		if (personalDto == null) return this;
		
		getVersandauftragDto().setPersonalIId(personalDto.getIId());
		return to(personalDto.getCEmail());
	}
	
	public MailTestMessage addMailPropertiesToMessage(boolean addMailProperties) {
		this.addMailProperties = addMailProperties;
		return this;
	}
	
	public boolean isAddMailPropertiesToMessage() {
		return addMailProperties;
	}
	
	public String asString() {
		return "From: " + getFrom() + ", To: " + getTo() + ", Subject: " + getSubject() + ", Message: " + getMessage();
	}
}
