package com.restapiframework.common;

public class DataUtility {

    public static Object[][] getDataFromSpreadSheet(String workBookName, String workSheetName) {
        ReadExcelFile readExcelFile = new ReadExcelFile(System.getProperty("user.dir") +
                "//src//test//resources//data//" + workBookName, workSheetName);

        int rowCount = readExcelFile.getRowCount();
        int colCount = readExcelFile.getColumnCount();
        Object[][] data = new Object[rowCount - 1][colCount];
        for (int rowNum = 2; rowNum <= rowCount; rowNum++) {
            for (int colNum = 0; colNum < colCount; colNum++) {
                data[rowNum - 2][colNum] = readExcelFile.getCellValue(rowNum, colNum);
            }
        }

        return data;
    }

}
