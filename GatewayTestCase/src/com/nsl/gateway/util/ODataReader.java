package com.nsl.gateway.util;

import org.json.JSONObject;
import org.json.XML;

/**
 * OData (XML)�ରJSON�r��, �٨S�Ψ�
 * 
 * @author WSNPI05
 *
 */
public class ODataReader {
	/**
	 * OData (XML)�ରJSON�r��
	 * 
	 * @param xmlString
	 * @return
	 * @throws Exception
	 */
	public static JSONObject xmlToJson(String xmlString) throws Exception {
		return XML.toJSONObject(xmlString);
	}

	/**
	 * Ū��XML file�ରJSON�r��
	 * 
	 * @param abstractFileName
	 * @return
	 * @throws Exception
	 */
	public static JSONObject xmlFileToJson(String abstractFileName) throws Exception {
		return xmlToJson(FileUtils.readFileToString(abstractFileName));
	}

	/**
	 * Ū��XML file�ରJSON�r��
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static JSONObject xmlFileToJson(String path, String fileName) throws Exception {
		return xmlToJson(FileUtils.readFileToString(path + fileName));
	}
}
