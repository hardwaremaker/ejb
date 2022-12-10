
package com.lp.server.schema.postplc;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.lp.server.schema.postplc package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _ColloCodeRow_QNAME = new QName("http://post.ondot.at", "ColloCodeRow");
    private final static QName _AdditionalInformationRow_QNAME = new QName("http://post.ondot.at", "AdditionalInformationRow");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _ArrayOfAdditionalInformationRow_QNAME = new QName("http://post.ondot.at", "ArrayOfAdditionalInformationRow");
    private final static QName _ErrorRow_QNAME = new QName("http://post.ondot.at", "ErrorRow");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _ArrayOfColloCodeRow_QNAME = new QName("http://post.ondot.at", "ArrayOfColloCodeRow");
    private final static QName _ColloRow_QNAME = new QName("http://post.ondot.at", "ColloRow");
    private final static QName _ArrayOfColloArticleRow_QNAME = new QName("http://post.ondot.at", "ArrayOfColloArticleRow");
    private final static QName _ColloArticleRow_QNAME = new QName("http://post.ondot.at", "ColloArticleRow");
    private final static QName _AdditionalInformationResult_QNAME = new QName("http://post.ondot.at", "AdditionalInformationResult");
    private final static QName _ArrayOfstring_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfstring");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _ArrayOfCancelShipmentResult_QNAME = new QName("http://post.ondot.at", "ArrayOfCancelShipmentResult");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _CancelShipmentResult_QNAME = new QName("http://post.ondot.at", "CancelShipmentResult");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _ArrayOfShipmentDocumentEntry_QNAME = new QName("http://Core.Model", "ArrayOfShipmentDocumentEntry");
    private final static QName _ArrayOfAdditionalInformationResult_QNAME = new QName("http://post.ondot.at", "ArrayOfAdditionalInformationResult");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _PrinterRow_QNAME = new QName("http://post.ondot.at", "PrinterRow");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _ArrayOfAddressRow_QNAME = new QName("http://post.ondot.at", "ArrayOfAddressRow");
    private final static QName _AddressRow_QNAME = new QName("http://post.ondot.at", "AddressRow");
    private final static QName _ArrayOfCancelShipmentRow_QNAME = new QName("http://post.ondot.at", "ArrayOfCancelShipmentRow");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _CancelShipmentRow_QNAME = new QName("http://post.ondot.at", "CancelShipmentRow");
    private final static QName _ArrayOfErrorRow_QNAME = new QName("http://post.ondot.at", "ArrayOfErrorRow");
    private final static QName _ArrayOfColloRow_QNAME = new QName("http://post.ondot.at", "ArrayOfColloRow");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _EntityBase_QNAME = new QName("http://Core.Model", "EntityBase");
    private final static QName _ArrayOfCarrierServiceRow_QNAME = new QName("http://post.ondot.at", "ArrayOfCarrierServiceRow");
    private final static QName _CarrierServiceRow_QNAME = new QName("http://post.ondot.at", "CarrierServiceRow");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _ShipmentDocumentEntry_QNAME = new QName("http://Core.Model", "ShipmentDocumentEntry");
    private final static QName _ShipmentRow_QNAME = new QName("http://post.ondot.at", "ShipmentRow");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _ImportShipmentAndGenerateBarcodeResponseErrorCode_QNAME = new QName("http://post.ondot.at", "errorCode");
    private final static QName _ImportShipmentAndGenerateBarcodeResponseCode128_QNAME = new QName("http://post.ondot.at", "code128");
    private final static QName _ImportShipmentAndGenerateBarcodeResponseErrorMessage_QNAME = new QName("http://post.ondot.at", "errorMessage");
    private final static QName _ImportShipmentAndGenerateBarcodeResponseShipmentDocuments_QNAME = new QName("http://post.ondot.at", "shipmentDocuments");
    private final static QName _ImportShipmentAndGenerateBarcodeResponseImportShipmentAndGenerateBarcodeResult_QNAME = new QName("http://post.ondot.at", "ImportShipmentAndGenerateBarcodeResult");
    private final static QName _ImportShipmentAndGenerateBarcodeResponseZplLabelData_QNAME = new QName("http://post.ondot.at", "zplLabelData");
    private final static QName _ImportShipmentAndGenerateBarcodeResponsePdfData_QNAME = new QName("http://post.ondot.at", "pdfData");
    private final static QName _ImportShipmentAndGenerateBarcodeResponseQrCode_QNAME = new QName("http://post.ondot.at", "qrCode");
    private final static QName _PrinterRowEncoding_QNAME = new QName("http://post.ondot.at", "Encoding");
    private final static QName _PrinterRowLanguageID_QNAME = new QName("http://post.ondot.at", "LanguageID");
    private final static QName _PrinterRowLabelFormatID_QNAME = new QName("http://post.ondot.at", "LabelFormatID");
    private final static QName _PrinterRowPaperLayoutID_QNAME = new QName("http://post.ondot.at", "PaperLayoutID");
    private final static QName _ColloCodeRowOUCarrierThirdPartyID_QNAME = new QName("http://post.ondot.at", "OUCarrierThirdPartyID");
    private final static QName _ColloCodeRowCode_QNAME = new QName("http://post.ondot.at", "Code");
    private final static QName _ColloRowColloArticleList_QNAME = new QName("http://post.ondot.at", "ColloArticleList");
    private final static QName _ColloRowHeight_QNAME = new QName("http://post.ondot.at", "Height");
    private final static QName _ColloRowWidth_QNAME = new QName("http://post.ondot.at", "Width");
    private final static QName _ColloRowWeight_QNAME = new QName("http://post.ondot.at", "Weight");
    private final static QName _ColloRowColloCodeList_QNAME = new QName("http://post.ondot.at", "ColloCodeList");
    private final static QName _ColloRowLength_QNAME = new QName("http://post.ondot.at", "Length");
    private final static QName _ImportAddressAddresses_QNAME = new QName("http://post.ondot.at", "addresses");
    private final static QName _CancelShipmentResultErrorCode_QNAME = new QName("http://post.ondot.at", "ErrorCode");
    private final static QName _CancelShipmentResultErrorMessage_QNAME = new QName("http://post.ondot.at", "ErrorMessage");
    private final static QName _CancelShipmentResultNumber_QNAME = new QName("http://post.ondot.at", "Number");
    private final static QName _ImportShipmentAndGenerateBarcodeRow_QNAME = new QName("http://post.ondot.at", "row");
    private final static QName _ShipmentDocumentEntryNumber_QNAME = new QName("http://Core.Model", "Number");
    private final static QName _ShipmentDocumentEntryDocumentDate_QNAME = new QName("http://Core.Model", "DocumentDate");
    private final static QName _ImportShipmentReturnImageResponseImportShipmentReturnImageResult_QNAME = new QName("http://post.ondot.at", "ImportShipmentReturnImageResult");
    private final static QName _ImportShipmentReturnImageResponseImageData_QNAME = new QName("http://post.ondot.at", "imageData");
    private final static QName _GetAllowedServicesForCountryCountryList_QNAME = new QName("http://post.ondot.at", "countryList");
    private final static QName _CancelShipmentsResponseCancelShipmentsResult_QNAME = new QName("http://post.ondot.at", "CancelShipmentsResult");
    private final static QName _ColloArticleRowQuantity_QNAME = new QName("http://post.ondot.at", "Quantity");
    private final static QName _ColloArticleRowCountryOfOriginID_QNAME = new QName("http://post.ondot.at", "CountryOfOriginID");
    private final static QName _ColloArticleRowArticleName_QNAME = new QName("http://post.ondot.at", "ArticleName");
    private final static QName _ColloArticleRowHSTariffNumber_QNAME = new QName("http://post.ondot.at", "HSTariffNumber");
    private final static QName _ColloArticleRowCurrencyID_QNAME = new QName("http://post.ondot.at", "CurrencyID");
    private final static QName _ColloArticleRowConsumerUnitNetWeight_QNAME = new QName("http://post.ondot.at", "ConsumerUnitNetWeight");
    private final static QName _ColloArticleRowValueOfGoodsPerUnit_QNAME = new QName("http://post.ondot.at", "ValueOfGoodsPerUnit");
    private final static QName _ColloArticleRowCustomsOptionID_QNAME = new QName("http://post.ondot.at", "CustomsOptionID");
    private final static QName _ColloArticleRowUnitID_QNAME = new QName("http://post.ondot.at", "UnitID");
    private final static QName _ColloArticleRowArticleNumber_QNAME = new QName("http://post.ondot.at", "ArticleNumber");
    private final static QName _ShipmentRowCostCenterThirdPartyID_QNAME = new QName("http://post.ondot.at", "CostCenterThirdPartyID");
    private final static QName _ShipmentRowAlternativeReturnOrgUnitAddress_QNAME = new QName("http://post.ondot.at", "AlternativeReturnOrgUnitAddress");
    private final static QName _ShipmentRowRefBarcodeType_QNAME = new QName("http://post.ondot.at", "RefBarcodeType");
    private final static QName _ShipmentRowCustomerProduct_QNAME = new QName("http://post.ondot.at", "CustomerProduct");
    private final static QName _ShipmentRowReturnOptionID_QNAME = new QName("http://post.ondot.at", "ReturnOptionID");
    private final static QName _ShipmentRowMovementReferenceNumber_QNAME = new QName("http://post.ondot.at", "MovementReferenceNumber");
    private final static QName _ShipmentRowDeliveryServiceThirdPartyID_QNAME = new QName("http://post.ondot.at", "DeliveryServiceThirdPartyID");
    private final static QName _ShipmentRowColloList_QNAME = new QName("http://post.ondot.at", "ColloList");
    private final static QName _ShipmentRowDeliveryInstruction_QNAME = new QName("http://post.ondot.at", "DeliveryInstruction");
    private final static QName _ShipmentRowFeatureList_QNAME = new QName("http://post.ondot.at", "FeatureList");
    private final static QName _ShipmentRowReturnDays_QNAME = new QName("http://post.ondot.at", "ReturnDays");
    private final static QName _ShipmentRowShippingDateTimeFrom_QNAME = new QName("http://post.ondot.at", "ShippingDateTimeFrom");
    private final static QName _ShipmentRowBusinessDocumentEntryList_QNAME = new QName("http://post.ondot.at", "BusinessDocumentEntryList");
    private final static QName _ShipmentRowShippingDateTimeTo_QNAME = new QName("http://post.ondot.at", "ShippingDateTimeTo");
    private final static QName _ShipmentRowOUShipperAddress_QNAME = new QName("http://post.ondot.at", "OUShipperAddress");
    private final static QName _ShipmentRowOUShipperReference2_QNAME = new QName("http://post.ondot.at", "OUShipperReference2");
    private final static QName _ShipmentRowReturnModeID_QNAME = new QName("http://post.ondot.at", "ReturnModeID");
    private final static QName _ShipmentRowCustomsDescription_QNAME = new QName("http://post.ondot.at", "CustomsDescription");
    private final static QName _ShipmentRowOUShipperReference1_QNAME = new QName("http://post.ondot.at", "OUShipperReference1");
    private final static QName _ShipmentRowPrinterObject_QNAME = new QName("http://post.ondot.at", "PrinterObject");
    private final static QName _ShipmentRowShipmentDocumentEntryList_QNAME = new QName("http://post.ondot.at", "ShipmentDocumentEntryList");
    private final static QName _ShipmentRowOURecipientAddress_QNAME = new QName("http://post.ondot.at", "OURecipientAddress");
    private final static QName _AdditionalInformationResultThirdPartyID_QNAME = new QName("http://post.ondot.at", "ThirdPartyID");
    private final static QName _AdditionalInformationResultName_QNAME = new QName("http://post.ondot.at", "Name");
    private final static QName _CancelShipmentsShipments_QNAME = new QName("http://post.ondot.at", "shipments");
    private final static QName _PerformEndOfDayResponsePerformEndOfDayResult_QNAME = new QName("http://post.ondot.at", "PerformEndOfDayResult");
    private final static QName _AddressRowPostalCode_QNAME = new QName("http://post.ondot.at", "PostalCode");
    private final static QName _AddressRowCity_QNAME = new QName("http://post.ondot.at", "City");
    private final static QName _AddressRowCountryID_QNAME = new QName("http://post.ondot.at", "CountryID");
    private final static QName _AddressRowEmail_QNAME = new QName("http://post.ondot.at", "Email");
    private final static QName _AddressRowHomepage_QNAME = new QName("http://post.ondot.at", "Homepage");
    private final static QName _AddressRowHouseNumber_QNAME = new QName("http://post.ondot.at", "HouseNumber");
    private final static QName _AddressRowPersonalTaxNumber_QNAME = new QName("http://post.ondot.at", "PersonalTaxNumber");
    private final static QName _AddressRowEORINumber_QNAME = new QName("http://post.ondot.at", "EORINumber");
    private final static QName _AddressRowProvinceCode_QNAME = new QName("http://post.ondot.at", "ProvinceCode");
    private final static QName _AddressRowFax_QNAME = new QName("http://post.ondot.at", "Fax");
    private final static QName _AddressRowName2_QNAME = new QName("http://post.ondot.at", "Name2");
    private final static QName _AddressRowName1_QNAME = new QName("http://post.ondot.at", "Name1");
    private final static QName _AddressRowVATID_QNAME = new QName("http://post.ondot.at", "VATID");
    private final static QName _AddressRowAddressLine2_QNAME = new QName("http://post.ondot.at", "AddressLine2");
    private final static QName _AddressRowName4_QNAME = new QName("http://post.ondot.at", "Name4");
    private final static QName _AddressRowTel1_QNAME = new QName("http://post.ondot.at", "Tel1");
    private final static QName _AddressRowName3_QNAME = new QName("http://post.ondot.at", "Name3");
    private final static QName _AddressRowTel2_QNAME = new QName("http://post.ondot.at", "Tel2");
    private final static QName _AddressRowAddressLine1_QNAME = new QName("http://post.ondot.at", "AddressLine1");
    private final static QName _ImportShipmentResponseImportShipmentResult_QNAME = new QName("http://post.ondot.at", "ImportShipmentResult");
    private final static QName _GetAllowedServicesForCountryResponseGetAllowedServicesForCountryResult_QNAME = new QName("http://post.ondot.at", "GetAllowedServicesForCountryResult");
    private final static QName _CarrierServiceRowOrderID_QNAME = new QName("http://post.ondot.at", "OrderID");
    private final static QName _ErrorRowMessage_QNAME = new QName("http://post.ondot.at", "Message");
    private final static QName _ErrorRowReference_QNAME = new QName("http://post.ondot.at", "Reference");
    private final static QName _PerformEndOfDaySelectColloCodeList_QNAME = new QName("http://post.ondot.at", "colloCodeList");
    private final static QName _AdditionalInformationRowValue4_QNAME = new QName("http://post.ondot.at", "Value4");
    private final static QName _AdditionalInformationRowValue3_QNAME = new QName("http://post.ondot.at", "Value3");
    private final static QName _AdditionalInformationRowValue2_QNAME = new QName("http://post.ondot.at", "Value2");
    private final static QName _AdditionalInformationRowValue1_QNAME = new QName("http://post.ondot.at", "Value1");
    private final static QName _PerformEndOfDaySelectResponsePerformEndOfDaySelectResult_QNAME = new QName("http://post.ondot.at", "PerformEndOfDaySelectResult");
    private final static QName _PerformEndOfDaySelectResponseErrorList_QNAME = new QName("http://post.ondot.at", "errorList");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.lp.server.schema.postplc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArrayOfAdditionalInformationRow }
     * 
     */
    public ArrayOfAdditionalInformationRow createArrayOfAdditionalInformationRow() {
        return new ArrayOfAdditionalInformationRow();
    }

    /**
     * Create an instance of {@link ErrorRow }
     * 
     */
    public ErrorRow createErrorRow() {
        return new ErrorRow();
    }

    /**
     * Create an instance of {@link ColloRow }
     * 
     */
    public ColloRow createColloRow() {
        return new ColloRow();
    }

    /**
     * Create an instance of {@link ArrayOfColloCodeRow }
     * 
     */
    public ArrayOfColloCodeRow createArrayOfColloCodeRow() {
        return new ArrayOfColloCodeRow();
    }

    /**
     * Create an instance of {@link GetAllowedServicesForCountry }
     * 
     */
    public GetAllowedServicesForCountry createGetAllowedServicesForCountry() {
        return new GetAllowedServicesForCountry();
    }

    /**
     * Create an instance of {@link ArrayOfstring }
     * 
     */
    public ArrayOfstring createArrayOfstring() {
        return new ArrayOfstring();
    }

    /**
     * Create an instance of {@link ColloCodeRow }
     * 
     */
    public ColloCodeRow createColloCodeRow() {
        return new ColloCodeRow();
    }

    /**
     * Create an instance of {@link AdditionalInformationRow }
     * 
     */
    public AdditionalInformationRow createAdditionalInformationRow() {
        return new AdditionalInformationRow();
    }

    /**
     * Create an instance of {@link ImportShipment }
     * 
     */
    public ImportShipment createImportShipment() {
        return new ImportShipment();
    }

    /**
     * Create an instance of {@link ShipmentRow }
     * 
     */
    public ShipmentRow createShipmentRow() {
        return new ShipmentRow();
    }

    /**
     * Create an instance of {@link CancelShipments }
     * 
     */
    public CancelShipments createCancelShipments() {
        return new CancelShipments();
    }

    /**
     * Create an instance of {@link ArrayOfCancelShipmentRow }
     * 
     */
    public ArrayOfCancelShipmentRow createArrayOfCancelShipmentRow() {
        return new ArrayOfCancelShipmentRow();
    }

    /**
     * Create an instance of {@link CancelShipmentResult }
     * 
     */
    public CancelShipmentResult createCancelShipmentResult() {
        return new CancelShipmentResult();
    }

    /**
     * Create an instance of {@link CancelShipmentsResponse }
     * 
     */
    public CancelShipmentsResponse createCancelShipmentsResponse() {
        return new CancelShipmentsResponse();
    }

    /**
     * Create an instance of {@link ArrayOfCancelShipmentResult }
     * 
     */
    public ArrayOfCancelShipmentResult createArrayOfCancelShipmentResult() {
        return new ArrayOfCancelShipmentResult();
    }

    /**
     * Create an instance of {@link PerformEndOfDaySelect }
     * 
     */
    public PerformEndOfDaySelect createPerformEndOfDaySelect() {
        return new PerformEndOfDaySelect();
    }

    /**
     * Create an instance of {@link ImportAddress }
     * 
     */
    public ImportAddress createImportAddress() {
        return new ImportAddress();
    }

    /**
     * Create an instance of {@link ArrayOfAddressRow }
     * 
     */
    public ArrayOfAddressRow createArrayOfAddressRow() {
        return new ArrayOfAddressRow();
    }

    /**
     * Create an instance of {@link ArrayOfColloArticleRow }
     * 
     */
    public ArrayOfColloArticleRow createArrayOfColloArticleRow() {
        return new ArrayOfColloArticleRow();
    }

    /**
     * Create an instance of {@link ColloArticleRow }
     * 
     */
    public ColloArticleRow createColloArticleRow() {
        return new ColloArticleRow();
    }

    /**
     * Create an instance of {@link PerformEndOfDayResponse }
     * 
     */
    public PerformEndOfDayResponse createPerformEndOfDayResponse() {
        return new PerformEndOfDayResponse();
    }

    /**
     * Create an instance of {@link AdditionalInformationResult }
     * 
     */
    public AdditionalInformationResult createAdditionalInformationResult() {
        return new AdditionalInformationResult();
    }

    /**
     * Create an instance of {@link PerformEndOfDaySelectResponse }
     * 
     */
    public PerformEndOfDaySelectResponse createPerformEndOfDaySelectResponse() {
        return new PerformEndOfDaySelectResponse();
    }

    /**
     * Create an instance of {@link ArrayOfErrorRow }
     * 
     */
    public ArrayOfErrorRow createArrayOfErrorRow() {
        return new ArrayOfErrorRow();
    }

    /**
     * Create an instance of {@link AddressRow }
     * 
     */
    public AddressRow createAddressRow() {
        return new AddressRow();
    }

    /**
     * Create an instance of {@link ImportAddressResponse }
     * 
     */
    public ImportAddressResponse createImportAddressResponse() {
        return new ImportAddressResponse();
    }

    /**
     * Create an instance of {@link ArrayOfAdditionalInformationResult }
     * 
     */
    public ArrayOfAdditionalInformationResult createArrayOfAdditionalInformationResult() {
        return new ArrayOfAdditionalInformationResult();
    }

    /**
     * Create an instance of {@link PrinterRow }
     * 
     */
    public PrinterRow createPrinterRow() {
        return new PrinterRow();
    }

    /**
     * Create an instance of {@link ImportShipmentReturnImage }
     * 
     */
    public ImportShipmentReturnImage createImportShipmentReturnImage() {
        return new ImportShipmentReturnImage();
    }

    /**
     * Create an instance of {@link ImportShipmentReturnImageResponse }
     * 
     */
    public ImportShipmentReturnImageResponse createImportShipmentReturnImageResponse() {
        return new ImportShipmentReturnImageResponse();
    }

    /**
     * Create an instance of {@link ArrayOfColloRow }
     * 
     */
    public ArrayOfColloRow createArrayOfColloRow() {
        return new ArrayOfColloRow();
    }

    /**
     * Create an instance of {@link GetAllowedServicesForCountryResponse }
     * 
     */
    public GetAllowedServicesForCountryResponse createGetAllowedServicesForCountryResponse() {
        return new GetAllowedServicesForCountryResponse();
    }

    /**
     * Create an instance of {@link ArrayOfCarrierServiceRow }
     * 
     */
    public ArrayOfCarrierServiceRow createArrayOfCarrierServiceRow() {
        return new ArrayOfCarrierServiceRow();
    }

    /**
     * Create an instance of {@link PerformEndOfDay }
     * 
     */
    public PerformEndOfDay createPerformEndOfDay() {
        return new PerformEndOfDay();
    }

    /**
     * Create an instance of {@link ImportShipmentAndGenerateBarcodeResponse }
     * 
     */
    public ImportShipmentAndGenerateBarcodeResponse createImportShipmentAndGenerateBarcodeResponse() {
        return new ImportShipmentAndGenerateBarcodeResponse();
    }

    /**
     * Create an instance of {@link ImportShipmentResponse }
     * 
     */
    public ImportShipmentResponse createImportShipmentResponse() {
        return new ImportShipmentResponse();
    }

    /**
     * Create an instance of {@link CancelShipmentRow }
     * 
     */
    public CancelShipmentRow createCancelShipmentRow() {
        return new CancelShipmentRow();
    }

    /**
     * Create an instance of {@link ImportShipmentAndGenerateBarcode }
     * 
     */
    public ImportShipmentAndGenerateBarcode createImportShipmentAndGenerateBarcode() {
        return new ImportShipmentAndGenerateBarcode();
    }

    /**
     * Create an instance of {@link CarrierServiceRow }
     * 
     */
    public CarrierServiceRow createCarrierServiceRow() {
        return new CarrierServiceRow();
    }

    /**
     * Create an instance of {@link EntityBase }
     * 
     */
    public EntityBase createEntityBase() {
        return new EntityBase();
    }

    /**
     * Create an instance of {@link ArrayOfShipmentDocumentEntry }
     * 
     */
    public ArrayOfShipmentDocumentEntry createArrayOfShipmentDocumentEntry() {
        return new ArrayOfShipmentDocumentEntry();
    }

    /**
     * Create an instance of {@link ShipmentDocumentEntry }
     * 
     */
    public ShipmentDocumentEntry createShipmentDocumentEntry() {
        return new ShipmentDocumentEntry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ColloCodeRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ColloCodeRow")
    public JAXBElement<ColloCodeRow> createColloCodeRow(ColloCodeRow value) {
        return new JAXBElement<ColloCodeRow>(_ColloCodeRow_QNAME, ColloCodeRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdditionalInformationRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "AdditionalInformationRow")
    public JAXBElement<AdditionalInformationRow> createAdditionalInformationRow(AdditionalInformationRow value) {
        return new JAXBElement<AdditionalInformationRow>(_AdditionalInformationRow_QNAME, AdditionalInformationRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAdditionalInformationRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArrayOfAdditionalInformationRow")
    public JAXBElement<ArrayOfAdditionalInformationRow> createArrayOfAdditionalInformationRow(ArrayOfAdditionalInformationRow value) {
        return new JAXBElement<ArrayOfAdditionalInformationRow>(_ArrayOfAdditionalInformationRow_QNAME, ArrayOfAdditionalInformationRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErrorRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ErrorRow")
    public JAXBElement<ErrorRow> createErrorRow(ErrorRow value) {
        return new JAXBElement<ErrorRow>(_ErrorRow_QNAME, ErrorRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfColloCodeRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArrayOfColloCodeRow")
    public JAXBElement<ArrayOfColloCodeRow> createArrayOfColloCodeRow(ArrayOfColloCodeRow value) {
        return new JAXBElement<ArrayOfColloCodeRow>(_ArrayOfColloCodeRow_QNAME, ArrayOfColloCodeRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ColloRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ColloRow")
    public JAXBElement<ColloRow> createColloRow(ColloRow value) {
        return new JAXBElement<ColloRow>(_ColloRow_QNAME, ColloRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfColloArticleRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArrayOfColloArticleRow")
    public JAXBElement<ArrayOfColloArticleRow> createArrayOfColloArticleRow(ArrayOfColloArticleRow value) {
        return new JAXBElement<ArrayOfColloArticleRow>(_ArrayOfColloArticleRow_QNAME, ArrayOfColloArticleRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ColloArticleRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ColloArticleRow")
    public JAXBElement<ColloArticleRow> createColloArticleRow(ColloArticleRow value) {
        return new JAXBElement<ColloArticleRow>(_ColloArticleRow_QNAME, ColloArticleRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdditionalInformationResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "AdditionalInformationResult")
    public JAXBElement<AdditionalInformationResult> createAdditionalInformationResult(AdditionalInformationResult value) {
        return new JAXBElement<AdditionalInformationResult>(_AdditionalInformationResult_QNAME, AdditionalInformationResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays", name = "ArrayOfstring")
    public JAXBElement<ArrayOfstring> createArrayOfstring(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_ArrayOfstring_QNAME, ArrayOfstring.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCancelShipmentResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArrayOfCancelShipmentResult")
    public JAXBElement<ArrayOfCancelShipmentResult> createArrayOfCancelShipmentResult(ArrayOfCancelShipmentResult value) {
        return new JAXBElement<ArrayOfCancelShipmentResult>(_ArrayOfCancelShipmentResult_QNAME, ArrayOfCancelShipmentResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelShipmentResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "CancelShipmentResult")
    public JAXBElement<CancelShipmentResult> createCancelShipmentResult(CancelShipmentResult value) {
        return new JAXBElement<CancelShipmentResult>(_CancelShipmentResult_QNAME, CancelShipmentResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfShipmentDocumentEntry }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Core.Model", name = "ArrayOfShipmentDocumentEntry")
    public JAXBElement<ArrayOfShipmentDocumentEntry> createArrayOfShipmentDocumentEntry(ArrayOfShipmentDocumentEntry value) {
        return new JAXBElement<ArrayOfShipmentDocumentEntry>(_ArrayOfShipmentDocumentEntry_QNAME, ArrayOfShipmentDocumentEntry.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAdditionalInformationResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArrayOfAdditionalInformationResult")
    public JAXBElement<ArrayOfAdditionalInformationResult> createArrayOfAdditionalInformationResult(ArrayOfAdditionalInformationResult value) {
        return new JAXBElement<ArrayOfAdditionalInformationResult>(_ArrayOfAdditionalInformationResult_QNAME, ArrayOfAdditionalInformationResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrinterRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "PrinterRow")
    public JAXBElement<PrinterRow> createPrinterRow(PrinterRow value) {
        return new JAXBElement<PrinterRow>(_PrinterRow_QNAME, PrinterRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAddressRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArrayOfAddressRow")
    public JAXBElement<ArrayOfAddressRow> createArrayOfAddressRow(ArrayOfAddressRow value) {
        return new JAXBElement<ArrayOfAddressRow>(_ArrayOfAddressRow_QNAME, ArrayOfAddressRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "AddressRow")
    public JAXBElement<AddressRow> createAddressRow(AddressRow value) {
        return new JAXBElement<AddressRow>(_AddressRow_QNAME, AddressRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCancelShipmentRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArrayOfCancelShipmentRow")
    public JAXBElement<ArrayOfCancelShipmentRow> createArrayOfCancelShipmentRow(ArrayOfCancelShipmentRow value) {
        return new JAXBElement<ArrayOfCancelShipmentRow>(_ArrayOfCancelShipmentRow_QNAME, ArrayOfCancelShipmentRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelShipmentRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "CancelShipmentRow")
    public JAXBElement<CancelShipmentRow> createCancelShipmentRow(CancelShipmentRow value) {
        return new JAXBElement<CancelShipmentRow>(_CancelShipmentRow_QNAME, CancelShipmentRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErrorRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArrayOfErrorRow")
    public JAXBElement<ArrayOfErrorRow> createArrayOfErrorRow(ArrayOfErrorRow value) {
        return new JAXBElement<ArrayOfErrorRow>(_ArrayOfErrorRow_QNAME, ArrayOfErrorRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArrayOfColloRow")
    public JAXBElement<ArrayOfColloRow> createArrayOfColloRow(ArrayOfColloRow value) {
        return new JAXBElement<ArrayOfColloRow>(_ArrayOfColloRow_QNAME, ArrayOfColloRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityBase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Core.Model", name = "EntityBase")
    public JAXBElement<EntityBase> createEntityBase(EntityBase value) {
        return new JAXBElement<EntityBase>(_EntityBase_QNAME, EntityBase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCarrierServiceRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArrayOfCarrierServiceRow")
    public JAXBElement<ArrayOfCarrierServiceRow> createArrayOfCarrierServiceRow(ArrayOfCarrierServiceRow value) {
        return new JAXBElement<ArrayOfCarrierServiceRow>(_ArrayOfCarrierServiceRow_QNAME, ArrayOfCarrierServiceRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CarrierServiceRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "CarrierServiceRow")
    public JAXBElement<CarrierServiceRow> createCarrierServiceRow(CarrierServiceRow value) {
        return new JAXBElement<CarrierServiceRow>(_CarrierServiceRow_QNAME, CarrierServiceRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShipmentDocumentEntry }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Core.Model", name = "ShipmentDocumentEntry")
    public JAXBElement<ShipmentDocumentEntry> createShipmentDocumentEntry(ShipmentDocumentEntry value) {
        return new JAXBElement<ShipmentDocumentEntry>(_ShipmentDocumentEntry_QNAME, ShipmentDocumentEntry.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShipmentRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ShipmentRow")
    public JAXBElement<ShipmentRow> createShipmentRow(ShipmentRow value) {
        return new JAXBElement<ShipmentRow>(_ShipmentRow_QNAME, ShipmentRow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorCode", scope = ImportShipmentAndGenerateBarcodeResponse.class)
    public JAXBElement<String> createImportShipmentAndGenerateBarcodeResponseErrorCode(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorCode_QNAME, String.class, ImportShipmentAndGenerateBarcodeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "code128", scope = ImportShipmentAndGenerateBarcodeResponse.class)
    public JAXBElement<String> createImportShipmentAndGenerateBarcodeResponseCode128(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseCode128_QNAME, String.class, ImportShipmentAndGenerateBarcodeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorMessage", scope = ImportShipmentAndGenerateBarcodeResponse.class)
    public JAXBElement<String> createImportShipmentAndGenerateBarcodeResponseErrorMessage(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorMessage_QNAME, String.class, ImportShipmentAndGenerateBarcodeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "shipmentDocuments", scope = ImportShipmentAndGenerateBarcodeResponse.class)
    public JAXBElement<String> createImportShipmentAndGenerateBarcodeResponseShipmentDocuments(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseShipmentDocuments_QNAME, String.class, ImportShipmentAndGenerateBarcodeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ImportShipmentAndGenerateBarcodeResult", scope = ImportShipmentAndGenerateBarcodeResponse.class)
    public JAXBElement<ArrayOfColloRow> createImportShipmentAndGenerateBarcodeResponseImportShipmentAndGenerateBarcodeResult(ArrayOfColloRow value) {
        return new JAXBElement<ArrayOfColloRow>(_ImportShipmentAndGenerateBarcodeResponseImportShipmentAndGenerateBarcodeResult_QNAME, ArrayOfColloRow.class, ImportShipmentAndGenerateBarcodeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "zplLabelData", scope = ImportShipmentAndGenerateBarcodeResponse.class)
    public JAXBElement<String> createImportShipmentAndGenerateBarcodeResponseZplLabelData(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseZplLabelData_QNAME, String.class, ImportShipmentAndGenerateBarcodeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "pdfData", scope = ImportShipmentAndGenerateBarcodeResponse.class)
    public JAXBElement<String> createImportShipmentAndGenerateBarcodeResponsePdfData(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponsePdfData_QNAME, String.class, ImportShipmentAndGenerateBarcodeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "qrCode", scope = ImportShipmentAndGenerateBarcodeResponse.class)
    public JAXBElement<String> createImportShipmentAndGenerateBarcodeResponseQrCode(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseQrCode_QNAME, String.class, ImportShipmentAndGenerateBarcodeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Encoding", scope = PrinterRow.class)
    public JAXBElement<String> createPrinterRowEncoding(String value) {
        return new JAXBElement<String>(_PrinterRowEncoding_QNAME, String.class, PrinterRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "LanguageID", scope = PrinterRow.class)
    public JAXBElement<String> createPrinterRowLanguageID(String value) {
        return new JAXBElement<String>(_PrinterRowLanguageID_QNAME, String.class, PrinterRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "LabelFormatID", scope = PrinterRow.class)
    public JAXBElement<String> createPrinterRowLabelFormatID(String value) {
        return new JAXBElement<String>(_PrinterRowLabelFormatID_QNAME, String.class, PrinterRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "PaperLayoutID", scope = PrinterRow.class)
    public JAXBElement<String> createPrinterRowPaperLayoutID(String value) {
        return new JAXBElement<String>(_PrinterRowPaperLayoutID_QNAME, String.class, PrinterRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorCode", scope = ImportAddressResponse.class)
    public JAXBElement<String> createImportAddressResponseErrorCode(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorCode_QNAME, String.class, ImportAddressResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorMessage", scope = ImportAddressResponse.class)
    public JAXBElement<String> createImportAddressResponseErrorMessage(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorMessage_QNAME, String.class, ImportAddressResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "OUCarrierThirdPartyID", scope = ColloCodeRow.class)
    public JAXBElement<String> createColloCodeRowOUCarrierThirdPartyID(String value) {
        return new JAXBElement<String>(_ColloCodeRowOUCarrierThirdPartyID_QNAME, String.class, ColloCodeRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Code", scope = ColloCodeRow.class)
    public JAXBElement<String> createColloCodeRowCode(String value) {
        return new JAXBElement<String>(_ColloCodeRowCode_QNAME, String.class, ColloCodeRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfColloArticleRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ColloArticleList", scope = ColloRow.class)
    public JAXBElement<ArrayOfColloArticleRow> createColloRowColloArticleList(ArrayOfColloArticleRow value) {
        return new JAXBElement<ArrayOfColloArticleRow>(_ColloRowColloArticleList_QNAME, ArrayOfColloArticleRow.class, ColloRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Height", scope = ColloRow.class)
    public JAXBElement<Integer> createColloRowHeight(Integer value) {
        return new JAXBElement<Integer>(_ColloRowHeight_QNAME, Integer.class, ColloRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Width", scope = ColloRow.class)
    public JAXBElement<Integer> createColloRowWidth(Integer value) {
        return new JAXBElement<Integer>(_ColloRowWidth_QNAME, Integer.class, ColloRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Weight", scope = ColloRow.class)
    public JAXBElement<BigDecimal> createColloRowWeight(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ColloRowWeight_QNAME, BigDecimal.class, ColloRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfColloCodeRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ColloCodeList", scope = ColloRow.class)
    public JAXBElement<ArrayOfColloCodeRow> createColloRowColloCodeList(ArrayOfColloCodeRow value) {
        return new JAXBElement<ArrayOfColloCodeRow>(_ColloRowColloCodeList_QNAME, ArrayOfColloCodeRow.class, ColloRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Length", scope = ColloRow.class)
    public JAXBElement<Integer> createColloRowLength(Integer value) {
        return new JAXBElement<Integer>(_ColloRowLength_QNAME, Integer.class, ColloRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAddressRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "addresses", scope = ImportAddress.class)
    public JAXBElement<ArrayOfAddressRow> createImportAddressAddresses(ArrayOfAddressRow value) {
        return new JAXBElement<ArrayOfAddressRow>(_ImportAddressAddresses_QNAME, ArrayOfAddressRow.class, ImportAddress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ErrorCode", scope = CancelShipmentResult.class)
    public JAXBElement<String> createCancelShipmentResultErrorCode(String value) {
        return new JAXBElement<String>(_CancelShipmentResultErrorCode_QNAME, String.class, CancelShipmentResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ErrorMessage", scope = CancelShipmentResult.class)
    public JAXBElement<String> createCancelShipmentResultErrorMessage(String value) {
        return new JAXBElement<String>(_CancelShipmentResultErrorMessage_QNAME, String.class, CancelShipmentResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Number", scope = CancelShipmentResult.class)
    public JAXBElement<String> createCancelShipmentResultNumber(String value) {
        return new JAXBElement<String>(_CancelShipmentResultNumber_QNAME, String.class, CancelShipmentResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShipmentRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "row", scope = ImportShipmentAndGenerateBarcode.class)
    public JAXBElement<ShipmentRow> createImportShipmentAndGenerateBarcodeRow(ShipmentRow value) {
        return new JAXBElement<ShipmentRow>(_ImportShipmentAndGenerateBarcodeRow_QNAME, ShipmentRow.class, ImportShipmentAndGenerateBarcode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShipmentRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "row", scope = ImportShipmentReturnImage.class)
    public JAXBElement<ShipmentRow> createImportShipmentReturnImageRow(ShipmentRow value) {
        return new JAXBElement<ShipmentRow>(_ImportShipmentAndGenerateBarcodeRow_QNAME, ShipmentRow.class, ImportShipmentReturnImage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Core.Model", name = "Number", scope = ShipmentDocumentEntry.class)
    public JAXBElement<String> createShipmentDocumentEntryNumber(String value) {
        return new JAXBElement<String>(_ShipmentDocumentEntryNumber_QNAME, String.class, ShipmentDocumentEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Core.Model", name = "DocumentDate", scope = ShipmentDocumentEntry.class)
    public JAXBElement<XMLGregorianCalendar> createShipmentDocumentEntryDocumentDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ShipmentDocumentEntryDocumentDate_QNAME, XMLGregorianCalendar.class, ShipmentDocumentEntry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorCode", scope = ImportShipmentReturnImageResponse.class)
    public JAXBElement<String> createImportShipmentReturnImageResponseErrorCode(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorCode_QNAME, String.class, ImportShipmentReturnImageResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorMessage", scope = ImportShipmentReturnImageResponse.class)
    public JAXBElement<String> createImportShipmentReturnImageResponseErrorMessage(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorMessage_QNAME, String.class, ImportShipmentReturnImageResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "shipmentDocuments", scope = ImportShipmentReturnImageResponse.class)
    public JAXBElement<String> createImportShipmentReturnImageResponseShipmentDocuments(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseShipmentDocuments_QNAME, String.class, ImportShipmentReturnImageResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ImportShipmentReturnImageResult", scope = ImportShipmentReturnImageResponse.class)
    public JAXBElement<ArrayOfColloRow> createImportShipmentReturnImageResponseImportShipmentReturnImageResult(ArrayOfColloRow value) {
        return new JAXBElement<ArrayOfColloRow>(_ImportShipmentReturnImageResponseImportShipmentReturnImageResult_QNAME, ArrayOfColloRow.class, ImportShipmentReturnImageResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "imageData", scope = ImportShipmentReturnImageResponse.class)
    public JAXBElement<ArrayOfstring> createImportShipmentReturnImageResponseImageData(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_ImportShipmentReturnImageResponseImageData_QNAME, ArrayOfstring.class, ImportShipmentReturnImageResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ColloCodeList", scope = CancelShipmentRow.class)
    public JAXBElement<ArrayOfstring> createCancelShipmentRowColloCodeList(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_ColloRowColloCodeList_QNAME, ArrayOfstring.class, CancelShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Number", scope = CancelShipmentRow.class)
    public JAXBElement<String> createCancelShipmentRowNumber(String value) {
        return new JAXBElement<String>(_CancelShipmentResultNumber_QNAME, String.class, CancelShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "countryList", scope = GetAllowedServicesForCountry.class)
    public JAXBElement<ArrayOfstring> createGetAllowedServicesForCountryCountryList(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_GetAllowedServicesForCountryCountryList_QNAME, ArrayOfstring.class, GetAllowedServicesForCountry.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCancelShipmentResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "CancelShipmentsResult", scope = CancelShipmentsResponse.class)
    public JAXBElement<ArrayOfCancelShipmentResult> createCancelShipmentsResponseCancelShipmentsResult(ArrayOfCancelShipmentResult value) {
        return new JAXBElement<ArrayOfCancelShipmentResult>(_CancelShipmentsResponseCancelShipmentsResult_QNAME, ArrayOfCancelShipmentResult.class, CancelShipmentsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Quantity", scope = ColloArticleRow.class)
    public JAXBElement<BigDecimal> createColloArticleRowQuantity(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ColloArticleRowQuantity_QNAME, BigDecimal.class, ColloArticleRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "CountryOfOriginID", scope = ColloArticleRow.class)
    public JAXBElement<String> createColloArticleRowCountryOfOriginID(String value) {
        return new JAXBElement<String>(_ColloArticleRowCountryOfOriginID_QNAME, String.class, ColloArticleRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArticleName", scope = ColloArticleRow.class)
    public JAXBElement<String> createColloArticleRowArticleName(String value) {
        return new JAXBElement<String>(_ColloArticleRowArticleName_QNAME, String.class, ColloArticleRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "HSTariffNumber", scope = ColloArticleRow.class)
    public JAXBElement<String> createColloArticleRowHSTariffNumber(String value) {
        return new JAXBElement<String>(_ColloArticleRowHSTariffNumber_QNAME, String.class, ColloArticleRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "CurrencyID", scope = ColloArticleRow.class)
    public JAXBElement<String> createColloArticleRowCurrencyID(String value) {
        return new JAXBElement<String>(_ColloArticleRowCurrencyID_QNAME, String.class, ColloArticleRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ConsumerUnitNetWeight", scope = ColloArticleRow.class)
    public JAXBElement<BigDecimal> createColloArticleRowConsumerUnitNetWeight(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ColloArticleRowConsumerUnitNetWeight_QNAME, BigDecimal.class, ColloArticleRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ValueOfGoodsPerUnit", scope = ColloArticleRow.class)
    public JAXBElement<BigDecimal> createColloArticleRowValueOfGoodsPerUnit(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ColloArticleRowValueOfGoodsPerUnit_QNAME, BigDecimal.class, ColloArticleRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "CustomsOptionID", scope = ColloArticleRow.class)
    public JAXBElement<Integer> createColloArticleRowCustomsOptionID(Integer value) {
        return new JAXBElement<Integer>(_ColloArticleRowCustomsOptionID_QNAME, Integer.class, ColloArticleRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "UnitID", scope = ColloArticleRow.class)
    public JAXBElement<String> createColloArticleRowUnitID(String value) {
        return new JAXBElement<String>(_ColloArticleRowUnitID_QNAME, String.class, ColloArticleRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ArticleNumber", scope = ColloArticleRow.class)
    public JAXBElement<String> createColloArticleRowArticleNumber(String value) {
        return new JAXBElement<String>(_ColloArticleRowArticleNumber_QNAME, String.class, ColloArticleRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "CostCenterThirdPartyID", scope = ShipmentRow.class)
    public JAXBElement<String> createShipmentRowCostCenterThirdPartyID(String value) {
        return new JAXBElement<String>(_ShipmentRowCostCenterThirdPartyID_QNAME, String.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "AlternativeReturnOrgUnitAddress", scope = ShipmentRow.class)
    public JAXBElement<AddressRow> createShipmentRowAlternativeReturnOrgUnitAddress(AddressRow value) {
        return new JAXBElement<AddressRow>(_ShipmentRowAlternativeReturnOrgUnitAddress_QNAME, AddressRow.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "RefBarcodeType", scope = ShipmentRow.class)
    public JAXBElement<String> createShipmentRowRefBarcodeType(String value) {
        return new JAXBElement<String>(_ShipmentRowRefBarcodeType_QNAME, String.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "CustomerProduct", scope = ShipmentRow.class)
    public JAXBElement<String> createShipmentRowCustomerProduct(String value) {
        return new JAXBElement<String>(_ShipmentRowCustomerProduct_QNAME, String.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ReturnOptionID", scope = ShipmentRow.class)
    public JAXBElement<Integer> createShipmentRowReturnOptionID(Integer value) {
        return new JAXBElement<Integer>(_ShipmentRowReturnOptionID_QNAME, Integer.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "MovementReferenceNumber", scope = ShipmentRow.class)
    public JAXBElement<String> createShipmentRowMovementReferenceNumber(String value) {
        return new JAXBElement<String>(_ShipmentRowMovementReferenceNumber_QNAME, String.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "DeliveryServiceThirdPartyID", scope = ShipmentRow.class)
    public JAXBElement<String> createShipmentRowDeliveryServiceThirdPartyID(String value) {
        return new JAXBElement<String>(_ShipmentRowDeliveryServiceThirdPartyID_QNAME, String.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ColloList", scope = ShipmentRow.class)
    public JAXBElement<ArrayOfColloRow> createShipmentRowColloList(ArrayOfColloRow value) {
        return new JAXBElement<ArrayOfColloRow>(_ShipmentRowColloList_QNAME, ArrayOfColloRow.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "DeliveryInstruction", scope = ShipmentRow.class)
    public JAXBElement<String> createShipmentRowDeliveryInstruction(String value) {
        return new JAXBElement<String>(_ShipmentRowDeliveryInstruction_QNAME, String.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAdditionalInformationRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "FeatureList", scope = ShipmentRow.class)
    public JAXBElement<ArrayOfAdditionalInformationRow> createShipmentRowFeatureList(ArrayOfAdditionalInformationRow value) {
        return new JAXBElement<ArrayOfAdditionalInformationRow>(_ShipmentRowFeatureList_QNAME, ArrayOfAdditionalInformationRow.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ReturnDays", scope = ShipmentRow.class)
    public JAXBElement<Integer> createShipmentRowReturnDays(Integer value) {
        return new JAXBElement<Integer>(_ShipmentRowReturnDays_QNAME, Integer.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ShippingDateTimeFrom", scope = ShipmentRow.class)
    public JAXBElement<XMLGregorianCalendar> createShipmentRowShippingDateTimeFrom(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ShipmentRowShippingDateTimeFrom_QNAME, XMLGregorianCalendar.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "BusinessDocumentEntryList", scope = ShipmentRow.class)
    public JAXBElement<ArrayOfstring> createShipmentRowBusinessDocumentEntryList(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_ShipmentRowBusinessDocumentEntryList_QNAME, ArrayOfstring.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ShippingDateTimeTo", scope = ShipmentRow.class)
    public JAXBElement<XMLGregorianCalendar> createShipmentRowShippingDateTimeTo(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ShipmentRowShippingDateTimeTo_QNAME, XMLGregorianCalendar.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "OUShipperAddress", scope = ShipmentRow.class)
    public JAXBElement<AddressRow> createShipmentRowOUShipperAddress(AddressRow value) {
        return new JAXBElement<AddressRow>(_ShipmentRowOUShipperAddress_QNAME, AddressRow.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "OUShipperReference2", scope = ShipmentRow.class)
    public JAXBElement<String> createShipmentRowOUShipperReference2(String value) {
        return new JAXBElement<String>(_ShipmentRowOUShipperReference2_QNAME, String.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ReturnModeID", scope = ShipmentRow.class)
    public JAXBElement<Integer> createShipmentRowReturnModeID(Integer value) {
        return new JAXBElement<Integer>(_ShipmentRowReturnModeID_QNAME, Integer.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "CustomsDescription", scope = ShipmentRow.class)
    public JAXBElement<String> createShipmentRowCustomsDescription(String value) {
        return new JAXBElement<String>(_ShipmentRowCustomsDescription_QNAME, String.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "OUShipperReference1", scope = ShipmentRow.class)
    public JAXBElement<String> createShipmentRowOUShipperReference1(String value) {
        return new JAXBElement<String>(_ShipmentRowOUShipperReference1_QNAME, String.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Number", scope = ShipmentRow.class)
    public JAXBElement<String> createShipmentRowNumber(String value) {
        return new JAXBElement<String>(_CancelShipmentResultNumber_QNAME, String.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PrinterRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "PrinterObject", scope = ShipmentRow.class)
    public JAXBElement<PrinterRow> createShipmentRowPrinterObject(PrinterRow value) {
        return new JAXBElement<PrinterRow>(_ShipmentRowPrinterObject_QNAME, PrinterRow.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfShipmentDocumentEntry }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ShipmentDocumentEntryList", scope = ShipmentRow.class)
    public JAXBElement<ArrayOfShipmentDocumentEntry> createShipmentRowShipmentDocumentEntryList(ArrayOfShipmentDocumentEntry value) {
        return new JAXBElement<ArrayOfShipmentDocumentEntry>(_ShipmentRowShipmentDocumentEntryList_QNAME, ArrayOfShipmentDocumentEntry.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "OURecipientAddress", scope = ShipmentRow.class)
    public JAXBElement<AddressRow> createShipmentRowOURecipientAddress(AddressRow value) {
        return new JAXBElement<AddressRow>(_ShipmentRowOURecipientAddress_QNAME, AddressRow.class, ShipmentRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ThirdPartyID", scope = AdditionalInformationResult.class)
    public JAXBElement<String> createAdditionalInformationResultThirdPartyID(String value) {
        return new JAXBElement<String>(_AdditionalInformationResultThirdPartyID_QNAME, String.class, AdditionalInformationResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Name", scope = AdditionalInformationResult.class)
    public JAXBElement<String> createAdditionalInformationResultName(String value) {
        return new JAXBElement<String>(_AdditionalInformationResultName_QNAME, String.class, AdditionalInformationResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCancelShipmentRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "shipments", scope = CancelShipments.class)
    public JAXBElement<ArrayOfCancelShipmentRow> createCancelShipmentsShipments(ArrayOfCancelShipmentRow value) {
        return new JAXBElement<ArrayOfCancelShipmentRow>(_CancelShipmentsShipments_QNAME, ArrayOfCancelShipmentRow.class, CancelShipments.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "PerformEndOfDayResult", scope = PerformEndOfDayResponse.class)
    public JAXBElement<String> createPerformEndOfDayResponsePerformEndOfDayResult(String value) {
        return new JAXBElement<String>(_PerformEndOfDayResponsePerformEndOfDayResult_QNAME, String.class, PerformEndOfDayResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorCode", scope = PerformEndOfDayResponse.class)
    public JAXBElement<String> createPerformEndOfDayResponseErrorCode(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorCode_QNAME, String.class, PerformEndOfDayResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorMessage", scope = PerformEndOfDayResponse.class)
    public JAXBElement<String> createPerformEndOfDayResponseErrorMessage(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorMessage_QNAME, String.class, PerformEndOfDayResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "PostalCode", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowPostalCode(String value) {
        return new JAXBElement<String>(_AddressRowPostalCode_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "City", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowCity(String value) {
        return new JAXBElement<String>(_AddressRowCity_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ThirdPartyID", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowThirdPartyID(String value) {
        return new JAXBElement<String>(_AdditionalInformationResultThirdPartyID_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "CountryID", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowCountryID(String value) {
        return new JAXBElement<String>(_AddressRowCountryID_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Email", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowEmail(String value) {
        return new JAXBElement<String>(_AddressRowEmail_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Homepage", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowHomepage(String value) {
        return new JAXBElement<String>(_AddressRowHomepage_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "HouseNumber", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowHouseNumber(String value) {
        return new JAXBElement<String>(_AddressRowHouseNumber_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "PersonalTaxNumber", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowPersonalTaxNumber(String value) {
        return new JAXBElement<String>(_AddressRowPersonalTaxNumber_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "EORINumber", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowEORINumber(String value) {
        return new JAXBElement<String>(_AddressRowEORINumber_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ProvinceCode", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowProvinceCode(String value) {
        return new JAXBElement<String>(_AddressRowProvinceCode_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Fax", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowFax(String value) {
        return new JAXBElement<String>(_AddressRowFax_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Name2", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowName2(String value) {
        return new JAXBElement<String>(_AddressRowName2_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Name1", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowName1(String value) {
        return new JAXBElement<String>(_AddressRowName1_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "VATID", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowVATID(String value) {
        return new JAXBElement<String>(_AddressRowVATID_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "AddressLine2", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowAddressLine2(String value) {
        return new JAXBElement<String>(_AddressRowAddressLine2_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Name4", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowName4(String value) {
        return new JAXBElement<String>(_AddressRowName4_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Tel1", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowTel1(String value) {
        return new JAXBElement<String>(_AddressRowTel1_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Name3", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowName3(String value) {
        return new JAXBElement<String>(_AddressRowName3_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Tel2", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowTel2(String value) {
        return new JAXBElement<String>(_AddressRowTel2_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "AddressLine1", scope = AddressRow.class)
    public JAXBElement<String> createAddressRowAddressLine1(String value) {
        return new JAXBElement<String>(_AddressRowAddressLine1_QNAME, String.class, AddressRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorCode", scope = ImportShipmentResponse.class)
    public JAXBElement<String> createImportShipmentResponseErrorCode(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorCode_QNAME, String.class, ImportShipmentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorMessage", scope = ImportShipmentResponse.class)
    public JAXBElement<String> createImportShipmentResponseErrorMessage(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorMessage_QNAME, String.class, ImportShipmentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfColloRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ImportShipmentResult", scope = ImportShipmentResponse.class)
    public JAXBElement<ArrayOfColloRow> createImportShipmentResponseImportShipmentResult(ArrayOfColloRow value) {
        return new JAXBElement<ArrayOfColloRow>(_ImportShipmentResponseImportShipmentResult_QNAME, ArrayOfColloRow.class, ImportShipmentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "shipmentDocuments", scope = ImportShipmentResponse.class)
    public JAXBElement<String> createImportShipmentResponseShipmentDocuments(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseShipmentDocuments_QNAME, String.class, ImportShipmentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "zplLabelData", scope = ImportShipmentResponse.class)
    public JAXBElement<String> createImportShipmentResponseZplLabelData(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseZplLabelData_QNAME, String.class, ImportShipmentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "pdfData", scope = ImportShipmentResponse.class)
    public JAXBElement<String> createImportShipmentResponsePdfData(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponsePdfData_QNAME, String.class, ImportShipmentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorCode", scope = GetAllowedServicesForCountryResponse.class)
    public JAXBElement<String> createGetAllowedServicesForCountryResponseErrorCode(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorCode_QNAME, String.class, GetAllowedServicesForCountryResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorMessage", scope = GetAllowedServicesForCountryResponse.class)
    public JAXBElement<String> createGetAllowedServicesForCountryResponseErrorMessage(String value) {
        return new JAXBElement<String>(_ImportShipmentAndGenerateBarcodeResponseErrorMessage_QNAME, String.class, GetAllowedServicesForCountryResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCarrierServiceRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "GetAllowedServicesForCountryResult", scope = GetAllowedServicesForCountryResponse.class)
    public JAXBElement<ArrayOfCarrierServiceRow> createGetAllowedServicesForCountryResponseGetAllowedServicesForCountryResult(ArrayOfCarrierServiceRow value) {
        return new JAXBElement<ArrayOfCarrierServiceRow>(_GetAllowedServicesForCountryResponseGetAllowedServicesForCountryResult_QNAME, ArrayOfCarrierServiceRow.class, GetAllowedServicesForCountryResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShipmentRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "row", scope = ImportShipment.class)
    public JAXBElement<ShipmentRow> createImportShipmentRow(ShipmentRow value) {
        return new JAXBElement<ShipmentRow>(_ImportShipmentAndGenerateBarcodeRow_QNAME, ShipmentRow.class, ImportShipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAdditionalInformationResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "FeatureList", scope = CarrierServiceRow.class)
    public JAXBElement<ArrayOfAdditionalInformationResult> createCarrierServiceRowFeatureList(ArrayOfAdditionalInformationResult value) {
        return new JAXBElement<ArrayOfAdditionalInformationResult>(_ShipmentRowFeatureList_QNAME, ArrayOfAdditionalInformationResult.class, CarrierServiceRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "OrderID", scope = CarrierServiceRow.class)
    public JAXBElement<Integer> createCarrierServiceRowOrderID(Integer value) {
        return new JAXBElement<Integer>(_CarrierServiceRowOrderID_QNAME, Integer.class, CarrierServiceRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ThirdPartyID", scope = CarrierServiceRow.class)
    public JAXBElement<String> createCarrierServiceRowThirdPartyID(String value) {
        return new JAXBElement<String>(_AdditionalInformationResultThirdPartyID_QNAME, String.class, CarrierServiceRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Name", scope = CarrierServiceRow.class)
    public JAXBElement<String> createCarrierServiceRowName(String value) {
        return new JAXBElement<String>(_AdditionalInformationResultName_QNAME, String.class, CarrierServiceRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Code", scope = ErrorRow.class)
    public JAXBElement<String> createErrorRowCode(String value) {
        return new JAXBElement<String>(_ColloCodeRowCode_QNAME, String.class, ErrorRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Message", scope = ErrorRow.class)
    public JAXBElement<String> createErrorRowMessage(String value) {
        return new JAXBElement<String>(_ErrorRowMessage_QNAME, String.class, ErrorRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Reference", scope = ErrorRow.class)
    public JAXBElement<String> createErrorRowReference(String value) {
        return new JAXBElement<String>(_ErrorRowReference_QNAME, String.class, ErrorRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "colloCodeList", scope = PerformEndOfDaySelect.class)
    public JAXBElement<ArrayOfstring> createPerformEndOfDaySelectColloCodeList(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_PerformEndOfDaySelectColloCodeList_QNAME, ArrayOfstring.class, PerformEndOfDaySelect.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Value4", scope = AdditionalInformationRow.class)
    public JAXBElement<String> createAdditionalInformationRowValue4(String value) {
        return new JAXBElement<String>(_AdditionalInformationRowValue4_QNAME, String.class, AdditionalInformationRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Value3", scope = AdditionalInformationRow.class)
    public JAXBElement<String> createAdditionalInformationRowValue3(String value) {
        return new JAXBElement<String>(_AdditionalInformationRowValue3_QNAME, String.class, AdditionalInformationRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Value2", scope = AdditionalInformationRow.class)
    public JAXBElement<String> createAdditionalInformationRowValue2(String value) {
        return new JAXBElement<String>(_AdditionalInformationRowValue2_QNAME, String.class, AdditionalInformationRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Value1", scope = AdditionalInformationRow.class)
    public JAXBElement<String> createAdditionalInformationRowValue1(String value) {
        return new JAXBElement<String>(_AdditionalInformationRowValue1_QNAME, String.class, AdditionalInformationRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "ThirdPartyID", scope = AdditionalInformationRow.class)
    public JAXBElement<String> createAdditionalInformationRowThirdPartyID(String value) {
        return new JAXBElement<String>(_AdditionalInformationResultThirdPartyID_QNAME, String.class, AdditionalInformationRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "Name", scope = AdditionalInformationRow.class)
    public JAXBElement<String> createAdditionalInformationRowName(String value) {
        return new JAXBElement<String>(_AdditionalInformationResultName_QNAME, String.class, AdditionalInformationRow.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "PerformEndOfDaySelectResult", scope = PerformEndOfDaySelectResponse.class)
    public JAXBElement<String> createPerformEndOfDaySelectResponsePerformEndOfDaySelectResult(String value) {
        return new JAXBElement<String>(_PerformEndOfDaySelectResponsePerformEndOfDaySelectResult_QNAME, String.class, PerformEndOfDaySelectResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErrorRow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://post.ondot.at", name = "errorList", scope = PerformEndOfDaySelectResponse.class)
    public JAXBElement<ArrayOfErrorRow> createPerformEndOfDaySelectResponseErrorList(ArrayOfErrorRow value) {
        return new JAXBElement<ArrayOfErrorRow>(_PerformEndOfDaySelectResponseErrorList_QNAME, ArrayOfErrorRow.class, PerformEndOfDaySelectResponse.class, value);
    }

}
