package com.sky.mapper;

<<<<<<< HEAD
import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
=======
>>>>>>> origin/master
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

<<<<<<< HEAD

    /*
    * 插入菜品数据
    * */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);
=======
>>>>>>> origin/master
}
