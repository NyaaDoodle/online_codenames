package lobby;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lobby.game.list.GameList;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import utils.constants.ClientConstants;
import utils.http.ClientHttpClientUtils;

import java.util.Objects;

public class LobbyController {
    private static final String GAME_LIST_URL = ClientConstants.BASE_URL + ClientConstants.GAME_LIST_RESOURCE_URI;

    // At the server, only_actives takes priority over with_actives.
    private static final String ACTIVE_ONLY_QUERY_NAME = "only-actives";
    private static final String ACTIVE_INCLUDE_QUERY_NAME = "with-actives";

    @NotNull
    public static GameList getGameListOnlyPending() throws Exception {
        return getGameList(false, false);
    }

    @NotNull
    public static GameList getGameListOnlyActive() throws Exception {
        return getGameList(true, false); // The false is ignored.
    }

    @NotNull
    public static GameList getGameListAll() throws Exception {
        return getGameList(false, true);
    }

    @NotNull
    private static GameList getGameList(final boolean activeOnly, final boolean activeIncluded) throws Exception {
        // If activeOnly is set, retrieve game list with only active games. Otherwise, include pending games.
        // If activeIncluded is set, active games are included in the list. Otherwise, only pending games will be retrieved.
        final HttpUrl finalUrl = Objects.requireNonNull(HttpUrl.parse(GAME_LIST_URL)).newBuilder()
                .addQueryParameter(ACTIVE_ONLY_QUERY_NAME, String.valueOf(activeOnly))
                .addQueryParameter(ACTIVE_INCLUDE_QUERY_NAME, String.valueOf(activeIncluded))
                .build();
        final Request req = new Request.Builder().get().url(finalUrl).build();
        final String jsonBody = ClientHttpClientUtils.sendRequestSync(req);
        final Gson gson = new Gson();
        try {
            return gson.fromJson(jsonBody, GameList.class);
        } catch (JsonSyntaxException e) {
            throw new Exception("Received response from server with bad syntax.\n" + e.getMessage());
        }
    }
}
