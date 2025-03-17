package com.conduit.application.article.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MultipleArticlesDTO {
    ArrayList<SingleArticleDTO> articles;
    int articlesCount;
}
