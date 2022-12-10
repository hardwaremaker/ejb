package com.lp.server.fertigung.bl;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.lp.server.schema.vendidata.articles.ObjectFactory;
import com.lp.server.schema.vendidata.articles.XMLArticles;
import com.lp.server.util.XMLMarshaller;

public class VendidataArticlesMarshaller extends XMLMarshaller<XMLArticles> {
	
	String xsdArticles = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<xsd:schema xmlns=\"http://www.vendidata.com/XML/Schema/Articles\"\r\n" + 
			"    targetNamespace=\"http://www.vendidata.com/XML/Schema/Articles\" version=\"2.0.2\"\r\n" + 
			"    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\r\n" + 
			"    xmlns:co=\"http://www.vendidata.com/XML/Schema/CommonObjects\">\r\n" + 
			"    <xsd:import namespace=\"http://www.vendidata.com/XML/Schema/CommonObjects\"\r\n" + 
			"        schemaLocation=\"commonobjects.xsd\"/>\r\n" + 
			"    <xsd:element name=\"Articles\" type=\"Articles\"/>\r\n" + 
			"    <xsd:complexType name=\"Article\">\r\n" + 
			"        <xsd:annotation>\r\n" + 
			"            <xsd:documentation>A valid article either contain a SalesArticle elemnent or a BaseArcticle element or both.</xsd:documentation>\r\n" + 
			"        </xsd:annotation>\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"BaseArticle\" type=\"BaseArticle\"/>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"SalesArticle\" type=\"SalesArticle\"/>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"        <xsd:attribute name=\"referenceCriterion\" type=\"ReferenceCriterion\" use=\"required\">\r\n" + 
			"            <xsd:annotation>\r\n" + 
			"                <xsd:documentation>The reference criterion determines which element/attribute should be used to check if the article is a new one or if it should be updated.</xsd:documentation>\r\n" + 
			"            </xsd:annotation>\r\n" + 
			"        </xsd:attribute>\r\n" + 
			"        <xsd:attribute name=\"rowid\">\r\n" + 
			"            <xsd:annotation>\r\n" + 
			"                <xsd:documentation>Do not use this attribute for importing data. The importer will ignore this attribute. It is only used by the exporter.</xsd:documentation>\r\n" + 
			"            </xsd:annotation>\r\n" + 
			"            <xsd:simpleType>\r\n" + 
			"                <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                    <xsd:minInclusive value=\"-2147483640\"/>\r\n" + 
			"                    <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                </xsd:restriction>\r\n" + 
			"            </xsd:simpleType>\r\n" + 
			"        </xsd:attribute>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"SalesArticle\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"ItemNumber\" type=\"ItemNumber\"/>\r\n" + 
			"            <xsd:element name=\"Price\" type=\"xsd:double\"/>\r\n" + 
			"            <xsd:element name=\"Name\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"Group\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"VATRate\" type=\"xsd:double\"/>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"        <xsd:attribute name=\"isSalesArticle\" type=\"xsd:boolean\" use=\"required\">\r\n" + 
			"            <xsd:annotation>\r\n" + 
			"                <xsd:documentation>Denominates if this article is an active sales article. The SalesArticle's subelements and attributes can be provided, regardless if the article is active or not.</xsd:documentation>\r\n" + 
			"            </xsd:annotation>\r\n" + 
			"        </xsd:attribute>\r\n" + 
			"        <xsd:attribute default=\"ACTIVE\" name=\"status\" type=\"ArticleStatus\" use=\"optional\"/>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"BaseArticle\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"ItemNumber\" type=\"ItemNumber\"/>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"ErpId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"Price\" type=\"xsd:double\"/>\r\n" + 
			"            <xsd:element name=\"Name\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"Group\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"VATRate\" type=\"xsd:double\"/>\r\n" + 
			"            <xsd:sequence minOccurs=\"0\">\r\n" + 
			"                <xsd:element maxOccurs=\"unbounded\" minOccurs=\"1\" type=\"co:Barcode\" name=\"Barcode\"/>\r\n" + 
			"            </xsd:sequence>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"        <xsd:attribute name=\"isBaseArticle\" type=\"xsd:boolean\" use=\"required\">\r\n" + 
			"            <xsd:annotation>\r\n" + 
			"                <xsd:documentation>Denominates if this article is an active base article. The BaseArticle's subelements and attributes can be provided, regardless if the article is active or not.</xsd:documentation>\r\n" + 
			"            </xsd:annotation>\r\n" + 
			"        </xsd:attribute>\r\n" + 
			"        <xsd:attribute default=\"ACTIVE\" name=\"status\" type=\"ArticleStatus\" use=\"optional\"/>\r\n" + 
			"        <xsd:attribute default=\"false\" name=\"dontShowInLoadingLists\" type=\"xsd:boolean\">\r\n" + 
			"            <xsd:annotation>\r\n" + 
			"                <xsd:documentation>If this attribute's value is set to \"TRUE\", this article won't show in VirtualVendingmachine's loading lists. In this case it will only be used for price calculations.</xsd:documentation>\r\n" + 
			"            </xsd:annotation>\r\n" + 
			"        </xsd:attribute>\r\n" + 
			"        <xsd:attribute default=\"false\" name=\"strictBarcodeSynchronisation\" type=\"xsd:boolean\"\r\n" + 
			"            use=\"optional\">\r\n" + 
			"            <xsd:annotation>\r\n" + 
			"                <xsd:documentation>If strictBarcodeSynchronisation is set to \"TRUE\" every barcode which is not listed in the XML file will be deleted in the database. If it is set to \"FALSE\" only new barcodes will be added to the database.</xsd:documentation>\r\n" + 
			"            </xsd:annotation>\r\n" + 
			"        </xsd:attribute>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"Articles\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element maxOccurs=\"unbounded\" name=\"Article\" type=\"Article\"/>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:simpleType name=\"ReferenceCriterion\">\r\n" + 
			"        <xsd:restriction base=\"xsd:string\">\r\n" + 
			"            <xsd:enumeration value=\"SALES_ITEM_NUMBER\"/>\r\n" + 
			"            <xsd:enumeration value=\"BASE_ITEM_NUMBER\"/>\r\n" + 
			"            <xsd:enumeration value=\"ERP_ID\"/>\r\n" + 
			"        </xsd:restriction>\r\n" + 
			"    </xsd:simpleType>\r\n" + 
			"    <xsd:simpleType name=\"ArticleStatus\">\r\n" + 
			"        <xsd:restriction base=\"xsd:string\">\r\n" + 
			"            <xsd:enumeration value=\"ACTIVE\"/>\r\n" + 
			"            <xsd:enumeration value=\"DELETED\"/>\r\n" + 
			"            <xsd:enumeration value=\"BLOCKED\"/>\r\n" + 
			"        </xsd:restriction>\r\n" + 
			"    </xsd:simpleType>\r\n" + 
			"    <xsd:complexType name=\"ItemNumber\">\r\n" + 
			"        <xsd:attribute default=\"false\" name=\"autoGenerate\" type=\"xsd:boolean\"/>\r\n" + 
			"        <xsd:attribute name=\"value\" use=\"required\">\r\n" + 
			"            <xsd:simpleType>\r\n" + 
			"                <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                    <xsd:maxInclusive value=\"999999999\"/>\r\n" + 
			"                    <xsd:minInclusive value=\"1\"/>\r\n" + 
			"                </xsd:restriction>\r\n" + 
			"            </xsd:simpleType>\r\n" + 
			"        </xsd:attribute>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"</xsd:schema>\r\n";
	String xsdCommonObjectsPart1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\r\n" + 
			"    xmlns=\"http://www.vendidata.com/XML/Schema/CommonObjects\"\r\n" + 
			"    targetNamespace=\"http://www.vendidata.com/XML/Schema/CommonObjects\"\r\n" + 
			"    elementFormDefault=\"qualified\" version=\"2.2.0\">\r\n" + 
			"    <xsd:complexType name=\"MonthlyBalance\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"RowId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"CreationDate\" type=\"xsd:dateTime\"/>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"CashBalance\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"RowId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"CreationDate\" type=\"xsd:dateTime\"/>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"DataSource\">\r\n" + 
			"        <xsd:choice>\r\n" + 
			"            <xsd:element name=\"MonthlyBalance\" type=\"MonthlyBalance\"/>\r\n" + 
			"            <xsd:element name=\"CashBalance\" type=\"CashBalance\"/>\r\n" + 
			"        </xsd:choice>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"Employee\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"RowId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"EmployeeID\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"Firstname\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"1\" name=\"Lastname\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"VirtualVendingmachine\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"VvmId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"LegacyId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"0\"/>\r\n" + 
			"                        <xsd:maxLength value=\"255\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element type=\"AllocationType\" name=\"AllocationType\"/>\r\n" + 
			"            <xsd:element type=\"OperationVariant\" name=\"OperationVariant\"/>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"AdditionalFields\" type=\"AdditionalFields\"/>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"AdditionalFields\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"code1\" type=\"AdditionalFieldsStringValue\"> </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"code2\" type=\"AdditionalFieldsStringValue\"> </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"code3\" type=\"AdditionalFieldsStringValue\"> </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"code4\" type=\"AdditionalFieldsStringValue\"> </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"code5\" type=\"AdditionalFieldsDoubleValue\"> </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"code6\" type=\"AdditionalFieldsDoubleValue\">\r\n" + 
			"            </xsd:element>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"Stand\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"RowId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"Name\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"IsWaterPresent\" type=\"xsd:boolean\"/>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:simpleType name=\"AllocationType\">\r\n" + 
			"        <xsd:restriction base=\"xsd:string\">\r\n" + 
			"            <xsd:enumeration value=\"EASYDATA\"/>\r\n" + 
			"            <xsd:enumeration value=\"QUICK_ONLY\"/>\r\n" + 
			"            <xsd:enumeration value=\"CASH_ONLY\"/>\r\n" + 
			"            <xsd:enumeration value=\"CASHLESS_ONLY\"/>\r\n" + 
			"            <xsd:enumeration value=\"EVA_DTS\"/>\r\n" + 
			"        </xsd:restriction>\r\n" + 
			"    </xsd:simpleType>\r\n" + 
			"    <xsd:simpleType name=\"OperationVariant\">\r\n" + 
			"        <xsd:restriction base=\"xsd:string\">\r\n" + 
			"            <xsd:enumeration value=\"FULL_OPERATING\"/>\r\n" + 
			"            <xsd:enumeration value=\"PARTIAL_OPERATING\"/>\r\n" + 
			"            <xsd:enumeration value=\"CUSTOMER\"/>\r\n" + 
			"            <xsd:enumeration value=\"CLEARING\"/>\r\n" + 
			"        </xsd:restriction>\r\n" + 
			"    </xsd:simpleType>\r\n" + 
			"    <xsd:complexType name=\"Customer\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"RowId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"CustomerId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"FinancialAccountingId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"AdditionalFields\" type=\"AdditionalFields\"/>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"\r\n" + 
			"    <xsd:simpleType name=\"String1to50\">\r\n" + 
			"        <xsd:restriction base=\"xsd:string\">\r\n" + 
			"            <xsd:minLength value=\"1\"/>\r\n" + 
			"            <xsd:maxLength value=\"50\"/>\r\n" + 
			"        </xsd:restriction>\r\n" + 
			"    </xsd:simpleType>\r\n" + 
			"    <xsd:complexType name=\"AdditionalFieldsStringValue\">\r\n" + 
			"        <xsd:attribute name=\"label\" use=\"required\">\r\n" + 
			"            <xsd:simpleType>\r\n" + 
			"                <xsd:restriction base=\"xsd:string\">\r\n";
	String xsdCommonObjectsPart2 = 
			"                    <xsd:minLength value=\"1\"/>\r\n" + 
			"                    <xsd:maxLength value=\"50\"/>\r\n" + 
			"                </xsd:restriction>\r\n" + 
			"            </xsd:simpleType>\r\n" + 
			"        </xsd:attribute>\r\n" + 
			"        <xsd:attribute name=\"value\" use=\"required\">\r\n" + 
			"            <xsd:simpleType>\r\n" + 
			"                <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                    <xsd:minLength value=\"1\"/>\r\n" + 
			"                    <xsd:maxLength value=\"50\"/>\r\n" + 
			"                </xsd:restriction>\r\n" + 
			"            </xsd:simpleType>\r\n" + 
			"        </xsd:attribute>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"AdditionalFieldsDoubleValue\">\r\n" + 
			"        <xsd:attribute name=\"label\" use=\"required\">\r\n" + 
			"            <xsd:simpleType>\r\n" + 
			"                <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                    <xsd:minLength value=\"1\"/>\r\n" + 
			"                    <xsd:maxLength value=\"50\"/>\r\n" + 
			"                </xsd:restriction>\r\n" + 
			"            </xsd:simpleType>\r\n" + 
			"        </xsd:attribute>\r\n" + 
			"        <xsd:attribute name=\"value\" use=\"required\">\r\n" + 
			"            <xsd:simpleType>\r\n" + 
			"                <xsd:restriction base=\"xsd:double\"/>\r\n" + 
			"            </xsd:simpleType>\r\n" + 
			"        </xsd:attribute>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"Article\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"RowId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:choice maxOccurs=\"unbounded\">\r\n" + 
			"                <xsd:element name=\"BaseArticleData\" type=\"BaseArticleData\"/>\r\n" + 
			"                <xsd:element name=\"SalesArticleData\" type=\"SalesArticleData\"/>\r\n" + 
			"            </xsd:choice>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"VatRate\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"RowId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"VatRateValue\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:decimal\">\r\n" + 
			"                        <xsd:minInclusive value=\"0\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"100\"/>\r\n" + 
			"                        <xsd:totalDigits value=\"10\"/>\r\n" + 
			"                        <xsd:fractionDigits value=\"7\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"BaseArticleData\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"BaseArticleId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"1\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"BaseArticleName\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"ErpCode\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"SalesArticleData\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"SalesArticleId\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:int\">\r\n" + 
			"                        <xsd:minInclusive value=\"1\"/>\r\n" + 
			"                        <xsd:maxInclusive value=\"2147483640\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"SalesArticleName\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:simpleType name=\"VendPaymentType\">\r\n" + 
			"        <xsd:restriction base=\"xsd:string\">\r\n" + 
			"            <xsd:enumeration value=\"PAID\"/>\r\n" + 
			"            <xsd:enumeration value=\"FREE\"/>\r\n" + 
			"            <xsd:enumeration value=\"TEST\"/>\r\n" + 
			"        </xsd:restriction>\r\n" + 
			"    </xsd:simpleType>\r\n" + 
			"    <xsd:complexType name=\"VendingmachineComponent\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"RowId\" type=\"xsd:int\"/>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"InventoryId\" type=\"xsd:int\"/>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"SerialNumber\" type=\"xsd:string\"/>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"DeviceId\" type=\"xsd:string\"/>\r\n" + 
			"            <xsd:element name=\"AssetTracked\" type=\"xsd:boolean\"/>\r\n" + 
			"            <xsd:element name=\"Class\" type=\"VendingmachineComponentClass\"/>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"Vendor\" type=\"SupplierInformation\"/>\r\n" + 
			"            <xsd:element name=\"Denomination\" type=\"xsd:string\"/>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:simpleType name=\"VendingmachineComponentClass\">\r\n" + 
			"        <xsd:restriction base=\"xsd:string\">\r\n" + 
			"            <xsd:enumeration value=\"ADDON\"/>\r\n" + 
			"            <xsd:enumeration value=\"BASEMACHINE\"/>\r\n" + 
			"            <xsd:enumeration value=\"BILLVALIDATOR\"/>\r\n" + 
			"            <xsd:enumeration value=\"COINCONTROLUNIT\"/>\r\n" + 
			"            <xsd:enumeration value=\"PAYMENTSYSTEM\"/>\r\n" + 
			"            <xsd:enumeration value=\"QUICK\"/>\r\n" + 
			"            <xsd:enumeration value=\"WEARING_PART\"/>\r\n" + 
			"        </xsd:restriction>\r\n" + 
			"    </xsd:simpleType>\r\n" + 
			"    <xsd:complexType name=\"SupplierInformation\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"RowId\" type=\"xsd:int\"/>\r\n" + 
			"            <xsd:element name=\"SupplierId\" type=\"xsd:int\"/>\r\n" + 
			"            <xsd:element name=\"FinancialAccountingId\" type=\"xsd:int\"/>\r\n" + 
			"            <xsd:element name=\"Name\" type=\"xsd:string\"/>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:simpleType name=\"BarcodeType\">\r\n" + 
			"        <xsd:restriction base=\"xsd:integer\">\r\n" + 
			"            <xsd:minInclusive value=\"1\"/>\r\n" + 
			"            <xsd:maxInclusive value=\"999999999999999999\"/>\r\n" + 
			"        </xsd:restriction>\r\n" + 
			"    </xsd:simpleType>\r\n" + 
			"    <xsd:complexType name=\"Barcode\">\r\n" + 
			"        <xsd:simpleContent>\r\n" + 
			"            <xsd:extension base=\"BarcodeType\">\r\n" + 
			"                <xsd:attribute name=\"multiplier\" use=\"optional\">\r\n" + 
			"                    <xsd:annotation>\r\n" + 
			"                        <xsd:documentation>\r\n" + 
			"Multiplier for this barcode.\r\n" + 
			"Specifies the amount of base items this barcode represents.\r\n" + 
			"If omited, on import \"1.0\" is assumed, while on update the current value is not modified. \r\n" + 
			"                        </xsd:documentation>\r\n" + 
			"                    </xsd:annotation>\r\n" + 
			"                    <xsd:simpleType>\r\n" + 
			"                        <xsd:restriction base=\"xsd:double\">\r\n" + 
			"                            <xsd:minInclusive value=\"0.0\"/>\r\n" + 
			"                        </xsd:restriction>\r\n" + 
			"                    </xsd:simpleType>\r\n" + 
			"                </xsd:attribute>\r\n" + 
			"            </xsd:extension>\r\n" + 
			"        </xsd:simpleContent>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"Postalcode\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"Country\" type=\"Country\"/>\r\n" + 
			"            <xsd:element name=\"Province\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"County\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"ZipCode\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"City\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"Country\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"Fullname\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"Shortname\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:complexType name=\"Language\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element name=\"Language\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element name=\"ISO3166\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"1\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"    <xsd:simpleType name=\"Salutation\">\r\n" + 
			"        <xsd:restriction base=\"xsd:string\">\r\n" + 
			"            <xsd:enumeration value=\"MISTER\"/>\r\n" + 
			"            <xsd:enumeration value=\"MISS\"/>\r\n" + 
			"            <xsd:enumeration value=\"MISSES\"/>\r\n" + 
			"        </xsd:restriction>\r\n" + 
			"    </xsd:simpleType>\r\n" + 
			"    <xsd:simpleType name=\"Status\">\r\n" + 
			"        <xsd:restriction base=\"xsd:string\">\r\n" + 
			"            <xsd:enumeration value=\"ACTIVE\"/>\r\n" + 
			"            <xsd:enumeration value=\"DELETED\"/>\r\n" + 
			"            <xsd:enumeration value=\"BLOCKED\"/>\r\n" + 
			"        </xsd:restriction>\r\n" + 
			"    </xsd:simpleType>\r\n" + 
			"    <xsd:simpleType name=\"AutoGenerateId\">\r\n" + 
			"        <xsd:restriction base=\"xsd:string\">\r\n" + 
			"            <xsd:enumeration value=\"true\"/>\r\n" + 
			"        </xsd:restriction>\r\n" + 
			"    </xsd:simpleType>\r\n" + 
			"    <xsd:complexType name=\"CustomerContact\">\r\n" + 
			"        <xsd:sequence>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"Salutation\" type=\"Salutation\"/>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"Title\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"0\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"Firstname\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"0\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"Lastname\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"0\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"Street\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"0\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"Postalcode\" type=\"Postalcode\"/>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"PhoneNumber\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"0\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"MobilePhoneNumber\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"0\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"FaxNumber\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"0\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"            <xsd:element minOccurs=\"0\" name=\"EMailAddress\">\r\n" + 
			"                <xsd:simpleType>\r\n" + 
			"                    <xsd:restriction base=\"xsd:string\">\r\n" + 
			"                        <xsd:minLength value=\"0\"/>\r\n" + 
			"                        <xsd:maxLength value=\"50\"/>\r\n" + 
			"                    </xsd:restriction>\r\n" + 
			"                </xsd:simpleType>\r\n" + 
			"            </xsd:element>\r\n" + 
			"        </xsd:sequence>\r\n" + 
			"        <xsd:attribute name=\"rowid\" type=\"xsd:int\" use=\"required\"/>\r\n" + 
			"    </xsd:complexType>\r\n" + 
			"</xsd:schema>\r\n";
	
