package game.instance;

import game.structure.GameStructure;
import game.structure.Team;
import org.jetbrains.annotations.NotNull;
import utils.OtherUtils;

import java.util.*;

public class GameWordCards {
    private final static Team NEUTRAL_TEAM = new Team();
    private final List<WordCard> wordCards;

    public GameWordCards(final GameStructure gameStructure) {
        this(gameStructure.getWords(), gameStructure.getTeams(), gameStructure.getBoard().getCardCount(), gameStructure.getBoard().getBlackCardCount());
    }

    public GameWordCards(final Set<String> wordBank, final List<Team> teams, final int gameCardsCount, final int blackCardsCount) {
        this.wordCards = makeWordCards(wordBank, teams, gameCardsCount, blackCardsCount);
    }

    @NotNull
    public List<WordCard> getWordCardList() { return Collections.unmodifiableList(wordCards); }

    @NotNull
    private List<WordCard> makeWordCards(final Set<String> wordBank, final List<Team> teams, final int gameCardsCount, final int blackCardsCount) {
        List<WordCard> wordCardList = new ArrayList<>();
        Set<String> drawnGameWords = drawWordsFromWordBank(wordBank, gameCardsCount);
        Set<String> drawnBlackWords = drawWordsAndCheckIfExistsAtOtherBank(wordBank, drawnGameWords, blackCardsCount);
        int skipCount = 0;
        for (Team team : teams) {
            drawnGameWords.stream().skip(skipCount).limit(team.getCardCount())
                    .forEach((word) -> wordCardList.add(new WordCard(word, team, false)));
            skipCount += team.getCardCount();
        }
        drawnGameWords.stream().skip(skipCount).forEach((word) -> wordCardList.add(new WordCard(word, NEUTRAL_TEAM, false)));
        drawnBlackWords.forEach((word) -> wordCardList.add(new WordCard(word, NEUTRAL_TEAM, true)));
        Collections.shuffle(wordCardList);
        for (int i = 0; i < wordCardList.size(); i++) {
            wordCardList.get(i).setIndex(i);
        }
        return wordCardList;
    }

    @NotNull
    private Set<String> drawWordsFromWordBank(final Collection<String> wordBank, final int drawAmount) {
        Set<String> drawnWords = new HashSet<>();
        while (drawnWords.size() != drawAmount) {
            OtherUtils.getRandomMemberFromCollection(wordBank).ifPresent(drawnWords::add);
        }
        return drawnWords;
    }

    @NotNull
    private Set<String> drawWordsAndCheckIfExistsAtOtherBank(final Collection<String> sourceBank, final Collection<String> checkBank, final int drawAmount) {
        Set<String> drawnWords = new HashSet<>();
        while (drawnWords.size() != drawAmount) {
            OtherUtils.getRandomMemberFromCollection(sourceBank).ifPresent((word) -> {
                if (!(checkBank.contains(word))) { drawnWords.add(word); }
            });
        }
        return drawnWords;
    }
}
