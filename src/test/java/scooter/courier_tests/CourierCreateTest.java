package scooter.courier_tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scooter.courier.Courier;
import scooter.courier.CourierClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static scooter.courier.CourierFields.fieldsFrom;
import static scooter.courier.CourierGenerator.randomeCourier;

public class CourierCreateTest {

    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        courier = randomeCourier();
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            CourierClient.courierDeleteAfterTest(courierId);
        }
    }

    // Тесты на ручку "/api/v1/courier"

    @Feature(value = "/api/v1/courier")
    @Test
    @DisplayName("Проверка создания курьера (позитив).")
    @Description("Тест проверяет создание курьера. Успешный запрос возвращает ok: true.")
    public void courierCanBeCreatedTest() {
        Response newCourier = CourierClient.courierCreate(courier);

        checkStatusCourierCreate(newCourier, 201, true);
        printResponseBodyToConsole(newCourier);

        courierId = CourierClient.getCourierId(fieldsFrom(courier));
    }

    @Feature(value = "/api/v1/courier")
    @Test
    @DisplayName("Проверка создания курьера с дублирующими данными (негатив).")
    @Description("Тест проверяет, что повторное создание уже имеющегося курьера возвращает ошибку.")
    public void doubleCourierCanNotBeCreatedTest() {
        Response newCourier1 = CourierClient.courierCreate(courier);
        courierId = CourierClient.getCourierId(fieldsFrom(courier));

        Response newCourier2 = CourierClient.courierCreate(courier);
        checkResponseCourierCanNotBeCreate(newCourier2, 409, "Этот логин уже используется. Попробуйте другой.");

        // !!! Баг в описании ошибки:
        // ожидаемый результат (в документации) {"message": "Этот логин уже используется"},
        // фактический результат {"message": "Этот логин уже используется. Попробуйте другой."}
    }

    @Feature(value = "/api/v1/courier")
    @Test
    @DisplayName("Проверка создания курьера без ввода логина (негатив).")
    @Description("Тест проверяет, что нельзя создать курьера, если не заполнено обязательное поле login. Возвращается ошибка.")
    public void courierCanNotBeCreatedWithoutLoginTest() {
        Response newCourier = CourierClient.courierCreate(new Courier("", courier.getPassword(), courier.getFirstName()));
        checkResponseCourierCanNotBeCreate(newCourier, 400, "Недостаточно данных для создания учетной записи");
    }

    @Feature(value = "/api/v1/courier")
    @Test
    @DisplayName("Проверка создания курьера без ввода пароля (негатив).")
    @Description("Тест проверяет, что нельзя создать курьера, если не заполнено обязательное поле password. Возвращается ошибка.")
    public void courierCanNotBeCreatedWithoutPasswordTest() {
        Response newCourier = CourierClient.courierCreate(new Courier(courier.getLogin(), "", courier.getPassword()));
        checkResponseCourierCanNotBeCreate(newCourier, 400, "Недостаточно данных для создания учетной записи");
    }

    @Step("Проверить статус кода (201) и тело ответа (ok).")
    public void checkStatusCourierCreate(Response newCourier, int code, boolean ok) {
        newCourier.then().assertThat()
                .statusCode(code)
                .and()
                .body("ok", equalTo(ok));
    }

    @Step("Вывести в консоль ответ на успешный запрос.")
    public void printResponseBodyToConsole(Response response) {
        System.out.println(response.body().asString());
    }

    @Step("Проверить статус кода (4хх) и тело ответа при передаче некорректных данных.")
    public void checkResponseCourierCanNotBeCreate(Response newCourier, int code, String message) {
        newCourier.then()
                .assertThat()
                .statusCode(code)
                .and()
                .body("code", equalTo(code))
                .and()
                .body("message", equalTo(message));
    }
}