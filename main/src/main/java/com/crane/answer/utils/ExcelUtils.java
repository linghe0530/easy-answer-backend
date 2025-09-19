package com.crane.answer.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author crane
 * @date 2025.09.15 下午2:41
 * @description
 **/
@Slf4j
public class ExcelUtils {

    public static String excelToCsv(MultipartFile chartFile) {
        List<Map<Integer, String>> list;

        try {
            list = EasyExcel.read(chartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYS_ERROR, "读取excel文件错误");
        }
        if (CollUtils.isEmpty(list)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        for (Map<Integer, String> dataMap : list) {
            List<String> dataList = dataMap.values().stream().filter(Objects::nonNull).toList();
            sb.append(CollUtils.join(dataList, ",")).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(excelToCsv(null));
    }
}
