package per.chao.lifeshow.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import per.chao.lifeshow.entity.pojo.Activs;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author W.Chao
 * @since 2020-02-28
 */
@Repository
public interface ActivsMapper extends BaseMapper<Activs> {
	@Select("select * from tb_activs where activ_name = #{activName}")
	Activs selectByName(@Param("activName") String activName);
}
