//
// 이 파일은 JAXB(JavaTM Architecture for XML Binding) 참조 구현 2.2.8-b130911.1802 버전을 통해 생성되었습니다. 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>를 참조하십시오. 
// 이 파일을 수정하면 소스 스키마를 재컴파일할 때 수정 사항이 손실됩니다. 
// 생성 날짜: 2024.01.27 시간 04:15:52 PM KST 
//

package com.gaga.springtoby.user.sqlService.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>sqlType complex type에 대한 Java 클래스입니다.
 *
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 *
 * <pre>
 * &lt;complexType name="sqlType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sqlType", propOrder = {
	"value"
})
// <sql> 태그 한 개당 SqlType 오브젝트가 하나씩 만들어진다.
public class SqlType {

	// SQL 값을 저장할 스트링 타입의 필드
	@XmlValue
	protected String value;
	// key 애트리뷰트에 담긴 검색용 키 값을 위한 스트링 타입의 필드
	@XmlAttribute(name = "key", required = true)
	protected String key;

	/**
	 * value 속성의 값을 가져옵니다.
	 *
	 * @return
	 *     possible object is
	 *     {@link String }
	 *
	 */
	public String getValue() {
		return value;
	}

	/**
	 * value 속성의 값을 설정합니다.
	 *
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * key 속성의 값을 가져옵니다.
	 *
	 * @return
	 *     possible object is
	 *     {@link String }
	 *
	 */
	public String getKey() {
		return key;
	}

	/**
	 * key 속성의 값을 설정합니다.
	 *
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *
	 */
	public void setKey(String value) {
		this.key = value;
	}

}
