package com.uf.genshinwishes.model.enums;

import com.uf.genshinwishes.model.User;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Region {
    CHINA("1"),
    AMERICA("6"),
    EUROPE("7"),
    ASIA("8");

    private final String prefix;

    Region(String prefix) {
        this.prefix = prefix;
    }

    public static Region getFromUser(User user) {
        return Arrays.stream(Region.values())
            .filter(r -> r.getPrefix().equals(user.getRegion()))
            .findFirst().orElse(Region.ASIA);
    }
}
