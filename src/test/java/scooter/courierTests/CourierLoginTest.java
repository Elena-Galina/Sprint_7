package scooter.courierTests;

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
import static scooter.courier.CourierFields.*;
import static scooter.courier.CourierGenerator.randomeCourier;

public class CourierLoginTest {
    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        courier = randomeCourier();
    }

    @After
    public void tearDown() {
        CourierClient.courierDeleteAfterTest(courierId);
    }

    // Тесты на ручку "/api/v1/courier/login"
    @Feature(value = "/api/v1/courier/login")
    @Test
    @DisplayName("Проверка авторизации курьера (позитив).")
    @Description("Тест проверяет возможность авторизации курьера. Успешный запрос возвращает id.")
    public void CourierCanBeLoginTest() {
        CourierClient.courierCreate(courier);

        Response newCourierLogin = CourierClient.courierLogin(fieldsFrom(courier));
        checkResponseCourierLogin(newCourierLogin, 200, "id");

        courierId = newCourierLogin.then().extract().path("id");
        printResponseBodyToConsole(newCourierLogin);
    }

    @Feature(value = "/api/v1/courier/login")
    @Test
    @DisplayName("Проверка авторизации курьера без логина (негатив).")
    @Description("Тест проверяет, что курьер не может быть авторизован, если не заполнено обязательное поле login. Возвращается ошибка.")
    public void CourierCanNotBeLoginWithoutLoginTest() {
        CourierClient.courierCreate(courier);

        Response newCourierWithoutLogin = CourierClient.courierLogin(passwordFieldFrom(courier));
        checkResponseCourierLoginWithInvalidField(newCourierWithoutLogin, 400, "Недостаточно данных для входа");

        courierId = CourierClient.getCourierId(fieldsFrom(courier));
    }

    @Feature(value = "/api/v1/courier/login")
    @Test
    @DisplayName("Проверка авторизации курьера без пароля (негатив).")
    @Description("Тест проверяет, что курьер не может быть авторизован, если не заполнено обязательное поле password. Возвращается ошибка.")
    public void CourierCanNotBeLoginWithoutPasswordTest() {
        CourierClient.courierCreate(courier);

        Response newCourierWithoutPassword = CourierClient.courierLogin(loginFieldFrom(courier));
        checkResponseCourierLoginWithInvalidField(newCourierWithoutPassword, 400, "Недостаточно данных для входа");

        courierId = CourierClient.getCourierId(fieldsFrom(courier));
    }

    @Feature(value = "/api/v1/courier/login")
    @Test
    @DisplayName("Проверка авторизации курьера с некорректным паролем (негатив).")
    @Description("Тест проверяет, что если при авторизации курьера указан неверный password, возвращается ошибка.")
    public void CourierCanNotBeLoginWithInvalidPasswordTest() {
        CourierClient.courierCreate(courier);

        Response newCourierInvalidField = CourierClient.courierLogin(invalidPasswordFieldFrom(courier));
        checkResponseCourierLoginWithInvalidField(newCourierInvalidField, 404, "Учетная запись не найдена");

        courierId = CourierClient.getCourierId(fieldsFrom(courier));
    }

    @Feature(value = "/api/v1/courier/login")
    @Test
    @DisplayName("Проверка авторизации курьера с некорректным логином (негатив).")
    @Description("Тест проверяет, что если при авторизации курьера указан неверный login, возвращается ошибка.")
    public void CourierCanNotBeLoginWithInvalidLoginTest() {
        CourierClient.courierCreate(courier);

        Response newCourierInvalidField = CourierClient.courierLogin(invalidLoginFieldFrom(courier));
        checkResponseCourierLoginWithInvalidField(newCourierInvalidField, 404, "Учетная запись не найдена");

        courierId = CourierClient.getCourierId(fieldsFrom(courier));
    }

    @Step("Проверить статус кода (200) и тело ответа (id).")
    public void checkResponseCourierLogin(Response newCourierLogin, int code, String id) {
        newCourierLogin.then().assertThat()
                .statusCode(code)
                .and()
                .extract()
                .path(id);
    }

    @Step("Вывести в консоль ответ на успешный запрос (id).")
    public void printResponseBodyToConsole(Response response) {
        System.out.println(response.body().asString());
    }

    @Step("Проверить статус кода (4хх) и тело ответа при передаче некорректных данных.")
    public void checkResponseCourierLoginWithInvalidField(Response newCourierInvalidField, int code, String message) {
        newCourierInvalidField.then().assertThat()
                .statusCode(code)
                .and()
                .body("code", equalTo(code))
                .and()
                .body("message", equalTo(message));
    }
}
