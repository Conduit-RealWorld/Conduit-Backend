package com.conduit.application.article.data;

import lombok.*;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MultipleArticlesDTO {
    ArrayList<SingleArticleDTO> articles = new ArrayList<>();
    int articlesCount;
}