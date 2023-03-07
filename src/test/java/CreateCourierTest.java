import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
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
import static generator.CourierRequestGenerator.*;

public class CreateCourierTest {
    private Courier courierClient;
    private Integer id;

    @Before
    public void setUp() {
        courierClient = new Courier();
    }
    @After
    public void tearDown() {
        if (id != null) {
            courierClient.delete(id)
                    .assertThat()
                    .body("ok", Matchers.equalTo(true));
        }
    }

    @Test
    @DisplayName("Создание курьера - позитивный кейс")
    @Description("Проверка, что в ответе возвращается ok и status code 201")
    public void checkCreateCourier() {
        CourierRequest courierRequest = getRandomCourier();
        courierClient.create(courierRequest).assertThat().statusCode(SC_CREATED)
                .and().body("ok", equalTo(true));
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
    @DisplayName("Создание дубликата курьера")
    @Description("Проверка, что в ответе возвращается ошибка 409 - Этот логин уже используется.")
    @Issue("BUG")
    public void creatingDuplicatingCourier() {
        CourierRequest courierRequest = getRandomCourier();
        courierClient.create(courierRequest).assertThat().statusCode(SC_CREATED)
                .and().body("ok", equalTo(true));
        LoginRequest loginRequest = LoginRequestGenerator.from(courierRequest);
        id = courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
        System.out.println(id);
        courierClient.create(courierRequest).assertThat().statusCode(SC_CONFLICT);
        courierClient.create(courierRequest).assertThat().body("message", equalTo("Этот логин уже используется"));
    }
    @Test
    @DisplayName("Сreating a courier without a name(Создание курьера без имени)")
    public void сreatingСourierWithoutName() {
        CourierRequest courierRequest = getCourierWithoutFirstName();
        courierClient.create(courierRequest).assertThat().statusCode(SC_CREATED)
                .and().body("ok", equalTo(true));
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
    @DisplayName("Creating a courier without a password (создание курьера без пароля)")
    public void сreatingСourierWithoutPassword() {
        CourierRequest courierRequest = getCourierWithoutPassword();
        courierClient.create(courierRequest).assertThat().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Сreating a courier without a login (создание курьера без логина)")
    public void сreatingСourierWithoutLogin() {
        CourierRequest courierRequest = getCourierWithoutLogin();
        courierClient.create(courierRequest).assertThat().statusCode(SC_BAD_REQUEST);
    }
}


