package com.java.transaction.model;

import java.time.LocalDate;
import java.util.UUID;

public class Transaction implements Comparable<Transaction>{

	
	private double amount;
	private UUID uuid;
	private String description;
	private int userId;
	private LocalDate date;
	

	Transaction(){
		setUuid();
	}
	
	public Transaction(int userId) {
		this.setUserId(userId);
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid() {
		this.uuid = UUID.randomUUID();
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
		
	public double getAmount() {
		return amount;
	}
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate createdDate) {
		this.date = createdDate;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@Override
    public String toString() { 
		String trans = new String ("transaction_id -> "+getUuid()+"\namount -> "+getAmount()+"\ndescription -> "+getDescription()+"\ndate -> "+getDate()+"\nuserId -> "+getUserId()+"\n");
        return trans; 
    } 
	
	@Override
	  public int compareTo(Transaction u) {
	    if (getDate() == null ) {
	      return 0;
	    }
	    return getDate().compareTo(u.getDate());
	  }
		
}