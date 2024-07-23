package lobby.game.list;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import game.structure.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        GameList gameList = new GameList();
        GameListingData gameListingData = new GameListingData();
        ConnectedPlayersMap connectedPlayersMap = new ConnectedPlayersMap();
        Map<Team, Integer> map = new HashMap<>();
        Team team = new Team();
        team.setName("hewwo");
        team.setCardCount(1);
        team.setDefinersCount(1);
        team.setGuessersCount(1);
        map.put(team, 0);
        connectedPlayersMap.setConnectedDefinersPerTeam(map);
        connectedPlayersMap.setConnectedGuessersPerTeam(map);
        gameListingData.setConnectedPlayers(connectedPlayersMap);
        gameList.setGameList(new ArrayList<>(Collections.singletonList(gameListingData)));
        final Gson gson = new Gson();
        System.out.println(gson.toJson(gameList));
        try {
            GameList gameList1 = gson.fromJson(gson.toJson(gameList), GameList.class);
        } catch (JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
    }
}
