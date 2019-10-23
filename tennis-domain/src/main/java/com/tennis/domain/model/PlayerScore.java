package com.tennis.domain.model;

import com.tennis.domain.rules.GamesRules;
import com.tennis.domain.rules.RoundRules;
import com.tennis.domain.rules.SetRules;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static com.tennis.domain.model.RoundPoints.ZERO;
import static com.tennis.domain.model.TennisScore.GAMES_TIE_BREAK;
import static com.tennis.domain.model.TennisScore.GAMES_TIE_BREAK_DIFF;
import static com.tennis.domain.model.TennisScore.GAMES_TIE_BREAK_LEVEL;
import static com.tennis.domain.model.TennisScore.GAMES_WIN_DIFF;
import static com.tennis.domain.model.TennisScore.SET_TIE_BREAK;
import static com.tennis.domain.model.TennisScore.SET_TIE_BREAK_DIFF;
import static com.tennis.domain.model.TennisScore.SET_TIE_BREAK_LEVEL;
import static com.tennis.domain.model.TennisScore.SET_WIN_DIFF;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class PlayerScore {

    PlayerType playerType;

    @Builder.Default
    ComplexPoints setPoints = new ComplexPoints(0, 0, SET_TIE_BREAK, SET_WIN_DIFF, SET_TIE_BREAK_DIFF, SET_TIE_BREAK_LEVEL, new SetRules(), ZERO);

    @Builder.Default
    ComplexPoints gamesPoints = new ComplexPoints(0, 0, GAMES_TIE_BREAK, GAMES_WIN_DIFF, GAMES_TIE_BREAK_DIFF, GAMES_TIE_BREAK_LEVEL, new GamesRules(), ZERO);

    @Builder.Default
    ComplexPoints roundPoints = new ComplexPoints(0, 0, 0, 0, 0, 0, new RoundRules(), ZERO);

    PlayerScore addRoundPoints(boolean gainPoint, ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {
        return new PlayerScore(playerType, setPoints, gamesPoints, roundPoints.applyPoint(gainPoint, playerOnePoints, playerTwoPoints));
    }

    PlayerScore addGamesPoint(boolean gainPoint, ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {
        return new PlayerScore(playerType, setPoints, playerOnePoints.applyPoint(gainPoint, playerOnePoints, playerTwoPoints), roundPoints);
    }

    PlayerScore addSetPoint(boolean gainPoint, ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {
        return new PlayerScore(playerType, playerOnePoints.applyPoint(gainPoint, playerOnePoints, playerTwoPoints), gamesPoints, roundPoints);
    }
}
