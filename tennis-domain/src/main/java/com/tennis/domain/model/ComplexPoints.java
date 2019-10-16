package com.tennis.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComplexPoints {

    Integer main;
    Integer subPoints;
    Integer tieBreak;
    Integer winDiff;
    Integer tieBreakDiff;
    Integer tieBreakLevel;


    Integer getDiff() {
        return tieBreak - winDiff;
    }

    ComplexPoints addMainPoints() {
        return new ComplexPoints(main + 1, subPoints, tieBreak, winDiff, tieBreakDiff, tieBreakLevel);
    }

    ComplexPoints getErasedComplexPoints() {
        return new ComplexPoints(0, 0, tieBreak, winDiff, tieBreakDiff, tieBreakLevel);
    }

    ComplexPoints addBaseOn(Integer opponentSubPoints) {
        // 6 / 6 (6 / 3) -> 0 / 0 (0 / 0)
        if (subPoints >= tieBreakLevel && (subPoints - opponentSubPoints) == tieBreakDiff) {
            return getErasedComplexPoints();
        }
        // 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        return new ComplexPoints(main, subPoints + 1, tieBreak, winDiff, tieBreakDiff, tieBreakLevel);
    }

    public ComplexPoints eraseBaseOn(Integer opponentSubPoints) {
        // opponent: 6 / 6 (6 / 3) -> 0 / 0 (0 / 0)
        if (opponentSubPoints >= tieBreakLevel && (opponentSubPoints - subPoints) == tieBreakDiff) {
            return getErasedComplexPoints();
        }
        // opponent: 6 / 6 (3 / 3) -> 6 / 6 (4 / 3)
        return this;
    }
}
