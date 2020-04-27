package per.chao.lifeshow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import per.chao.lifeshow.entity.pojo.AdminAuths;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author W.Chao
 * @since 2020-02-28
 */
@Repository
public interface AdminAuthsMapper extends BaseMapper<AdminAuths> {
	@Select("select * from tb_admin_auths a where a.account = #{account}")
	AdminAuths selectByAccount(@Param("account") String account);

	@Select("select * from `tb_admin_auths` s where s.admin_id = #{adminId}")
	AdminAuths selectByAdminId(@Param("adminId") Integer adminId);
}
