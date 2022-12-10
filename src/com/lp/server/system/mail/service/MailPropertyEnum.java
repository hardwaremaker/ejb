package com.lp.server.system.mail.service;

public enum MailPropertyEnum {
	//Imap
	HvImapAdmin("hv.imap.admin"),
	HvImapPwd("hv.imap.pwd"),
	HvImapFolder("hv.imap.sentfolder"),
	MailImapHost("mail.imap.host"),
	MailImapPort("mail.imap.port"),
	MailImapSocketFactoryClass("mail.imap.socketFactory.class"),
	MailImapSocketFactoryPort("mail.imap.socketFactory.port"),
	MailImapSslEnable("mail.imap.ssl.enable"),
	MailImapStarttlsEnable("mail.imap.starttls.enable"),
	MailStoreProtocol("mail.store.protocol"),

	//Smtp
	HvSmtpPwd("hv.smtp.pwd"),
	MailSmtpAuth("mail.smtp.auth"),
	MailSmtpHost("mail.smtp.host"),
	MailSmtpPort("mail.smtp.port"),
	MailSmtpUser("mail.smtp.user"),
	MailSmtpSocketFactoryClass("mail.smtp.socketFactory.class"),
	MailSmtpSocketFactoryPort("mail.smtp.socketFactory.port"),
	MailSmtpSslEnable("mail.smtp.ssl.enable"),
	MailSmtpStarttlsEnable("mail.smtp.starttls.enable"),
	MailTransportProtocol("mail.transport.protocol");

	private String value;
	
	private MailPropertyEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static MailPropertyEnum fromString(String text) {
		if (text != null) {
			for (MailPropertyEnum param : MailPropertyEnum.values()) {
				if (text.equalsIgnoreCase(param.value)) {
					return param;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}
	
	public static boolean isEnumFromString(String text) {
		try {
			fromString(text);
			return true;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}
}
