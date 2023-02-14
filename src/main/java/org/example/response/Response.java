package org.example.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Response {
    private Integer id;
    private final String text;
    private final Boolean correct;

    public Boolean isCorrect() {
        return correct;
    }

    public Response(Integer id, String text, Boolean correct) {
        this.id = id;
        this.text = text;
        this.correct = correct;
    }
}
