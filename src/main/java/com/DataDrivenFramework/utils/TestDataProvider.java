package com.DataDrivenFramework.utils;

import java.util.Hashtable;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestDataProvider {
	
	
	/*
	 * @Test(dataProvider="getTestData") public void
	 * sampleTestOne(Hashtable<String,String> table){
	 * System.out.println(table.get("col2")); }
	 */
	 
	
	//@DataProvider
	
	// this method now works as data provider //
	public static Object[][] getTestData(String DataFileName, String SheetName, String TestName) {
		ReadExcelDataFile readData =  new ReadExcelDataFile("C:\\Users\\himanshu.pawar\\eclipse-workspace\\DataDrivenFramework\\src\\test\\testData\\"+DataFileName);
		String sheetName = SheetName;
		String testName = TestName;
		
		
		//find start row for test
		int startRowNum = 1;
		while(!readData.getCellData(sheetName, 0, startRowNum).equalsIgnoreCase(testName)) {
			startRowNum++;
		}
		System.out.println("test start from row num "+startRowNum);
		
		int StartTestColumn = startRowNum+1;
		int startTestRow = startRowNum+2;
		
		
		//find total rows of testcases
		int rows =0;
		while(!readData.getCellData(sheetName, 0, startTestRow+rows).equals("")) {
			rows++;
		}
		System.out.println("total test rows for "+testName+" is "+rows);
		
		
		//find total no of columns in tests
		int col =0;
		while(!readData.getCellData(sheetName, col, StartTestColumn).equals("")) {
			col++;
		}
		System.out.println("total test rows for "+testName+" is "+col);
		
		Object[][] dataSet =  new Object[rows][1];
		Hashtable<String, String>dataTable= null;
		
		
		int dataRowNum=0;
	
		for(int rowNum= startTestRow; rowNum<=StartTestColumn+rows; rowNum++) {
			dataTable = new Hashtable<String, String>();
			for(int colNum=0; colNum<col; colNum++) {
				String key = readData.getCellData(sheetName, colNum, StartTestColumn);
				String value = readData.getCellData(sheetName, colNum, rowNum);
				dataTable.put(key, value);
					
			}
			dataSet[dataRowNum][0]=dataTable;
			dataRowNum++;
			
		}
		return dataSet;
	}

}
