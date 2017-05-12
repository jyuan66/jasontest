package com.nsl.gateway.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * 讀取檔案並回傳String
 * 
 * @author WSNPI05
 *
 */
public class FileUtils {
	public static String readFileToString(String abstractFileName) throws Exception {
		BufferedReader input = null;
		try {
			File aFile = new File(abstractFileName);
			input = new BufferedReader(new FileReader(aFile));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = input.readLine()) != null) {
				sb.append(line).append(System.lineSeparator());
			}
			return sb.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			input.close();
		}
	}

	public static String readFileToString(String path, String fileName) throws Exception {
		return readFileToString(path + fileName);
	}
}
