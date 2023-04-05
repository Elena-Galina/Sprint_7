package scooter;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseSpecification {

    public static String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    public static String PATH_COURIER = "/api/v1/courier/";
    public static String PATH_ORDERS = "/api/v1/orders";
    public static String PATH_ORDER_CANCEL = "/api/v1/orders/cancel/?track=";

    public static RequestSpecification getBaseSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                //  .log(LogDetail.ALL)
                .build();
    }
}
