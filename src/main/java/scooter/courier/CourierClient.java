package scooter.courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static scooter.BaseSpecification.*;

public class CourierClient {
    static int courierId;

    @Step("Передать запрос на создание курьера (\"/api/v1/courier\")")
    public static Response courierCreate(Courier courier) {
        return given()
                .spec(getBaseSpecification())
                .body(courier)
                .when()
                .post(PATH_COURIER);
    }

    @Step("Передать запрос на авторизацию курьера (\"/api/v1/courier/login\").")
    public static Response courierLogin(CourierFields courierFields) {
        return given()
                .spec(getBaseSpecification())
                .body(courierFields)
                .when()
                .post(PATH_COURIER + "login");
    }

    @Step("Получить Id созданного курьера.")
    public static int getCourierId(CourierFields courierFields) {
        Response courierLogin = courierLogin(courierFields);
        courierId = courierLogin.then().extract().path("id");
        return courierId;
    }

    @Step("Передать запрос на удаление курьера (\"/api/v1/courier/:id\")")
    public static Response setCourierDelete(int courierId) {
        return given()
                .spec(getBaseSpecification())
                .when()
                .delete(PATH_COURIER + courierId);
    }

    @Step("Удалить тестовые данные после выполнения теста.")
    public static void courierDeleteAfterTest(int courierId) {
        Response response = setCourierDelete(courierId);
        response.then().assertThat()
                .statusCode(200)
                .extract().path("ok");
    }


}

