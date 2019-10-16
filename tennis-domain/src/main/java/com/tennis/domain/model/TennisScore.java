package com.tennis.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.tennis.domain.model.PlayerType.PlayerA;
import static com.tennis.domain.model.PlayerType.PlayerB;
import static com.tennis.domain.model.Points.ZERO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TennisScore {

    @Builder.Default
    private PlayerScore playerA = PlayerScore.builder().playerType(PlayerA).build();
    @Builder.Default
    private PlayerScore playerB = PlayerScore.builder().playerType(PlayerB).build();
    @Builder.Default
    private PointsTuple carryPoints = new PointsTuple();

    static Integer GAMES_TIE_BREAK = 6;
    static Integer GAMES_WIN_DIFF = 2;
    static Integer GAMES_TIE_BREAK_DIFF = 2;
    static Integer GAMES_TIE_BREAK_LEVEL = 7;

    static Integer SET_TIE_BREAK = 12;
    static Integer SET_WIN_DIFF = 3;
    static Integer SET_TIE_BREAK_DIFF = 2;
    static Integer SET_TIE_BREAK_LEVEL = 7;

    public TennisScore playerAWonPoint() {
        return calculateNewScore(carryPoints.toBuilder().pointA(true).pointB(false).build());
    }

    public TennisScore playerBWonPoint() {
        return calculateNewScore(carryPoints.toBuilder().pointA(false).pointB(true).build());
    }

    public String currentScore() {
        if (!playerA.getGamesPoints().getSubPoints().equals(0) || !playerB.getGamesPoints().getSubPoints().equals(0)) {
            return String.format("%d/%d %d/%d %d/%d",
                playerA.getSetPoints().getMain(),
                playerB.getSetPoints().getMain(),
                playerA.getGamesPoints().getMain(),
                playerB.getGamesPoints().getMain(),
                playerA.getGamesPoints().getSubPoints(),
                playerB.getGamesPoints().getSubPoints());
        }

        if (!playerA.getSetPoints().getSubPoints().equals(0) || !playerB.getSetPoints().getSubPoints().equals(0)) {
            return String.format("%d/%d %d/%d",
                playerA.getSetPoints().getMain(),
                playerB.getSetPoints().getMain(),
                playerA.getGamesPoints().getSubPoints(),
                playerB.getGamesPoints().getSubPoints());
        }

        return String.format("%d/%d %d/%d %s/%s",
            playerA.getSetPoints().getMain(),
            playerB.getSetPoints().getMain(),
            playerA.getGamesPoints().getMain(),
            playerB.getGamesPoints().getMain(),
            playerA.getPoints(),
            playerB.getPoints());

    }

    private TennisScore calculateNewScore(PointsTuple carryPoints) {
        return updatePoints(carryPoints)
            .updateGamesPoints()
            .updateSetPoints();
    }

    private TennisScore updatePoints(PointsTuple carryPoints) {
        PlayerScore updatedPlayerA = playerA.addPoint(carryPoints.isPointA(), playerB.getPoints());
        PlayerScore updatedPlayerB = playerB.addPoint(carryPoints.isPointB(), playerA.getPoints());
        PointsTuple updatedCarryPoints = updateCarryPoints(carryPoints, updatedPlayerA, updatedPlayerB);
        return new TennisScore(updatedPlayerA, updatedPlayerB, updatedCarryPoints);
    }

    private PointsTuple updateCarryPoints(PointsTuple pointsTuple, PlayerScore updatedPlayerA, PlayerScore updatedPlayerB) {
        if (updatedPlayerA.getPoints() == ZERO && updatedPlayerB.getPoints() == ZERO) {
            return pointsTuple;
        }
        return new PointsTuple();
    }

    private TennisScore updateGamesPoints() {
        PlayerScore updatedPlayerA = playerA.addGamesPoints(carryPoints.isPointA(), playerB.getGamesPoints());
        PlayerScore updatedPlayerB = playerB.addGamesPoints(carryPoints.isPointB(), playerA.getGamesPoints());
        PointsTuple updatedCarryPoints = updateCarryPointsForGames(carryPoints, updatedPlayerA, updatedPlayerB);
        return new TennisScore(updatedPlayerA, updatedPlayerB, updatedCarryPoints);
    }

    private PointsTuple updateCarryPointsForGames(PointsTuple carryPoints, PlayerScore updatedPlayerA, PlayerScore updatedPlayerB) {
        if (updatedPlayerA.getGamesPoints().getMain().equals(0) && updatedPlayerB.getGamesPoints().getMain().equals(0)) {
            return carryPoints;
        }
        return new PointsTuple();
    }

    private TennisScore updateSetPoints() {
        PlayerScore updatedPlayerA = playerA.addSetPoints(carryPoints.isPointA(), playerB.getSetPoints());
        PlayerScore updatedPlayerB = playerB.addSetPoints(carryPoints.isPointB(), playerA.getSetPoints());
        PointsTuple updatedCarryPoints = updateCarryPointsForSet(carryPoints, updatedPlayerA, updatedPlayerB);
        return new TennisScore(updatedPlayerA, updatedPlayerB, updatedCarryPoints);
    }

    private PointsTuple updateCarryPointsForSet(PointsTuple carryPoints, PlayerScore updatedPlayerA, PlayerScore updatedPlayerB) {
        if (updatedPlayerA.getSetPoints().getMain().equals(0) && updatedPlayerB.getSetPoints().getMain().equals(0)) {
            return carryPoints;
        }
        return new PointsTuple();
    }
}
