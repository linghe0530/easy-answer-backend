package com.crane.answer.mapper;

import com.crane.answer.model.dto.statistic.AppAnswerCountDTO;
import com.crane.answer.model.dto.statistic.AppAnswerResultCountDTO;
import com.crane.answer.model.po.UserAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author crane
 * @description 针对表【user_answer(用户答题记录表（主表）)】的数据库操作Mapper
 * @createDate 2025-09-10 18:40:23
 * @Entity com.crane.answer.model.po.UserAnswer
 */
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {


    @Select("select app_id,count(10) as answerCount from  user_answer  group by app_id order by answerCount desc  limit 10")
    List<AppAnswerCountDTO> selectAppAnswerCount();


    List<AppAnswerResultCountDTO> selectAppAnswerResultCount(@Param("appId") Long appId);
}




