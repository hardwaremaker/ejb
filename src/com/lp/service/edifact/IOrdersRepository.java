package com.lp.service.edifact;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.util.HvOptional;
import com.lp.service.edifact.orders.OrdersPosition;
import com.lp.service.edifact.schema.ImdInfo;
import com.lp.service.edifact.schema.LinInfo;
import com.lp.service.edifact.schema.NadInfo;
import com.lp.service.edifact.schema.PiaInfo;

public interface IOrdersRepository {

	/**
	 * Das Bestelldatum
	 * @return
	 */
	HvOptional<Date> orderDate();
	
	/**
	 * Die Bestellnummer des Auftraggebers (K&auml;ufers)
	 * @return
	 */
	HvOptional<String> issuerOrderNumber();
	
	/**
	 * Der Auftraggeber / Besteller
	 * @return
	 */
	List<KundeDto> buyerAddress();
	
	/**
	 * Der Lieferempf&auml;nger
	 * @return
	 */
	List<KundeDto> deliveryAddress();
	
	/**
	 * Der Rechnungsempf&auml;nger
	 * @return
	 */
	List<KundeDto> invoiceAddress();
	
	/**
	 * Die Vertragsinfo</p>
	 * Wird aktuell missbr&auml;uchlich durch Besteller
	 * optional f&uuml;r Auftragsinformation verwendet.
	 * @return
	 */
	HvOptional<String> contractInfo();
	
	/**
	 * Das Bestelldatum
	 * 
	 * @param orderDate
	 */
	void applyOrderDate(Date orderDate);
	
	/**
	 * Die Belegreferenz des Bestellers selbst
	 * @param orderNumber
	 */
	void applyIssuerOrderNumber(String orderNumber);
	
	/**
	 * Die K&auml;feradresse
	 * @param buyerAddress
	 */
	void applyBuyerAddress(List<KundeDto> buyerAddress, String identifier);
	void applyBuyerNadAddress(NadInfo nadInfo);
	
	/**
	 * Die Lieferadresse
	 * 
	 * @param deliveryAddress
	 */
	void applyDeliveryAddress(List<KundeDto> deliveryAddress, String identifier);

	void applyDeliveryNadAddress(NadInfo nadInfo);


	/**
	 * Die Rechnungsadresse festlegen
	 * 
	 * @param invoiceAddress
	 */
	void applyInvoiceAddress(List<KundeDto> invoiceAddress, String identifier);

	void applyInvoiceNadAddress(NadInfo nadInfo);

	/**
	 * Den Ident-Artikel der Position festlegen
	 * @param artikelDto
	 */
	void apply(ArtikelDto artikelDto, LinInfo linInfo);

	/**
	 * Die "Additional Product Info"</br>
	 * <p>Von der Idee her nur dann verwendet, wenn
	 * der Ident-Artikel (ArtikelDto) nicht(!) gefunden
	 * werden konnte.</p>
	 * 
	 * @param piaInfo
	 */
	void applyPiaInfo(PiaInfo piaInfo);

	/**
	 * Die "Item Description"</br>
	 * <p>Von der Idee her nur dann verwendet, wenn
	 * der Ident-Artikel (ArtikelDto) nicht(!) gefunden
	 * werden konnte.</p>
	 * @param imdInfo
	 */
	void applyImdInfo(ImdInfo imdInfo);

	/**
	 * Die "Line Item" Information</br>
	 * <p>Wird nur dann verwendet, wenn der Ident-Artikel
	 * (ArtikelDto) nicht(!) gefunden werden konnte.</p>
	 * 
	 * @param linInfo
	 */
	void applyLinInfo(LinInfo linInfo);

	/**
	 * Die Positionsmenge festlegen
	 * @param quantity
	 */
	void applyQuantity(BigDecimal quantity, String unitCode);

	/**
	 * Der Nettopreis pro Stueck
	 * 
	 * @param price
	 */
	void applyPrice(BigDecimal price);

	/**
	 * Den freien Text einer Position definieren
	 * 
	 * @param positionText
	 */
	void applyText(String positionText);
	
	/**
	 * Das Lieferdatum einer "Liefereinteilung"
	 * @param deliveryDate
	 */
	void applyDeliveryDate(Date deliveryDate);
	
	/**
	 * Die Menge einer "Liefereinteilung"
	 * @param quantity
	 */
	void applyDeliveryQuantity(BigDecimal quantity);
	void pushDeliveryPosition();

	boolean hasDeliveryPosition();

	/**
	 * Das Lieferdatum der Position
	 * 
	 * @param deliveryDate
	 */
	void apply(Date deliveryDate);

	List<OrdersPosition> positions();

	/**
	 * Die Waehrung mit der die Bestellung erfolgt
	 * @param codedCurrency
	 */
	void applyOrderCurrency(String codedCurrency);

	/**
	 * Die Lieferart aus der Bestellung</br>
	 * <p>"EXW", ...</p>
	 * @param transportationCode
	 */
	void applyTransportation(String transportationCode);

	String buyerIdentifier();

	String deliveryIdentifier();

	String invoiceIdentifier();

	void applyBuyerContact(String departmentCode, String departmentName);

	void applyBuyerCommunication(String number, String channel);

	void applyContractInfo(String contractInfo);
	
	/**
	 * Die (intern) Identifikation des Kontakts (leer)
	 * @return
	 */
	HvOptional<String> buyerContactDepartmentCode();

	/**
	 * Der Name der Abteilung und/oder der Person
	 * @return
	 */
	HvOptional<String> buyerContactDepartmentName();

	/**
	 * Die Kommunikationsdaten (Telefonnummer, Faxnummer, Email, ...)
	 * @return
	 */
	HvOptional<String> buyerCommunicationNumber();

	/**
	 * Der Kanal ueber den die Kommunikation laeuft
	 * 
	 * 
	 * @return TE .. Telefon, FX ... Telefax, EM ... Electronic mail
	 *   EX ... Telefon-Durchwahl 
	 */
	HvOptional<String> buyerCommunicationChannel();


}
