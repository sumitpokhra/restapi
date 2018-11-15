package com.restapiframework.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.relevantcodes.extentreports.LogStatus;
import com.restapiframework.testbase.TestBase;

public class ReadExcelFile {

    private String              path;
    private FileInputStream     fileInputStream = null;
    private XSSFWorkbook        workbook        = null;
    private XSSFSheet           sheet           = null;
    private XSSFRow             row             = null;
    private XSSFCell            cell            = null;
    private FileOutputStream    fileOut         = null;

    private static final Logger log             = LogManager.getLogger(ReadExcelFile.class);

    public ReadExcelFile(String filePath, String sheetName) {
        if (!StringUtils.isNotEmpty(filePath) && !StringUtils.isNotEmpty(sheetName)) {
            return;
        }
        this.setPath(filePath);
        try {
            setFileInputStream(new FileInputStream(filePath));
            XSSFWorkbook xssfworkbook = new XSSFWorkbook(getFileInputStream());
            setWorkbook(xssfworkbook);
            this.setSheet(getWorkbook().getSheet(sheetName));
        } catch (Exception e) {
            TestBase.getTest().log(LogStatus.ERROR, "Unable to Open Excel due to error: "+e);
            log.error("Unable to open excel File due to error: "+e.getMessage());
        }

        if (getFileInputStream() != null) {
            try {
                getFileInputStream().close();
            } catch (IOException e) {
                TestBase.getTest().log(LogStatus.ERROR, "Error: "+e);
                log.error(e.getMessage());
            }
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(FileInputStream fis) {
        this.fileInputStream = fis;
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public XSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    public XSSFRow getRow() {
        return row;
    }

    public void setRow(XSSFRow row) {
        this.row = row;
    }

    public XSSFCell getCell() {
        return cell;
    }

    public void setCell(XSSFCell cell) {
        this.cell = cell;
    }

    public FileOutputStream getFileOut() {
        return fileOut;
    }

    public void setFileOut(FileOutputStream fileOut) {
        this.fileOut = fileOut;
    }

    public int getRowCount() {
        if (getSheet() != null) {
            int rowCount = getSheet().getLastRowNum();
            return rowCount + 1;
        } else {
            TestBase.getTest().log(LogStatus.ERROR, "Sheet Not Found");
            log.error("Sheet Not Found");
            return 0;
        }
    }
    
    public int getColumnCount() {
        if (getSheet() != null){
            Row rowNum = getSheet().getRow(0);
            int columnCount = rowNum.getLastCellNum();
            return columnCount;
        } else {
            TestBase.getTest().log(LogStatus.ERROR, "Sheet Not Found");
            log.error("Sheet Not Found");
            return 0;
        }
    }
    
    public String getCellValue(final int row, final int col) {
        Row rows = getSheet().getRow(row - 1);
        if (rows == null){
            return "";
        } else {
            Cell cell;
            try {
                cell = rows.getCell(col);
            } catch (IllegalArgumentException e) {
                throw e;
            }
            if (cell == null) {
                return "";
            } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                // return String.valueOf(cell.getNumericCellValue());
                return new java.text.DecimalFormat("0").format(cell.getNumericCellValue());
            } else {
                return cell.getStringCellValue();
            }
        }
    }
}
