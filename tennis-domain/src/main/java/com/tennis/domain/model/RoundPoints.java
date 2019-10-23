package com.tennis.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoundPoints {
    ZERO(1),
    FIFTEEN(2),
    THIRTY(3),
    FORTY(4),
    AD(5);

    private int value;

    @Override
    public String toString() {
        switch (this) {
            case ZERO:
                return "0";
            case FIFTEEN:
                return "15";
            case THIRTY:
                return "30";
            case FORTY:
                return "40";
            case AD:
                return "AD";
            default:
                throw new IllegalStateException("Unexpected value: " + this.value);
        }
    }

    public RoundPoints addPoint() {
        switch (this) {
            case ZERO:
                return FIFTEEN;
            case FIFTEEN:
                return THIRTY;
            case THIRTY:
                return FORTY;
            case FORTY:
            case AD:
            default:
                throw new IllegalStateException("Unexpected value: " + this.value);
        }
    }
}
