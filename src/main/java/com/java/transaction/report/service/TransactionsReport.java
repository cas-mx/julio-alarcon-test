package com.java.transaction.report.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.DayOfWeek;
import com.java.transaction.controller.TransactionController;
import com.java.transaction.model.Transaction;
import com.java.util.Week;

@Service
public class TransactionsReport implements ITransactionsReport {
	
	@Override
	public StringBuilder report(int userId) {
		List<Transaction> list = getTransByUser(userId);
		list =list.stream().sorted(Comparator.comparing(Transaction::getDate)).collect(Collectors.toList());
		Map<LocalDate, List<Transaction>> byMonth = list.stream().collect(Collectors.groupingBy(d -> d.getDate().withDayOfMonth(1)));
		List <Week> weeks = getWeeksOfMonth(list);
		int quantity=0;
		double amount = 0.0d;
		double beforeStart = 0.0d;
		
		List<String> reportWeeks = new ArrayList<>();
		List<Transaction> auxList = new ArrayList<>();
		DecimalFormat dformat = new DecimalFormat("###,###.##");
		reportWeeks.add("Id  " +String.format("%17s","Week start ")+String.format("%19s","Week finish")+String.format("%11s","Quantity")+String.format("%7s","Amount" )+String.format("%13s","Total Amount")+"\n");
		for (Week week : weeks) {
			auxList = byMonth.get(LocalDate.of(week.getStart().getYear(), week.getStart().getMonth(), 1));
			for (Transaction auxTrans : auxList) {
				if (auxTrans.getDate().isAfter(week.getEnd())) {
					if(quantity>0) {
						reportWeeks.add(userId+" "+week.getStart()+" "+String.format("%8s", week.getStart().getDayOfWeek())+" "+week.getEnd()+" "+String.format("%8s", week.getEnd().getDayOfWeek())+" " +String.format("%3s", quantity)+" "+String.format("%10s", dformat.format(amount))+" "+String.format("%13s", dformat.format(beforeStart))+"\n");
					beforeStart+=amount;
					amount = 0;
					quantity = 0;
					}				
					break;
				}
				if ((auxTrans.getDate().isBefore(week.getEnd()) || auxTrans.getDate().isEqual(week.getEnd()))
						&& (auxTrans.getDate().isAfter(week.getStart())
								|| auxTrans.getDate().isEqual(week.getStart()))) {
					amount += auxTrans.getAmount();
					quantity++;
				}
			}
			if(quantity>0) {
				reportWeeks.add(userId+" "+week.getStart()+" "+String.format("%8s", week.getStart().getDayOfWeek())+" "+week.getEnd()+" "+String.format("%8s", week.getEnd().getDayOfWeek())+" " +String.format("%3s", quantity)+" "+String.format("%10s", dformat.format(amount))+" "+String.format("%13s", dformat.format(beforeStart))+"\n");
				beforeStart+=amount;
				amount = 0;
				quantity = 0;
				}
		}		
		StringBuilder  report = new StringBuilder ();
		reportWeeks.forEach(report::append);
		return report;
	}
	
	@Override
	public List<Week> getWeeksOfMonth(List<Transaction> lista){
		List<Week> semanas = new ArrayList<>();
		List<Week> semanasSorted = new ArrayList<>();
		Map<LocalDate, List<Transaction>> byMonth = lista.stream().collect(Collectors.groupingBy(d -> d.getDate().withDayOfMonth(1)));
		byMonth.forEach((k,v) -> {
			LocalDate start = LocalDate.now();
			LocalDate end;
			LocalDate auxDate;
			boolean incompleteWeek = false;
			for(int i=1;i<=k.lengthOfMonth();i++) {
				auxDate =  LocalDate.of(k.getYear(),k.getMonthValue(),i);
				if(i==1 ){
					start = LocalDate.from(auxDate);
					incompleteWeek = true;
					if(auxDate.getDayOfWeek()== DayOfWeek.THURSDAY) {
						end = LocalDate.from(auxDate);
						semanas.add(new Week(start, end));
						incompleteWeek = false;
					}
					}
					else if ( incompleteWeek == false && i > 1 && auxDate.getDayOfWeek()== DayOfWeek.FRIDAY )  {
						start = LocalDate.from(auxDate);
						incompleteWeek = true;
						//special case when last day of month is in FRIDAY
						if(i==k.lengthOfMonth()) {
							end = LocalDate.from(auxDate);
							semanas.add(new Week(start, end));
							incompleteWeek = false;
						}
						if(i+5<k.lengthOfMonth()) {
							i+=5;
						}
					}else if (incompleteWeek == true && i == k.lengthOfMonth() || auxDate.getDayOfWeek()== DayOfWeek.THURSDAY) {
						end = LocalDate.from(auxDate);
						semanas.add(new Week(start, end));
						incompleteWeek = false;
				}
			}
		});			    	    
		semanasSorted=semanas.stream().sorted(Comparator.comparing(Week::getStart)).collect(Collectors.toList());
		return semanasSorted;
	}
	
	@Override
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
