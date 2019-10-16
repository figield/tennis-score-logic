package com.tennis.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.tennis.domain.model.Points.AD;
import static com.tennis.domain.model.Points.FORTY;
import static com.tennis.domain.model.Points.ZERO;
import static com.tennis.domain.model.TennisScore.GAMES_TIE_BREAK;
import static com.tennis.domain.model.TennisScore.GAMES_TIE_BREAK_DIFF;
import static com.tennis.domain.model.TennisScore.GAMES_TIE_BREAK_LEVEL;
import static com.tennis.domain.model.TennisScore.GAMES_WIN_DIFF;
import static com.tennis.domain.model.TennisScore.SET_TIE_BREAK;
import static com.tennis.domain.model.TennisScore.SET_TIE_BREAK_DIFF;
import static com.tennis.domain.model.TennisScore.SET_TIE_BREAK_LEVEL;
import static com.tennis.domain.model.TennisScore.SET_WIN_DIFF;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerScore {

    PlayerType playerType;
    @Builder.Default
    ComplexPoints setPoints = new ComplexPoints(0, 0, SET_TIE_BREAK, SET_WIN_DIFF, SET_TIE_BREAK_DIFF, SET_TIE_BREAK_LEVEL);

    @Builder.Default
    ComplexPoints gamesPoints = new ComplexPoints(0, 0, GAMES_TIE_BREAK, GAMES_WIN_DIFF, GAMES_TIE_BREAK_DIFF, GAMES_TIE_BREAK_LEVEL);

    @Builder.Default
    Points points = ZERO;

    private static int SET = 3;
    private static int GAMES = 2;
    private static int PLAYS = 1;

    // Logic for the smallest points. First approach, not transformed to more generic (but it is easy to follow)
    PlayerScore addPoint(boolean gainPoint, Points opponentPoints) {
        if (gainPoint) {
            // 0,15,30 / X -> 15,30,40 / X
            if (points.getValue() < FORTY.getValue()) {
                return getPlayerWithPoints(points.addPoint());
            }
            // 40 / AD -> 40 / 40 (opponent loses points)
            if (points.getValue() == FORTY.getValue() && opponentPoints.getValue() == AD.getValue()) {
                return this;
            }
            // 40 / 0,15,30 -> 0 / 0 (win! 40 -> 0)
            if (points.getValue() == FORTY.getValue() && points.getValue() > opponentPoints.getValue()) {
                return getPlayerWithPoints(ZERO);
            }
            // AD / 40 -> 0 / 0
            if (points.getValue() == AD.getValue() && opponentPoints.getValue() == FORTY.getValue()) {
                return getPlayerWithPoints(ZERO);
            }
            // 40 / 40 -> AD / 40
            if (points.getValue() == FORTY.getValue() && opponentPoints.getValue() == FORTY.getValue()) {
                return getPlayerWithPoints(AD);
            }
        } else {
            // opponent: 0,15,30 / X -> 25,30,40 / X
            if (opponentPoints.getValue() < FORTY.getValue()) {
                return this;
            }
            // opponent: 40 / AD -> 40 / 40 (winner loses points)
            if (opponentPoints.getValue() == FORTY.getValue() && points.getValue() == AD.getValue()) {
                return getPlayerWithPoints(FORTY);
            }
            // opponent: 40 / 0,15,30 -> 0 / 0 (loose! 40 -> 0)
            if (opponentPoints.getValue() == FORTY.getValue() && opponentPoints.getValue() > points.getValue()) {
                return getPlayerWithPoints(ZERO);
            }
            // opponent: AD / 40 -> 0 / 0
            if (opponentPoints.getValue() == AD.getValue() && points.getValue() == FORTY.getValue()) {
                return getPlayerWithPoints(ZERO);
            }
            // opponent: 40 / 40 -> AD / 40
            if (opponentPoints.getValue() == FORTY.getValue() && points.getValue() == FORTY.getValue()) {
                return this;
            }
        }
        throw new IllegalStateException("Unexpected value when adding points");
    }

    private PlayerScore getPlayerWithPoints(Points points) {
        return new PlayerScore(playerType, setPoints, gamesPoints, points);
    }

    // -------------------------------------------------------------------------
    // Logic for points in Games and Sets.
    PlayerScore addGamesPoints(boolean gainPoint, ComplexPoints opponentPoints) {
        if (gainPoint) {
            return getPlayerGamesScoreWhenWin(opponentPoints);
        }
        return getPlayerGamesScoreWhenLoose(opponentPoints);
    }

    PlayerScore addSetPoints(boolean gainPoint, ComplexPoints opponentPoints) {
        if (gainPoint) {
            return getPlayerSetScoreWhenWin(opponentPoints);
        }
        return getPlayerSetScoreWhenLoose(opponentPoints);
    }

    private PlayerScore getPlayerGamesScoreWhenLoose(ComplexPoints opponentPoints) {

        // opponent: 0..4 / X -> 1..5 / X
        if (opponentPoints.getMain() <= gamesPoints.getDiff()) {
            return this;
        }
        // opponent: 5 / 1..4 -> 6 / 1..4 (lose!) ?
        if (opponentPoints.getMain().equals(gamesPoints.getTieBreak() - 1) && gamesPoints.getMain() <= gamesPoints.getDiff()) {
            return getPlayerWithPlusZEROGamePoints();
        }
        // opponent: 5 / 5,6 -> 6 / 5,6
        if (opponentPoints.getMain().equals(gamesPoints.getTieBreak() - 1) && gamesPoints.getMain() <= gamesPoints.getTieBreak()) {
            return this;
        }
        // opponent: 6 / 5 -> 7 / 5 (lose!)
        if (opponentPoints.getMain().equals(gamesPoints.getTieBreak()) && gamesPoints.getMain()
                                                                                     .equals(gamesPoints.getTieBreak() - 1)) {
            return getPlayerWithPlusZEROGamePoints();
        }
        // opponent: 6 / 6 (4 / 3) -> 0 / 0 (0 / 0) ; 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        if (opponentPoints.getMain().equals(gamesPoints.getTieBreak()) && gamesPoints.getMain()
                                                                                     .equals(gamesPoints.getTieBreak())) {

            return new PlayerScore(playerType, setPoints, gamesPoints.eraseBaseOn(opponentPoints.getSubPoints()), points);
        }
        return null;
    }

    private PlayerScore getPlayerSetScoreWhenLoose(ComplexPoints opponentPoints) {

        // opponent: 0..4 / X -> 1..5 / X
        if (opponentPoints.getMain() <= setPoints.getDiff()) {
            return this;
        }
        // opponent: 5 / 1..4 -> 6 / 1..4 (lose!) ?
        if (opponentPoints.getMain().equals(setPoints.getTieBreak() - 1) && setPoints.getMain() <= setPoints.getDiff()) {
            return getPlayerWithPlusZEROSetPoints();
        }
        // opponent: 5 / 5,6 -> 6 / 5,6
        if (opponentPoints.getMain().equals(setPoints.getTieBreak() - 1) && setPoints.getMain() <= setPoints.getTieBreak()) {
            return this;
        }
        // opponent: 6 / 5 -> 7 / 5 (lose!)
        if (opponentPoints.getMain().equals(setPoints.getTieBreak()) && setPoints.getMain()
                                                                                 .equals(setPoints.getTieBreak() - 1)) {
            return getPlayerWithPlusZEROSetPoints();
        }
        // opponent: 6 / 6 (4 / 3) -> 0 / 0 (0 / 0) ; 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        if (opponentPoints.getMain().equals(setPoints.getTieBreak()) && setPoints.getMain()
                                                                                 .equals(setPoints.getTieBreak())) {

            return new PlayerScore(playerType, setPoints.eraseBaseOn(opponentPoints.getSubPoints()), gamesPoints, points);
        }
        return null;
    }

    private PlayerScore getPlayerGamesScoreWhenWin(ComplexPoints opponentPoints) {

        // 0..4 / X -> 1..5 / X
        if (gamesPoints.getMain() <= gamesPoints.getDiff()) {
            return getPlayerWithPlusONEGamePoints();
        }
        // 5 / 0..4 -> 6 / 0..4 (win!)
        if (gamesPoints.getMain().equals(gamesPoints.getTieBreak() - 1)
            && (gamesPoints.getMain() + 1 - opponentPoints.getMain()) >= gamesPoints.getWinDiff()) {
            return getPlayerWithPlusZEROGamePoints();
        }
        // 5 / 5,6 -> 6 / 5,6
        if (gamesPoints.getMain().equals(gamesPoints.getTieBreak() - 1) && opponentPoints.getMain() <= gamesPoints.getTieBreak()) {
            return getPlayerWithPlusONEGamePoints();
        }
        // 6 / 5 -> 7 / 5 (win!)
        if (gamesPoints.getMain().equals(gamesPoints.getTieBreak()) && opponentPoints.getMain()
                                                                                     .equals(gamesPoints.getTieBreak() - 1)) {
            return getPlayerWithPlusZEROGamePoints();
        }
        // 6 / 6 (4 / 3) -> 0 / 0 (0 / 0) ; 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        if (gamesPoints.getMain().equals(gamesPoints.getTieBreak()) && opponentPoints.getMain()
                                                                                     .equals(gamesPoints.getTieBreak())) {

            return new PlayerScore(playerType, setPoints, gamesPoints.addBaseOn(opponentPoints.getSubPoints()), points);
        }
        return null;
    }

    private PlayerScore getPlayerSetScoreWhenWin(ComplexPoints opponentPoints) {

        // 0..4 / X -> 1..5 / X
        if (setPoints.getMain() <= setPoints.getDiff()) {
            return getPlayerWithPlusONESetPoints();
        }
        // 5 / 0..4 -> 6 / 0..4 (win!)
        if (setPoints.getMain().equals(setPoints.getTieBreak() - 1)
            && (setPoints.getMain() + 1 - opponentPoints.getMain()) >= setPoints.getWinDiff()) {
            return getPlayerWithPlusZEROSetPoints();
        }
        // 5 / 5,6 -> 6 / 5,6
        if (setPoints.getMain().equals(setPoints.getTieBreak() - 1) && opponentPoints.getMain() <= setPoints.getTieBreak()) {
            return getPlayerWithPlusONESetPoints();
        }
        // 6 / 5 -> 7 / 5 (win!)
        if (setPoints.getMain().equals(setPoints.getTieBreak()) && opponentPoints.getMain()
                                                                                 .equals(setPoints.getTieBreak() - 1)) {
            return getPlayerWithPlusZEROSetPoints();
        }
        // 6 / 6 (4 / 3) -> 0 / 0 (0 / 0) ; 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        if (setPoints.getMain().equals(setPoints.getTieBreak()) && opponentPoints.getMain()
                                                                                 .equals(setPoints.getTieBreak())) {
            return new PlayerScore(playerType, setPoints.addBaseOn(opponentPoints.getSubPoints()), gamesPoints, points);
        }
        return null;
    }

    private PlayerScore getPlayerWithPlusONEGamePoints() {
        return new PlayerScore(playerType, setPoints, gamesPoints.addMainPoints(), points);
    }

    private PlayerScore getPlayerWithPlusZEROGamePoints() {
        return new PlayerScore(playerType, setPoints, gamesPoints.getErasedComplexPoints(), points);
    }

    private PlayerScore getPlayerWithPlusONESetPoints() {
        return new PlayerScore(playerType, setPoints.addMainPoints(), gamesPoints, points);
    }

    private PlayerScore getPlayerWithPlusZEROSetPoints() {
        return new PlayerScore(playerType, setPoints.getErasedComplexPoints(), gamesPoints, points);
    }
}