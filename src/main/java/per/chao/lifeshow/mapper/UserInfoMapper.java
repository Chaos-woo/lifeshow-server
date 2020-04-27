package per.chao.lifeshow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import per.chao.lifeshow.entity.pojo.UserInfo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author W.Chao
 * @since 2020-02-28
 */
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
	@Select("select id from tb_user_info where openid = #{openid}")
	Integer selectIdByOpenid(@Param("openid") String openid);

	@Select("select * from tb_user_info where openid = #{openid}")
	UserInfo selectByOpenid(@Param("openid") String openid);
}
