package org.example.topic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Topic {
    private Integer id;
    private final String name;

    public Topic(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
