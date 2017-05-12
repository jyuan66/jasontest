package com.nsl.gateway.util;

import com.nsl.gateway.testcase.TestCaseObj;

public class TestCaseReader {
	public TestCaseObj readFromFile(String abstractFileName) throws Exception {
		return (TestCaseObj) new XmlParser().parseFromFile(abstractFileName, TestCaseObj.class);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(new TestCaseReader().readFromFile("./testData/GW_CLIENT_Test_Cases.xml"));
	}
}
