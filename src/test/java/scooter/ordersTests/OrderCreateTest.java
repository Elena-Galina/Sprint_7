package scooter.ordersTests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import scooter.orders.Orders;

import static org.hamcrest.CoreMatchers.notNullValue;
import static scooter.orders.OrdersClient.*;


@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;
    public int track;
    private Orders order;

    public OrderCreateTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Оформление заказа самоката")

    public static Object[][] getCustomer() {
        return new Object[][]{
                {"Александр", "Иванов", "г.Москва, Тверская улица, д.19, кв.100", "31", "89050070707", 3, "2023.05.01", "детский самокат", new String[]{"BLACK"}},
                {"Владимир", "Попов", "г.Москва, Большая Пионерская улица, д.15 кв.98", "34", "89600090909", 5, "2023.05.05", "Звонить в будни после 18ч.", new String[]{"GREY"}},
                {"Наталья", "Петрова", "г.Москва, Ленина улица, д.1, кв.56", "10", "89030090909", 3, "2023.04.23", "После 18ч.", new String[]{"BLACK", "GREY"}},
                {"Ольга", "Смирнова", "г.Москва, Октябрьская улица, д.49, кв.78", "42", "89600010101", 5, "2023.04.25", "Позвоните за 30 минут до доставки", new String[]{}}
        };
    }

    @Before
    public void setUp() {
        order = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

    @After
    public void tearDown() {
        orderCancel(track);
    }

    // Тесты на ручку "/api/v1/orders"

    @Feature(value = "/api/v1/orders")
    @Test
    @DisplayName("Проверка создания заказа при разном значении color (позитив).")
    @Description("Тест проверяет, что можно создать заказ с указанием одного цвета, обоих цветов и без указания цвета. Успешный запрос возвращает track.")
    public void ordersCreate() {
        Response orderScooter = orderCreate(order);

        checkStatusOrderCreate(orderScooter, 201);
        printResponseBodyToConsole(orderScooter);

        track = getOrdersTrack(orderScooter);
    }

    @Step("Проверить статус кода (201) и тело ответа (track).")
    public void checkStatusOrderCreate(Response orderScooter, int code) {
        orderScooter.then().assertThat()
                .statusCode(code)
                .and()
                .body("track", notNullValue());
    }

    @Step("Вывести в консоль ответ на успешный запрос (track).")
    public void printResponseBodyToConsole(Response response) {
        System.out.println(response.body().asString());
    }
}
