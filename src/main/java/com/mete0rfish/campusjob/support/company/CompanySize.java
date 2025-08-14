package com.mete0rfish.campusjob.support.company;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CompanySize {
    SMALL("중소기업"),
    STARTUP("스타트업"),
    MEDIUM("중견기업"),
    LARGE("대기업"),
    ;

    private final String value;

    CompanySize(String value) {
        this.value = value;
    }

    // TODO Add Custom Exception
    public static CompanySize fromValue(String value) {
        return Arrays.stream(CompanySize.values())
                .filter(c -> c.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
    }
}
