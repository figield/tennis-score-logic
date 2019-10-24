package com.tennis.domain.rules;

import com.tennis.domain.model.ComplexPoints;

import static com.tennis.domain.model.RoundPoints.AD;
import static com.tennis.domain.model.RoundPoints.FORTY;
import static com.tennis.domain.model.RoundPoints.ZERO;

public class RoundRules implements Rules {

    public ComplexPoints winPoint(ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {

        // 0,15,30 / X -> 15,30,40 / X
        if (playerOnePoints.getRoundPoints().getValue() < FORTY.getValue()) {
            return playerOnePoints.toBuilder().roundPoints(playerOnePoints.getRoundPoints().addPoint()).build();
        }
        // 40 / AD -> 40 / 40 (opponent loses points)
        if (playerOnePoints.getRoundPoints().getValue() == FORTY.getValue() && playerTwoPoints.getRoundPoints().getValue() == AD.getValue()) {
            return playerOnePoints;
        }
        // 40 / 0,15,30 -> 0 / 0 (win! 40 -> 0)
        if (playerOnePoints.getRoundPoints().getValue() == FORTY.getValue() && playerOnePoints.getRoundPoints().getValue() > playerTwoPoints.getRoundPoints()
                                                                                                                                            .getValue()) {
            return playerOnePoints.toBuilder().roundPoints(ZERO).build();
        }
        // AD / 40 -> 0 / 0
        if (playerOnePoints.getRoundPoints().getValue() == AD.getValue() && playerTwoPoints.getRoundPoints().getValue() == FORTY.getValue()) {
            return playerOnePoints.toBuilder().roundPoints(ZERO).build();
        }
        // 40 / 40 -> AD / 40
        if (playerOnePoints.getRoundPoints().getValue() == FORTY.getValue() && playerTwoPoints.getRoundPoints().getValue() == FORTY.getValue()) {
            return playerOnePoints.toBuilder().roundPoints(AD).build();
        }

        throw new IllegalStateException("Unexpected value when adding points");
    }

    public ComplexPoints losePoint(ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {

        // opponent: 0,15,30 / X -> 25,30,40 / X
        if (playerTwoPoints.getRoundPoints().getValue() < FORTY.getValue()) {
            return playerOnePoints;
        }
        // opponent: 40 / AD -> 40 / 40 (winner loses points)
        if (playerTwoPoints.getRoundPoints().getValue() == FORTY.getValue() && playerOnePoints.getRoundPoints().getValue() == AD.getValue()) {
            return playerOnePoints.toBuilder().roundPoints(FORTY).build();
        }
        // opponent: 40 / 0,15,30 -> 0 / 0 (loose! 40 -> 0)
        if (playerTwoPoints.getRoundPoints().getValue() == FORTY.getValue() && playerTwoPoints.getRoundPoints().getValue() > playerOnePoints.getRoundPoints()
                                                                                                                                            .getValue()) {
            return playerOnePoints.toBuilder().roundPoints(ZERO).build();
        }
        // opponent: AD / 40 -> 0 / 0
        if (playerTwoPoints.getRoundPoints().getValue() == AD.getValue() && playerOnePoints.getRoundPoints().getValue() == FORTY.getValue()) {
            return playerOnePoints.toBuilder().roundPoints(ZERO).build();
        }
        // opponent: 40 / 40 -> AD / 40
        if (playerTwoPoints.getRoundPoints().getValue() == FORTY.getValue() && playerOnePoints.getRoundPoints().getValue() == FORTY.getValue()) {
            return playerOnePoints;
        }
        throw new IllegalStateException("Unexpected value when adding points");
    }

    public static ComplexPoints setupPoints() {
        return ComplexPoints.builder()
                            .rules(new RoundRules())
                            .roundPoints(ZERO)
                            .build();
    }
}
