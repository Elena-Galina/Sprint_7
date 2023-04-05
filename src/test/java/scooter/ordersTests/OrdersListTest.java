package scooter.ordersTests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static scooter.orders.OrdersClient.sendGetRequestOrders;

public class OrdersListTest {

    // Тесты на ручку "/api/v1/orders"

    @Feature(value = "/api/v1/orders")
    @Test
    @DisplayName("проверка возможности получения списка заказов (позитив).")
    @Description("Тест проверяет, что в тело ответа возвращается список заказов.")
    public void getOrdersListTest() {
        Response response = sendGetRequestOrders();
        checkResponseOrdersList(response, 200);
        printResponseBodyToConsole(response);
    }

    @Step("Проверить статус кода (200) и тело ответа")
    public void checkResponseOrdersList(Response response, int code) {
        response.then()
                .assertThat()
                .statusCode(code)
                .and()
                .body(notNullValue());
    }

    @Step("Вывести в консоль ответ на успешный запрос")
    public void printResponseBodyToConsole(Response response) {
        System.out.println(response.body().asString());
    }
}
