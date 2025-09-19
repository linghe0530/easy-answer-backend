package com.crane.answer.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author crane
 * @date 2025.09.13 下午5:33
 * @description
 **/
public class rx {
    @SneakyThrows
    public static void main(String[] args) {
        List<Map<Integer, String>> list;
        List<Map<Integer, String>> list2;
        File file = ResourceUtils.getFile("classpath:data.xlsx");
        File file2 = ResourceUtils.getFile("classpath:score.xlsx");
        list = EasyExcel.read(file)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet()
                .headRowNumber(0)
                .doReadSync();
        list2 = EasyExcel.read(file2)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet()
                .headRowNumber(0)
                .doReadSync();
        HashMap<Object, Object> map1 = new HashMap<>();
        HashMap<Object, Object> map2 = new HashMap<>();

        for (int i = 1; i < list.size(); i++) {
            Map<Integer, String> dataMap = list.get(i);
            String name = dataMap.get(1);
            String num = dataMap.get(2);
            if (num != null) {
                map1.put(num, name);
            }
        }
        for (int i = 1; i < list2.size(); i++) {
            Map<Integer, String> dataMap = list2.get(i);
            String num = dataMap.get(1);
            String score = dataMap.get(12);
            if (num != null) {
                map2.put(num, score);
            }
        }
        HashMap<Object, Object> map3 = new HashMap<>();
        map1.forEach((k, v) -> {
            Object o = map2.get(k);
            map3.put(v, o);
        });
        map3.forEach((k, v) -> {
            System.out.println(k + " " + v);
        });
    }
}
