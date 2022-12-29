package com.DataDrivenFramework.utils;

public class readTestData {
	public static void main(String[] args) {
		ReadExcelDataFile readData =  new ReadExcelDataFile("C:\\Users\\himanshu.pawar\\eclipse-workspace\\DataDrivenFramework\\src\\main\\java\\testData\\testData2.xlsx");
		String sheetName = "Feature1";
		String testName = "test One";
		
		
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
		
		
		//data of complete test case
		for(int rowNum= startTestRow; rowNum<=startTestRow+rows; rowNum++) {
			for(int colNum=0; colNum<col; colNum++) {
				System.out.println(readData.getCellData(sheetName, colNum, rowNum));
			}
		}
	}

}
