package resources;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.io.*;
import java.util.Properties;

public class Utilities {
    public static RequestSpecification req;

    public RequestSpecification requestSpecification() throws IOException {
        if(req == null) {
            PrintStream log = new PrintStream(new FileOutputStream("PlaceValidationLogging.txt"));
            req = new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrl"))
                    .addQueryParam("key", "qaclick123")
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .setContentType(ContentType.JSON).build();
            return req;
        }
        return req;
    }

    public ResponseSpecification responseSpecification(){

         return new ResponseSpecBuilder().expectStatusCode(200)
                .expectContentType(ContentType.JSON).build();
    }

    public RequestSpecification loginRequestSpecification() throws IOException {

        PrintStream log = new PrintStream(new FileOutputStream("LoginLogs.txt"));
        return new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrl"))
                .addFilter(RequestLoggingFilter.logRequestTo(log))
                .addFilter(ResponseLoggingFilter.logResponseTo(log))
                .setContentType(ContentType.JSON).build();
    }

    public RequestSpecification addProductRequestSpecification(String token, String userId) throws IOException {

        PrintStream log = new PrintStream(new FileOutputStream("AddProductLogs.txt"));
        return new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrl"))
                .addFilter(RequestLoggingFilter.logRequestTo(log))
                .addFilter(ResponseLoggingFilter.logResponseTo(log))
                .addHeader("authorization", token)
                .addParam("productName","laptop")
                .addParam("productAddedBy", userId)
                .addParam("productCategory","fashion")
                .addParam("productSubCategory","shirts")
                .addParam("productPrice","11500")
                .addParam("productDescription","Lenovo")
                .addParam("productFor","men")
                .addMultiPart("productImage", new File("laptop.jpg")).build();
    }

    public RequestSpecification createOrderRequestSpecification(String token) throws IOException {

        PrintStream log = new PrintStream(new FileOutputStream("CreateOrderLogs.txt"));
        return new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrl"))
                .addFilter(RequestLoggingFilter.logRequestTo(log))
                .addFilter(ResponseLoggingFilter.logResponseTo(log))
                .addHeader("authorization", token)
                .setContentType(ContentType.JSON).build();
    }

    public static String getGlobalValue(String key) throws IOException {

        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(
                "C:\\Users\\Ionut\\IdeaProjects\\BDD-API-Framework\\src\\test\\java\\resources\\global.properties");
        properties.load(fileInputStream);
        return properties.getProperty(key);
    }

    public String getJsonPath(Response response, String key) {

        String res = response.asString();
        JsonPath jsonPath = new JsonPath(res);
        return jsonPath.get(key).toString();
    }
}
