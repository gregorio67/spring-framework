	package kics.main.sample.cxf.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ksign.securedb.api.SDBCrypto;

import kics.framework.collection.KicsBaseVo;
import kics.framework.constant.KsignConstants;
import kics.framework.exception.KicsRuntimeException;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "" , 
	propOrder = { "id", 
        "name", 
        "age", 
        "tel", 
        "bloodtype",
        "gender",
        "cmCd",
        "cmCdName"} )
@XmlRootElement(name="CUSTOMER")
public class CustomerVo extends KicsBaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7380469079760289356L;

	/** 아이디 */
	@XmlElement(name ="ID")
	private String id;

	/** 이름 */
	@XmlElement(name ="NAME")
	private String name;

	/** 나이 */
	@XmlElement(name ="AGE")
	private long age;
	
	/** 전화번호 */
	@XmlElement(name ="TEL")
	private String tel;
	
	/** 혈액형 */
	@XmlElement(name ="BLOODTYPE")
	private String bloodtype;
	
	/** 성별 */
	@XmlElement(name ="GENDER")
	private String gender;
	
	/** 공통코드 */
	@XmlElement(name ="CM_CD")
	private String cmCd;
	
	/** 공통코드명 */
	@XmlElement(name ="CM_CD_NAME")
	private String cmCdName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getBloodtype() {
		return bloodtype;
	}

	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getCmCd() {
		return cmCd;
	}

	public void setCmCd(String cmCd) {
		this.cmCd = cmCd;
	}

	public String getCmCdName() {
		return cmCdName;
	}

	public void setCmCdName(String cmCdName) {
		this.cmCdName = cmCdName;
	}
}
