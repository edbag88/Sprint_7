package generator;


import POJO.CourierRequest;
import POJO.LoginRequest;

public class LoginRequestGenerator {
    public static LoginRequest from(CourierRequest courierRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(courierRequest.getLogin());
        loginRequest.setPassword(courierRequest.getPassword());
        return loginRequest;
    }

    public static LoginRequest withoutLogin(CourierRequest courierRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(courierRequest.getPassword());
        return loginRequest;
    }

    public static LoginRequest withoutPassword(CourierRequest courierRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(courierRequest.getLogin());
        return loginRequest;
    }
}
