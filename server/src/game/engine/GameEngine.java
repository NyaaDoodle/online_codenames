package game.engine;

import game.instance.GameInstance;
import game.instance.Hint;
import game.instance.MoveEvent;
import game.instance.data.GameInstanceData;
import game.structure.GameStructure;
import game.structure.Team;
import lobby.game.list.GameListingData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameEngine {
    private final Map<String, GameInstance> gameInstances = new HashMap<>();

    public void addGame(@NotNull final GameListingData gameListingData) {
        // Assuming all games in the game list have unique names.
        final GameStructure gameStructure = gameListingData.getGameStructure();
        final List<Team> teamList = gameStructure.getTeams();
        Collections.shuffle(teamList);
        final LinkedList<Team> teamOrderList = new LinkedList<>(teamList);
        final GameInstance gameInstance = new GameInstance(gameStructure, teamOrderList);
        gameInstances.put(gameListingData.getName(), gameInstance);
    }

    public void removeGame(@NotNull final String gameName) {
        gameInstances.remove(gameName);
    }

    public GameInstanceData getGameInstanceDataFull(@NotNull final String gameName) {
        return new GameInstanceData(gameInstances.get(gameName));
    }

    public GameInstanceData getGameInstanceDataGuessers(@NotNull final String gameName) {
        final GameInstanceData gameInstanceData = new GameInstanceData(gameInstances.get(gameName));
        gameInstanceData.censorWordCards();
        return gameInstanceData;
    }

    public void setHintAtGame(@NotNull final String gameName, @NotNull final Hint hint) {
        final GameInstance gameInstance = gameInstances.get(gameName);
        if (gameInstance != null) {
            gameInstance.setCurrentHint(hint);
            gameInstance.moveToNextTurn();
        }
    }

    public MoveEvent makeMoveAtGame(@NotNull final String gameName, final int cardIndex) {
        final GameInstance gameInstance = gameInstances.get(gameName);
        MoveEvent moveEvent = null;
        if (gameInstance != null) {
            moveEvent = gameInstance.makeMove(cardIndex);
        }
        return moveEvent;
    }

    public void endTurnAtGame(@NotNull final String gameName) {
        final GameInstance gameInstance = gameInstances.get(gameName);
        if (gameInstance != null) {
            gameInstance.endTurn();
        }
    }

    public boolean hasGameEnded(@NotNull final String gameName) {
        final GameInstance gameInstance = gameInstances.get(gameName);
        return (gameInstance != null) && (gameInstance.hasGameEnded());
    }

    public boolean isCardFoundAtGame(@NotNull final String gameName, final int cardIndex) {
        final GameInstance gameInstance = gameInstances.get(gameName);
        return (gameInstance != null) && (gameInstance.isCardFound(cardIndex));
    }
}
