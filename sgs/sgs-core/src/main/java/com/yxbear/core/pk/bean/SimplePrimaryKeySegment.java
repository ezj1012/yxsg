package com.yxbear.core.pk.bean;

import com.yxbear.core.pk.PrimaryKeySegment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplePrimaryKeySegment implements PrimaryKeySegment {

    private static final long serialVersionUID = 1L;

    private Integer batchSize;

    private Long max;

    private Long min;

}
