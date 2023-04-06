package scooter.orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static scooter.BaseSpecification.*;

public class OrdersClient {

    public static int orderTrack;

    @Step("Передать запрос на создание заказа (\"/api/v1/orders\").")
    public static Response orderCreate(Orders order) {
        return given()
                .spec(getBaseSpecification())
                .body(order)
                .when()
                .post(PATH_ORDERS);
    }

    @Step("Передача запроса на получение списка заказов (\"/api/v1/orders\")")
    public static Response sendGetRequestOrders() {
        return given()
                .spec(getBaseSpecification())
                .get(PATH_ORDERS);
    }

    @Step("Получить track созданного заказа.")
    public static int getOrdersTrack(Response orderScooter) {
        orderTrack = orderScooter.then().extract().path("track");
        return orderTrack;
    }

    @Step("Отменить созданный заказ (\"/api/v1/orders/cancel\").")
    public static void orderCancel(int orderTrack) {
        given()
                .spec(getBaseSpecification())
                .when()
                .put(PATH_ORDER_CANCEL + orderTrack)
                .then().assertThat()
                .statusCode(200)
                .extract().path("ok");
    }

}
