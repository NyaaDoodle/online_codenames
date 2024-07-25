package utils.constants;

import javafx.print.Collation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Constants {
    public static final String LOBBY_MANAGER_ATTRIBUTE_NAME = "lobbyManager";
    public static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";

    public static final String LOGIN_SERVLET_NAME = "LoginServlet";
    public static final String LOGOUT_SERVLET_NAME = "LogoutServlet";
    public static final String GAME_LIST_SERVLET_NAME = "GameListServlet";
    public static final String NEW_GAME_SERVLET_NAME = "NewGameServlet";
    public static final String JOIN_GAME_SERVLET_NAME = "JoinGameServlet";
    public static final String GAME_SERVLET_NAME = "GameServlet";
    public static final String LOGIN_RESOURCE_URI = "/login";
    public static final String LOGOUT_RESOURCE_URI = "/logout";
    public static final String GAME_LIST_RESOURCE_URI = "/game-list";
    public static final String NEW_GAME_RESOURCE_URI = "/new-game";
    public static final String JOIN_GAME_RESOURCE_URI = "/join-game";
    public static final String GAME_RESOURCE_URI = "/game";

    // In case we want more admin usernames(?), for now it's only "admin".
    private static final Set<String> ADMIN_USERNAMES = new HashSet<>(Collections.singletonList("admin"));
    // In case we have more than just the admins as reserved usernames, for now it's only the admins.
    private static final Set<String> RESERVED_USERNAMES = new HashSet<>(ADMIN_USERNAMES);

    public static final int ERROR_NUM = -127;
}
