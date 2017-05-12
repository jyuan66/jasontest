package com.nsl.gateway.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.nsl.gateway.util.GatewayCaller;
import com.nsl.gateway.util.DiffUtils;
import com.nsl.gateway.util.FileUtils;
import com.nsl.gateway.util.ResponseObj;

public class Candidate360Test {

	private GatewayCaller caller;
	private String testDataPath;

	@Before
	public void setUp() throws Exception {
		caller = new GatewayCaller("dp2wdp.nanshan.com.tw", 8888, "http", "W9006357", "Qwer1234");
		testDataPath = "./testData/Candidate360/";
	}

	@Test
	public void testGetCandidateSet() throws Exception {
		ResponseObj response = caller.get("/sap/opu/odata/NSL/CANDIDATE_360_SRV/CandidateSet");
		assertEquals(response.getStatus(), 200);
		// 從Gateway Client儲存的檔案
		String dest = FileUtils.readFileToString(testDataPath, "GetCandidateSet.xml");
		DiffUtils diff = new DiffUtils().ignoreAttribute("xml:base").ignoreTag("updated").ignoreTag("id")
				.diff(response.getContent(), dest);
		assertNull(diff.getDiff());

		System.out.println("\n");
	}

	@Test
	public void testSearchCandidate() throws Exception {
		ResponseObj response = caller
				.get("/sap/opu/odata/NSL/CANDIDATE_360_SRV/SearchCandidate?NameLast='Danaus Tsai'&RoleType='ALL'");
		assertEquals(response.getStatus(), 200);

		DiffUtils diff = new DiffUtils().diff(response.getContent(),
				FileUtils.readFileToString(testDataPath, "SearchCandidate.xml"));
		assertNull(diff.getDiff());

		System.out.println("\n");
	}

	@Test
	public void testAptitudeTestResultList() throws Exception {
		ResponseObj response = caller
				.get("/sap/opu/odata/NSL/CANDIDATE_360_SRV/AptitudeTestResultList?CandidateId='9000000959'");
		assertEquals(response.getStatus(), 400);

		DiffUtils diff = new DiffUtils().ignoreTag("transactionid").ignoreTag("timestamp").diff(response.getContent(),
				FileUtils.readFileToString(testDataPath, "AptitudeTestResultList.xml"));
		assertNull(diff.getDiff());
		System.out.println("\n");
	}

	@Test
	public void testSearchHelpSet() throws Exception {
		ResponseObj response = caller.get("/sap/opu/odata/NSL/CANDIDATE_360_SRV/SearchHelpSet");
		assertEquals(response.getStatus(), 200);

		DiffUtils diff = new DiffUtils().ignoreAttribute("xml:base").ignoreTag("id").ignoreTag("updated")
				.printLog(false)
				.diff(response.getContent(), FileUtils.readFileToString(testDataPath, "SearchHelpSet.xml"));
		assertNull(diff.getDiff());
		System.out.println("\n");
	}

	@Test
	public void testBatchUpdateRECRUIT_LEVEL() throws Exception {
		ResponseObj response = caller.post("/sap/opu/odata/NSL/CANDIDATE_360_SRV/$batch",
				FileUtils.readFileToString(testDataPath, "BatchUpdate_RECRUIT_LEVEL.txt"));
		assertEquals(response.getStatus(), 200);

		DiffUtils diff = new DiffUtils().ignoreAttribute("xml:base").ignoreTag("id").ignoreTag("updated")
				.printLog(false)
				.diff(response.getContent(), FileUtils.readFileToString(testDataPath, "SearchHelpSet.xml"));
		assertNull(diff.getDiff());
		System.out.println("\n");
	}
}
