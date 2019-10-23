package com.tennis.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TennisScore {

    @Builder.Default
    private PlayerScore playerScoreA = PlayerScore.forPlayerA();
    @Builder.Default
    private PlayerScore playerScoreB = PlayerScore.forPlayerB();
    @Builder.Default
    private PointsTuple carryPoints = PointsTuple.builder().build();

    public TennisScore playerAWonPoint() {
        return calculateNewScore(carryPoints.toBuilder().pointA(true).build());
    }

    public TennisScore playerBWonPoint() {
        return calculateNewScore(carryPoints.toBuilder().pointB(true).build());
    }

    public String currentScore() {
        if (hasSubPoints(playerScoreA.getGamesPoints(), playerScoreB.getGamesPoints())) {
            return String.format("%d/%d %d/%d %d/%d",
                playerScoreA.getSetPoints().getMain(),
                playerScoreB.getSetPoints().getMain(),
                playerScoreA.getGamesPoints().getMain(),
                playerScoreB.getGamesPoints().getMain(),
                playerScoreA.getGamesPoints().getSubPoints(),
                playerScoreB.getGamesPoints().getSubPoints());
        }

        if (hasSubPoints(playerScoreA.getSetPoints(), playerScoreB.getSetPoints())) {
            return String.format("%d/%d %d/%d",
                playerScoreA.getSetPoints().getMain(),
                playerScoreB.getSetPoints().getMain(),
                playerScoreA.getGamesPoints().getSubPoints(),
                playerScoreB.getGamesPoints().getSubPoints());
        }

        return String.format("%d/%d %d/%d %s/%s",
            playerScoreA.getSetPoints().getMain(),
            playerScoreB.getSetPoints().getMain(),
            playerScoreA.getGamesPoints().getMain(),
            playerScoreB.getGamesPoints().getMain(),
            playerScoreA.getRoundPoints().getRoundPoints(),
            playerScoreB.getRoundPoints().getRoundPoints());
    }

    private boolean hasSubPoints(ComplexPoints playerPoints1, ComplexPoints playerPoints2) {
        return !playerPoints1.getSubPoints().equals(0) || !playerPoints2.getSubPoints().equals(0);
    }

    private TennisScore calculateNewScore(PointsTuple carryPoints) {
        return applyRoundPoints(carryPoints)
            .applyGamesPoints()
            .applySetPoints();
    }

    private TennisScore applyRoundPoints(PointsTuple carryPoints) {
        PlayerScore updatedPlayerA = playerScoreA.addRoundPoints(carryPoints.isPointA(), playerScoreA.getRoundPoints(), playerScoreB.getRoundPoints());
        PlayerScore updatedPlayerB = playerScoreB.addRoundPoints(carryPoints.isPointB(), playerScoreB.getRoundPoints(), playerScoreA.getRoundPoints());
        PointsTuple updatedCarryPoints = updateCarryPoints(carryPoints, updatedPlayerA, updatedPlayerB);
        return new TennisScore(updatedPlayerA, updatedPlayerB, updatedCarryPoints);
    }

    private PointsTuple updateCarryPoints(PointsTuple carryPoints, PlayerScore updatedPlayerA, PlayerScore updatedPlayerB) {
        if (updatedPlayerA.getRoundPoints().getRoundPoints().isZero() && updatedPlayerB.getRoundPoints().getRoundPoints().isZero()) {
            return carryPoints;
        }
        return new PointsTuple();
    }

    private TennisScore applyGamesPoints() {
        PlayerScore updatedPlayerA = playerScoreA.addGamesPoint(carryPoints.isPointA(), playerScoreA.getGamesPoints(), playerScoreB.getGamesPoints());
        PlayerScore updatedPlayerB = playerScoreB.addGamesPoint(carryPoints.isPointB(), playerScoreB.getGamesPoints(), playerScoreA.getGamesPoints());
        PointsTuple updatedCarryPoints = updateCarryPointsForGames(carryPoints, updatedPlayerA, updatedPlayerB);
        return new TennisScore(updatedPlayerA, updatedPlayerB, updatedCarryPoints);
    }

    private PointsTuple updateCarryPointsForGames(PointsTuple carryPoints, PlayerScore updatedPlayerA, PlayerScore updatedPlayerB) {
        if (updatedPlayerA.getGamesPoints().getMain().equals(0) && updatedPlayerB.getGamesPoints().getMain().equals(0)) {
            return carryPoints;
        }
        return new PointsTuple();
    }

    private TennisScore applySetPoints() {
        PlayerScore updatedPlayerA = playerScoreA.addSetPoint(carryPoints.isPointA(), playerScoreA.getSetPoints(), playerScoreB.getSetPoints());
        PlayerScore updatedPlayerB = playerScoreB.addSetPoint(carryPoints.isPointB(), playerScoreB.getSetPoints(), playerScoreA.getSetPoints());
        return new TennisScore(updatedPlayerA, updatedPlayerB, new PointsTuple());
    }
}
