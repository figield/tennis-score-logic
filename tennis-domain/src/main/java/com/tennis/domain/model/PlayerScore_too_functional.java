package com.tennis.domain.model;

import java.util.function.Function;
import java.util.function.Supplier;

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
public class PlayerScore_too_functional {

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
    PlayerScore_too_functional addPoint(boolean gainPoint, Points opponentPoints) {
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

    private PlayerScore_too_functional getPlayerWithPoints(Points points) {
        return new PlayerScore_too_functional(playerType, setPoints, gamesPoints, points);
    }

    // -------------------------------------------------------------------------

    // Logic for points in Games and Sets. Second approach for counting points (more generic, but hard to follow)
    PlayerScore_too_functional addGamesPoints(boolean gainPoint, ComplexPoints opponentPoints) {
        Supplier<ComplexPoints> pointsSupplier = this::getGamesPoints;
        Function<Integer, PlayerScore_too_functional> newPlayerScore = this::getPlayerWithGamePoints;
        return addGamesOrSetPoints(gainPoint, opponentPoints, pointsSupplier, newPlayerScore, GAMES);
    }

    PlayerScore_too_functional addSetPoints(boolean gainPoint, ComplexPoints opponentPoints) {
        Supplier<ComplexPoints> pointsSupplier = this::getSetPoints;
        Function<Integer, PlayerScore_too_functional> newPlayerScore = this::getPlayerWithSetPoints;
        return addGamesOrSetPoints(gainPoint, opponentPoints, pointsSupplier, newPlayerScore, SET);  // this is shortcut
    }

    PlayerScore_too_functional addGamesOrSetPoints(boolean gainPoint, ComplexPoints opponentPoints,
                                                   Supplier<ComplexPoints> pointsSupplier,
                                                   Function<Integer, PlayerScore_too_functional> newPlayerScore,
                                                   int setOrGames) {
        if (gainPoint) {
            return getPlayerScoreWhenWin(opponentPoints, pointsSupplier, newPlayerScore, setOrGames);
        }
        return getPlayerScoreWhenLoose(opponentPoints, pointsSupplier, newPlayerScore, setOrGames);
    }

    private PlayerScore_too_functional getPlayerScoreWhenLoose(ComplexPoints opponentPoints,
                                                               Supplier<ComplexPoints> pointsSupplier,
                                                               Function<Integer, PlayerScore_too_functional> newPlayerScore,
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
                return new PlayerScore_too_functional(playerType, setPoints, looserPoints.eraseBaseOn(opponentPoints.getSubPoints()), points);
            } else {
                return new PlayerScore_too_functional(playerType, looserPoints.eraseBaseOn(opponentPoints.getSubPoints()), gamesPoints, points);
            }
        }
        return null;
    }

    private PlayerScore_too_functional getPlayerScoreWhenWin(ComplexPoints opponentPoints,
                                                             Supplier<ComplexPoints> pointsSupplier,
                                                             Function<Integer, PlayerScore_too_functional> newPlayerScore,
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
                return new PlayerScore_too_functional(playerType, setPoints, winnerPoints.addBaseOn(opponentPoints.getSubPoints()), points);
            } else {
                return new PlayerScore_too_functional(playerType, winnerPoints.addBaseOn(opponentPoints.getSubPoints()), gamesPoints, points);
            }
        }
        return null;
    }

    private PlayerScore_too_functional getPlayerWithGamePoints(Integer points) {
        return points.equals(0) ? getPlayerWithPlusZEROGamePoints() : getPlayerWithPlusONEGamePoints();
    }

    private PlayerScore_too_functional getPlayerWithPlusONEGamePoints() {
        return new PlayerScore_too_functional(playerType, setPoints, gamesPoints.addMainPoints(), points);
    }

    private PlayerScore_too_functional getPlayerWithPlusZEROGamePoints() {
        return new PlayerScore_too_functional(playerType, setPoints, gamesPoints.getErasedComplexPoints(), points);
    }

    private PlayerScore_too_functional getPlayerWithSetPoints(Integer points) {
        return points.equals(0) ? getPlayerWithPlusONESetPoints() : getPlayerWithPlusZEROSetPoints();
    }

    private PlayerScore_too_functional getPlayerWithPlusONESetPoints() {
        return new PlayerScore_too_functional(playerType, setPoints.addMainPoints(), gamesPoints, points);
    }

    private PlayerScore_too_functional getPlayerWithPlusZEROSetPoints() {
        return new PlayerScore_too_functional(playerType, setPoints.getErasedComplexPoints(), gamesPoints, points);
    }
}
