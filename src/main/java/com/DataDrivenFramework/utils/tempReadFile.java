package com.DataDrivenFramework.utils;

public class tempReadFile {
	public static void main(String[] args) {

	ReadExcelDataFile readData = new ReadExcelDataFile(System.getProperty("user.dir")+"\\src\\main\\java\\testData\\testData.xlsx");
//	System.out.println(System.getProperty("user.dir"));
	int totalRows = readData.getRowCount("Sheet1");
	System.out.println(totalRows);
	
	System.out.println(readData.getCellData("Sheet1", 0, 3));
	}
}
