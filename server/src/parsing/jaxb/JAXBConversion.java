package parsing.jaxb;

import exceptions.GameStructureFileException;
import game.structure.Board;
import game.structure.GameStructure;
import game.structure.Team;
import parsing.WordsCollector;
import parsing.jaxb.generated.ECNBoard;
import parsing.jaxb.generated.ECNGame;
import parsing.jaxb.generated.ECNLayout;
import parsing.jaxb.generated.ECNTeams;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JAXBConversion {
    private final static String GENERATED_XJC_CLASSES_PACKAGE = "parsing.jaxb.generated";
    public static GameStructure parseToGameStructure(final InputStream xmlStream, final InputStream dictionaryStream) throws JAXBException, GameStructureFileException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(GENERATED_XJC_CLASSES_PACKAGE);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ECNGame ecnGame = (ECNGame) unmarshaller.unmarshal(xmlStream);
        final String name = ecnGame.getName();
        final Board board = parseECNBoard(ecnGame.getECNBoard());
        final List<Team> teams = parseECNTeams(ecnGame.getECNTeams());
        final Set<String> words = WordsCollector.collectWords(dictionaryStream);
        final String dictionaryFileName = ecnGame.getECNDictionaryFile();
        final GameStructure outGameStructure = new GameStructure(name, board, teams, words, dictionaryFileName);
        checkValidity(outGameStructure);
        return outGameStructure;
    }

    private static Board parseECNBoard(final ECNBoard ecnBoard) {
        final int cardCount = ecnBoard.getCardsCount();
        final int blackCardsCount = ecnBoard.getBlackCardsCount();
        final ECNLayout ecnLayout = ecnBoard.getECNLayout();
        final int rows = ecnLayout.getRows();
        final int columns = ecnLayout.getColumns();
        return new Board(cardCount, blackCardsCount, rows, columns);
    }

    private static List<Team> parseECNTeams(final ECNTeams ecnTeams) {
        List<Team> teams = new ArrayList<>();
        ecnTeams.getECNTeam().forEach(ecnTeam -> {
            final String name = ecnTeam.getName();
            final int cardCount = ecnTeam.getCardsCount();
            final int definersCount = ecnTeam.getDefiners();
            final int guessersCount = ecnTeam.getGuessers();
            teams.add(new Team(name, cardCount, definersCount, guessersCount));
        });
        return teams;
    }

    private static void checkValidity(final GameStructure gameStructure) throws GameStructureFileException {
        // Game name uniqueness will be handled later, right before adding the game to the lobby.
        checkTotalCardsCountToWordCount(gameStructure.getBoard(), gameStructure.getWords().size());
        checkTotalTeamCardsCountToCardCount(gameStructure.getTeams(), gameStructure.getBoard().getCardCount());
        checkTableSizeToTotalCardsCount(gameStructure.getBoard());
        for (Team team : gameStructure.getTeams()) {
            checkDefinersAndGuessersCounts(team);
        }
        checkTeamNameUniqueness(gameStructure.getTeams());
    }

    private static void checkTotalCardsCountToWordCount(final Board board, final int wordCount) throws GameStructureFileException {
        final int totalCardsInGame = board.getCardCount() + board.getBlackCardCount();
        if (totalCardsInGame > wordCount) {
            throw new GameStructureFileException("Amount of all word cards (regular and black words) in a game is greater than the amount of words in the word bank: "
                    + totalCardsInGame + " > " + wordCount
                    + "\nCorrect the \"cards-count\" and/or \"black-cards-count\" in the XML file so that the sum is no greater than " + wordCount);
        }
    }

    private static void checkTotalTeamCardsCountToCardCount(final List<Team> teams, final int cardCount) throws GameStructureFileException {
        final int totalTeamCards = teams.stream().mapToInt(Team::getCardCount).sum();
        if (totalTeamCards > cardCount) {
            throw new GameStructureFileException("The amount of cards assigned for all teams is greater than the amount of regular cards available in a game: "
                    + totalTeamCards + " > " + cardCount
                    + "\nCorrect the values of \"cards-count\" in the XML file for each team, so that the sum is no greater than " + cardCount);
        }
    }

    private static void checkTableSizeToTotalCardsCount(final Board board) throws GameStructureFileException {
        final int cardsTableSize = board.getRows() * board.getColumns();
        final int totalCardsInGame = board.getCardCount() + board.getBlackCardCount();
        if (cardsTableSize < totalCardsInGame) {
            throw new GameStructureFileException("The specified rows and columns result in a product less than the amount of total cards in the game: "
                    + board.getRows() + " * " + board.getColumns() + " < " + totalCardsInGame
                    + "\nCorrect the values of \"rows\" and \"columns\" in the XML file so that their product is equal or more than " + totalCardsInGame);
        }
    }

    private static void checkTeamNameUniqueness(final List<Team> teams) throws GameStructureFileException {
        List<String> teamNamesList = teams.stream().map(Team::getName).collect(Collectors.toList());
        for (String teamName : teamNamesList) {
            if (Collections.frequency(teamNamesList, teamName) > 1) {
                throw new GameStructureFileException("There is more than one team sharing the same name: " + teamName + "\nPlease give a different name to one of the teams.");
            }
        }
    }

    private static void checkDefinersAndGuessersCounts(final Team team) throws GameStructureFileException {
        if (team.getDefinersCount() < 1) {
            throw new GameStructureFileException("The number of definers in Team " + team.getName() + " is less than 1, it is " + team.getDefinersCount()
                    + "\nPlease change the number in \"definers\" of Team " + team.getName() + " in the XML file to be at least 1.");
        }
        else if (team.getGuessersCount() < 1) {
            throw new GameStructureFileException("The number of guessers in Team " + team.getName() + " is less than 1, it is " + team.getGuessersCount()
                    + "\nPlease change the number in \"guessers\" of Team " + team.getName() + " in the XML file to be at least 1.");
        }
    }
}
