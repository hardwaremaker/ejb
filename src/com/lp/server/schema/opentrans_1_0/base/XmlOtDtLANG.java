//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.base;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer dtLANG.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="dtLANG">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="aar"/>
 *     &lt;enumeration value="abk"/>
 *     &lt;enumeration value="ace"/>
 *     &lt;enumeration value="ach"/>
 *     &lt;enumeration value="ada"/>
 *     &lt;enumeration value="afa"/>
 *     &lt;enumeration value="afh"/>
 *     &lt;enumeration value="afr"/>
 *     &lt;enumeration value="aka"/>
 *     &lt;enumeration value="akk"/>
 *     &lt;enumeration value="alb"/>
 *     &lt;enumeration value="ale"/>
 *     &lt;enumeration value="alg"/>
 *     &lt;enumeration value="amh"/>
 *     &lt;enumeration value="ang"/>
 *     &lt;enumeration value="apa"/>
 *     &lt;enumeration value="ara"/>
 *     &lt;enumeration value="arc"/>
 *     &lt;enumeration value="arm"/>
 *     &lt;enumeration value="arn"/>
 *     &lt;enumeration value="arp"/>
 *     &lt;enumeration value="art"/>
 *     &lt;enumeration value="arw"/>
 *     &lt;enumeration value="asm"/>
 *     &lt;enumeration value="ath"/>
 *     &lt;enumeration value="aus"/>
 *     &lt;enumeration value="ava"/>
 *     &lt;enumeration value="ave"/>
 *     &lt;enumeration value="awa"/>
 *     &lt;enumeration value="aym"/>
 *     &lt;enumeration value="aze"/>
 *     &lt;enumeration value="bad"/>
 *     &lt;enumeration value="bai"/>
 *     &lt;enumeration value="bak"/>
 *     &lt;enumeration value="bal"/>
 *     &lt;enumeration value="bam"/>
 *     &lt;enumeration value="ban"/>
 *     &lt;enumeration value="baq"/>
 *     &lt;enumeration value="bas"/>
 *     &lt;enumeration value="bat"/>
 *     &lt;enumeration value="bej"/>
 *     &lt;enumeration value="bel"/>
 *     &lt;enumeration value="bem"/>
 *     &lt;enumeration value="ben"/>
 *     &lt;enumeration value="ber"/>
 *     &lt;enumeration value="bho"/>
 *     &lt;enumeration value="bih"/>
 *     &lt;enumeration value="bik"/>
 *     &lt;enumeration value="bin"/>
 *     &lt;enumeration value="bis"/>
 *     &lt;enumeration value="bla"/>
 *     &lt;enumeration value="bnt"/>
 *     &lt;enumeration value="bod"/>
 *     &lt;enumeration value="bos"/>
 *     &lt;enumeration value="bra"/>
 *     &lt;enumeration value="bre"/>
 *     &lt;enumeration value="btk"/>
 *     &lt;enumeration value="bua"/>
 *     &lt;enumeration value="bug"/>
 *     &lt;enumeration value="bul"/>
 *     &lt;enumeration value="bur"/>
 *     &lt;enumeration value="cad"/>
 *     &lt;enumeration value="cai"/>
 *     &lt;enumeration value="car"/>
 *     &lt;enumeration value="cat"/>
 *     &lt;enumeration value="cau"/>
 *     &lt;enumeration value="ceb"/>
 *     &lt;enumeration value="cel"/>
 *     &lt;enumeration value="ces"/>
 *     &lt;enumeration value="cha"/>
 *     &lt;enumeration value="chb"/>
 *     &lt;enumeration value="che"/>
 *     &lt;enumeration value="chg"/>
 *     &lt;enumeration value="chi"/>
 *     &lt;enumeration value="chk"/>
 *     &lt;enumeration value="chm"/>
 *     &lt;enumeration value="chn"/>
 *     &lt;enumeration value="cho"/>
 *     &lt;enumeration value="chp"/>
 *     &lt;enumeration value="chr"/>
 *     &lt;enumeration value="chu"/>
 *     &lt;enumeration value="chv"/>
 *     &lt;enumeration value="chy"/>
 *     &lt;enumeration value="cmc"/>
 *     &lt;enumeration value="cop"/>
 *     &lt;enumeration value="cor"/>
 *     &lt;enumeration value="cos"/>
 *     &lt;enumeration value="cpe"/>
 *     &lt;enumeration value="cpf"/>
 *     &lt;enumeration value="cpp"/>
 *     &lt;enumeration value="cre"/>
 *     &lt;enumeration value="crp"/>
 *     &lt;enumeration value="cus"/>
 *     &lt;enumeration value="cym"/>
 *     &lt;enumeration value="cze"/>
 *     &lt;enumeration value="dak"/>
 *     &lt;enumeration value="dan"/>
 *     &lt;enumeration value="day"/>
 *     &lt;enumeration value="del"/>
 *     &lt;enumeration value="den"/>
 *     &lt;enumeration value="deu"/>
 *     &lt;enumeration value="dgr"/>
 *     &lt;enumeration value="din"/>
 *     &lt;enumeration value="div"/>
 *     &lt;enumeration value="doi"/>
 *     &lt;enumeration value="dra"/>
 *     &lt;enumeration value="dua"/>
 *     &lt;enumeration value="dum"/>
 *     &lt;enumeration value="dut"/>
 *     &lt;enumeration value="dyu"/>
 *     &lt;enumeration value="dzo"/>
 *     &lt;enumeration value="efi"/>
 *     &lt;enumeration value="egy"/>
 *     &lt;enumeration value="eka"/>
 *     &lt;enumeration value="ell"/>
 *     &lt;enumeration value="elx"/>
 *     &lt;enumeration value="eng"/>
 *     &lt;enumeration value="enm"/>
 *     &lt;enumeration value="epo"/>
 *     &lt;enumeration value="est"/>
 *     &lt;enumeration value="eus"/>
 *     &lt;enumeration value="ewe"/>
 *     &lt;enumeration value="ewo"/>
 *     &lt;enumeration value="fan"/>
 *     &lt;enumeration value="fao"/>
 *     &lt;enumeration value="fas"/>
 *     &lt;enumeration value="fat"/>
 *     &lt;enumeration value="fij"/>
 *     &lt;enumeration value="fin"/>
 *     &lt;enumeration value="fiu"/>
 *     &lt;enumeration value="fon"/>
 *     &lt;enumeration value="fra"/>
 *     &lt;enumeration value="fre"/>
 *     &lt;enumeration value="frm"/>
 *     &lt;enumeration value="fro"/>
 *     &lt;enumeration value="fry"/>
 *     &lt;enumeration value="ful"/>
 *     &lt;enumeration value="fur"/>
 *     &lt;enumeration value="gaa"/>
 *     &lt;enumeration value="gay"/>
 *     &lt;enumeration value="gba"/>
 *     &lt;enumeration value="gem"/>
 *     &lt;enumeration value="geo"/>
 *     &lt;enumeration value="ger"/>
 *     &lt;enumeration value="gez"/>
 *     &lt;enumeration value="gil"/>
 *     &lt;enumeration value="gla"/>
 *     &lt;enumeration value="gle"/>
 *     &lt;enumeration value="glg"/>
 *     &lt;enumeration value="glv"/>
 *     &lt;enumeration value="gmh"/>
 *     &lt;enumeration value="goh"/>
 *     &lt;enumeration value="gon"/>
 *     &lt;enumeration value="gor"/>
 *     &lt;enumeration value="got"/>
 *     &lt;enumeration value="grb"/>
 *     &lt;enumeration value="grc"/>
 *     &lt;enumeration value="gre"/>
 *     &lt;enumeration value="grn"/>
 *     &lt;enumeration value="guj"/>
 *     &lt;enumeration value="gwi"/>
 *     &lt;enumeration value="hai"/>
 *     &lt;enumeration value="hau"/>
 *     &lt;enumeration value="haw"/>
 *     &lt;enumeration value="heb"/>
 *     &lt;enumeration value="her"/>
 *     &lt;enumeration value="hil"/>
 *     &lt;enumeration value="him"/>
 *     &lt;enumeration value="hin"/>
 *     &lt;enumeration value="hit"/>
 *     &lt;enumeration value="hmn"/>
 *     &lt;enumeration value="hmo"/>
 *     &lt;enumeration value="hrv"/>
 *     &lt;enumeration value="hun"/>
 *     &lt;enumeration value="hup"/>
 *     &lt;enumeration value="hye"/>
 *     &lt;enumeration value="iba"/>
 *     &lt;enumeration value="ibo"/>
 *     &lt;enumeration value="ice"/>
 *     &lt;enumeration value="ijo"/>
 *     &lt;enumeration value="iku"/>
 *     &lt;enumeration value="ile"/>
 *     &lt;enumeration value="ilo"/>
 *     &lt;enumeration value="ina"/>
 *     &lt;enumeration value="inc"/>
 *     &lt;enumeration value="ind"/>
 *     &lt;enumeration value="ine"/>
 *     &lt;enumeration value="ipk"/>
 *     &lt;enumeration value="ira"/>
 *     &lt;enumeration value="iro"/>
 *     &lt;enumeration value="isl"/>
 *     &lt;enumeration value="ita"/>
 *     &lt;enumeration value="jav"/>
 *     &lt;enumeration value="jpn"/>
 *     &lt;enumeration value="jpr"/>
 *     &lt;enumeration value="jrb"/>
 *     &lt;enumeration value="kaa"/>
 *     &lt;enumeration value="kab"/>
 *     &lt;enumeration value="kac"/>
 *     &lt;enumeration value="kal"/>
 *     &lt;enumeration value="kam"/>
 *     &lt;enumeration value="kan"/>
 *     &lt;enumeration value="kar"/>
 *     &lt;enumeration value="kas"/>
 *     &lt;enumeration value="kat"/>
 *     &lt;enumeration value="kau"/>
 *     &lt;enumeration value="kaw"/>
 *     &lt;enumeration value="kaz"/>
 *     &lt;enumeration value="kha"/>
 *     &lt;enumeration value="khi"/>
 *     &lt;enumeration value="khm"/>
 *     &lt;enumeration value="kho"/>
 *     &lt;enumeration value="kik"/>
 *     &lt;enumeration value="kin"/>
 *     &lt;enumeration value="kir"/>
 *     &lt;enumeration value="kmb"/>
 *     &lt;enumeration value="kok"/>
 *     &lt;enumeration value="kom"/>
 *     &lt;enumeration value="kon"/>
 *     &lt;enumeration value="kor"/>
 *     &lt;enumeration value="kos"/>
 *     &lt;enumeration value="kpe"/>
 *     &lt;enumeration value="kro"/>
 *     &lt;enumeration value="kru"/>
 *     &lt;enumeration value="kua"/>
 *     &lt;enumeration value="kum"/>
 *     &lt;enumeration value="kur"/>
 *     &lt;enumeration value="kut"/>
 *     &lt;enumeration value="lad"/>
 *     &lt;enumeration value="lah"/>
 *     &lt;enumeration value="lam"/>
 *     &lt;enumeration value="lao"/>
 *     &lt;enumeration value="lat"/>
 *     &lt;enumeration value="lav"/>
 *     &lt;enumeration value="lez"/>
 *     &lt;enumeration value="lin"/>
 *     &lt;enumeration value="lit"/>
 *     &lt;enumeration value="lol"/>
 *     &lt;enumeration value="loz"/>
 *     &lt;enumeration value="ltz"/>
 *     &lt;enumeration value="lua"/>
 *     &lt;enumeration value="lub"/>
 *     &lt;enumeration value="lug"/>
 *     &lt;enumeration value="lui"/>
 *     &lt;enumeration value="lun"/>
 *     &lt;enumeration value="luo"/>
 *     &lt;enumeration value="lus"/>
 *     &lt;enumeration value="mac"/>
 *     &lt;enumeration value="mad"/>
 *     &lt;enumeration value="mag"/>
 *     &lt;enumeration value="mah"/>
 *     &lt;enumeration value="mai"/>
 *     &lt;enumeration value="mak"/>
 *     &lt;enumeration value="mal"/>
 *     &lt;enumeration value="man"/>
 *     &lt;enumeration value="mao"/>
 *     &lt;enumeration value="map"/>
 *     &lt;enumeration value="mar"/>
 *     &lt;enumeration value="mas"/>
 *     &lt;enumeration value="may"/>
 *     &lt;enumeration value="mdr"/>
 *     &lt;enumeration value="men"/>
 *     &lt;enumeration value="mga"/>
 *     &lt;enumeration value="mic"/>
 *     &lt;enumeration value="min"/>
 *     &lt;enumeration value="mis"/>
 *     &lt;enumeration value="mkd"/>
 *     &lt;enumeration value="mkh"/>
 *     &lt;enumeration value="mlg"/>
 *     &lt;enumeration value="mlt"/>
 *     &lt;enumeration value="mnc"/>
 *     &lt;enumeration value="mni"/>
 *     &lt;enumeration value="mno"/>
 *     &lt;enumeration value="moh"/>
 *     &lt;enumeration value="mol"/>
 *     &lt;enumeration value="mon"/>
 *     &lt;enumeration value="mos"/>
 *     &lt;enumeration value="mri"/>
 *     &lt;enumeration value="msa"/>
 *     &lt;enumeration value="mul"/>
 *     &lt;enumeration value="mun"/>
 *     &lt;enumeration value="mus"/>
 *     &lt;enumeration value="mwr"/>
 *     &lt;enumeration value="mya"/>
 *     &lt;enumeration value="myn"/>
 *     &lt;enumeration value="nah"/>
 *     &lt;enumeration value="nai"/>
 *     &lt;enumeration value="nau"/>
 *     &lt;enumeration value="nav"/>
 *     &lt;enumeration value="nbl"/>
 *     &lt;enumeration value="nde"/>
 *     &lt;enumeration value="ndo"/>
 *     &lt;enumeration value="nds"/>
 *     &lt;enumeration value="nep"/>
 *     &lt;enumeration value="new"/>
 *     &lt;enumeration value="nia"/>
 *     &lt;enumeration value="nic"/>
 *     &lt;enumeration value="niu"/>
 *     &lt;enumeration value="nld"/>
 *     &lt;enumeration value="nno"/>
 *     &lt;enumeration value="nob"/>
 *     &lt;enumeration value="non"/>
 *     &lt;enumeration value="nor"/>
 *     &lt;enumeration value="nso"/>
 *     &lt;enumeration value="nub"/>
 *     &lt;enumeration value="nya"/>
 *     &lt;enumeration value="nym"/>
 *     &lt;enumeration value="nyn"/>
 *     &lt;enumeration value="nyo"/>
 *     &lt;enumeration value="nzi"/>
 *     &lt;enumeration value="oci"/>
 *     &lt;enumeration value="oji"/>
 *     &lt;enumeration value="ori"/>
 *     &lt;enumeration value="orm"/>
 *     &lt;enumeration value="osa"/>
 *     &lt;enumeration value="oss"/>
 *     &lt;enumeration value="ota"/>
 *     &lt;enumeration value="oto"/>
 *     &lt;enumeration value="paa"/>
 *     &lt;enumeration value="pag"/>
 *     &lt;enumeration value="pal"/>
 *     &lt;enumeration value="pam"/>
 *     &lt;enumeration value="pan"/>
 *     &lt;enumeration value="pap"/>
 *     &lt;enumeration value="pau"/>
 *     &lt;enumeration value="peo"/>
 *     &lt;enumeration value="per"/>
 *     &lt;enumeration value="phi"/>
 *     &lt;enumeration value="phn"/>
 *     &lt;enumeration value="pli"/>
 *     &lt;enumeration value="pol"/>
 *     &lt;enumeration value="pon"/>
 *     &lt;enumeration value="por"/>
 *     &lt;enumeration value="pra"/>
 *     &lt;enumeration value="pro"/>
 *     &lt;enumeration value="pus"/>
 *     &lt;enumeration value="qaa"/>
 *     &lt;enumeration value="que"/>
 *     &lt;enumeration value="raj"/>
 *     &lt;enumeration value="rap"/>
 *     &lt;enumeration value="rar"/>
 *     &lt;enumeration value="roa"/>
 *     &lt;enumeration value="roh"/>
 *     &lt;enumeration value="rom"/>
 *     &lt;enumeration value="ron"/>
 *     &lt;enumeration value="rum"/>
 *     &lt;enumeration value="run"/>
 *     &lt;enumeration value="rus"/>
 *     &lt;enumeration value="sad"/>
 *     &lt;enumeration value="sag"/>
 *     &lt;enumeration value="sah"/>
 *     &lt;enumeration value="sai"/>
 *     &lt;enumeration value="sal"/>
 *     &lt;enumeration value="sam"/>
 *     &lt;enumeration value="san"/>
 *     &lt;enumeration value="sas"/>
 *     &lt;enumeration value="sat"/>
 *     &lt;enumeration value="scc"/>
 *     &lt;enumeration value="sco"/>
 *     &lt;enumeration value="scr"/>
 *     &lt;enumeration value="sel"/>
 *     &lt;enumeration value="sem"/>
 *     &lt;enumeration value="sga"/>
 *     &lt;enumeration value="sgn"/>
 *     &lt;enumeration value="shn"/>
 *     &lt;enumeration value="sid"/>
 *     &lt;enumeration value="sin"/>
 *     &lt;enumeration value="sio"/>
 *     &lt;enumeration value="sit"/>
 *     &lt;enumeration value="sla"/>
 *     &lt;enumeration value="slk"/>
 *     &lt;enumeration value="slo"/>
 *     &lt;enumeration value="slv"/>
 *     &lt;enumeration value="sme"/>
 *     &lt;enumeration value="smi"/>
 *     &lt;enumeration value="smo"/>
 *     &lt;enumeration value="sna"/>
 *     &lt;enumeration value="snd"/>
 *     &lt;enumeration value="snk"/>
 *     &lt;enumeration value="sog"/>
 *     &lt;enumeration value="som"/>
 *     &lt;enumeration value="son"/>
 *     &lt;enumeration value="sot"/>
 *     &lt;enumeration value="spa"/>
 *     &lt;enumeration value="sqi"/>
 *     &lt;enumeration value="srd"/>
 *     &lt;enumeration value="srp"/>
 *     &lt;enumeration value="srr"/>
 *     &lt;enumeration value="ssa"/>
 *     &lt;enumeration value="ssw"/>
 *     &lt;enumeration value="suk"/>
 *     &lt;enumeration value="sun"/>
 *     &lt;enumeration value="sus"/>
 *     &lt;enumeration value="sux"/>
 *     &lt;enumeration value="swa"/>
 *     &lt;enumeration value="swe"/>
 *     &lt;enumeration value="syr"/>
 *     &lt;enumeration value="tah"/>
 *     &lt;enumeration value="tai"/>
 *     &lt;enumeration value="tam"/>
 *     &lt;enumeration value="tat"/>
 *     &lt;enumeration value="tel"/>
 *     &lt;enumeration value="tem"/>
 *     &lt;enumeration value="ter"/>
 *     &lt;enumeration value="tet"/>
 *     &lt;enumeration value="tgk"/>
 *     &lt;enumeration value="tgl"/>
 *     &lt;enumeration value="tha"/>
 *     &lt;enumeration value="tib"/>
 *     &lt;enumeration value="tig"/>
 *     &lt;enumeration value="tir"/>
 *     &lt;enumeration value="tiv"/>
 *     &lt;enumeration value="tkl"/>
 *     &lt;enumeration value="tli"/>
 *     &lt;enumeration value="tmh"/>
 *     &lt;enumeration value="tog"/>
 *     &lt;enumeration value="ton"/>
 *     &lt;enumeration value="tpi"/>
 *     &lt;enumeration value="tsi"/>
 *     &lt;enumeration value="tsn"/>
 *     &lt;enumeration value="tso"/>
 *     &lt;enumeration value="tuk"/>
 *     &lt;enumeration value="tum"/>
 *     &lt;enumeration value="tur"/>
 *     &lt;enumeration value="tut"/>
 *     &lt;enumeration value="tvl"/>
 *     &lt;enumeration value="twi"/>
 *     &lt;enumeration value="tyv"/>
 *     &lt;enumeration value="uga"/>
 *     &lt;enumeration value="uig"/>
 *     &lt;enumeration value="ukr"/>
 *     &lt;enumeration value="umb"/>
 *     &lt;enumeration value="und"/>
 *     &lt;enumeration value="urd"/>
 *     &lt;enumeration value="uzb"/>
 *     &lt;enumeration value="vai"/>
 *     &lt;enumeration value="ven"/>
 *     &lt;enumeration value="vie"/>
 *     &lt;enumeration value="vol"/>
 *     &lt;enumeration value="vot"/>
 *     &lt;enumeration value="wak"/>
 *     &lt;enumeration value="wal"/>
 *     &lt;enumeration value="war"/>
 *     &lt;enumeration value="was"/>
 *     &lt;enumeration value="wel"/>
 *     &lt;enumeration value="wen"/>
 *     &lt;enumeration value="wol"/>
 *     &lt;enumeration value="xho"/>
 *     &lt;enumeration value="yao"/>
 *     &lt;enumeration value="yap"/>
 *     &lt;enumeration value="yid"/>
 *     &lt;enumeration value="yor"/>
 *     &lt;enumeration value="ypk"/>
 *     &lt;enumeration value="zap"/>
 *     &lt;enumeration value="zen"/>
 *     &lt;enumeration value="zha"/>
 *     &lt;enumeration value="zho"/>
 *     &lt;enumeration value="znd"/>
 *     &lt;enumeration value="zul"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "dtLANG")
