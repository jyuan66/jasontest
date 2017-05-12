package com.nsl.gateway.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.nsl.gateway.util.DiffUtils;
import com.nsl.gateway.util.FileUtils;
import com.nsl.gateway.util.GatewayCaller;
import com.nsl.gateway.util.ResponseObj;

public class CandidateOpportunity {

	private GatewayCaller caller;
	private String testDataPath;

	@Before
	public void setUp() throws Exception {
		caller = new GatewayCaller("dp2wdp.nanshan.com.tw", 8888, "http", "W9006357", "Qwer1234");
		testDataPath = "./testData/CandidateOpportunity/";
	}  

	@Test
	public void testBatchUpdateRECRUIT_LEVEL() throws Exception {
		ResponseObj response = caller.post("/sap/opu/odata/NSL/CANDIDATE_OPPORTUNITY_SRV/OpportunityDetailSet",
				FileUtils.readFileToString(testDataPath, "BatchUpdate_RECRUIT_LEVEL.txt"));
		assertEquals(response.getStatus(), 200);

		DiffUtils diff = new DiffUtils().ignoreAttribute("xml:base").ignoreTag("id").ignoreTag("updated")
				.printLog(false)
				.diff(response.getContent(), FileUtils.readFileToString(testDataPath, "SearchHelpSet.xml"));
		assertNull(diff.getDiff());
		System.out.println("\n");
	}

	@Test
	public void testCheckOpportunityByCandidate() throws Exception {
		ResponseObj response = caller.post("/sap/opu/odata/NSL/CANDIDATE_OPPORTUNITY_SRV/CheckOpportunityByCandidate?CandidateId='7000000551'");
		assertEquals(response.getStatus(), 200);

		DiffUtils diff = new DiffUtils()
				.diff(response.getContent(), FileUtils.readFileToString(testDataPath, "CheckOpportunityByCandidate.xml"));
		assertNull(diff.getDiff());
		System.out.println("\n"); 
	}

}
