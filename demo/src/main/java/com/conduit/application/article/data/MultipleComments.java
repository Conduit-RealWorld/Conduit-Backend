package com.conduit.application.article.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MultipleComments {
    private ArrayList<SingleComment> comments = new ArrayList<>();
    public void addComment(SingleComment comment) {
        this.comments.add(comment);
    }
}
