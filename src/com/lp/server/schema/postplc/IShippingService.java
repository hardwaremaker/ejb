
package com.lp.server.schema.postplc;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.0
 * 
 */
@WebService(name = "IShippingService", targetNamespace = "http://post.ondot.at")
public interface IShippingService {


    /**
     * 
     * @param pdfData
     * @param errorMessage
     * @param errorCode
     * @param row
     * @param zplLabelData
     * @param importShipmentResult
     * @param shipmentDocuments
     */
    @WebMethod(operationName = "ImportShipment", action = "http://post.ondot.at/IShippingService/ImportShipment")
    @RequestWrapper(localName = "ImportShipment", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.ImportShipment")
    @ResponseWrapper(localName = "ImportShipmentResponse", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.ImportShipmentResponse")
    public void importShipment(
        @WebParam(name = "row", targetNamespace = "http://post.ondot.at")
        ShipmentRow row,
        @WebParam(name = "ImportShipmentResult", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<ArrayOfColloRow> importShipmentResult,
        @WebParam(name = "zplLabelData", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> zplLabelData,
        @WebParam(name = "pdfData", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> pdfData,
        @WebParam(name = "shipmentDocuments", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> shipmentDocuments,
        @WebParam(name = "errorCode", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorCode,
        @WebParam(name = "errorMessage", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorMessage);

    /**
     * 
     * @param importShipmentAndGenerateBarcodeResult
     * @param pdfData
     * @param qrCode
     * @param code128
     * @param errorMessage
     * @param errorCode
     * @param row
     * @param zplLabelData
     * @param shipmentDocuments
     */
    @WebMethod(operationName = "ImportShipmentAndGenerateBarcode", action = "http://post.ondot.at/IShippingService/ImportShipmentAndGenerateBarcode")
    @RequestWrapper(localName = "ImportShipmentAndGenerateBarcode", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.ImportShipmentAndGenerateBarcode")
    @ResponseWrapper(localName = "ImportShipmentAndGenerateBarcodeResponse", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.ImportShipmentAndGenerateBarcodeResponse")
    public void importShipmentAndGenerateBarcode(
        @WebParam(name = "row", targetNamespace = "http://post.ondot.at")
        ShipmentRow row,
        @WebParam(name = "ImportShipmentAndGenerateBarcodeResult", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<ArrayOfColloRow> importShipmentAndGenerateBarcodeResult,
        @WebParam(name = "zplLabelData", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> zplLabelData,
        @WebParam(name = "pdfData", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> pdfData,
        @WebParam(name = "shipmentDocuments", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> shipmentDocuments,
        @WebParam(name = "qrCode", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> qrCode,
        @WebParam(name = "code128", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> code128,
        @WebParam(name = "errorCode", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorCode,
        @WebParam(name = "errorMessage", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorMessage);

    /**
     * 
     * @param imageData
     * @param importShipmentReturnImageResult
     * @param errorMessage
     * @param errorCode
     * @param row
     * @param shipmentDocuments
     */
    @WebMethod(operationName = "ImportShipmentReturnImage", action = "http://post.ondot.at/IShippingService/ImportShipmentReturnImage")
    @RequestWrapper(localName = "ImportShipmentReturnImage", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.ImportShipmentReturnImage")
    @ResponseWrapper(localName = "ImportShipmentReturnImageResponse", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.ImportShipmentReturnImageResponse")
    public void importShipmentReturnImage(
        @WebParam(name = "row", targetNamespace = "http://post.ondot.at")
        ShipmentRow row,
        @WebParam(name = "ImportShipmentReturnImageResult", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<ArrayOfColloRow> importShipmentReturnImageResult,
        @WebParam(name = "imageData", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<ArrayOfstring> imageData,
        @WebParam(name = "shipmentDocuments", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> shipmentDocuments,
        @WebParam(name = "errorCode", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorCode,
        @WebParam(name = "errorMessage", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorMessage);

    /**
     * 
     * @param addresses
     * @param clientID
     * @param orgUnitGuid
     * @param errorMessage
     * @param errorCode
     * @param orgUnitID
     */
    @WebMethod(operationName = "ImportAddress", action = "http://post.ondot.at/IShippingService/ImportAddress")
    @RequestWrapper(localName = "ImportAddress", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.ImportAddress")
    @ResponseWrapper(localName = "ImportAddressResponse", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.ImportAddressResponse")
    public void importAddress(
        @WebParam(name = "clientID", targetNamespace = "http://post.ondot.at")
        Integer clientID,
        @WebParam(name = "orgUnitID", targetNamespace = "http://post.ondot.at")
        Integer orgUnitID,
        @WebParam(name = "orgUnitGuid", targetNamespace = "http://post.ondot.at")
        String orgUnitGuid,
        @WebParam(name = "addresses", targetNamespace = "http://post.ondot.at")
        ArrayOfAddressRow addresses,
        @WebParam(name = "errorCode", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorCode,
        @WebParam(name = "errorMessage", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorMessage);

    /**
     * 
     * @param clientID
     * @param orgUnitGuid
     * @param performEndOfDayResult
     * @param errorMessage
     * @param errorCode
     * @param orgUnitID
     */
    @WebMethod(operationName = "PerformEndOfDay", action = "http://post.ondot.at/IShippingService/PerformEndOfDay")
    @RequestWrapper(localName = "PerformEndOfDay", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.PerformEndOfDay")
    @ResponseWrapper(localName = "PerformEndOfDayResponse", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.PerformEndOfDayResponse")
    public void performEndOfDay(
        @WebParam(name = "clientID", targetNamespace = "http://post.ondot.at")
        Integer clientID,
        @WebParam(name = "orgUnitID", targetNamespace = "http://post.ondot.at")
        Integer orgUnitID,
        @WebParam(name = "orgUnitGuid", targetNamespace = "http://post.ondot.at")
        String orgUnitGuid,
        @WebParam(name = "PerformEndOfDayResult", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> performEndOfDayResult,
        @WebParam(name = "errorCode", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorCode,
        @WebParam(name = "errorMessage", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorMessage);

    /**
     * 
     * @param colloCodeList
     * @param clientID
     * @param errorList
     * @param orgUnitGuid
     * @param orgUnitID
     * @param performEndOfDaySelectResult
     */
    @WebMethod(operationName = "PerformEndOfDaySelect", action = "http://post.ondot.at/IShippingService/PerformEndOfDaySelect")
    @RequestWrapper(localName = "PerformEndOfDaySelect", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.PerformEndOfDaySelect")
    @ResponseWrapper(localName = "PerformEndOfDaySelectResponse", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.PerformEndOfDaySelectResponse")
    public void performEndOfDaySelect(
        @WebParam(name = "clientID", targetNamespace = "http://post.ondot.at")
        Integer clientID,
        @WebParam(name = "orgUnitID", targetNamespace = "http://post.ondot.at")
        Integer orgUnitID,
        @WebParam(name = "orgUnitGuid", targetNamespace = "http://post.ondot.at")
        String orgUnitGuid,
        @WebParam(name = "colloCodeList", targetNamespace = "http://post.ondot.at")
        ArrayOfstring colloCodeList,
        @WebParam(name = "PerformEndOfDaySelectResult", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> performEndOfDaySelectResult,
        @WebParam(name = "errorList", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<ArrayOfErrorRow> errorList);

    /**
     * 
     * @param clientID
     * @param orgUnitGuid
     * @param errorMessage
     * @param errorCode
     * @param orgUnitID
     * @param countryList
     * @param getAllowedServicesForCountryResult
     */
    @WebMethod(operationName = "GetAllowedServicesForCountry", action = "http://post.ondot.at/IShippingService/GetAllowedServicesForCountry")
    @RequestWrapper(localName = "GetAllowedServicesForCountry", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.GetAllowedServicesForCountry")
    @ResponseWrapper(localName = "GetAllowedServicesForCountryResponse", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.GetAllowedServicesForCountryResponse")
    public void getAllowedServicesForCountry(
        @WebParam(name = "clientID", targetNamespace = "http://post.ondot.at")
        Integer clientID,
        @WebParam(name = "orgUnitID", targetNamespace = "http://post.ondot.at")
        Integer orgUnitID,
        @WebParam(name = "orgUnitGuid", targetNamespace = "http://post.ondot.at")
        String orgUnitGuid,
        @WebParam(name = "countryList", targetNamespace = "http://post.ondot.at")
        ArrayOfstring countryList,
        @WebParam(name = "GetAllowedServicesForCountryResult", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<ArrayOfCarrierServiceRow> getAllowedServicesForCountryResult,
        @WebParam(name = "errorCode", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorCode,
        @WebParam(name = "errorMessage", targetNamespace = "http://post.ondot.at", mode = WebParam.Mode.OUT)
        Holder<String> errorMessage);

    /**
     * 
     * @param shipments
     * @return
     *     returns com.lp.server.schema.postplc.ArrayOfCancelShipmentResult
     */
    @WebMethod(operationName = "CancelShipments", action = "http://post.ondot.at/IShippingService/CancelShipments")
    @WebResult(name = "CancelShipmentsResult", targetNamespace = "http://post.ondot.at")
    @RequestWrapper(localName = "CancelShipments", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.CancelShipments")
    @ResponseWrapper(localName = "CancelShipmentsResponse", targetNamespace = "http://post.ondot.at", className = "com.lp.server.schema.postplc.CancelShipmentsResponse")
    public ArrayOfCancelShipmentResult cancelShipments(
        @WebParam(name = "shipments", targetNamespace = "http://post.ondot.at")
        ArrayOfCancelShipmentRow shipments);

}
