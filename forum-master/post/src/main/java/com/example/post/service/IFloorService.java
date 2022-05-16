package com.example.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.entity.Result;
import com.example.post.dto.NewFloor;
import com.example.post.entity.Floor;

/**
 * 楼层服务接口
 */
public interface IFloorService extends IService<Floor> {

    //发表楼层
    Result publishFloor(String token, NewFloor newFloor);

    //删除楼层
    Result deleteFloor(String token, Long floorId);

    //给楼层点赞
    Result likeTheFloor(String token, Long floorId);

    //给楼层取消赞
    Result dislikeTheFloor(String token, Long floorId);

}
