package com.lp.server.auftrag.service;

public enum EdifactOrdersImportResult {
	Ok,
	Unknown,
	NoAuthorization,
	ParserError,
	ApplicationError,
	NoContent,
	UnknownSenderReceiver,
	UnknownApplication,
	UnknownDocCode,
	MissingOrderNumber,
	DuplicateOrderNumber,
	NoBuyerParty, MultipleBuyerParties
}
