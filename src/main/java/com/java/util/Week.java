package com.java.util;
import java.time.LocalDate;

public class Week {
	private LocalDate start;
	private LocalDate end;
	
	public Week(LocalDate start, LocalDate end){
		this.start = start;
		this.end = end;
	}
	public LocalDate getEnd() {
		return end;
	}
	public void setEnd(LocalDate end) {
		this.end = end;
	}
	public LocalDate getStart() {
		return start;
	}
	public void setStart(LocalDate start) {
		this.start = start;
	}
	
	@Override
	public String toString() {
		return "Start: "+start+" "+start.getDayOfWeek()+" end: "+end+" "+end.getDayOfWeek();
	}
	
	
	

}
