package com.restapiframework.common;

import java.util.concurrent.TimeUnit;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static org.hamcrest.Matchers.*;

public class ReusableSpecifications {

    public static RequestSpecBuilder reqSpec;
    public static RequestSpecification requestSpecification;
    
    public static ResponseSpecBuilder resSpec;
    public static ResponseSpecification responseSpecification;
    
    /**
     * 
     * @return
     */
    public static RequestSpecification getGenericRequestSpec() {
        reqSpec = new RequestSpecBuilder();
        reqSpec.setContentType(ContentType.JSON);
        requestSpecification = reqSpec.build();
        return requestSpecification;
    }
    
    public static ResponseSpecification getGenericResponseSpec() {
        resSpec = new ResponseSpecBuilder();
        resSpec.expectHeader("Content-Type", "application/json;charset=UTF-8");
        resSpec.expectResponseTime(lessThan(5L), TimeUnit.SECONDS);
        responseSpecification = resSpec.build();
        return responseSpecification;
    }
}
