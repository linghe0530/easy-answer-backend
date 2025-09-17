package com.crane.answer.utils;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * @author crane
 * @date 2025.09.13 下午5:33
 * @description
 **/
public class rx {
    @SneakyThrows
    public static void main(String[] args) {
        Flowable<Long> flowable = Flowable.interval(1, TimeUnit.SECONDS)
                .map(i -> i + 1)
                .subscribeOn(Schedulers.io());
        Disposable subscribe = flowable
                .observeOn(Schedulers.io())
                .doOnNext(System.out::println)
                .subscribe();
        Thread.sleep(10000000);
    }
}
