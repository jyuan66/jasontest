package com.nsl.gateway.util;

import org.json.JSONObject;
import org.json.XML;

/**
 * OData (XML)轉為JSON字串, 還沒用到
 * 
 * @author WSNPI05
 *
 */
public class ODataReader {
	/**
	 * OData (XML)轉為JSON字串
	 * 
	 * @param xmlString
	 * @return
	 * @throws Exception
	 */
	public static JSONObject xmlToJson(String xmlString) throws Exception {
		return XML.toJSONObject(xmlString);
	}

	/**
	 * 讀取XML file轉為JSON字串
	 * 
	 * @param abstractFileName
	 * @return
	 * @throws Exception
	 */
	public static JSONObject xmlFileToJson(String abstractFileName) throws Exception {
		return xmlToJson(FileUtils.readFileToString(abstractFileName));
	}

	/**
	 * 讀取XML file轉為JSON字串
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
