package com.tennis.domain.model

import com.tennis.domain.rules.GamesRules
import com.tennis.domain.rules.RoundRules
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static PlayerType.PlayerA
import static PlayerType.PlayerB
import static RoundPoints.AD
import static RoundPoints.FIFTEEN
import static RoundPoints.FORTY
import static RoundPoints.THIRTY
import static RoundPoints.ZERO

@Unroll
class TennisScoreSpec extends Specification {

    @Shared
    TennisScore sharedTennisScore = new TennisScore()

    def "Should create empty tennis score"() {
        when:
            TennisScore tennisScore1 = new TennisScore()
        then:
            tennisScore1.currentScore() == "0/0 0/0 0/0"
    }

    @Unroll
    def "Should update game score when player A won, #expectedScore"() {
        given:
            ComplexPoints complexRoundPointsA = RoundRules.setupPoints()
                                                          .toBuilder()
                                                          .roundPoints(pointsA)
                                                          .build()
            ComplexPoints complexRoundPointsB = RoundRules.setupPoints()
                                                          .toBuilder()
                                                          .roundPoints(pointsB)
                                                          .build()
            PlayerScore playerA = PlayerScore.builder()
                                             .roundPoints(complexRoundPointsA)
                                             .playerType(PlayerA)
                                             .build()
            PlayerScore playerB = PlayerScore.builder()
                                             .roundPoints(complexRoundPointsB)
                                             .playerType(PlayerB)
                                             .build()

            TennisScore tennisScore1 = TennisScore.builder()
                                                  .playerScoreA(playerA)
                                                  .playerScoreB(playerB)
                                                  .build()
        when:
            TennisScore tennisScore2 = tennisScore1.playerAWonPoint()
        and:
            String currentScore = tennisScore2.currentScore()
        then:
            currentScore == expectedScore
        where:
            pointsA | pointsB || expectedScore
            ZERO    | ZERO    || "0/0 0/0 15/0"
            ZERO    | FIFTEEN || "0/0 0/0 15/15"
            ZERO    | THIRTY  || "0/0 0/0 15/30"
            ZERO    | FORTY   || "0/0 0/0 15/40"
            FIFTEEN | ZERO    || "0/0 0/0 30/0"
            FIFTEEN | FIFTEEN || "0/0 0/0 30/15"
            FIFTEEN | THIRTY  || "0/0 0/0 30/30"
            FIFTEEN | FORTY   || "0/0 0/0 30/40"
            THIRTY  | ZERO    || "0/0 0/0 40/0"
            THIRTY  | FIFTEEN || "0/0 0/0 40/15"
            THIRTY  | THIRTY  || "0/0 0/0 40/30"
            THIRTY  | FORTY   || "0/0 0/0 40/40"
            FORTY   | ZERO    || "0/0 1/0 0/0"
            FORTY   | FIFTEEN || "0/0 1/0 0/0"
            FORTY   | THIRTY  || "0/0 1/0 0/0"
            FORTY   | FORTY   || "0/0 0/0 AD/40"
            AD      | FORTY   || "0/0 1/0 0/0"
    }

    @Unroll
    def "Should update game score when player B won, #expectedScore"() {
        given:
            ComplexPoints complexRoundPointsA = RoundRules.setupPoints()
                                                          .toBuilder()
                                                          .roundPoints(pointsA)
                                                          .build()
            ComplexPoints complexRoundPointsB = RoundRules.setupPoints()
                                                          .toBuilder()
                                                          .roundPoints(pointsB)
                                                          .build()
            PlayerScore playerA = PlayerScore.builder()
                                             .roundPoints(complexRoundPointsA)
                                             .playerType(PlayerA)
                                             .build()
            PlayerScore playerB = PlayerScore.builder()
                                             .roundPoints(complexRoundPointsB)
                                             .playerType(PlayerB)
                                             .build()

            TennisScore tennisScore1 = TennisScore.builder()
                                                  .playerScoreA(playerA)
                                                  .playerScoreB(playerB)
                                                  .build()
        when:
            TennisScore tennisScore2 = tennisScore1.playerBWonPoint()
        and:
            String currentScore = tennisScore2.currentScore()
        then:
            currentScore == expectedScore
        where:
            pointsA | pointsB || expectedScore
            ZERO    | ZERO    || "0/0 0/0 0/15"
            ZERO    | FIFTEEN || "0/0 0/0 0/30"
            ZERO    | THIRTY  || "0/0 0/0 0/40"
            ZERO    | FORTY   || "0/0 0/1 0/0"
            FIFTEEN | ZERO    || "0/0 0/0 15/15"
            FIFTEEN | FIFTEEN || "0/0 0/0 15/30"
            FIFTEEN | THIRTY  || "0/0 0/0 15/40"
            FIFTEEN | FORTY   || "0/0 0/1 0/0"
            THIRTY  | ZERO    || "0/0 0/0 30/15"
            THIRTY  | FIFTEEN || "0/0 0/0 30/30"
            THIRTY  | THIRTY  || "0/0 0/0 30/40"
            THIRTY  | FORTY   || "0/0 0/1 0/0"
            FORTY   | ZERO    || "0/0 0/0 40/15"
            FORTY   | FIFTEEN || "0/0 0/0 40/30"
            FORTY   | THIRTY  || "0/0 0/0 40/40"
            FORTY   | FORTY   || "0/0 0/0 40/AD"
            AD      | FORTY   || "0/0 0/0 40/40"
    }

