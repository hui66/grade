package cn.com.yan.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ability {
    /**理论知识*/
    private String realTheoryAbilityTotal;
    /**专业编程能力指标*/
    private String realCodeAbilityTotal;
    /**实践能力指标*/
    private String realPracticeAbilityTotal;
    /**创新能力指标*/
    private String realCreateAbilityTotal;
    /**艺术表现能力指标*/
    private String realArtAbilityTotal;
    /**最缺少能力*/
    private String abilityGap;
}
