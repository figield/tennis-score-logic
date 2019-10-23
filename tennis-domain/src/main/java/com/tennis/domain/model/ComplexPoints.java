package com.tennis.domain.model;

import com.tennis.domain.rules.Rules;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

//import io.vavr.collection.List;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class ComplexPoints {

    Integer main;
    Integer subPoints;
    Integer tieBreak;
    Integer winDiff;
    Integer tieBreakDiff;
    Integer tieBreakLevel;
    Rules rules;

    RoundPoints roundPoints;

    public Integer getDiff() {
        return tieBreak - winDiff;
    }

    public ComplexPoints addMainPoints() {
        return this.toBuilder()
                   .main(main + 1)
                   .build();
    }

    public ComplexPoints getErasedComplexPoints() {
        return this.toBuilder()
                   .main(0)
                   .subPoints(0)
                   .build();
    }

    public ComplexPoints addBaseOn(Integer opponentSubPoints) {
        // 6 / 6 (6 / 3) -> 0 / 0 (0 / 0)
        if (subPoints >= tieBreakLevel && (subPoints - opponentSubPoints) == tieBreakDiff) {
            return getErasedComplexPoints(); // Win!
        }
        // 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        return this.toBuilder()
                   .subPoints(subPoints + 1)
                   .build();
    }

    public ComplexPoints eraseBaseOn(Integer opponentSubPoints) {
        // opponent: 6 / 6 (6 / 3) -> 0 / 0 (0 / 0)
        if (opponentSubPoints >= tieBreakLevel && (opponentSubPoints - subPoints) == tieBreakDiff) {
            return getErasedComplexPoints();
        }
        // opponent: 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        return this;
    }

    ComplexPoints applyPoint(boolean gainPoint, ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints) {
        return gainPoint
            ? rules.winPoint(playerOnePoints, playerTwoPoints)
            : rules.losePoint(playerOnePoints, playerTwoPoints);
    }
}
