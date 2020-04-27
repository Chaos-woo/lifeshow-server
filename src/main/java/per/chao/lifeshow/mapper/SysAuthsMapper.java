package per.chao.lifeshow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import per.chao.lifeshow.entity.pojo.SysAuths;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author W.Chao
 * @since 2020-02-28
 */
@Repository
public interface SysAuthsMapper extends BaseMapper<SysAuths> {
	@Select("select s.id,s.name from tb_admin_info a, tb_sys_auths s where a.id = #{adminId} and a.sys_auth_id = s.id")
	SysAuths selectSysAuthByAdminId(@Param("adminId") Integer adminId);
}
