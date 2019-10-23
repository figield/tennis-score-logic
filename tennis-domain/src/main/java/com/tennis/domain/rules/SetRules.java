package com.tennis.domain.rules;

import com.tennis.domain.model.ComplexPoints;

public class SetRules implements Rules {

    public ComplexPoints winPoint(ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {

        // 0..4 / X -> 1..5 / X
        if (playerOnePoints.getMain() <= playerOnePoints.getDiff()) {
            return playerOnePoints.addMainPoints();
        }
        // 5 / 0..4 -> 6 / 0..4 (win!)
        if (playerOnePoints.getMain().equals(playerOnePoints.getTieBreak() - 1)
            && (playerOnePoints.getMain() + 1 - playerTwoPoints.getMain()) >= playerOnePoints.getWinDiff()) {
            return playerOnePoints.getErasedComplexPoints();
        }
        // 5 / 5,6 -> 6 / 5,6
        if (playerOnePoints.getMain().equals(playerOnePoints.getTieBreak() - 1) && playerTwoPoints.getMain() <= playerTwoPoints.getTieBreak()) {
            return playerOnePoints.addMainPoints();
        }
        // 6 / 5 -> 7 / 5 (win!)
        if (playerOnePoints.getMain().equals(playerOnePoints.getTieBreak()) && playerTwoPoints.getMain()
                                                                                              .equals(playerTwoPoints.getTieBreak() - 1)) {
            return playerOnePoints.getErasedComplexPoints();
        }
        // 6 / 6 (4 / 3) -> 0 / 0 (0 / 0) ; 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        if (playerOnePoints.getMain().equals(playerOnePoints.getTieBreak()) && playerTwoPoints.getMain()
                                                                                              .equals(playerTwoPoints.getTieBreak())) {

            return playerOnePoints.addBaseOn(playerTwoPoints.getSubPoints());
        }
        throw new IllegalStateException("Unexpected value when adding points");
    }

    public ComplexPoints losePoint(ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {

        // opponent: 0..4 / X -> 1..5 / X
        if (playerTwoPoints.getMain() <= playerOnePoints.getDiff()) {
            return playerOnePoints;
        }
        // opponent: 5 / 1..4 -> 6 / 1..4 (lose!) ?
        if (playerTwoPoints.getMain().equals(playerOnePoints.getTieBreak() - 1) && playerOnePoints.getMain() <= playerOnePoints.getDiff()) {
            return playerOnePoints.getErasedComplexPoints();
        }
        // opponent: 5 / 5,6 -> 6 / 5,6
        if (playerTwoPoints.getMain().equals(playerOnePoints.getTieBreak() - 1) && playerOnePoints.getMain() <= playerOnePoints.getTieBreak()) {
            return playerOnePoints;
        }
        // opponent: 6 / 5 -> 7 / 5 (lose!)
        if (playerTwoPoints.getMain().equals(playerOnePoints.getTieBreak()) && playerOnePoints.getMain()
                                                                                              .equals(playerOnePoints.getTieBreak() - 1)) {
            return playerOnePoints.getErasedComplexPoints();
        }
        // opponent: 6 / 6 (4 / 3) -> 0 / 0 (0 / 0) ; 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        if (playerTwoPoints.getMain().equals(playerOnePoints.getTieBreak()) && playerOnePoints.getMain()
                                                                                              .equals(playerOnePoints.getTieBreak())) {

            return playerOnePoints.eraseBaseOn(playerTwoPoints.getSubPoints());
        }
        throw new IllegalStateException("Unexpected value when adding points");
    }
}
