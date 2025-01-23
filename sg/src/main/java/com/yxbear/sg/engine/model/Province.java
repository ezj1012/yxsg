package com.yxbear.sg.engine.model;

import lombok.Data;

@Data
public class Province {

    final int id;

    final String name;

    final boolean minority;

    int playerMaxCount = 50;

    int playerCount = 0;

    int cityCount = 0;

}
