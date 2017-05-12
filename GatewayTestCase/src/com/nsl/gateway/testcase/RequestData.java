package com.nsl.gateway.testcase;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@XmlRootElement(name = "REQUEST_DATA")
public class RequestData {
	private RequestHeader header;

	private String body;

	@XmlElement(name = "HTTP_HEADER")
	public RequestHeader getHeaders() {
		return header;
	}

	public void setHeaders(RequestHeader header) {
		this.header = header;
	}

	@XmlElement(name = "HTTP_BODY")
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
