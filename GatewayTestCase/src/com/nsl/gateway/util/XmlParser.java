package com.nsl.gateway.util;

import java.io.File;
import java.io.StringReader;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.transform.stream.StreamSource;

public class XmlParser {
	private static final String START_TAG_REPLACE = "|||";

	private static final String END_TAG_REPLACE = "~~~";

	public <T> Object parseFromFile(String abstractFileName, Class<T> clazz) throws Exception {
		try {
			File file = new File(abstractFileName);
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller u = jc.createUnmarshaller();
			return u.unmarshal(file);
		} catch (Exception e) {
			throw e;
		}
	}

	public <T> Object parseFromString(String xmlString, Class<T> clazz) throws Exception {
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller u = jc.createUnmarshaller();
			StringBuffer xmlStr = new StringBuffer(xmlString);
			return u.unmarshal(new StreamSource(new StringReader(xmlStr.toString())));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * �����Y��tag���B�z
	 * <p>
	 * �p�U, a�̭���b & d tag�n�ഫ, d�̭���xml tag���B�z
	 * 
	 * <pre>
	 * <a>
	 * 	<b></b>
	 * 	<d>
	 * 		<!-- "Unknown" XML here -->
	 * 		<maybeE></maybeE>
	 * 		<maybeF></maybeF>
	 * 		<!-- etc etc -->
	 * 	<d/>
	 * </a>
	 * </pre>
	 * 
	 * @param xmlString
	 * @param clazz
	 * @param doNotProcessInnerTag
	 * @return
	 * @throws Exception
	 */
	public <T> Object parseFromString(String xmlString, Class<T> clazz, String doNotProcessInnerTag) throws Exception {
		Object obj = this.parseFromString(this.processInnerXmlToString(xmlString, doNotProcessInnerTag), clazz);
		revertInnerStringToXml(obj, doNotProcessInnerTag);
		return obj;
	}

	/**
	 * �NinnerTag�������Ÿ�
	 * 
	 * @param xmlString
	 * @param doNotProcessInnerTag
	 * @return
	 */
	private String processInnerXmlToString(String xmlString, String doNotProcessInnerTag) {
		String tagBegin = "<" + doNotProcessInnerTag + ">";
		String tagEnd = "</" + doNotProcessInnerTag + ">";
		String result = null;
		if (xmlString.indexOf(tagBegin) >= 0) {
			int begin = xmlString.indexOf(tagBegin) + tagBegin.length();
			int end = xmlString.indexOf(tagEnd);
			result = xmlString.substring(0, begin)
					+ xmlString.substring(begin, end).replace("<", START_TAG_REPLACE).replace(">", END_TAG_REPLACE)
					+ xmlString.substring(end);
		} else {
			result = xmlString;
		}
		return result;
	}

	/**
	 * �N�Ÿ�������innerTag
	 * 
	 * @param obj
	 * @param doNotProcessInnerTag
	 * @throws Exception
	 */
	private void revertInnerStringToXml(Object obj, String doNotProcessInnerTag) throws Exception {
		String getMethodName = this.getMethodNameByAnnotation(obj, doNotProcessInnerTag);
		Method getMethod = obj.getClass().getMethod(getMethodName);
		String value = (String) getMethod.invoke(obj);

		value = value.replace(START_TAG_REPLACE, "<").replace(END_TAG_REPLACE, ">");

		String setMethodName = getMethodName.replace("get", "set");
		Method setMethod = obj.getClass().getMethod(setMethodName, new Class[] { value.getClass() });
		setMethod.invoke(obj, value);
	}

	/**
	 * �ھ�annotationName���o����method
	 * 
	 * @param obj
	 * @param annotationName
	 * @return
	 */
	private String getMethodNameByAnnotation(Object obj, String annotationName) {
		Method[] methods = obj.getClass().getMethods();
		if (methods != null && methods.length > 0) {
			for (Method m : methods) {
				XmlElement annotation = m.getAnnotation(XmlElement.class);
				if (annotation != null && annotation.name().equals(annotationName)) {
					return m.getName();
				}
			}
		}
		return null;
	}
}