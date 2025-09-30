package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.SequenceDO;

public interface SequenceMapper {
    void updateCurrentValue(String business);
    int getCurrentValue(String business);
}