package per.chao.lifeshow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import per.chao.lifeshow.entity.pojo.Danmaku;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author W.Chao
 * @since 2020-02-28
 */
@Repository
public interface DanmakuMapper extends BaseMapper<Danmaku> {
	@Select("select * from tb_danmaku where video_id = #{id} order by id asc")
	List<Danmaku> selectByVideoId(@Param("id")Integer id);
}