    @Unroll
    def "Should update game score when player A won #expectedScore2 and then player B won #expectedScore3"() {
        given:
            ComplexPoints complexGamesPointsA = GamesRules.setupPoints()
                                                          .toBuilder()
                                                          .main(gamesPointsA)
                                                          .build()
            ComplexPoints complexRoundPointsA = RoundRules.setupPoints()
                                                          .toBuilder()
                                                          .roundPoints(pointsA)
                                                          .build()
            ComplexPoints complexGamesPointsB = GamesRules.setupPoints()
                                                          .toBuilder()
                                                          .main(gamesPointsB)
                                                          .build()
            ComplexPoints complexRoundPointsB = RoundRules.setupPoints()
                                                          .toBuilder()
                                                          .roundPoints(pointsB)
                                                          .build()
            PlayerScore playerA = PlayerScore.builder()
                                             .gamesPoints(complexGamesPointsA)
                                             .roundPoints(complexRoundPointsA)
                                             .playerType(PlayerA)
                                             .build()
            PlayerScore playerB = PlayerScore.builder()
                                             .gamesPoints(complexGamesPointsB)
                                             .roundPoints(complexRoundPointsB)
                                             .playerType(PlayerB)
                                             .build()

            TennisScore tennisScore1 = TennisScore.builder()
                                                  .playerScoreA(playerA)
                                                  .playerScoreB(playerB)
                                                  .build()
        when:
            TennisScore tennisScore2 = tennisScore1.playerAWonPoint()
        and:
            TennisScore tennisScore3 = tennisScore2.playerBWonPoint()
        and:
            String score2 = tennisScore2.currentScore()
            String score3 = tennisScore3.currentScore()
        then:
            score2 == expectedScore2
            score3 == expectedScore3
        where:
            gamesPointsA | gamesPointsB | pointsA | pointsB || expectedScore2  | expectedScore3
            0            | 0            | ZERO    | ZERO    || "0/0 0/0 15/0"  | "0/0 0/0 15/15"
            0            | 0            | FORTY   | FORTY   || "0/0 0/0 AD/40" | "0/0 0/0 40/40"
            0            | 0            | FORTY   | AD      || "0/0 0/0 40/40" | "0/0 0/0 40/AD"
            0            | 0            | FIFTEEN | THIRTY  || "0/0 0/0 30/30" | "0/0 0/0 30/40"
            0            | 0            | AD      | FORTY   || "0/0 1/0 0/0"   | "0/0 1/0 0/15"
            0            | 0            | FIFTEEN | FORTY   || "0/0 0/0 30/40" | "0/0 0/1 0/0"
            1            | 0            | ZERO    | ZERO    || "0/0 1/0 15/0"  | "0/0 1/0 15/15"
            0            | 1            | FORTY   | FORTY   || "0/0 0/1 AD/40" | "0/0 0/1 40/40"
            1            | 0            | FORTY   | AD      || "0/0 1/0 40/40" | "0/0 1/0 40/AD"
            0            | 1            | FIFTEEN | THIRTY  || "0/0 0/1 30/30" | "0/0 0/1 30/40"
            1            | 0            | AD      | FORTY   || "0/0 2/0 0/0"   | "0/0 2/0 0/15"
            1            | 0            | FIFTEEN | FORTY   || "0/0 1/0 30/40" | "0/0 1/1 0/0"
            4            | 0            | FORTY   | THIRTY  || "0/0 5/0 0/0"   | "0/0 5/0 0/15"
            4            | 0            | THIRTY  | THIRTY  || "0/0 4/0 40/30" | "0/0 4/0 40/40"
            5            | 0            | FORTY   | THIRTY  || "1/0 0/0 0/0"   | "1/0 0/0 0/15"
    }

    @Unroll
    def "Should update game score when player B is winning only: #expectedScore"() {
        when:
            sharedTennisScore = sharedTennisScore.playerBWonPoint()
        and:
            String currentScore = sharedTennisScore.currentScore()
        then:
            currentScore == expectedScore
        where:
            functionName      || expectedScore
            'playerBWonPoint' || "0/0 0/0 0/15"
            'playerBWonPoint' || "0/0 0/0 0/30"
            'playerBWonPoint' || "0/0 0/0 0/40"
            'playerBWonPoint' || "0/0 0/1 0/0"
            'playerBWonPoint' || "0/0 0/1 0/15"
            'playerBWonPoint' || "0/0 0/1 0/30"
            'playerBWonPoint' || "0/0 0/1 0/40"
            'playerBWonPoint' || "0/0 0/2 0/0"
            'playerBWonPoint' || "0/0 0/2 0/15"
            'playerBWonPoint' || "0/0 0/2 0/30"
            'playerBWonPoint' || "0/0 0/2 0/40"
            'playerBWonPoint' || "0/0 0/3 0/0"
            'playerBWonPoint' || "0/0 0/3 0/15"
            'playerBWonPoint' || "0/0 0/3 0/30"
            'playerBWonPoint' || "0/0 0/3 0/40"
            'playerBWonPoint' || "0/0 0/4 0/0"
            'playerBWonPoint' || "0/0 0/4 0/15"
            'playerBWonPoint' || "0/0 0/4 0/30"
            'playerBWonPoint' || "0/0 0/4 0/40"
            'playerBWonPoint' || "0/0 0/5 0/0"
            'playerBWonPoint' || "0/0 0/5 0/15"
            'playerBWonPoint' || "0/0 0/5 0/30"
            'playerBWonPoint' || "0/0 0/5 0/40"
            'playerBWonPoint' || "0/1 0/0 0/0"
    }
}
