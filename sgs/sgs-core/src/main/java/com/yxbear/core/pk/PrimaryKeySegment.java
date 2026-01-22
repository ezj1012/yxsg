package com.yxbear.core.pk;

import java.io.Serializable;

public interface PrimaryKeySegment extends Serializable {

    public Integer getBatchSize();

    public Long getMax();

    public Long getMin();
}
