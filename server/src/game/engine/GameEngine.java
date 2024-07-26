package game.engine;

import game.instance.GameInstance;
import game.instance.Hint;
import game.instance.MoveEvent;
import game.instance.data.GameInstanceData;
import game.structure.GameStructure;
import game.structure.Team;
import lobby.game.list.GameListingData;

import java.util.*;

public class GameEngine {
    private final Map<String, GameInstance> gameInstances = new HashMap<>();

    public void addGame(final GameListingData gameListingData) {
        // Assuming all games in the game list have unique names.
        final GameStructure gameStructure = gameListingData.getGameStructure();
        final List<Team> teamList = gameStructure.getTeams();
        Collections.shuffle(teamList);
        final LinkedList<Team> teamOrderList = new LinkedList<>(teamList);
        final GameInstance gameInstance = new GameInstance(gameStructure, teamOrderList);
        gameInstances.put(gameListingData.getName(), gameInstance);
    }

    public void removeGame(final String gameName) {
        gameInstances.remove(gameName);
    }

    public GameInstanceData getGameInstanceDataFull(final String gameName) {
        return new GameInstanceData(gameInstances.get(gameName));
    }

    public GameInstanceData getGameInstanceDataGuessers(final String gameName) {
        final GameInstanceData gameInstanceData = new GameInstanceData(gameInstances.get(gameName));
        gameInstanceData.censorWordCards();
        return gameInstanceData;
    }

    public void setHintAtGame(final String gameName, final Hint hint) {
        final GameInstance gameInstance = gameInstances.get(gameName);
        if (gameInstance != null) {
            gameInstance.setCurrentHint(hint);
            gameInstance.moveToNextTurn();
        }
    }

    public MoveEvent makeMoveAtGame(final String gameName, final int cardIndex) {
        final GameInstance gameInstance = gameInstances.get(gameName);
        MoveEvent moveEvent = null;
        if (gameInstance != null) {
            moveEvent = gameInstance.makeMove(cardIndex);
            // TODO did the game end check via moveEvent
        }
        return moveEvent;
    }
}
