import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import Steps.OrderClient;
import POJO.OrderRequest;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrdersTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;
    OrderRequest orderRequest;
    OrderClient orderClient;
    int track;

    public CreateOrdersTest(String firstName, String lastName, String address, String metroStation, String phone,
                            int rentTime, String deliveryDate, String comment, String[] color) {
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

    @Parameterized.Parameters(name = "Тестовые данные {0},{1},{2},{3}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {"Сергей", "Светлаков", "Ленина, 2, 22", "90", "+7 917 000 00 44", 6, "2024-03-06", "Привет всем", new String[]{"BLACK"}},
                {"Филипп", "Киркоров", "Аксакова, 123", "56", "+7 987 555 55 55", 5, "2022-12-31", "Нихау", new String[]{"GREY", "BLACK"}},
                {"Юрий", "Дудь", "Уфа, Айская, 15, 27", "4", "+7 917 222 22 22", 7, "2023-02-02", "Кто тут", new String[]{"GREY"}},
                {"Просто", "Человек", "Паук", "93", "+7 987 333 33 33", 1, "2023-05-11", "НЛО", new String[]{""}},
        };
    }
    @Test
    @DisplayName("API POST /api/v1/orders testing creating parameterized orders")
    public void createOrderTest() {
        orderRequest = new OrderRequest(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate,
                comment, color);
        orderClient = new OrderClient();
        track = orderClient.createOrder(orderRequest)
                .assertThat().statusCode(SC_CREATED)
                .and().body("track", notNullValue())
                .extract().path("track");
        System.out.println(track);
    }
}
