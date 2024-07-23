package application;

import input.InputController;
import lobby.LobbyController;
import lobby.game.list.GameList;
import ui.UIElements;

public class GameSpectator {
    public static void selectGame() {
        selectActiveGameMessage();
        GameList activeGameList = null;
        try {
            activeGameList = LobbyController.getGameListOnlyActive();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        UIElements.printGameList(activeGameList);
        if (activeGameList.getGameAmount() > 0) {
            final String selectedGameName = InputController.spectateGameSelection(activeGameList);
            // TODO needs to be implemented after game implementation is done
            System.out.println(selectedGameName);
        }
    }

    private static void selectActiveGameMessage() {
        System.out.println("Select one of the following active games, according to the number on top of each game listing:");
    }
}
