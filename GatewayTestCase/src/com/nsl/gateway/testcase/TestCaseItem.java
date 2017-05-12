package com.nsl.gateway.testcase;

import java.util.Base64;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.nsl.gateway.util.XmlParser;

@XmlRootElement(name = "item")
public class TestCaseItem {
	private String sapClient;

	private String testGroup;

	private String testCase;

	private String changedBy;

	private String changedAt;

	private String method;

	private String requestUri;

	private String requestBase64Data;

	private String changedDate;

	private String changedTime;

	@XmlElement(name = "SAPCLIENT")
	public String getSapClient() {
		return sapClient;
	}

	public void setSapClient(String sapClient) {
		this.sapClient = sapClient;
	}

	@XmlElement(name = "TESTGROUP")
	public String getTestGroup() {
		return testGroup;
	}

	public void setTestGroup(String testGroup) {
		this.testGroup = testGroup;
	}

	@XmlElement(name = "TESTCASE")
	public String getTestCase() {
		return testCase;
	}

	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}

	@XmlElement(name = "CHANGED_BY")
	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	@XmlElement(name = "CHANGED_AT")
	public String getChangedAt() {
		return changedAt;
	}

	public void setChangedAt(String changedAt) {
		this.changedAt = changedAt;
	}

	@XmlElement(name = "METHOD")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@XmlElement(name = "REQUEST_URI")
	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	@XmlElement(name = "CHANGED_DATE")
	public String getChangedDate() {
		return changedDate;
	}

	public void setChangedDate(String changedDate) {
		this.changedDate = changedDate;
	}

	@XmlElement(name = "CHANGED_TIME")
	public String getChangedTime() {
		return changedTime;
	}

	public void setChangedTime(String changedTime) {
		this.changedTime = changedTime;
	}

	@XmlElement(name = "REQUEST_DATA")
	public String getRequestBase64Data() {
		return requestBase64Data;
	}

	public void setRequestBase64Data(String requestBase64Data) {
		this.requestBase64Data = requestBase64Data;
	}

	/**
	 * RequestData為base 64編碼, 解開之後為xml, 取出裡面的
	 * 
	 * @return
	 */
	public RequestData getRequestData() {
		try {
			final Base64.Decoder decoder = Base64.getDecoder();
			byte[] result = DatatypeConverter.parseBase64Binary(this.getRequestBase64Data());
			String result2 = DatatypeConverter.printBase64Binary(result);
			String requestData = new String(decoder.decode(result2), "UTF-8");
			requestData = requestData.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
			return (RequestData) new XmlParser().parseFromString(requestData, RequestData.class, "HTTP_BODY");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE) + getRequestData().toString();
	}
}
