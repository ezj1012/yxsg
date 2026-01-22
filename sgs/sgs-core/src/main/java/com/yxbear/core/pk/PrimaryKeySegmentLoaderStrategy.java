package com.yxbear.core.pk;

public interface PrimaryKeySegmentLoaderStrategy {

    PrimaryKeySegment load(String pkName, int batchSize);
}