	String xsdCommonObjects = xsdCommonObjectsPart1 + xsdCommonObjectsPart2;
	
	public VendidataArticlesMarshaller() throws JAXBException {
		super();
	}

	public String marshal(XMLArticles xmlArticles) throws JAXBException, SAXException {
		StreamSource[] streamSources = new StreamSource[] {
				new StreamSource(new StringReader(xsdCommonObjects)),
				new StreamSource(new StringReader(xsdArticles))
		};
		SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		Schema schema = schemaFactory.newSchema(streamSources);
		JAXBContext jaxbContext = JAXBContext.newInstance(xmlArticles.getClass().getPackage().getName());
		StringWriter writer = new StringWriter();
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setSchema(schema);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
				"http://www.vendidata.com/XML/Schema/Articles http://www.vendidata.com/XML/Schema/Articles/articles.xsd "
				+ "http://www.vendidata.com/XML/Schema/CommonObjects "
				+ "http://www.vendidata.com/XML/Schema/CommonObjects/commonobjects.xsd");
		ObjectFactory objectFactory = new ObjectFactory();
		marshaller.marshal(objectFactory.createArticles(xmlArticles), writer);
		
		return writer.toString();
	}

	@Override
	protected Class<XMLArticles> getClazz() {
		return XMLArticles.class;
	}
}
