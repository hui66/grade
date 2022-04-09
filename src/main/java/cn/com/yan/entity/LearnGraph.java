package cn.com.yan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearnGraph {

    /**x线*/
    private List<String> xAxis;
    /**y线*/
    private List<String> yAxis;
    /**能力顺序降序排序*/
    private String learnSuggest;
    /**能力顺序降序排序*/
    private String word;
}
