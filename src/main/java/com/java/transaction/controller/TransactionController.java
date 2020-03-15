package com.java.transaction.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.ResponseEntity;

import com.java.transaction.model.Transaction;
import com.java.transaction.report.service.TransactionsReport;
import com.java.util.FileUtil;

/**
 * Handles requests for the Transactions service.
 */
@Controller
public class TransactionController {
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
	
	//Map to store transactions, ideally we should use database
	public static Map<UUID, Transaction> tansData = new HashMap<UUID, Transaction>();
		
	@RequestMapping(value = TransRestURIConstants.TRANS_REPORT, method = RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String>  getReport(@PathVariable("userId") int userId) {
		logger.info("Start getTransactions for userId="+userId);
		TransactionsReport report = new TransactionsReport();
		StringBuilder stringReport = report.report(userId);
		return new ResponseEntity<>(stringReport.toString(),HttpStatus.OK);
	}
	
	@RequestMapping(value = TransRestURIConstants.GET_TRANS, method = RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String>  getTransactions(@PathVariable("userId") int userId, @PathVariable("transId") UUID transId) {
		logger.info("Start getTransactions for userId="+userId);
		Transaction trans = tansData.get(transId);
		if (null!= trans && userId==trans.getUserId()) {
			return new ResponseEntity<>(trans.toString(),HttpStatus.OK);
		}
		else
			return new ResponseEntity<>("Transaction not found",HttpStatus.OK);
	}
	
	@RequestMapping(value = TransRestURIConstants.LIST_TRANS, method = RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String>  getTransactions(@PathVariable("userId") int userId) {
		logger.info("Start getting transactions for userId="+userId);
		List<Transaction> transactions = new ArrayList<>();
		List<Transaction> sortedList = new ArrayList<>();
		String list = new String();
		transactions = getTransByUser(userId);
		sortedList = transactions.stream().sorted(Comparator.comparing(Transaction::getDate)).collect(Collectors.toList());
		for(Transaction trans:sortedList) {
			list+= trans.toString()+"\n";
		}
		return new ResponseEntity<>(list,HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value = TransRestURIConstants.SUM_TRANS, method = RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getTransactionSum(@PathVariable("userId") int userId) {
		logger.info("Start getting transactions for userId="+userId);
		BigDecimal total = new BigDecimal(0);
		DecimalFormat dformat = new DecimalFormat("###,###.00");
		for (Map.Entry<UUID, Transaction> trans: tansData.entrySet()){
			if(null != trans.getValue() && trans.getValue().getUserId() == userId) {
				total=total.add(new BigDecimal(trans.getValue().getAmount()));
			}
		}		
		return new ResponseEntity<>( new String("userId -> "+userId+"\nsum -> "+dformat.format(total)),HttpStatus.OK);
	}
	
	
	@RequestMapping(value = TransRestURIConstants.CREATE_TRANS, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String>createTransaction(@RequestBody Transaction trans ) {
		logger.info("Start createTransaction.");
		FileUtil fileUtil = FileUtil.getInstance();
		try {
			fileUtil.write("Transactions.txt", trans.toString());
		}finally {}
		tansData.put(trans.getUuid(), trans);
		return new ResponseEntity<>(trans.toString(),HttpStatus.CREATED);
	}
	
	public List<Transaction> getTransByUser(int userId){
		List<Transaction> list = new ArrayList<>();
		for (Map.Entry<UUID, Transaction> trans: TransactionController.tansData.entrySet()){
			if(null != trans.getValue() && trans.getValue().getUserId() == userId) {
				list.add(trans.getValue());
			}				
		}	
		return list;
	}
	
}