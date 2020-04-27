package per.chao.lifeshow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import per.chao.lifeshow.entity.pojo.UserAuths;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author W.Chao
 * @since 2020-02-28
 */
@Repository
public interface UserAuthsMapper extends BaseMapper<UserAuths> {
	@Select("select user_id from tb_user_auths where credential = #{auth}")
	Integer selectUserIdByAuth(@Param("auth") String auth);
}
