package com.tennis.domain.model;

import com.tennis.domain.rules.GamesRules;
import com.tennis.domain.rules.RoundRules;
import com.tennis.domain.rules.SetRules;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static com.tennis.domain.model.PlayerType.PlayerA;
import static com.tennis.domain.model.PlayerType.PlayerB;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class PlayerScore {

    private final PlayerType playerType;

    @Builder.Default
    private final ComplexPoints setPoints = SetRules.setupPoints();

    @Builder.Default
    private final ComplexPoints gamesPoints = GamesRules.setupPoints();

    @Builder.Default
    private final ComplexPoints roundPoints = RoundRules.setupPoints();

    PlayerScore addRoundPoints(boolean gainPoint, ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {
        return new PlayerScore(playerType, setPoints, gamesPoints, roundPoints.applyPoint(gainPoint, playerOnePoints, playerTwoPoints));
    }

    PlayerScore addGamesPoint(boolean gainPoint, ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {
        return new PlayerScore(playerType, setPoints, playerOnePoints.applyPoint(gainPoint, playerOnePoints, playerTwoPoints), roundPoints);
    }

    PlayerScore addSetPoint(boolean gainPoint, ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {
        return new PlayerScore(playerType, playerOnePoints.applyPoint(gainPoint, playerOnePoints, playerTwoPoints), gamesPoints, roundPoints);
    }

    static PlayerScore forPlayerA() {
        return PlayerScore.builder().playerType(PlayerA).build();
    }

    static PlayerScore forPlayerB() {
        return PlayerScore.builder().playerType(PlayerB).build();
    }
}
