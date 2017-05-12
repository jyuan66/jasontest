package com.nsl.gateway.testcase;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@XmlRootElement(name = "HTTP_HEADER")
public class RequestHeader {
	private List<RequestHeaderProperty> properties;

	@XmlElement(name = "_-IWFND_-SUTIL_PROPERTY")
	public List<RequestHeaderProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<RequestHeaderProperty> properties) {
		this.properties = properties;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
