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

    public static Board parseECNBoard(final ECNBoard ecnBoard) {
        final int cardCount = ecnBoard.getCardsCount();
        final int blackCardsCount = ecnBoard.getBlackCardsCount();
        final ECNLayout ecnLayout = ecnBoard.getECNLayout();
        final int rows = ecnLayout.getRows();
        final int columns = ecnLayout.getColumns();
        return new Board(cardCount, blackCardsCount, rows, columns);
    }

    public static List<Team> parseECNTeams(final ECNTeams ecnTeams) throws GameStructureFileException {
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

    public static void checkValidity(final GameStructure gameStructure) throws GameStructureFileException {

        checkGameNameUniqueness(gameStructure.getName());

        checkDictionaryFile(gameStructure.getDictionaryFileName());

        Board board = gameStructure.getBoard();
        final int totalCardsCount = board.getCardCount() + board.getBlackCardCount();
        checkTotalCardsCountToWordCount(totalCardsCount, gameStructure.getWords().size());

        final int totalTeamCards = gameStructure.getTeams().stream().mapToInt(Team::getCardCount).sum();
        checkTotalTeamCardsCountToCardCount(totalTeamCards, board.getCardCount());

        checkTableSizeToTotalCardsCount(board.getRows(), board.getColumns(), totalCardsCount);

        gameStructure.getTeams().forEach(team -> checkDefinersAndGuessersCounts(team.getDefinersCount(), team.getGuessersCount()));

        checkTeamNameUniqueness(gameStructure.getTeams());





        if (teams.get(0).getName().equals(teams.get(1).getName())) {
            throw new GameStructureFileException("Both teams share the same name: " + teams.get(0).getName() + "\nPlease give a different name to one of the teams.");
        }
    }
    public static void checkGameNameUniqueness(final String gameName) throws GameStructureFileException {

    }
    public static void checkDictionaryFile(final String dictionaryFileName) throws GameStructureFileException {

    }
    public static void checkTotalCardsCountToWordCount(final int totalCardsCount, final int wordCount) throws GameStructureFileException {
        if (board.getCardCount() > words.getGameWords().size()) {
            throw new GameStructureFileException("Amount of regular words in a game is greater than the amount of words in the word bank: "
                    + board.getCardCount() + " > " + words.getGameWords().size()
                    + "\nCorrect the \"cards-count\" in the XML file to be less than " + words.getGameWords().size());
        }

        if (board.getBlackCardCount() > words.getBlackWords().size()) {
            throw new GameStructureFileException("Amount of black words in a game is greater than the amount of black words in the word bank: "
                    + board.getBlackCardCount() + " > " + words.getBlackWords().size()
                    + "\nCorrect the \"black-cards-count\" in the XML file to be less than " + words.getBlackWords().size());
        }
    }
    public static void checkTotalTeamCardsCountToCardCount(final int totalTeamCardsCount, final int cardCount) throws GameStructureFileException {
        final int totalTeamsCards = teams.stream().mapToInt(Team::getCardCount).sum();
        if (totalTeamsCards > board.getCardCount()) {
            throw new GameStructureFileException("The amount of cards assigned for both teams is greater than the amount of regular cards available in a game: "
                    + totalTeamsCards + " > " + board.getCardCount()
                    + "\nCorrect the values of \"cards-count\" in the XML file for each team, so that the sum is less than " + board.getCardCount());
        }
    }
    public static void checkTableSizeToTotalCardsCount(final int rows, final int columns, final int totalCardsCount) throws GameStructureFileException {
        final int cardsTableSize = board.getRows() * board.getColumns();
        final int totalCardsInGame = board.getCardCount() + board.getBlackCardCount();
        if (cardsTableSize < totalCardsInGame) {
            throw new GameStructureFileException("The specified rows and columns result in a product less than the amount of total cards in the game: "
                    + board.getRows() + " * " + board.getColumns() + " < " + totalCardsInGame
                    + "\nCorrect the values of \"rows\" and \"columns\" in the XML file so that their product is equal or more than " + totalCardsInGame);
        }
    }
    public static void checkTeamNameUniqueness(final List<Team> teams) throws GameStructureFileException {
        List<String> teamNamesList = teams.stream().map(Team::getName).collect(Collectors.toList());
        for (String teamName : teamNamesList) {
            if (Collections.frequency(teamNamesList, teamName) > 1) {

            }
        }
    }

    public static void checkDefinersAndGuessersCounts(final int definersCount, final int guessersCount) throws GameStructureFileException {

    }
}
