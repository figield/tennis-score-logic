package com.tennis.domain.rules;

import com.tennis.domain.model.ComplexPoints;

public interface Rules {

    ComplexPoints winPoint(ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints);

    ComplexPoints losePoint(ComplexPoints playerOnePoints, ComplexPoints playerTwoPoints);
}
