package com.java.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class FileUtil {
	
	private static FileUtil instance;
	
	private FileUtil() {	
	}
	
	public static FileUtil getInstance() {
		if (null == instance) {
			instance = new FileUtil();
			
		}
		return instance;
		
	}
	
	public void write(String fileName, String text) {
		String fullPath= getPath(fileName);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath,true))){
			writer.append(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPath(String fileName) {
		URL main = FileUtil.class.getResource("FileUtil.class");
		String OS = System.getProperty("os.name").toLowerCase();
		String[] array;
		StringBuffer output = new StringBuffer();
		if (OS.contains("windows")) {
			StringBuffer d = new StringBuffer(main.getPath().toString().replace("/", "\\\\"));
			array = d.toString().split("\\\\");
			for(int i=0; i<array.length-6;i++) {
				output.append(array[i]+"\\");
			}
		}else {
			array = main.getPath().toString().split("/");
			for(int i=0; i<array.length-6;i++) {
				output.append(array[i]+"/");
			}
		}
		output.append(fileName);
		return output.toString();
	}

}
