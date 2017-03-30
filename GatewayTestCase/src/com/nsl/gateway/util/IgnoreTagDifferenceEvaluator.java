package com.nsl.gateway.util;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;

public final class IgnoreTagDifferenceEvaluator implements DifferenceEvaluator {
	private boolean printLog;

	private List<String> ignoreTagList = new ArrayList<String>();

	private List<String> ignoreAttributeList = new ArrayList<String>();

	public IgnoreTagDifferenceEvaluator(List<String> ignoreNodeList, List<String> ignoreAttributeList,
			boolean printLog) {
		this.ignoreTagList = ignoreNodeList;
		this.ignoreAttributeList = ignoreAttributeList;
		this.printLog = printLog;
	}

	@Override
	public ComparisonResult evaluate(Comparison comparison, ComparisonResult outcome) {
		if (outcome == ComparisonResult.EQUAL)
			return outcome; // only evaluate differences.
		final Node controlNode = comparison.getControlDetails().getTarget();
		final Node testNode = comparison.getTestDetails().getTarget();
		if (controlNode instanceof Node) {
			Node node1 = (Node) controlNode;

			if (node1.getNodeType() == Node.ATTRIBUTE_NODE) {
				if (ignoreAttributeList.contains(node1.getNodeName())) {
					if (printLog) {
						System.out.println("DiffUtils : ignore difference attribute " + node1.getNodeName() + ", "
								+ node1.getNodeValue() + " to " + testNode.getNodeValue());
					}
					return ComparisonResult.SIMILAR; // will evaluate this
														// difference as similar
				}
			} else if (node1.getNodeType() == Node.TEXT_NODE) {
				// 比對到不同處的node為value, tag為parent node,
				// 例如<updated>2017-03-29T02:08:25Z</updated>,
				// controlNode為2017-03-29T02:08:25Z, parentNode才是<updated>
				Node parentNode = node1.getParentNode();
				if (ignoreTagList.contains(parentNode.getNodeName())) {
					if (printLog) {
						System.out.println("DiffUtils : ignore difference tag <" + parentNode.getNodeName() + ">, "
								+ node1.getNodeValue() + " to " + testNode.getNodeValue());
					}
					return ComparisonResult.SIMILAR; // will evaluate this
														// difference as similar
				}
			} else {
				if (printLog) {
					System.out.println("DiffUtils : NodeType=" + node1.getNodeType() + ", " + node1.getNodeValue()
							+ " to " + testNode.getNodeValue());
				}
			}
		}
		return outcome;
	}
}