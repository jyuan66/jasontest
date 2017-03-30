package com.nsl.gateway.util;

import java.util.ArrayList;
import java.util.List;

import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

public class DiffUtils {
	private boolean printLog = true;

	private List<String> ignoreTagList = new ArrayList<String>();

	private List<String> ignoreAttributeList = new ArrayList<String>();

	private Diff myDiff = null;

	public DiffUtils() {

	}

	/**
	 * �O�_�n�L�Xlog, �w�]��true
	 * 
	 * @param printLog
	 * @return
	 */
	public DiffUtils printLog(boolean printLog) {
		this.printLog = printLog;
		return this;
	}

	/**
	 * �����Y��tag���e���t��, �p<Updated>2017/03/29 01:02:03.004</Updated>
	 * 
	 * @param node
	 * @return
	 */
	public DiffUtils ignoreTag(String node) {
		ignoreTagList.add(node);
		return this;
	}

	/**
	 * �����Y��attribute���e���t��, �p<feed xml:base="http://DP2WDP...."/>
	 * 
	 * @param attribute
	 * @return
	 */
	public DiffUtils ignoreAttribute(String attribute) {
		ignoreAttributeList.add(attribute);
		return this;
	}

	/**
	 * ����tag�������ť�
	 * 
	 * @param str
	 * @return
	 */
	private String removeWhiteSpace(String str) {
		return str.replaceAll(">\\s+<", "><");
	}

	/**
	 * ���xml���e
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public DiffUtils diff(String src, String dest) {
		String src1 = removeWhiteSpace(src);
		String dest1 = removeWhiteSpace(dest);
		if (printLog) {
			System.out.println("DiffUtils : compare src : " + src1);
			System.out.println("            dest : " + dest1);
		}

		myDiff = DiffBuilder.compare(src1).withTest(dest1)
				.withDifferenceEvaluator(new IgnoreTagDifferenceEvaluator(ignoreTagList, ignoreAttributeList, printLog))
				.checkForSimilar().build();
		return this;
	}

	/**
	 * �O�_��Difference
	 * 
	 * @return
	 */
	public boolean hasDifferences() {
		return myDiff.hasDifferences();
	}

	/**
	 * ���Difference
	 * 
	 * @return
	 */
	public String getDiff() {
		if (hasDifferences()) {
			System.out.println("DiffUtils : " + myDiff.toString());
			return myDiff.toString();
		} else {
			return null;
		}
	}
}
