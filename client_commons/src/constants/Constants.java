package constants;

public class Constants {
    public static final String SERVER_NAME = "localhost";
    public static final String SERVER_PORT = "8080";
    public static final String BASE_URL = "http://" + SERVER_NAME + ':' + SERVER_PORT;
    public static final String LOGIN_RESOURCE_URI = "/login";

    public static final int STATUS_CODE_OK = 200;
    public static final int STATUS_CODE_CONFLICT = 409;
}
