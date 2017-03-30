package com.nsl.gateway.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
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
		HttpHost targetHost = new HttpHost(hostname, port, scheme);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);
		credsProvider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()), creds);

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);

		// Add AuthCache to the execution context
		HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
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
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String fullUrl = getFullUrl(url);
		System.out.println(fullUrl);
		HttpGet httpGet = new HttpGet(relpaceSpace(fullUrl));
		CloseableHttpResponse httpResponse = httpclient.execute(httpGet, this.context);

		try {
			HttpEntity entity = httpResponse.getEntity();
			String responseString = EntityUtils.toString(entity, "big5");

			ResponseObj responseObj = new ResponseObj();
			responseObj.setStatus(httpResponse.getStatusLine().getStatusCode());
			responseObj.setContentLength(entity.getContentLength());
			responseObj.setContentType(entity.getContentType().getValue());
			responseObj.setContent(responseString);
			return responseObj;
		} catch (Exception e) {
			throw e;
		} finally {
			httpResponse.close();
		}
	}

	/**
	 * execute http post method
	 * @param url
	 * @throws Exception
	 */
	public ResponseObj post(String url) throws Exception {
		//TODO by Jason
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "W9006357"));
		nvps.add(new BasicNameValuePair("password", "Qwer1234"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response2 = httpclient.execute(httpPost, this.context);

		try {
			System.out.println(response2.getStatusLine());
			HttpEntity entity2 = response2.getEntity();
			EntityUtils.consume(entity2);

			ResponseObj responseObj = new ResponseObj();
//			responseObj.setStatus(httpResponse.getStatusLine().getStatusCode());
//			responseObj.setContentLength(entity.getContentLength());
//			responseObj.setContentType(entity.getContentType().getValue());
//			responseObj.setContent(responseString);
			return responseObj;
		} catch (Exception e) {
			throw e;
		} finally {
			response2.close();
		}
	}

	/**
	 * execute http put method
	 * @param url
	 * @throws Exception
	 */
	public ResponseObj put(String url) throws Exception {
		//TODO by Ginny
		return null;
	}

	/**
	 * execute http delete method
	 * @param url
	 * @throws Exception
	 */
	public ResponseObj delete(String url) throws Exception {
		//TODO by Michelle
		return null;
	}

	public static void main(String args[]) throws Exception {
		GatewayCaller caller = new GatewayCaller("dp2wdp.nanshan.com.tw", 8888, "http", "W9006357", "Qwer1234");

		ResponseObj response = null;

		response = caller.get("/sap/opu/odata/NSL/CANDIDATE_360_SRV/CandidateSet");
		System.out.println(response.toString() + "\n");

		response = caller
				.get("/sap/opu/odata/NSL/CANDIDATE_360_SRV/SearchCandidate?NameLast='Danaus Tsai'&RoleType='ALL'");
		System.out.println(response.toString() + "\n");

		response = caller.get("/sap/opu/odata/NSL/CANDIDATE_360_SRV/AptitudeTestResultList?CandidateId='9000000959'");
		System.out.println(response.toString() + "\n");
	}
}
