package com.nsl.gateway.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 透過http client call Gateway service的Utils
 * 
 * @author WSNPI05
 *
 */
public class GatewayCaller {
	private String hostname;

	private int port;

	private String scheme;

	private HttpClientContext context;

	/**
	 * 
	 * @param hostname
	 * @param port
	 * @param scheme
	 * @param userName
	 * @param password
	 */
	public GatewayCaller(String hostname, int port, String scheme, String userName, String password) {
		this.hostname = hostname;
		this.port = port;
		this.scheme = scheme;
		this.context = getHttpClientContext(hostname, port, scheme, userName, password);
	}

	/**
	 * set authorization information to pass Single Sign On (SSO)
	 * 
	 * @param hostname
	 * @param port
	 * @param scheme
	 * @param userName
	 * @param password
	 * @return
	 */
	private HttpClientContext getHttpClientContext(String hostname, int port, String scheme, String userName,
			String password) {
		// Add AuthCache to the execution context
		HttpClientContext context = HttpClientContext.create();

		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(hostname, port),
				new UsernamePasswordCredentials(userName, password));
		context.setCredentialsProvider(credsProvider);

		// Generate BASIC scheme object and add it to the local auth cache
		AuthCache authCache = new BasicAuthCache();
		authCache.put(new HttpHost(hostname, port, scheme), new BasicScheme());
		context.setAuthCache(authCache);

