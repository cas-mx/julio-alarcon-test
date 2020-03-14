package com.java.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.java.transaction.controller.*;

import java.text.DecimalFormat;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.java.transaction.model.Transaction;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TransactionController.class, Transaction.class })
public class TransactionControllerTest {
	@Mock
	TransactionController transactionsController;
	private MockMvc mockMvc;

	@Mock
	Transaction transaction;
	@Autowired
	private WebApplicationContext wac;

	ResultMatcher ok = MockMvcResultMatchers.status().isOk();

	@Before
	public void setup() {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		this.mockMvc = builder.build();

	}

	@Test
	public void testListTransactions() {
		try {
			Transaction trans = new Transaction(9);
			trans.setUuid();
			trans.setAmount(12.0);
			trans.setDescription("tacps");
			trans.setDate(LocalDate.of(2010, 10, 10));
			TransactionController.tansData.put(trans.getUuid(), trans);
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/rest/trans/list/9");
			this.mockMvc.perform(builder).andExpect(ok);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSumTransactions() {
		try {
			Transaction trans = new Transaction(9);
			trans.setUuid();
			trans.setAmount(10.0);
			trans.setDate(LocalDate.of(2020, 10, 10));
			Transaction trans2 = new Transaction(9);
			trans2.setUuid();
			trans2.setAmount(10.0);
			trans2.setDate(LocalDate.of(2020, 10, 10));
			TransactionController.tansData.put(trans.getUuid(), trans);
			TransactionController.tansData.put(trans2.getUuid(), trans2);
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/rest/trans/sum/9");
			String result = new String("userId -> 9\nsum -> 20.00");
			this.mockMvc.perform(builder).toString().compareTo(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testReportService() {
		try {
			Transaction trans = new Transaction(9);
			trans.setUuid();
			trans.setAmount(10.0);
			trans.setDate(LocalDate.of(2020, 10, 10));
			Transaction trans2 = new Transaction(9);
			trans2.setUuid();
			trans2.setAmount(10.0);
			trans2.setDate(LocalDate.of(2020, 10, 10));
			TransactionController.tansData.put(trans.getUuid(), trans);
			TransactionController.tansData.put(trans2.getUuid(), trans2);
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/rest/trans/report/9");
			this.mockMvc.perform(builder).andExpect(ok);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetTransaction() {
		try {
			Transaction trans = new Transaction(9);
			trans.setUuid();
			TransactionController.tansData.put(trans.getUuid(), trans);
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/rest/trans/get/9/" + trans.getUuid());
			this.mockMvc.perform(builder).toString().compareTo(trans.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}