package com.yxbear.sg.svc.play.impl;

import org.springframework.stereotype.Service;

import com.yxbear.sg.domain.mapper.gi.GiUnionMapper;
import com.yxbear.sg.domain.model.gi.GiUnion;
import com.yxbear.sg.svc.play.UnionSvc;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UnionSvcImpl implements UnionSvc {

    GiUnionMapper unionMapper;

    @Override
    public GiUnion getUnionById(Integer id) {
        if (id == null || id < 0) { return null; }
        return unionMapper.selectById(id);
    }

}