		return context;
	}

	/**
	 * get full URL
	 * 
	 * @param url
	 * @return
	 */
	public String getFullUrl(String url) {
		return this.scheme + "://" + this.hostname + ":" + this.port + url;
	}

	/**
	 * 需替換URL內的空白為%20, 才能被http client執行
	 * 
	 * @param url
	 * @return
	 */
	private String relpaceSpace(String url) {
		return url.replaceAll(" ", "%20");
	}

	/**
	 * execute http get method
	 * 
	 * @param url
	 * @throws Exception
	 */
	public ResponseObj get(String url) throws Exception {
		String fullUrl = getFullUrl(url);
		System.out.println(fullUrl);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet request = new HttpGet(relpaceSpace(fullUrl));
		CloseableHttpResponse response = httpclient.execute(request, this.context);

		try {
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "big5");

			ResponseObj responseObj = new ResponseObj();
			responseObj.setStatus(response.getStatusLine().getStatusCode());
			responseObj.setContentLength(entity.getContentLength());
			responseObj.setContentType(entity.getContentType().getValue());
			responseObj.setContent(responseString);
			return responseObj;
		} catch (Exception e) {
			throw e;
		} finally {
			response.close();
		}
	}

	/**
	 * execute http post method
	 * 
	 * @param url
	 * @throws Exception
	 */
	public ResponseObj post(String url) throws Exception {
		return post(url, "");
	}

	public String getCookieValue(CookieStore cookieStore, String cookieName) {
		String value = null;
		for (Cookie cookie : cookieStore.getCookies()) {
			if (cookie.getName().equals(cookieName)) {
				value = cookie.getValue();
				break;
			}
		}
		return value;
	}

	/**
	 * execute http post method
	 * 
	 * @param url
	 * @throws Exception
	 */
	public ResponseObj post(String url, String requestBody) throws Exception {
		String fullUrl = getFullUrl(url);
		System.out.println(fullUrl);
		System.out.println(requestBody);

		// TODO by Jason
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost request = new HttpPost(relpaceSpace(fullUrl));

		CookieStore cookieStore = new BasicCookieStore();
		context.setCookieStore(cookieStore);
		List params = new ArrayList();
		params.add(new BasicNameValuePair("_csrf", getCookieValue(cookieStore, "_csrf")));

		// Add any other needed post parameters
		UrlEncodedFormEntity paramEntity = new UrlEncodedFormEntity(params);
		request.setEntity(paramEntity);

		request.addHeader("X-Requested-With", "XMLHttpRequest");
		request.addHeader("Accept", "application/atom+xml,application/atomsvc+xml,application/xml");
		request.addHeader("Content-Type", "application/atom+xml;type=entry; charset=utf-8");
		request.addHeader("Host","dp2wdp.nanshan.com.tw");
		request.addHeader("DataServiceVersion", "2.0");
		request.addHeader("X-CSRF-Token", "Fetch");
		request.addHeader("X-XHR-Logon", "accept=\"iframe\"");
		request.addHeader("X-HTTP-Method", "POST");
		request.addHeader("If-Match", "*");
		// request.addHeader("X-RequestDigest", FormDigestValue);

		StringEntity se = new StringEntity(requestBody);
		request.setEntity(se);

		// List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		// nvps.add(new BasicNameValuePair("username", "W9006357"));
		// nvps.add(new BasicNameValuePair("password", "Qwer1234"));
		// request.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response = httpclient.execute(request, this.context);

		try {
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "big5");
			EntityUtils.consume(entity);

			ResponseObj responseObj = new ResponseObj();
			responseObj.setStatus(response.getStatusLine().getStatusCode());
			responseObj.setContentLength(entity.getContentLength());
			responseObj.setContentType(entity.getContentType().getValue());
			responseObj.setContent(responseString);
			return responseObj;
		} catch (Exception e) {
			throw e;
		} finally {
			response.close();
		}
	}

	/**
	 * execute http post method
	 * 
	 * @param url
	 * @throws Exception
	 */
	public ResponseObj batch(String url, String requestBody) throws Exception {
		// TODO by Jason
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String fullUrl = getFullUrl(url);
		System.out.println(fullUrl);
		System.out.println(requestBody);
		HttpPost request = new HttpPost(relpaceSpace(fullUrl));
		request.addHeader("Content-type", "multipart/mixed;boundary=batch");
		request.addHeader("X-Requested-With", "XMLHttpRequest");
		request.addHeader("Accept", "application/atom+xml,application/atomsvc+xml,application/xml");
		request.addHeader("Content-Type", "application/atom+xml");

		// request.addHeader("Content-Type",
		// "application/x-www-form-urlencoded");

		request.addHeader("DataServiceVersion", "2.0");
		request.addHeader("X-CSRF-Token", "Fetch");
		// request.addHeader("Accept", "application/json;odata=verbose");
		// request.addHeader("Content-type", "application/json;odata=verbose");
		// request.addHeader("X-HTTP-Method", "POST");
		StringEntity se = new StringEntity(requestBody);
		request.setEntity(se);

		// List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		// nvps.add(new BasicNameValuePair("username", "W9006357"));
		// nvps.add(new BasicNameValuePair("password", "Qwer1234"));
		// request.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response = httpclient.execute(request, this.context);

		try {
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "big5");
			EntityUtils.consume(entity);

			ResponseObj responseObj = new ResponseObj();
			responseObj.setStatus(response.getStatusLine().getStatusCode());
			responseObj.setContentLength(entity.getContentLength());
			responseObj.setContentType(entity.getContentType().getValue());
			responseObj.setContent(responseString);
			return responseObj;
		} catch (Exception e) {
			throw e;
		} finally {
			response.close();
		}
	}

	/**
	 * execute http put method
	 * 
	 * @param url
	 * @throws Exception
	 */
	public ResponseObj put(String url) throws Exception {
		// TODO by Ginny
		return null;
	}

	/**
	 * execute http delete method
	 * 
	 * @param url
	 * @throws Exception
	 */
	public ResponseObj delete(String url) throws Exception {
		// TODO by Michelle
		return null;
	}

	public static void main(String args[]) throws Exception {
		GatewayCaller caller = new GatewayCaller("dp2wdp.nanshan.com.tw", 8888, "http", "W9006357", "Qwer1234");
		String testDataPath = "./testData/Candidate360/";

		ResponseObj response = null;

		// response =
		// caller.get("/sap/opu/odata/NSL/CANDIDATE_360_SRV/CandidateSet");
		// System.out.println(response.toString() + "\n");
		//
		// response = caller
		// .get("/sap/opu/odata/NSL/CANDIDATE_360_SRV/SearchCandidate?NameLast='Danaus
		// Tsai'&RoleType='ALL'");
		// System.out.println(response.toString() + "\n");
		//
		// response =
		// caller.get("/sap/opu/odata/NSL/CANDIDATE_360_SRV/AptitudeTestResultList?CandidateId='9000000959'");
		// System.out.println(response.toString() + "\n");

		response = caller.post("/sap/opu/odata/NSL/CANDIDATE_360_SRV/$batch",
				FileUtils.readFileToString(testDataPath, "BatchUpdate_RECRUIT_LEVEL.txt"));
		System.out.println(response.toString() + "\n");

	}
}
