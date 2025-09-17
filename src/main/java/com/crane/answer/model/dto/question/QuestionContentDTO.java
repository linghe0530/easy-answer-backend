package com.crane.answer.model.dto.question;

import com.crane.answer.utils.JsonUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionContentDTO {

    /**
     * 题目标题
     */
    @NotBlank(message = "问题标题不能为空")
    @Size(max = 200, message = "问题标题长度不能超过200字")
    private String title;

    /**
     * 题目选项列表
     */
    @NotNull(message = "选项列表不能为null")
    @Size(min = 2, max = 6, message = "题目选项数量必须在2-6之间")
    @Valid
    private List<Option> options;

    /**
     * 题目选项
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {

        private String result;
        private int score;
        @NotBlank(message = "选项内容不能为空")
        private String value;
        @NotNull(message = "选项不能为空")
        private String key;
    }

    public static void main(String[] args) {
        List<QuestionContentDTO> questionContentList = new ArrayList<>();

        // 1. 第一题：分布式技术单选题
        // 构造选项列表
        List<Option> optionList1 = Arrays.asList(
                new Option(
                        "技术认知偏差：Redis用于缓存，不解决一致性问题",
                        0,
                        "Redis缓存",
                        "A"
                ),
                new Option(
                        "技术认知正确：ZooKeeper基于ZAB协议保障分布式一致性",
                        5,
                        "ZooKeeper",
                        "B"
                ),
                new Option(
                        "技术认知偏差：Elasticsearch用于全文检索，与一致性无关",
                        0,
                        "Elasticsearch",
                        "C"
                ),
                new Option(
                        "技术认知偏差：Kafka用于消息队列，侧重高吞吐而非一致性",
                        0,
                        "Kafka",
                        "D"
                )
        );
        // 构造题目对象
        QuestionContentDTO question1 = new QuestionContentDTO(
                "以下哪种技术最适合解决分布式系统的一致性问题？",
                optionList1
        );

        // 2. 第二题：项目管理多选题
        // 构造选项列表
        List<Option> optionList2 = Arrays.asList(
                new Option(
                        "有效协作：及时暴露阻塞问题，对齐团队进度",
                        3,
                        "每日站会同步进度",
                        "A"
                ),
                new Option(
                        "目标明确：减少迭代过程中的方向偏差",
                        3,
                        "提前规划迭代目标",
                        "B"
                ),
                new Option(
                        "稳定交付：降低因需求变动导致的返工成本",
                        2,
                        "减少需求变更频率",
                        "C"
                ),
                new Option(
                        "风险行为：易引入质量问题，增加后期维护成本",
                        0,
                        "忽视代码评审环节",
                        "D"
                )
        );
        // 构造题目对象
        QuestionContentDTO question2 = new QuestionContentDTO(
                "项目迭代中，哪些做法有助于提升团队效率？（多选）",
                optionList2
        );

        // 将题目添加到集合
        questionContentList.add(question1);
        questionContentList.add(question2);
        System.out.println(JsonUtils.toJson(questionContentList));
    }
}


