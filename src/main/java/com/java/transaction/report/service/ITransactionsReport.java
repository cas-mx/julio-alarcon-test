package com.java.transaction.report.service;

import java.util.List;

import com.java.transaction.model.Transaction;
import com.java.util.Week;

public interface ITransactionsReport {
	public StringBuilder report(int userId);
	public List<Week> getWeeksOfMonth(List<Transaction> lista);
	public List<Transaction> getTransByUser(int userId);
}
