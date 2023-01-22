import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ApiTests {

    @BeforeAll
    static void beforeClass() {
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test
    @DisplayName("Поиск пользователя по id")
    void getUserByIdTest() {
        given()
                .log().uri()
                .get("/api/users/7")
                .then()
                .statusCode(200)
                .body(
                        "data.id", is(7),
                        "data.email", is("michael.lawson@reqres.in")
                );
    }

    @Test
    @DisplayName("Успешная авторизация")
    void successfulLoginUserTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("Неуспешная авторизация. Отсутствует пароль")
    void unsuccessfulLoginUserTest() {
        String data = "{ \"email\": \"george.edwards@reqres.in\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Создание пользователя")
    void createUserTest() {
        String data = "{ \"name\": \"ivan\", \"job\": \"teacher\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body(
                        "name", is("ivan"),
                        "job", is("teacher"),
                        "id", notNullValue()
                );
    }

    @Test
    @DisplayName("Удаление пользователя")
    void deleteUserTest() {
        given()
                .log().uri()
                .when()
                .delete("/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
