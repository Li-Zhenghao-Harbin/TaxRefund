package org.cityu.dao;

public interface SequenceMapper {
    void updateCurrentValue(String business);
    int getCurrentValue(String business);
}