@XmlEnum
public enum XmlOtDtLANG {

    @XmlEnumValue("aar")
    AAR("aar"),
    @XmlEnumValue("abk")
    ABK("abk"),
    @XmlEnumValue("ace")
    ACE("ace"),
    @XmlEnumValue("ach")
    ACH("ach"),
    @XmlEnumValue("ada")
    ADA("ada"),
    @XmlEnumValue("afa")
    AFA("afa"),
    @XmlEnumValue("afh")
    AFH("afh"),
    @XmlEnumValue("afr")
    AFR("afr"),
    @XmlEnumValue("aka")
    AKA("aka"),
    @XmlEnumValue("akk")
    AKK("akk"),
    @XmlEnumValue("alb")
    ALB("alb"),
    @XmlEnumValue("ale")
    ALE("ale"),
    @XmlEnumValue("alg")
    ALG("alg"),
    @XmlEnumValue("amh")
    AMH("amh"),
    @XmlEnumValue("ang")
    ANG("ang"),
    @XmlEnumValue("apa")
    APA("apa"),
    @XmlEnumValue("ara")
    ARA("ara"),
    @XmlEnumValue("arc")
    ARC("arc"),
    @XmlEnumValue("arm")
    ARM("arm"),
    @XmlEnumValue("arn")
    ARN("arn"),
    @XmlEnumValue("arp")
    ARP("arp"),
    @XmlEnumValue("art")
    ART("art"),
    @XmlEnumValue("arw")
    ARW("arw"),
    @XmlEnumValue("asm")
    ASM("asm"),
    @XmlEnumValue("ath")
    ATH("ath"),
    @XmlEnumValue("aus")
    AUS("aus"),
    @XmlEnumValue("ava")
    AVA("ava"),
    @XmlEnumValue("ave")
    AVE("ave"),
    @XmlEnumValue("awa")
    AWA("awa"),
    @XmlEnumValue("aym")
    AYM("aym"),
    @XmlEnumValue("aze")
    AZE("aze"),
    @XmlEnumValue("bad")
    BAD("bad"),
    @XmlEnumValue("bai")
    BAI("bai"),
    @XmlEnumValue("bak")
    BAK("bak"),
    @XmlEnumValue("bal")
    BAL("bal"),
    @XmlEnumValue("bam")
    BAM("bam"),
    @XmlEnumValue("ban")
    BAN("ban"),
    @XmlEnumValue("baq")
    BAQ("baq"),
    @XmlEnumValue("bas")
    BAS("bas"),
    @XmlEnumValue("bat")
    BAT("bat"),
    @XmlEnumValue("bej")
    BEJ("bej"),
    @XmlEnumValue("bel")
    BEL("bel"),
    @XmlEnumValue("bem")
    BEM("bem"),
    @XmlEnumValue("ben")
    BEN("ben"),
    @XmlEnumValue("ber")
    BER("ber"),
    @XmlEnumValue("bho")
    BHO("bho"),
    @XmlEnumValue("bih")
    BIH("bih"),
    @XmlEnumValue("bik")
    BIK("bik"),
    @XmlEnumValue("bin")
    BIN("bin"),
    @XmlEnumValue("bis")
    BIS("bis"),
    @XmlEnumValue("bla")
    BLA("bla"),
    @XmlEnumValue("bnt")
    BNT("bnt"),
    @XmlEnumValue("bod")
    BOD("bod"),
    @XmlEnumValue("bos")
    BOS("bos"),
    @XmlEnumValue("bra")
    BRA("bra"),
    @XmlEnumValue("bre")
    BRE("bre"),
    @XmlEnumValue("btk")
    BTK("btk"),
    @XmlEnumValue("bua")
    BUA("bua"),
    @XmlEnumValue("bug")
    BUG("bug"),
    @XmlEnumValue("bul")
    BUL("bul"),
    @XmlEnumValue("bur")
    BUR("bur"),
    @XmlEnumValue("cad")
    CAD("cad"),
    @XmlEnumValue("cai")
    CAI("cai"),
    @XmlEnumValue("car")
    CAR("car"),
    @XmlEnumValue("cat")
    CAT("cat"),
    @XmlEnumValue("cau")
    CAU("cau"),
    @XmlEnumValue("ceb")
    CEB("ceb"),
    @XmlEnumValue("cel")
    CEL("cel"),
    @XmlEnumValue("ces")
    CES("ces"),
    @XmlEnumValue("cha")
    CHA("cha"),
    @XmlEnumValue("chb")
    CHB("chb"),
    @XmlEnumValue("che")
    CHE("che"),
    @XmlEnumValue("chg")
    CHG("chg"),
    @XmlEnumValue("chi")
    CHI("chi"),
    @XmlEnumValue("chk")
    CHK("chk"),
    @XmlEnumValue("chm")
    CHM("chm"),
    @XmlEnumValue("chn")
    CHN("chn"),
    @XmlEnumValue("cho")
    CHO("cho"),
    @XmlEnumValue("chp")
    CHP("chp"),
    @XmlEnumValue("chr")
    CHR("chr"),
    @XmlEnumValue("chu")
    CHU("chu"),
    @XmlEnumValue("chv")
    CHV("chv"),
    @XmlEnumValue("chy")
    CHY("chy"),
    @XmlEnumValue("cmc")
    CMC("cmc"),
    @XmlEnumValue("cop")
    COP("cop"),
    @XmlEnumValue("cor")
    COR("cor"),
    @XmlEnumValue("cos")
    COS("cos"),
    @XmlEnumValue("cpe")
    CPE("cpe"),
    @XmlEnumValue("cpf")
    CPF("cpf"),
    @XmlEnumValue("cpp")
    CPP("cpp"),
    @XmlEnumValue("cre")
    CRE("cre"),
    @XmlEnumValue("crp")
    CRP("crp"),
    @XmlEnumValue("cus")
    CUS("cus"),
    @XmlEnumValue("cym")
    CYM("cym"),
    @XmlEnumValue("cze")
    CZE("cze"),
    @XmlEnumValue("dak")
    DAK("dak"),
    @XmlEnumValue("dan")
    DAN("dan"),
    @XmlEnumValue("day")
    DAY("day"),
    @XmlEnumValue("del")
    DEL("del"),
    @XmlEnumValue("den")
    DEN("den"),
    @XmlEnumValue("deu")
    DEU("deu"),
    @XmlEnumValue("dgr")
    DGR("dgr"),
    @XmlEnumValue("din")
    DIN("din"),
    @XmlEnumValue("div")
    DIV("div"),
    @XmlEnumValue("doi")
    DOI("doi"),
    @XmlEnumValue("dra")
    DRA("dra"),
    @XmlEnumValue("dua")
    DUA("dua"),
    @XmlEnumValue("dum")
    DUM("dum"),
    @XmlEnumValue("dut")
    DUT("dut"),
    @XmlEnumValue("dyu")
    DYU("dyu"),
    @XmlEnumValue("dzo")
    DZO("dzo"),
    @XmlEnumValue("efi")
    EFI("efi"),
    @XmlEnumValue("egy")
    EGY("egy"),
    @XmlEnumValue("eka")
    EKA("eka"),
    @XmlEnumValue("ell")
    ELL("ell"),
    @XmlEnumValue("elx")
    ELX("elx"),
    @XmlEnumValue("eng")
    ENG("eng"),
    @XmlEnumValue("enm")
    ENM("enm"),
    @XmlEnumValue("epo")
    EPO("epo"),
    @XmlEnumValue("est")
    EST("est"),
    @XmlEnumValue("eus")
    EUS("eus"),
    @XmlEnumValue("ewe")
    EWE("ewe"),
    @XmlEnumValue("ewo")
    EWO("ewo"),
    @XmlEnumValue("fan")
    FAN("fan"),
    @XmlEnumValue("fao")
    FAO("fao"),
    @XmlEnumValue("fas")
    FAS("fas"),
    @XmlEnumValue("fat")
    FAT("fat"),
    @XmlEnumValue("fij")
    FIJ("fij"),
    @XmlEnumValue("fin")
    FIN("fin"),
    @XmlEnumValue("fiu")
    FIU("fiu"),
    @XmlEnumValue("fon")
    FON("fon"),
    @XmlEnumValue("fra")
    FRA("fra"),
    @XmlEnumValue("fre")
    FRE("fre"),
    @XmlEnumValue("frm")
    FRM("frm"),
    @XmlEnumValue("fro")
    FRO("fro"),
    @XmlEnumValue("fry")
    FRY("fry"),
    @XmlEnumValue("ful")
    FUL("ful"),
    @XmlEnumValue("fur")
    FUR("fur"),
    @XmlEnumValue("gaa")
    GAA("gaa"),
    @XmlEnumValue("gay")
    GAY("gay"),
    @XmlEnumValue("gba")
    GBA("gba"),
    @XmlEnumValue("gem")
    GEM("gem"),
    @XmlEnumValue("geo")
    GEO("geo"),
    @XmlEnumValue("ger")
    GER("ger"),
    @XmlEnumValue("gez")
    GEZ("gez"),
    @XmlEnumValue("gil")
    GIL("gil"),
    @XmlEnumValue("gla")
    GLA("gla"),
    @XmlEnumValue("gle")
    GLE("gle"),
    @XmlEnumValue("glg")
    GLG("glg"),
    @XmlEnumValue("glv")
    GLV("glv"),
    @XmlEnumValue("gmh")
    GMH("gmh"),
    @XmlEnumValue("goh")
    GOH("goh"),
    @XmlEnumValue("gon")
    GON("gon"),
    @XmlEnumValue("gor")
    GOR("gor"),
    @XmlEnumValue("got")
    GOT("got"),
    @XmlEnumValue("grb")
    GRB("grb"),
    @XmlEnumValue("grc")
    GRC("grc"),
    @XmlEnumValue("gre")
    GRE("gre"),
    @XmlEnumValue("grn")
    GRN("grn"),
    @XmlEnumValue("guj")
    GUJ("guj"),
    @XmlEnumValue("gwi")
    GWI("gwi"),
    @XmlEnumValue("hai")
    HAI("hai"),
    @XmlEnumValue("hau")
    HAU("hau"),
    @XmlEnumValue("haw")
    HAW("haw"),
    @XmlEnumValue("heb")
    HEB("heb"),
    @XmlEnumValue("her")
    HER("her"),
    @XmlEnumValue("hil")
    HIL("hil"),
    @XmlEnumValue("him")
    HIM("him"),
    @XmlEnumValue("hin")
    HIN("hin"),
    @XmlEnumValue("hit")
    HIT("hit"),
    @XmlEnumValue("hmn")
    HMN("hmn"),
    @XmlEnumValue("hmo")
    HMO("hmo"),
    @XmlEnumValue("hrv")
    HRV("hrv"),
    @XmlEnumValue("hun")
    HUN("hun"),
    @XmlEnumValue("hup")
    HUP("hup"),
    @XmlEnumValue("hye")
    HYE("hye"),
    @XmlEnumValue("iba")
    IBA("iba"),
    @XmlEnumValue("ibo")
    IBO("ibo"),
    @XmlEnumValue("ice")
    ICE("ice"),
    @XmlEnumValue("ijo")
    IJO("ijo"),
    @XmlEnumValue("iku")
    IKU("iku"),
    @XmlEnumValue("ile")
    ILE("ile"),
    @XmlEnumValue("ilo")
    ILO("ilo"),
    @XmlEnumValue("ina")
    INA("ina"),
    @XmlEnumValue("inc")
    INC("inc"),
    @XmlEnumValue("ind")
    IND("ind"),
    @XmlEnumValue("ine")
    INE("ine"),
    @XmlEnumValue("ipk")
    IPK("ipk"),
    @XmlEnumValue("ira")
    IRA("ira"),
    @XmlEnumValue("iro")
    IRO("iro"),
    @XmlEnumValue("isl")
    ISL("isl"),
    @XmlEnumValue("ita")
    ITA("ita"),
    @XmlEnumValue("jav")
    JAV("jav"),
    @XmlEnumValue("jpn")
    JPN("jpn"),
    @XmlEnumValue("jpr")
    JPR("jpr"),
    @XmlEnumValue("jrb")
    JRB("jrb"),
    @XmlEnumValue("kaa")
    KAA("kaa"),
    @XmlEnumValue("kab")
    KAB("kab"),
    @XmlEnumValue("kac")
    KAC("kac"),
    @XmlEnumValue("kal")
    KAL("kal"),
    @XmlEnumValue("kam")
    KAM("kam"),
    @XmlEnumValue("kan")
    KAN("kan"),
    @XmlEnumValue("kar")
    KAR("kar"),
    @XmlEnumValue("kas")
    KAS("kas"),
    @XmlEnumValue("kat")
    KAT("kat"),
    @XmlEnumValue("kau")
    KAU("kau"),
    @XmlEnumValue("kaw")
    KAW("kaw"),
    @XmlEnumValue("kaz")
    KAZ("kaz"),
    @XmlEnumValue("kha")
    KHA("kha"),
    @XmlEnumValue("khi")
    KHI("khi"),
    @XmlEnumValue("khm")
    KHM("khm"),
    @XmlEnumValue("kho")
    KHO("kho"),
    @XmlEnumValue("kik")
    KIK("kik"),
    @XmlEnumValue("kin")
    KIN("kin"),
    @XmlEnumValue("kir")
    KIR("kir"),
    @XmlEnumValue("kmb")
    KMB("kmb"),
    @XmlEnumValue("kok")
    KOK("kok"),
    @XmlEnumValue("kom")
    KOM("kom"),
    @XmlEnumValue("kon")
    KON("kon"),
    @XmlEnumValue("kor")
    KOR("kor"),
    @XmlEnumValue("kos")
    KOS("kos"),
    @XmlEnumValue("kpe")
    KPE("kpe"),
    @XmlEnumValue("kro")
    KRO("kro"),
    @XmlEnumValue("kru")
    KRU("kru"),
    @XmlEnumValue("kua")
    KUA("kua"),
    @XmlEnumValue("kum")
    KUM("kum"),
    @XmlEnumValue("kur")
    KUR("kur"),
    @XmlEnumValue("kut")
    KUT("kut"),
    @XmlEnumValue("lad")
    LAD("lad"),
    @XmlEnumValue("lah")
    LAH("lah"),
    @XmlEnumValue("lam")
    LAM("lam"),
    @XmlEnumValue("lao")
    LAO("lao"),
    @XmlEnumValue("lat")
    LAT("lat"),
    @XmlEnumValue("lav")
    LAV("lav"),
    @XmlEnumValue("lez")
    LEZ("lez"),
    @XmlEnumValue("lin")
    LIN("lin"),
    @XmlEnumValue("lit")
    LIT("lit"),
    @XmlEnumValue("lol")
    LOL("lol"),
    @XmlEnumValue("loz")
    LOZ("loz"),
    @XmlEnumValue("ltz")
    LTZ("ltz"),
    @XmlEnumValue("lua")
    LUA("lua"),
    @XmlEnumValue("lub")
    LUB("lub"),
    @XmlEnumValue("lug")
    LUG("lug"),
    @XmlEnumValue("lui")
    LUI("lui"),
    @XmlEnumValue("lun")
    LUN("lun"),
    @XmlEnumValue("luo")
    LUO("luo"),
    @XmlEnumValue("lus")
    LUS("lus"),
    @XmlEnumValue("mac")
    MAC("mac"),
    @XmlEnumValue("mad")
    MAD("mad"),
    @XmlEnumValue("mag")
    MAG("mag"),
    @XmlEnumValue("mah")
    MAH("mah"),
    @XmlEnumValue("mai")
    MAI("mai"),
    @XmlEnumValue("mak")
    MAK("mak"),
    @XmlEnumValue("mal")
    MAL("mal"),
    @XmlEnumValue("man")
    MAN("man"),
    @XmlEnumValue("mao")
    MAO("mao"),
    @XmlEnumValue("map")
    MAP("map"),
    @XmlEnumValue("mar")
    MAR("mar"),
    @XmlEnumValue("mas")
    MAS("mas"),
    @XmlEnumValue("may")
    MAY("may"),
    @XmlEnumValue("mdr")
    MDR("mdr"),
    @XmlEnumValue("men")
    MEN("men"),
    @XmlEnumValue("mga")
    MGA("mga"),
    @XmlEnumValue("mic")
    MIC("mic"),
    @XmlEnumValue("min")
    MIN("min"),
    @XmlEnumValue("mis")
    MIS("mis"),
    @XmlEnumValue("mkd")
    MKD("mkd"),
    @XmlEnumValue("mkh")
    MKH("mkh"),
    @XmlEnumValue("mlg")
    MLG("mlg"),
    @XmlEnumValue("mlt")
    MLT("mlt"),
    @XmlEnumValue("mnc")
    MNC("mnc"),
    @XmlEnumValue("mni")
    MNI("mni"),
    @XmlEnumValue("mno")
    MNO("mno"),
    @XmlEnumValue("moh")
    MOH("moh"),
    @XmlEnumValue("mol")
    MOL("mol"),
    @XmlEnumValue("mon")
    MON("mon"),
    @XmlEnumValue("mos")
    MOS("mos"),
    @XmlEnumValue("mri")
    MRI("mri"),
    @XmlEnumValue("msa")
    MSA("msa"),
    @XmlEnumValue("mul")
    MUL("mul"),
    @XmlEnumValue("mun")
    MUN("mun"),
    @XmlEnumValue("mus")
    MUS("mus"),
    @XmlEnumValue("mwr")
    MWR("mwr"),
    @XmlEnumValue("mya")
    MYA("mya"),
    @XmlEnumValue("myn")
    MYN("myn"),
    @XmlEnumValue("nah")
    NAH("nah"),
    @XmlEnumValue("nai")
    NAI("nai"),
    @XmlEnumValue("nau")
    NAU("nau"),
    @XmlEnumValue("nav")
    NAV("nav"),
    @XmlEnumValue("nbl")
    NBL("nbl"),
    @XmlEnumValue("nde")
    NDE("nde"),
    @XmlEnumValue("ndo")
    NDO("ndo"),
    @XmlEnumValue("nds")
    NDS("nds"),
    @XmlEnumValue("nep")
    NEP("nep"),
    @XmlEnumValue("new")
    NEW("new"),
    @XmlEnumValue("nia")
    NIA("nia"),
    @XmlEnumValue("nic")
    NIC("nic"),
    @XmlEnumValue("niu")
    NIU("niu"),
    @XmlEnumValue("nld")
    NLD("nld"),
    @XmlEnumValue("nno")
    NNO("nno"),
    @XmlEnumValue("nob")
    NOB("nob"),
    @XmlEnumValue("non")
    NON("non"),
    @XmlEnumValue("nor")
    NOR("nor"),
    @XmlEnumValue("nso")
    NSO("nso"),
    @XmlEnumValue("nub")
    NUB("nub"),
    @XmlEnumValue("nya")
    NYA("nya"),
    @XmlEnumValue("nym")
    NYM("nym"),
    @XmlEnumValue("nyn")
    NYN("nyn"),
    @XmlEnumValue("nyo")
    NYO("nyo"),
    @XmlEnumValue("nzi")
    NZI("nzi"),
    @XmlEnumValue("oci")
    OCI("oci"),
    @XmlEnumValue("oji")
    OJI("oji"),
    @XmlEnumValue("ori")
    ORI("ori"),
    @XmlEnumValue("orm")
    ORM("orm"),
    @XmlEnumValue("osa")
    OSA("osa"),
    @XmlEnumValue("oss")
    OSS("oss"),
    @XmlEnumValue("ota")
    OTA("ota"),
    @XmlEnumValue("oto")
    OTO("oto"),
    @XmlEnumValue("paa")
    PAA("paa"),
    @XmlEnumValue("pag")
    PAG("pag"),
    @XmlEnumValue("pal")
    PAL("pal"),
    @XmlEnumValue("pam")
    PAM("pam"),
    @XmlEnumValue("pan")
    PAN("pan"),
    @XmlEnumValue("pap")
    PAP("pap"),
    @XmlEnumValue("pau")
    PAU("pau"),
    @XmlEnumValue("peo")
    PEO("peo"),
    @XmlEnumValue("per")
    PER("per"),
    @XmlEnumValue("phi")
    PHI("phi"),
    @XmlEnumValue("phn")
    PHN("phn"),
    @XmlEnumValue("pli")
    PLI("pli"),
    @XmlEnumValue("pol")
    POL("pol"),
    @XmlEnumValue("pon")
    PON("pon"),
    @XmlEnumValue("por")
    POR("por"),
    @XmlEnumValue("pra")
    PRA("pra"),
    @XmlEnumValue("pro")
    PRO("pro"),
    @XmlEnumValue("pus")
    PUS("pus"),
    @XmlEnumValue("qaa")
    QAA("qaa"),
    @XmlEnumValue("que")
    QUE("que"),
    @XmlEnumValue("raj")
    RAJ("raj"),
    @XmlEnumValue("rap")
    RAP("rap"),
    @XmlEnumValue("rar")
    RAR("rar"),
    @XmlEnumValue("roa")
    ROA("roa"),
    @XmlEnumValue("roh")
    ROH("roh"),
    @XmlEnumValue("rom")
    ROM("rom"),
    @XmlEnumValue("ron")
    RON("ron"),
    @XmlEnumValue("rum")
    RUM("rum"),
    @XmlEnumValue("run")
    RUN("run"),
    @XmlEnumValue("rus")
    RUS("rus"),
    @XmlEnumValue("sad")
    SAD("sad"),
    @XmlEnumValue("sag")
    SAG("sag"),
    @XmlEnumValue("sah")
    SAH("sah"),
    @XmlEnumValue("sai")
    SAI("sai"),
    @XmlEnumValue("sal")
    SAL("sal"),
    @XmlEnumValue("sam")
    SAM("sam"),
    @XmlEnumValue("san")
    SAN("san"),
    @XmlEnumValue("sas")
    SAS("sas"),
    @XmlEnumValue("sat")
    SAT("sat"),
    @XmlEnumValue("scc")
    SCC("scc"),
    @XmlEnumValue("sco")
    SCO("sco"),
    @XmlEnumValue("scr")
    SCR("scr"),
    @XmlEnumValue("sel")
    SEL("sel"),
    @XmlEnumValue("sem")
    SEM("sem"),
    @XmlEnumValue("sga")
    SGA("sga"),
    @XmlEnumValue("sgn")
    SGN("sgn"),
    @XmlEnumValue("shn")
    SHN("shn"),
    @XmlEnumValue("sid")
    SID("sid"),
    @XmlEnumValue("sin")
    SIN("sin"),
    @XmlEnumValue("sio")
    SIO("sio"),
    @XmlEnumValue("sit")
    SIT("sit"),
    @XmlEnumValue("sla")
    SLA("sla"),
    @XmlEnumValue("slk")
    SLK("slk"),
    @XmlEnumValue("slo")
    SLO("slo"),
    @XmlEnumValue("slv")
    SLV("slv"),
    @XmlEnumValue("sme")
    SME("sme"),
    @XmlEnumValue("smi")
    SMI("smi"),
    @XmlEnumValue("smo")
    SMO("smo"),
    @XmlEnumValue("sna")
    SNA("sna"),
    @XmlEnumValue("snd")
    SND("snd"),
    @XmlEnumValue("snk")
    SNK("snk"),
    @XmlEnumValue("sog")
    SOG("sog"),
    @XmlEnumValue("som")
    SOM("som"),
    @XmlEnumValue("son")
    SON("son"),
    @XmlEnumValue("sot")
    SOT("sot"),
    @XmlEnumValue("spa")
    SPA("spa"),
    @XmlEnumValue("sqi")
    SQI("sqi"),
    @XmlEnumValue("srd")
    SRD("srd"),
    @XmlEnumValue("srp")
    SRP("srp"),
    @XmlEnumValue("srr")
    SRR("srr"),
    @XmlEnumValue("ssa")
    SSA("ssa"),
    @XmlEnumValue("ssw")
    SSW("ssw"),
    @XmlEnumValue("suk")
    SUK("suk"),
    @XmlEnumValue("sun")
    SUN("sun"),
    @XmlEnumValue("sus")
    SUS("sus"),
    @XmlEnumValue("sux")
    SUX("sux"),
    @XmlEnumValue("swa")
    SWA("swa"),
    @XmlEnumValue("swe")
    SWE("swe"),
    @XmlEnumValue("syr")
    SYR("syr"),
    @XmlEnumValue("tah")
    TAH("tah"),
    @XmlEnumValue("tai")
    TAI("tai"),
    @XmlEnumValue("tam")
    TAM("tam"),
    @XmlEnumValue("tat")
    TAT("tat"),
    @XmlEnumValue("tel")
    TEL("tel"),
    @XmlEnumValue("tem")
    TEM("tem"),
    @XmlEnumValue("ter")
    TER("ter"),
    @XmlEnumValue("tet")
    TET("tet"),
    @XmlEnumValue("tgk")
    TGK("tgk"),
    @XmlEnumValue("tgl")
    TGL("tgl"),
    @XmlEnumValue("tha")
    THA("tha"),
    @XmlEnumValue("tib")
    TIB("tib"),
    @XmlEnumValue("tig")
    TIG("tig"),
    @XmlEnumValue("tir")
    TIR("tir"),
    @XmlEnumValue("tiv")
    TIV("tiv"),
    @XmlEnumValue("tkl")
    TKL("tkl"),
    @XmlEnumValue("tli")
    TLI("tli"),
    @XmlEnumValue("tmh")
    TMH("tmh"),
    @XmlEnumValue("tog")
    TOG("tog"),
    @XmlEnumValue("ton")
    TON("ton"),
    @XmlEnumValue("tpi")
    TPI("tpi"),
    @XmlEnumValue("tsi")
    TSI("tsi"),
    @XmlEnumValue("tsn")
    TSN("tsn"),
    @XmlEnumValue("tso")
    TSO("tso"),
    @XmlEnumValue("tuk")
    TUK("tuk"),
    @XmlEnumValue("tum")
    TUM("tum"),
    @XmlEnumValue("tur")
    TUR("tur"),
    @XmlEnumValue("tut")
    TUT("tut"),
    @XmlEnumValue("tvl")
    TVL("tvl"),
    @XmlEnumValue("twi")
    TWI("twi"),
    @XmlEnumValue("tyv")
    TYV("tyv"),
    @XmlEnumValue("uga")
    UGA("uga"),
    @XmlEnumValue("uig")
    UIG("uig"),
    @XmlEnumValue("ukr")
    UKR("ukr"),
    @XmlEnumValue("umb")
    UMB("umb"),
    @XmlEnumValue("und")
    UND("und"),
    @XmlEnumValue("urd")
    URD("urd"),
    @XmlEnumValue("uzb")
    UZB("uzb"),
    @XmlEnumValue("vai")
    VAI("vai"),
    @XmlEnumValue("ven")
    VEN("ven"),
    @XmlEnumValue("vie")
    VIE("vie"),
    @XmlEnumValue("vol")
    VOL("vol"),
    @XmlEnumValue("vot")
    VOT("vot"),
    @XmlEnumValue("wak")
    WAK("wak"),
    @XmlEnumValue("wal")
    WAL("wal"),
    @XmlEnumValue("war")
    WAR("war"),
    @XmlEnumValue("was")
    WAS("was"),
    @XmlEnumValue("wel")
    WEL("wel"),
    @XmlEnumValue("wen")
    WEN("wen"),
    @XmlEnumValue("wol")
    WOL("wol"),
    @XmlEnumValue("xho")
    XHO("xho"),
    @XmlEnumValue("yao")
    YAO("yao"),
    @XmlEnumValue("yap")
    YAP("yap"),
    @XmlEnumValue("yid")
    YID("yid"),
    @XmlEnumValue("yor")
    YOR("yor"),
    @XmlEnumValue("ypk")
    YPK("ypk"),
    @XmlEnumValue("zap")
    ZAP("zap"),
    @XmlEnumValue("zen")
    ZEN("zen"),
    @XmlEnumValue("zha")
    ZHA("zha"),
    @XmlEnumValue("zho")
    ZHO("zho"),
    @XmlEnumValue("znd")
    ZND("znd"),
    @XmlEnumValue("zul")
    ZUL("zul");
    private final String value;

    XmlOtDtLANG(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static XmlOtDtLANG fromValue(String v) {
        for (XmlOtDtLANG c: XmlOtDtLANG.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
