package utils.constants;

public class ClientConstants {
    public static final String SERVER_NAME = "localhost";
    public static final String SERVER_PORT = "8080";
    public static final String SERVLET_NAME = "server";
    public static final String BASE_URL = "http://" + SERVER_NAME + ':' + SERVER_PORT + '/' + SERVLET_NAME;
    public static final String LOGIN_RESOURCE_URI = "/login";
    public static final String LOGOUT_RESOURCE_URI = "/logout";
    public static final String GAME_LIST_RESOURCE_URI = "/game-list";

    public static final String GO_BACK_STRING = "BACK";

    public static final int STATUS_CODE_OK = 200;
    public static final int STATUS_CODE_BAD_REQUEST = 400;
    public static final int STATUS_CODE_CONFLICT = 409;
    public static final int GO_BACK_NUM = -10;
    public static final int ERROR_NUM = -127;
}
