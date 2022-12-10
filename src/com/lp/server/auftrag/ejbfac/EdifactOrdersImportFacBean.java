package com.lp.server.auftrag.ejbfac;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.lp.server.auftrag.service.EdifactOrdersImportFac;
import com.lp.server.auftrag.service.EdifactOrdersImportResult;
import com.lp.server.system.service.HeliumSimpleAuthController;
import com.lp.server.system.service.HeliumSimpleAuthException;
import com.lp.server.system.service.HeliumTokenAuthController;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WebshopAuthHeader;
import com.lp.server.util.Facade;
import com.lp.server.util.bean.EdifactOrdersImportImplBean;
import com.lp.service.edifact.EdifactInterpreter;
import com.lp.service.edifact.EdifactOrdersService;
import com.lp.service.edifact.EdifactProgramOrders96a;
import com.lp.service.edifact.EdifactProgramOrders96aHV;
import com.lp.service.edifact.EdifactReader;
import com.lp.service.edifact.OrdersRepository;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.schema.EdifactMessage;
import com.lp.service.edifact.schema.UnhInfoOrders96a;

@Stateless
public class EdifactOrdersImportFacBean extends Facade implements EdifactOrdersImportFac {
	private EdifactOrdersImportImplBean implBean = new EdifactOrdersImportImplBean();
	
	@Override
	public EdifactOrdersImportResult importWebMessage(
			String message, WebshopAuthHeader webAuth) {
		HeliumSimpleAuthController authController = new HeliumTokenAuthController(
				getBenutzerFac(), getMandantFac(), getPersonalFac());
		try {
			authController.setupSessionParams(webAuth);
			return getEdifactOrdersImportFac()
					.importMessageThrow(message, authController.getWebClientDto());
		} catch (EdifactException e) {
			myLogger.error("EdifactExc", e);
			implBean.get().reportEdifactException(message, 
					e, authController.getWebClientDto());
			return EdifactOrdersImportResult.ParserError;			
		} catch (HeliumSimpleAuthException e) {
			myLogger.warn("Could not authenticate webUser", e);
			return EdifactOrdersImportResult.NoAuthorization;
		} catch (Exception e) {
			myLogger.error("Exc", e);
			implBean.get().reportApplicationException(message,
					e, authController.getWebClientDto());
			return EdifactOrdersImportResult.ApplicationError;			
		}
	}
	
	@Override
	public EdifactOrdersImportResult importMessage(
			String message, TheClientDto theClientDto) {
		try {
			return getEdifactOrdersImportFac()
					.importMessageThrow(message, theClientDto);
		} catch (EdifactException e) {
			myLogger.error("EdifactExc", e);
			implBean.get().reportEdifactException(message, e, theClientDto);
			return EdifactOrdersImportResult.ParserError;
		} catch (Exception e) {
			myLogger.error("Exc", e);
			implBean.get().reportApplicationException(message, e, theClientDto);
			return EdifactOrdersImportResult.ApplicationError;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public EdifactOrdersImportResult importMessageThrow(
			String message, TheClientDto theClientDto) throws EdifactException, IOException {
		if (message == null || message.length() == 0) {
			return EdifactOrdersImportResult.NoContent;
		}
				
		OrdersRepository repository = new OrdersRepository();
		EdifactMessage msg = readMessage(message, repository, theClientDto);
		if (msg != null && !msg.getBgms().isEmpty()) {
			repository.applyIssuerOrderNumber(msg.getBgm().getDocumentIdentifier());
		}
		return implBean.get().importOrdersMsg(message, msg, repository, theClientDto);
	}
	
	private EdifactMessage readMessage(
			String message, OrdersRepository repository, TheClientDto theClientDto) 
					throws EdifactException, IOException {
		EdifactReader reader = new EdifactReader();
		EdifactOrdersService ordersEjbService = new EdifactOrdersService(
				getArtikelFac(), getKundeFac(), theClientDto);
		
		EdifactInterpreter interpreter = new EdifactInterpreter(reader);

		EdifactProgramOrders96a pgm96a = new EdifactProgramOrders96aHV(ordersEjbService, repository);
		interpreter.registerUserProgram(new UnhInfoOrders96a(), pgm96a);
		reader.setInputStream(new ByteArrayInputStream(message.getBytes()));

		return interpreter.interpret();
	}
}
