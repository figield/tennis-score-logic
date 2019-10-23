package com.tennis.domain.old.model;

//import com.tennis.domain.rules.GamesRules;
//import com.tennis.domain.rules.SetRules;
//import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//import static com.tennis.domain.model.RoundPoints.AD;
//import static com.tennis.domain.model.RoundPoints.FORTY;
//import static com.tennis.domain.model.RoundPoints.ZERO;
//import static com.tennis.domain.model.TennisScore.GAMES_TIE_BREAK;
//import static com.tennis.domain.model.TennisScore.GAMES_TIE_BREAK_DIFF;
//import static com.tennis.domain.model.TennisScore.GAMES_TIE_BREAK_LEVEL;
//import static com.tennis.domain.model.TennisScore.GAMES_WIN_DIFF;
//import static com.tennis.domain.model.TennisScore.SET_TIE_BREAK;
//import static com.tennis.domain.model.TennisScore.SET_TIE_BREAK_DIFF;
//import static com.tennis.domain.model.TennisScore.SET_TIE_BREAK_LEVEL;
//import static com.tennis.domain.model.TennisScore.SET_WIN_DIFF;

@Data
@Builder
@NoArgsConstructor
//@AllArgsConstructor
public class PlayerScore_v3 {
/*
    PlayerType playerType;
    @Builder.Default
    ComplexPoints setPoints = new ComplexPoints(0, 0, SET_TIE_BREAK, SET_WIN_DIFF, SET_TIE_BREAK_DIFF, SET_TIE_BREAK_LEVEL);

    @Builder.Default
    ComplexPoints gamesPoints = new ComplexPoints(0, 0, GAMES_TIE_BREAK, GAMES_WIN_DIFF, GAMES_TIE_BREAK_DIFF, GAMES_TIE_BREAK_LEVEL);

    @Builder.Default
    RoundPoints roundPoints = ZERO;

    // -------------------------------------------------------------------------
    // Logic for the smallest points. First approach, not transformed to more generic (but it is easy to follow)
    PlayerScore_v3 addPoint(boolean gainPoint, RoundPoints opponentRoundPoints) {
        if (gainPoint) {
            // 0,15,30 / X -> 15,30,40 / X
            if (roundPoints.getValue() < FORTY.getValue()) {
                return addPoints(roundPoints.addPoint());
            }
            // 40 / AD -> 40 / 40 (opponent loses points)
            if (roundPoints.getValue() == FORTY.getValue() && opponentRoundPoints.getValue() == AD.getValue()) {
                return this;
            }
            // 40 / 0,15,30 -> 0 / 0 (win! 40 -> 0)
            if (roundPoints.getValue() == FORTY.getValue() && roundPoints.getValue() > opponentRoundPoints.getValue()) {
                return addPoints(ZERO);
            }
            // AD / 40 -> 0 / 0
            if (roundPoints.getValue() == AD.getValue() && opponentRoundPoints.getValue() == FORTY.getValue()) {
                return addPoints(ZERO);
            }
            // 40 / 40 -> AD / 40
            if (roundPoints.getValue() == FORTY.getValue() && opponentRoundPoints.getValue() == FORTY.getValue()) {
                return addPoints(AD);
            }
        } else {
            // opponent: 0,15,30 / X -> 25,30,40 / X
            if (opponentRoundPoints.getValue() < FORTY.getValue()) {
                return this;
            }
            // opponent: 40 / AD -> 40 / 40 (winner loses points)
            if (opponentRoundPoints.getValue() == FORTY.getValue() && roundPoints.getValue() == AD.getValue()) {
                return addPoints(FORTY);
            }
            // opponent: 40 / 0,15,30 -> 0 / 0 (loose! 40 -> 0)
            if (opponentRoundPoints.getValue() == FORTY.getValue() && opponentRoundPoints.getValue() > roundPoints.getValue()) {
                return addPoints(ZERO);
            }
            // opponent: AD / 40 -> 0 / 0
            if (opponentRoundPoints.getValue() == AD.getValue() && roundPoints.getValue() == FORTY.getValue()) {
                return addPoints(ZERO);
            }
            // opponent: 40 / 40 -> AD / 40
            if (opponentRoundPoints.getValue() == FORTY.getValue() && roundPoints.getValue() == FORTY.getValue()) {
                return this;
            }
        }
        throw new IllegalStateException("Unexpected value when adding points");
    }

    private PlayerScore_v3 addPoints(RoundPoints roundPoints) {
        return new PlayerScore_v3(playerType, setPoints, gamesPoints, roundPoints);
    }

    // Logic for points in Games
    PlayerScore_v3 addGamesPoints(boolean gainPoint, ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {
        ComplexPoints newPlayerOnePoints = gainPoint
            ? GamesRules.winPoint(playerOnePoints, playerTwoPoints)
            : GamesRules.losePoint(playerOnePoints, playerTwoPoints);
        return new PlayerScore_v3(playerType, setPoints, newPlayerOnePoints, roundPoints);
    }

    // Logic for points in Sets.
    PlayerScore_v3 addSetPoints(boolean gainPoint, ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {
        ComplexPoints newSetPoints = gainPoint
            ? SetRules.winPoint(playerOnePoints, playerTwoPoints)
            : SetRules.losePoint(playerOnePoints, playerTwoPoints);
        return new PlayerScore_v3(playerType, newSetPoints, gamesPoints, roundPoints);
    }
*/
}
