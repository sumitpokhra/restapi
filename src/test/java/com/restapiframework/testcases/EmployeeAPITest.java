package com.restapiframework.testcases;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.restapiframework.common.DataUtility;
import com.restapiframework.common.ReusableSpecifications;
import com.restapiframework.testbase.TestBase;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class EmployeeAPITest extends TestBase {

    private static final Logger log    = LogManager.getLogger(EmployeeAPITest.class);

    ArrayList<Integer>          empIDs = new ArrayList<>();

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = getConfig().getProperty("employeebaseuri");
    }

    @Test(testName = "Create Employee", dataProvider = "GetEmployeeData")
    public void createEmployeeTest(String name, String salary, String age) {
        reportLog(log, TestBase.getTest(), "INFO", "Base URI: " + RestAssured.baseURI);
        HashMap<String, String> employeeDetails = new HashMap<>();
        employeeDetails.put("name", name);
        employeeDetails.put("salary", salary);
        employeeDetails.put("age", age);
        Response response = RestAssured.given().spec(ReusableSpecifications.getGenericRequestSpec()).when().body(employeeDetails).post("/api/v1/create").then().extract().response();
        reportLog(log, TestBase.getTest(), "INFO", "Endpoint URL: " + RestAssured.baseURI + "/api/v1/create");
        JsonPath jsonPath = response.jsonPath();
        assertEquals(response.getStatusCode(), 200);
        reportLog(log, TestBase.getTest(), "PASS", "Status Code: " + response.getStatusCode());
        empIDs.add(Integer.parseInt(jsonPath.getString("id")));
        reportLog(log, TestBase.getTest(), "INFO", "**** Employee is Created ****");
        reportLog(log, TestBase.getTest(), "INFO", "Employee ID: " + jsonPath.getString("id"));
        reportLog(log, TestBase.getTest(), "INFO", "Employee Name: " + jsonPath.getString("name"));
        reportLog(log, TestBase.getTest(), "INFO", "Employee Salary: " + jsonPath.getString("salary"));
        reportLog(log, TestBase.getTest(), "INFO", "Employee Age: " + jsonPath.getString("age"));
    }

    @Test(testName = "Get Employee Details", dependsOnMethods = "createEmployeeTest")
    public void getEmployeeDetailsTest() {
        reportLog(log, TestBase.getTest(), "INFO", "Base URI: " + RestAssured.baseURI);
        for (Integer empID : empIDs) {

            Response response = RestAssured.given().when().get("/api/v1/employee/" + empID).then().extract().response();
            assertEquals(response.getStatusCode(), 200);
            reportLog(log, TestBase.getTest(), "INFO", "Endpoint URI: " + RestAssured.baseURI + "/api/v1/employee/" +
                    empID);
            reportLog(log, TestBase.getTest(), "PASS", "Status Code: " + response.getStatusCode());
            JsonPath jsonPath = response.jsonPath();
            reportLog(log, TestBase.getTest(), "INFO", "Fetching the details of Employee having Employee ID: " + empID);
            reportLog(log, TestBase.getTest(), "INFO", "Employee Name: " + jsonPath.get("employee_name"));
            reportLog(log, TestBase.getTest(), "INFO", "Employee Age: " + jsonPath.get("employee_age"));
            reportLog(log, TestBase.getTest(), "INFO", "Employee Salary: " + jsonPath.get("employee_salary"));

        }
    }

    @Test(testName = "Update Employee Details", dependsOnMethods = "createEmployeeTest",
          dataProvider = "GetUpdateEmployeeData")
    public void updateEmployeeTest(String name, String salary, String age) {
        int i = 0;
        HashMap<String, String> updateEmployeeDetails = new HashMap<>();
        updateEmployeeDetails.put("name", name);
        updateEmployeeDetails.put("salary", salary);
        updateEmployeeDetails.put("age", age);
        Response response = RestAssured.given().body(updateEmployeeDetails).when().put("/api/v1/update/" +
                empIDs.get(i)).then().extract().response();
        assertEquals(response.getStatusCode(), 200);
        reportLog(log, TestBase.getTest(), "INFO", "Endpoint URI: " + RestAssured.baseURI + "/api/v1/update/" +
                empIDs.get(i));
        reportLog(log, TestBase.getTest(), "PASS", "Status Code: " + response.getStatusCode());
        JsonPath jsonPath = response.jsonPath();
        reportLog(log, TestBase.getTest(), "INFO", "Fetching the details of Updated Employee having Employee ID: " +
                empIDs.get(i));
        reportLog(log, TestBase.getTest(), "INFO", "Updated Employee Name: " + jsonPath.get("name"));
        reportLog(log, TestBase.getTest(), "INFO", "Updated Employee Age: " + jsonPath.get("age"));
        reportLog(log, TestBase.getTest(), "INFO", "Updated Employee Salary: " + jsonPath.get("salary"));
        i++;
    }
    
    @Test(testName = "Delete Employee Details", dependsOnMethods =  {"createEmployeeTest", "updateEmployeeTest", "getEmployeeDetailsTest"})
    public void deleteEmployeeTest() {
        reportLog(log, TestBase.getTest(), "INFO", "Base URI: " + RestAssured.baseURI);
        for (Integer empID : empIDs) {
            Response response = RestAssured.given().when().delete("/api/v1/delete/" + empID).then().extract().response();
            assertEquals(response.getStatusCode(), 200);
            reportLog(log, TestBase.getTest(), "INFO", "Endpoint URI: " + RestAssured.baseURI + "/api/v1/employee/" +
                    empID);
            reportLog(log, TestBase.getTest(), "PASS", "Status Code: " + response.getStatusCode());
            JsonPath jsonPath = response.jsonPath();
            reportLog(log, TestBase.getTest(), "INFO", "**** Deleted the details of Employee having Employee ID: " + empID + " ****");
           reportLog(log, TestBase.getTest(), "INFO", "Validating the response after deleting the Employee");
           assertEquals(jsonPath.get("success.text"), "successfully! deleted Records");
           reportLog(log, TestBase.getTest(), "PASS", jsonPath.get("success.text"));
        }
    }

    @DataProvider(name = "GetEmployeeData")
    public Object[][] getEmployeeData() {
        return DataUtility.getDataFromSpreadSheet("GetTestData.xlsx", "EMPLOYEE_DATA");
    }

    @DataProvider(name = "GetUpdateEmployeeData")
    public Object[][] getUpdateEmployeeData() {
        return DataUtility.getDataFromSpreadSheet("GetTestData.xlsx", "UPDATE_EMPLOYEE_DATA");
    }

}
