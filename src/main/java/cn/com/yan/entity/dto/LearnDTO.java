package cn.com.yan.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LearnDTO {
    private String name;
    private String value = "0";

    public LearnDTO(String name) {
        this.name = name;
    }
}
