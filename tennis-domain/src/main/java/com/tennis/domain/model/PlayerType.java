package com.tennis.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlayerType {

    PlayerA("A"),
    PlayerB("B");

    private String playerType;
}
