package com.tennis.domain.model;

import java.util.function.Function;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.tennis.domain.model.RoundPoints.AD;
import static com.tennis.domain.model.RoundPoints.FORTY;
import static com.tennis.domain.model.RoundPoints.ZERO;
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
//@AllArgsConstructor
public class PlayerScore_v1 {
/*
    PlayerType playerType;
    @Builder.Default
    ComplexPoints setPoints = new ComplexPoints(0, 0, SET_TIE_BREAK, SET_WIN_DIFF, SET_TIE_BREAK_DIFF, SET_TIE_BREAK_LEVEL);

    @Builder.Default
    ComplexPoints gamesPoints = new ComplexPoints(0, 0, GAMES_TIE_BREAK, GAMES_WIN_DIFF, GAMES_TIE_BREAK_DIFF, GAMES_TIE_BREAK_LEVEL);

    @Builder.Default
    RoundPoints roundPoints = ZERO;

    private static int SET = 3;
    private static int GAMES = 2;
    private static int PLAYS = 1;

    // Logic for the smallest points. First approach, not transformed to more generic (but it is easy to follow)
    PlayerScore_v1 addPoint(boolean gainPoint, RoundPoints opponentRoundPoints) {
        if (gainPoint) {
            // 0,15,30 / X -> 15,30,40 / X
            if (roundPoints.getValue() < FORTY.getValue()) {
                return getPlayerWithPoints(roundPoints.addPoint());
            }
            // 40 / AD -> 40 / 40 (opponent loses points)
            if (roundPoints.getValue() == FORTY.getValue() && opponentRoundPoints.getValue() == AD.getValue()) {
                return this;
            }
            // 40 / 0,15,30 -> 0 / 0 (win! 40 -> 0)
            if (roundPoints.getValue() == FORTY.getValue() && roundPoints.getValue() > opponentRoundPoints.getValue()) {
                return getPlayerWithPoints(ZERO);
            }
            // AD / 40 -> 0 / 0
            if (roundPoints.getValue() == AD.getValue() && opponentRoundPoints.getValue() == FORTY.getValue()) {
                return getPlayerWithPoints(ZERO);
            }
            // 40 / 40 -> AD / 40
            if (roundPoints.getValue() == FORTY.getValue() && opponentRoundPoints.getValue() == FORTY.getValue()) {
                return getPlayerWithPoints(AD);
            }
        } else {
            // opponent: 0,15,30 / X -> 25,30,40 / X
            if (opponentRoundPoints.getValue() < FORTY.getValue()) {
                return this;
            }
            // opponent: 40 / AD -> 40 / 40 (winner loses points)
            if (opponentRoundPoints.getValue() == FORTY.getValue() && roundPoints.getValue() == AD.getValue()) {
                return getPlayerWithPoints(FORTY);
            }
            // opponent: 40 / 0,15,30 -> 0 / 0 (loose! 40 -> 0)
            if (opponentRoundPoints.getValue() == FORTY.getValue() && opponentRoundPoints.getValue() > roundPoints.getValue()) {
                return getPlayerWithPoints(ZERO);
            }
            // opponent: AD / 40 -> 0 / 0
            if (opponentRoundPoints.getValue() == AD.getValue() && roundPoints.getValue() == FORTY.getValue()) {
                return getPlayerWithPoints(ZERO);
            }
            // opponent: 40 / 40 -> AD / 40
            if (opponentRoundPoints.getValue() == FORTY.getValue() && roundPoints.getValue() == FORTY.getValue()) {
                return this;
            }
        }
        throw new IllegalStateException("Unexpected value when adding points");
    }

    private PlayerScore_v1 getPlayerWithPoints(RoundPoints roundPoints) {
        return new PlayerScore_v1(playerType, setPoints, gamesPoints, roundPoints);
    }

    // -------------------------------------------------------------------------

    // Logic for points in Games and Sets. Second approach for counting points (more generic, but hard to follow)
    PlayerScore_v1 addGamesPoints(boolean gainPoint, ComplexPoints opponentPoints) {
        Supplier<ComplexPoints> pointsSupplier = this::getGamesPoints;
        Function<Integer, PlayerScore_v1> newPlayerScore = this::getPlayerWithGamePoints;
        return addGamesOrSetPoints(gainPoint, opponentPoints, pointsSupplier, newPlayerScore, GAMES);
    }

    PlayerScore_v1 addSetPoints(boolean gainPoint, ComplexPoints opponentPoints) {
        Supplier<ComplexPoints> pointsSupplier = this::getSetPoints;
        Function<Integer, PlayerScore_v1> newPlayerScore = this::getPlayerWithSetPoints;
        return addGamesOrSetPoints(gainPoint, opponentPoints, pointsSupplier, newPlayerScore, SET);  // this is shortcut
    }

    PlayerScore_v1 addGamesOrSetPoints(boolean gainPoint, ComplexPoints opponentPoints,
                                       Supplier<ComplexPoints> pointsSupplier,
                                       Function<Integer, PlayerScore_v1> newPlayerScore,
                                       int setOrGames) {
        if (gainPoint) {
            return getPlayerScoreWhenWin(opponentPoints, pointsSupplier, newPlayerScore, setOrGames);
        }
        return getPlayerScoreWhenLoose(opponentPoints, pointsSupplier, newPlayerScore, setOrGames);
    }

    private PlayerScore_v1 getPlayerScoreWhenLoose(ComplexPoints opponentPoints,
                                                   Supplier<ComplexPoints> pointsSupplier,
                                                   Function<Integer, PlayerScore_v1> newPlayerScore,
                                                   int setOrGames) {
        // opponent: 0..4 / X -> 1..5 / X
        if (opponentPoints.getMain() <= pointsSupplier.get().getDiff()) {
            return this;
        }
        // opponent: 5 / 1..4 -> 6 / 1..4 (lose!) ?
        if (opponentPoints.getMain().equals(pointsSupplier.get().getTieBreak() - 1) && pointsSupplier.get().getMain() <= pointsSupplier.get().getDiff()) {
            return newPlayerScore.apply(0);
        }
        // opponent: 5 / 5,6 -> 6 / 5,6
        if (opponentPoints.getMain().equals(pointsSupplier.get().getTieBreak() - 1) && pointsSupplier.get().getMain() <= pointsSupplier.get().getTieBreak()) {
            return this;
        }
        // opponent: 6 / 5 -> 7 / 5 (lose!)
        if (opponentPoints.getMain().equals(pointsSupplier.get().getTieBreak()) && pointsSupplier.get()
                                                                                                 .getMain()
                                                                                                 .equals(pointsSupplier.get().getTieBreak() - 1)) {
            return newPlayerScore.apply(0);
        }
        // opponent: 6 / 6 (4 / 3) -> 0 / 0 (0 / 0) ; 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        if (opponentPoints.getMain().equals(pointsSupplier.get().getTieBreak()) && pointsSupplier.get()
                                                                                                 .getMain()
                                                                                                 .equals(pointsSupplier.get().getTieBreak())) {
            ComplexPoints looserPoints = pointsSupplier.get();
            if (setOrGames == GAMES) {
                return new PlayerScore_v1(playerType, setPoints, looserPoints.eraseBaseOn(opponentPoints.getSubPoints()), roundPoints);
            } else {
                return new PlayerScore_v1(playerType, looserPoints.eraseBaseOn(opponentPoints.getSubPoints()), gamesPoints, roundPoints);
            }
        }
        return null;
    }

    private PlayerScore_v1 getPlayerScoreWhenWin(ComplexPoints opponentPoints,
                                                 Supplier<ComplexPoints> pointsSupplier,
                                                 Function<Integer, PlayerScore_v1> newPlayerScore,
                                                 int setOrGames) {

        // 0..4 / X -> 1..5 / X
        if (pointsSupplier.get().getMain() <= pointsSupplier.get().getDiff()) {
            return newPlayerScore.apply(1);
        }
        // 5 / 0..4 -> 6 / 0..4 (win!)
        if (pointsSupplier.get().getMain().equals(pointsSupplier.get().getTieBreak() - 1)
            && (pointsSupplier.get().getMain() + 1 - opponentPoints.getMain()) >= pointsSupplier.get().getWinDiff()) {
            return newPlayerScore.apply(0);
        }
        // 5 / 5,6 -> 6 / 5,6
        if (pointsSupplier.get().getMain().equals(pointsSupplier.get().getTieBreak() - 1) && opponentPoints.getMain() <= pointsSupplier.get().getTieBreak()) {
            return newPlayerScore.apply(1);
        }
        // 6 / 5 -> 7 / 5 (win!)
        if (pointsSupplier.get().getMain().equals(pointsSupplier.get().getTieBreak()) && opponentPoints.getMain()
                                                                                                       .equals(pointsSupplier.get().getTieBreak() - 1)) {
            return newPlayerScore.apply(0);
        }
        // 6 / 6 (4 / 3) -> 0 / 0 (0 / 0) ; 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        if (pointsSupplier.get().getMain().equals(pointsSupplier.get().getTieBreak()) && opponentPoints.getMain()
                                                                                                       .equals(pointsSupplier.get().getTieBreak())) {
            ComplexPoints winnerPoints = pointsSupplier.get();
            if (setOrGames == GAMES) {
                return new PlayerScore_v1(playerType, setPoints, winnerPoints.addBaseOn(opponentPoints.getSubPoints()), roundPoints);
            } else {
                return new PlayerScore_v1(playerType, winnerPoints.addBaseOn(opponentPoints.getSubPoints()), gamesPoints, roundPoints);
            }
        }
        return null;
    }

    private PlayerScore_v1 getPlayerWithGamePoints(Integer points) {
        return points.equals(0) ? getPlayerWithPlusZEROGamePoints() : getPlayerWithPlusONEGamePoints();
    }

    private PlayerScore_v1 getPlayerWithPlusONEGamePoints() {
        return new PlayerScore_v1(playerType, setPoints, gamesPoints.addMainPoints(), roundPoints);
    }

    private PlayerScore_v1 getPlayerWithPlusZEROGamePoints() {
        return new PlayerScore_v1(playerType, setPoints, gamesPoints.getErasedComplexPoints(), roundPoints);
    }

    private PlayerScore_v1 getPlayerWithSetPoints(Integer points) {
        return points.equals(0) ? getPlayerWithPlusONESetPoints() : getPlayerWithPlusZEROSetPoints();
    }

    private PlayerScore_v1 getPlayerWithPlusONESetPoints() {
        return new PlayerScore_v1(playerType, setPoints.addMainPoints(), gamesPoints, roundPoints);
    }

    private PlayerScore_v1 getPlayerWithPlusZEROSetPoints() {
        return new PlayerScore_v1(playerType, setPoints.getErasedComplexPoints(), gamesPoints, roundPoints);
    }
 */
}
