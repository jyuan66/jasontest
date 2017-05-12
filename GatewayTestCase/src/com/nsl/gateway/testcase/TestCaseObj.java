package com.nsl.gateway.testcase;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@XmlRootElement(name = "TEST_CASES")
public class TestCaseObj {
	private List<TestCaseItem> items;

	@XmlElement(name = "item")
	public List<TestCaseItem> getItems() {
		return items;
	}

	public void setItems(List<TestCaseItem> items) {
		this.items = items;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
