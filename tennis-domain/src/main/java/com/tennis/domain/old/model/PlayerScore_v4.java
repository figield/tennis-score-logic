package com.tennis.domain.old.model;

//import com.tennis.domain.rules.GamesRules;
//import com.tennis.domain.rules.RoundRules;
//import com.tennis.domain.rules.SetRules;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class PlayerScore_v4 {
/*
    PlayerType playerType;
    @Builder.Default
    ComplexPoints setPoints = new ComplexPoints(0, 0, SET_TIE_BREAK, SET_WIN_DIFF, SET_TIE_BREAK_DIFF, SET_TIE_BREAK_LEVEL);

    @Builder.Default
    ComplexPoints gamesPoints = new ComplexPoints(0, 0, GAMES_TIE_BREAK, GAMES_WIN_DIFF, GAMES_TIE_BREAK_DIFF, GAMES_TIE_BREAK_LEVEL);

    @Builder.Default
    RoundPoints roundPoints = ZERO;

    PlayerScore_v4 addRoundPoints(boolean gainPoint, RoundPoints playerOnePoints, RoundPoints playerTwoPoints) {
        RoundPoints newPlayerOnePoints = gainPoint
            ? RoundRules.winPoint(playerOnePoints, playerTwoPoints)
            : RoundRules.losePoint(playerOnePoints, playerTwoPoints);
        return new PlayerScore_v4(playerType, setPoints, gamesPoints, newPlayerOnePoints);
    }

    PlayerScore_v4 addGamesPoint(boolean gainPoint, ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {
        ComplexPoints newPlayerOnePoints = gainPoint
            ? new GamesRules().winPoint(playerOnePoints, playerTwoPoints)
            : new GamesRules().losePoint(playerOnePoints, playerTwoPoints);
        return new PlayerScore_v4(playerType, setPoints, newPlayerOnePoints, roundPoints);
    }

    PlayerScore_v4 addSetPoint(boolean gainPoint, ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {
        ComplexPoints newSetPoints = gainPoint
            ? new SetRules().winPoint(playerOnePoints, playerTwoPoints)
            : new SetRules().losePoint(playerOnePoints, playerTwoPoints);
        return new PlayerScore_v4(playerType, newSetPoints, gamesPoints, roundPoints);
    }
*/
}
