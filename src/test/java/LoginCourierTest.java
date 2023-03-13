import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import Steps.Courier;
import POJO.CourierRequest;
import POJO.LoginRequest;
import generator.LoginRequestGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static generator.CourierRequestGenerator.getRandomCourier;

public class LoginCourierTest {
    private Courier courierClient;
    private Integer id;
    private CourierRequest courierRequest;

    @Before
    public void setUp() {
        courierClient = new Courier();
        courierRequest = getRandomCourier();
        courierClient.create(courierRequest).assertThat().statusCode(SC_CREATED)
                .and().body("ok", equalTo(true));
    }

    @After
    public void tearDown() {
        LoginRequest loginRequest = LoginRequestGenerator.from(courierRequest);
        id = courierClient.login(loginRequest)
                .extract()
                .path("id");
        System.out.println(id);
        if (id != null) {
            courierClient.delete(id)
                    .assertThat()
                    .body("ok", equalTo(true));
        }
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Проверка, что ожидаемый ответ 200 и id не пустое")
    public void courierLogIn() {
        LoginRequest loginRequest = LoginRequestGenerator.from(courierRequest);
        id = courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
        System.out.println(id);
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("Проверка, что ожидаемый ответ 400 - Недостаточно данных для входа")
    public void authorizationWithoutLogin() {
        LoginRequest loginRequest = LoginRequestGenerator.withoutLogin(courierRequest);
        id = courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .extract()
                .path("id");
        System.out.println("id пользователя " + id);
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Проверка, что ожидаемый ответ 400 - Недостаточно данных для входа")
    @Issue("BUG")
    public void authorizationWithoutPassword() {
        LoginRequest loginRequest = LoginRequestGenerator.withoutPassword(courierRequest);
        id = courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .extract()
                .path("id");
        System.out.println(id);
    }

    @Test
    @DisplayName("Авторизация с несуществующей парой логин-пароль")
    @Description("Проверка, что ожидаемый ответ 404 - Учетная запись не найдена")
    public void LoginNonExistentCourier() {
        LoginRequest loginRequest = LoginRequestGenerator.from(courierRequest);
        id = courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
        System.out.println(id);
        courierClient.delete(id);
        courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
