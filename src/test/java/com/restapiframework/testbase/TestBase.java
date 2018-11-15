package com.restapiframework.testbase;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.restapiframework.common.CommonUtils;
import com.restapiframework.reporting.ReportUtils;


public class TestBase {

    @SuppressWarnings("unused")
    private static final Logger     log           = LogManager.getLogger(TestBase.class);
    private static Properties       config;
    protected ExtentTest            test;
    public static ExtentReports     report;

    static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();

    public TestBase() {
        setConfig(CommonUtils.readPropertiesFile(System.getProperty("user.dir") +
                "//src//test//resources//data//framework.properties"));
        setReport(ReportUtils.getReportInstance());
    }

    public static Properties getConfig() {
        return config;
    }

    public static void setConfig(Properties config) {
        TestBase.config = config;
    }

    public void setReport(ExtentReports report) {
        TestBase.report = report;
    }

    public static ExtentReports getReport() {
        return report;
    }

    @BeforeMethod
    public void beforeMethod(Method method, Object[] testData) {
        if (testData.length > 0) {
            startTest(getClass().getSimpleName() + "-" +
                    (StringUtils.isNotEmpty(method.getAnnotation(Test.class).testName()) ? method.getAnnotation(Test.class).testName() +
                            "_" +
                            testData[0] : method.getName()), StringUtils.isNotEmpty(method.getAnnotation(Test.class).description()) ? method.getAnnotation(Test.class).description() +
                                    "_" + testData[0] : "");
        } else {
            startTest(getClass().getSimpleName() + "-" +
                    (StringUtils.isNotEmpty(method.getAnnotation(Test.class).testName()) ? method.getAnnotation(Test.class).testName() : method.getName()), StringUtils.isNotEmpty(method.getAnnotation(Test.class).description()) ? method.getAnnotation(Test.class).description() : "");
        }
    }

    @AfterMethod
    public void afterMethod() {
        endTest();
        getReport().flush();
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = report.startTest(testName, desc);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
        return test;
    }

    public static synchronized ExtentTest getTest() {
        return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }

    public static synchronized void endTest() {
        report.endTest((ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId())));
    }

    public synchronized void reportLog(final Logger log, final ExtentTest test, final String status, final Object message) {

        if (status.equalsIgnoreCase("info")) {
            log.info(message);
            test.log(LogStatus.INFO, message.toString());
        } else if (status.equalsIgnoreCase("error")) {
            log.info(message);
            test.log(LogStatus.ERROR, message.toString());
        } else if (status.equalsIgnoreCase("fail")) {
            log.info(message);
            test.log(LogStatus.FAIL, message.toString());
        } else if (status.equalsIgnoreCase("warn")) {
            log.info(message);
            test.log(LogStatus.WARNING, message.toString());
        } else if (status.equalsIgnoreCase("fatal")) {
            log.info(message);
            test.log(LogStatus.FATAL, message.toString());
        } else if (status.equalsIgnoreCase("pass")) {
            log.info(message);
            test.log(LogStatus.PASS, message.toString());
        } else {
            log.info("Invalid log input provided");
            test.log(LogStatus.INFO, "Invalid log input provided");
        }

    }

}
