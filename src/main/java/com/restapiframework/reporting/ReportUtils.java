package com.restapiframework.reporting;

import java.io.File;
import java.util.Date;


import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;


public class ReportUtils {

    private static ExtentReports extent;
    
    private ReportUtils() {
        
    }
    
    public static synchronized ExtentReports getReportInstance() {
        if (extent == null) {
         // dynamic report file creation
         Date d = new Date();
         String fileName = d.toString().replace(" ", "_").replace(":", "_");
         String reportFileNamePath = System.getProperty("user.dir") + "//src//test//resources//reports//" + fileName
           + ".html";
         // 1.creating new instance of extent report
         extent = new ExtentReports(reportFileNamePath, true, DisplayOrder.NEWEST_FIRST);
         // 2. loading the config xml --> customize the html report
         extent.loadConfig(new File(System.getProperty("user.dir") + "//src//main//resources//"
           + "extent-config.xml"));
         // 3.System info
         
        // extent.addSystemInfo("Environment", TestBase.getConfig().getProperty("baseuri"));
        // extent.addSystemInfo("API URL", TestBase.getConfig().getProperty("baseuri"));
        }
        return extent;
       }
    
 
}
