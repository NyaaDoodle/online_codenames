package client.utils.constants;

public class ClientConstants {
    public static final String SERVER_NAME = "localhost";
    public static final String SERVER_PORT = "8080";
    public static final String SERVLET_NAME = "server";
    public static final String BASE_URL = "http://" + SERVER_NAME + ':' + SERVER_PORT + '/' + SERVLET_NAME;
    public static final String LOGIN_RESOURCE_URI = "/login";
    public static final String LOGOUT_RESOURCE_URI = "/logout";
    public static final String GAME_LIST_RESOURCE_URI = "/game-list";
    public static final String GAME_RESOURCE_URI = "/game";
    public static final String GAME_DATA_RESOURCE_URI = "/game-data";
    public static final String JOIN_GAME_RESOURCE_URI = "/join-game";

    public static final String GO_BACK_STRING = "BACK";
    public static final String QUIT_STRING = "END";

    public static final int STATUS_CODE_OK = 200;
    public static final int STATUS_CODE_BAD_REQUEST = 400;
    public static final int STATUS_CODE_CONFLICT = 409;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int GO_BACK_NUM = -10;
    public static final int QUIT_NUM = -20;
    public static final int ERROR_NUM = -127;
}
