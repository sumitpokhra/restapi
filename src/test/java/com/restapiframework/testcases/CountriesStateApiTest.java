package com.restapiframework.testcases;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.restapiframework.common.DataUtility;
import com.restapiframework.common.ReusableSpecifications;
import com.restapiframework.testbase.TestBase;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CountriesStateApiTest extends TestBase {

    private static final Logger log = LogManager.getLogger(CountriesStateApiTest.class);

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = getConfig().getProperty("countrybaseuri");
    }
    
    @Test(testName = "Get list of States and territories of a Country", dataProvider = "GetCountryData")
    public void getListOfStates(String country, String numberOfStates) {
        reportLog(log, TestBase.getTest(), "INFO", "Base URI: " + RestAssured.baseURI);
        Response response = RestAssured.given().when().get("/state/get/" + country +
                "/all").then().spec(ReusableSpecifications.getGenericResponseSpec()).extract().response();
        reportLog(log, TestBase.getTest(), "INFO", "Endpoint URL: " + RestAssured.baseURI + "/state/get/" + country +
                "/all");
        reportLog(log, TestBase.getTest(), "INFO", "Expected States for Country: " + country + " is: " +
                numberOfStates);
        JsonPath jsonPath = response.jsonPath();
        // Getting records count and replacing all letters
        int records = Integer.parseInt(jsonPath.get("RestResponse.messages").toString().replaceAll("[\\D]", ""));
        Assert.assertEquals(200, response.getStatusCode());
        reportLog(log, TestBase.getTest(), "PASS", "Status CODE: " + response.getStatusCode());
        Assert.assertEquals(Integer.parseInt(numberOfStates), records);
        reportLog(log, TestBase.getTest(), "INFO", "Number of States matched for Country: " + country);
    }

    @Test(testName = "REST API to search the State of a Country")
    public void getStateOfCountry() {
        reportLog(log, TestBase.getTest(), "INFO", "Base URI: " + RestAssured.baseURI);
        String state = states[new Random().nextInt(states.length)];
        Response response = RestAssured.given().when().get("/state/get/IND/" +
                state).then().spec(ReusableSpecifications.getGenericResponseSpec()).extract().response();
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(200, response.getStatusCode());
        reportLog(log, TestBase.getTest(), "PASS", "Status CODE: " + response.getStatusCode());
        reportLog(log, TestBase.getTest(), "INFO", "Endpoint URL: " + RestAssured.baseURI + "/state/get/IND/" + state);
        reportLog(log, TestBase.getTest(), "INFO", "Fetching the details of State: " + state);
        reportLog(log, TestBase.getTest(), "INFO", "State Name: " + jsonPath.get("RestResponse.result.name"));
        reportLog(log, TestBase.getTest(), "INFO", "Largest City: " + jsonPath.get("RestResponse.result.largest_city"));
        reportLog(log, TestBase.getTest(), "INFO", "Capital: " + jsonPath.get("RestResponse.result.capital"));
    }

    String states[] = { "RJ", "UP", "MP", "KA", "UK", "GJ", "KL", "WB", "PB", "MH", "OD", "DL" };

    @Test(testName = "Search state by any free form text")
    public void verifySearchState() {
        reportLog(log, TestBase.getTest(), "INFO", "Base URI: " + RestAssured.baseURI);

        Response response = RestAssured.given().when().get("/state/search/IND?text=pradesh").then().spec(ReusableSpecifications.getGenericResponseSpec()).extract().response();
        reportLog(log, TestBase.getTest(), "INFO", "Endpoint URL: " + RestAssured.baseURI +
                "/state/search/IND?text=pradesh");
        Assert.assertEquals(200, response.getStatusCode());
        reportLog(log, TestBase.getTest(), "PASS", "Status CODE: " + response.getStatusCode());
        JSONObject ja = new JSONObject(response.getBody().asString());
        ja = (JSONObject) ja.get("RestResponse");
        JSONArray jo = (JSONArray) ja.get("result");
        for (int i = 0; i < jo.length(); i++) {
            reportLog(log, TestBase.getTest(), "INFO", "**** Details of " + jo.getJSONObject(i).getString("name") +
                    " ****");
            reportLog(log, TestBase.getTest(), "INFO", "id: " + jo.getJSONObject(i).getInt("id"));
            reportLog(log, TestBase.getTest(), "INFO", "country: " + jo.getJSONObject(i).getString("country"));
            reportLog(log, TestBase.getTest(), "INFO", "name: " + jo.getJSONObject(i).getString("name"));
            reportLog(log, TestBase.getTest(), "INFO", "abbr: " + jo.getJSONObject(i).getString("abbr"));
            reportLog(log, TestBase.getTest(), "INFO", "area: " + jo.getJSONObject(i).getString("area"));
            reportLog(log, TestBase.getTest(), "INFO", "largest_city: " +
                    jo.getJSONObject(i).getString("largest_city"));
            reportLog(log, TestBase.getTest(), "INFO", "capital: " + jo.getJSONObject(i).getString("capital"));
            reportLog(log, TestBase.getTest(), "INFO", "********");
        }
    }

    @DataProvider(name = "GetCountryData")
    public Object[][] getCountryData() {
        return DataUtility.getDataFromSpreadSheet("GetTestData.xlsx", "COUNTRY_DATA");
    }
}
