package com.crane.answer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.model.dto.scoringResult.ScoringResultAddRequest;
import com.crane.answer.model.dto.scoringResult.ScoringResultEditRequest;
import com.crane.answer.model.dto.scoringResult.ScoringResultQueryRequest;
import com.crane.answer.model.po.ScoringResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crane.answer.model.vo.ScoringResultResp;

/**
* @author crane
* @description 针对表【scoring_result(评分结果对照表)】的数据库操作Service
* @createDate 2025-09-10 18:40:23
*/
public interface ScoringResultService extends IService<ScoringResult> {

    Page<ScoringResultResp> listScoringResultRespPage(ScoringResultQueryRequest request);

    LambdaQueryWrapper<ScoringResult> getQueryWrapper(ScoringResultQueryRequest scoringResultQueryRequest);

    Long addScoringResult(ScoringResultAddRequest request);

    void editScoringResult(ScoringResultEditRequest request);

    void validScoringResult(ScoringResult scoringResult, boolean add);

    void deleteScoringResult(Long id);
}
