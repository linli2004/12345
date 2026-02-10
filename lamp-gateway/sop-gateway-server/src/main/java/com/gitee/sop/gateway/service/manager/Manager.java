package com.gitee.sop.gateway.service.manager;

/**
 * @param <T> 入参
 * @param <R> 出参
 * @author 六如
 */
public interface Manager<T, R> {

    R refresh(T id);

    default void init() {

    }

}
