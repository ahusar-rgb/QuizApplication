package org.example.question;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.response.Response;
import org.example.topic.Topic;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Question {
    private Integer id;
    private final Topic topic;
    private final String content;
    private final DifficultyRank difficultyRank;
    private List<Response> responseList = new ArrayList<>();

    public Question(Integer id, Topic topic, String content, DifficultyRank difficultyRank) {
        this.id = id;
        this.topic = topic;
        this.content = content;
        this.difficultyRank = difficultyRank;
    }

    public void addResponse(Response response) {
        responseList.add(response);
    }

    public void removeResponse(Response response) {
        responseList.remove(response);
    }
}
