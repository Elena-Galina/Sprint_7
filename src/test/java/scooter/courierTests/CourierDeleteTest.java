package scooter.courierTests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import scooter.courier.Courier;
import scooter.courier.CourierClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static scooter.courier.CourierFields.*;
import static scooter.courier.CourierGenerator.randomeCourier;

public class CourierDeleteTest {

    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        courier = randomeCourier();
    }

    // Тесты на ручку "/api/v1/courier/:id"

    @Feature(value = "/api/v1/courier/:id")
    @Test
    @DisplayName("Проверка удаления курьера (позитив).")
    @Description("Тест проверяет возможность удаления курьера. Успешный запрос возвращает ok: true")
    public void CourierCanBeDeleteTest() {
        CourierClient.courierCreate(courier);

        courierId = CourierClient.getCourierId(fieldsFrom(courier));

        Response newCourierDel = CourierClient.setCourierDelete(courierId);
        checkStatusCourierDelete(newCourierDel, 200, true);
        printResponseBodyToConsole(newCourierDel);
    }

    @Feature(value = "/api/v1/courier/:id")
    @Test
    @DisplayName("Проверка удаления курьера с несуществующим Id (негатив).")
    @Description("Тест проверяет, что курьера нельзя удалить, если указан несуществующий Id. Возвращается ошибка.")
    public void CourierCanNotBeDeleteWithInvalidIdTest() {
        CourierClient.courierCreate(courier);

        int incorrectId = 1000000;
        Response newCourierDel = CourierClient.setCourierDelete(incorrectId);
        checkResponseCourierDeleteWithInvalidId(newCourierDel, 404, "Курьера с таким id нет.");

        courierId = CourierClient.getCourierId(fieldsFrom(courier));
        CourierClient.courierDeleteAfterTest(courierId);
    }

    @Feature(value = "/api/v1/courier/:id")
    @Test
    @DisplayName("Проверка удаления курьера без Id (негатив).")
    @Description("Тест проверяет, что курьера нельзя удалить, если не указан Id. Возвращается ошибка.")
    public void CourierCanNotBeDeleteWithNullIdTest() {
        CourierClient.courierCreate(courier);

        Response newCourierDelWithoutId = CourierClient.setCourierDelete(0);
        checkResponseCourierDeleteWithInvalidId(newCourierDelWithoutId, 404, "Курьера с таким id нет.");

        courierId = CourierClient.getCourierId(fieldsFrom(courier));
        CourierClient.courierDeleteAfterTest(courierId);

        // !!! Баг в статусе кода и описании ошибки:
        // ожидаемый результат (в документации) {"code": 400, "message": "Недостаточно данных для удаления курьера."},
        // фактический результат {"code": 404, "message": "Курьера с таким id нет."}
    }

    @Step("Проверить статус кода (200) и тело ответа (ок).")
    public void checkStatusCourierDelete(Response newCourier, int code, boolean ok) {
        newCourier.then().assertThat()
                .statusCode(code)
                .and()
                .body("ok", equalTo(ok));
    }

    @Step("Проверить статус кода (4хх) и тело ответа при передаче неправильного Id.")
    public void checkResponseCourierDeleteWithInvalidId(Response newCourierDel, int code, String message) {
        newCourierDel.then().assertThat()
                .statusCode(code)
                .and()
                .body("code", equalTo(code))
                .and()
                .body("message", equalTo(message));
    }

    @Step("Вывести в консоль ответ на успешный запрос (ok).")
    public void printResponseBodyToConsole(Response response) {
        System.out.println(response.body().asString());
    }
}
