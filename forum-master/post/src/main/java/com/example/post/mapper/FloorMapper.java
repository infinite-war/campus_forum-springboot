package com.example.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.post.entity.Floor;
import org.apache.ibatis.annotations.Update;

/**
 * 楼层Mapper
 */
public interface FloorMapper extends BaseMapper<Floor> {

    //删除楼层
    @Update({"update `floor` set `comments`=`comments`-1 where floor_id = #{floorId}"})
    int removeCommentFromFloor(Long floorId);

    //添加楼层
    @Update({"update `floor` set `comments`=`comments`+1, `total_comments`=`total_comments`+1 where floor_id = #{floorId}"})
    int addCommentToFloor(Long floorId);

    //给楼层点赞
    @Update({"update `floor` set `likes`=`likes`+1 where floor_id = #{floorId}"})
    int increaseFloorLikes(Long floorId);

    //给楼层取消赞
    @Update({"update `floor` set `likes`=`likes`-1 where floor_id = #{floorId}"})
    int decreaseFloorLikes(Long floorId);

}
