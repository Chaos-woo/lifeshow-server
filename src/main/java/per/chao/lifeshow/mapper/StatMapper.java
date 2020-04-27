package per.chao.lifeshow.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/27 9:56
 **/
@Repository
public interface StatMapper {
	@Select("select count(id) from tb_videos where created_at between #{start} and #{end}")
	Integer countVideosBetweenDate(@Param("start") Long start, @Param("end") Long end);

	@Select("select count(id) from tb_user_info where registered_date between #{start} and #{end}")
	Integer countUsersBetweenDate(@Param("start") Long start, @Param("end") Long end);
}
