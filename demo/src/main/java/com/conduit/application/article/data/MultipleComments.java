package com.conduit.application.article.data;

import lombok.*;
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