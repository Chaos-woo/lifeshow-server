package per.chao.lifeshow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import per.chao.lifeshow.entity.pojo.Comments;
import per.chao.lifeshow.entity.vo.UserCommentVO;

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
public interface CommentsMapper extends BaseMapper<Comments> {
	@Select("select c.video_id as videoId,c.content as content,u.nickname as author,u.avatar as authorAvatar, v.cover_path as videoCover from tb_videos v,tb_user_info u,tb_comments c where c.video_id=#{id} and c.video_id=v.id and c.created_by=u.id order by c.created_at desc limit #{pages},#{limits}")
	List<UserCommentVO> selectCommentByVideoId(@Param("id") Integer id, @Param("pages") Integer pages, @Param("limits") Integer limits);

	@Select("select c.video_id as videoId,c.content as content,u.nickname as author,u.avatar as authorAvatar, v.cover_path as videoCover from tb_videos v,tb_user_info u,tb_comments c where c.created_by=#{id} and c.created_by=u.id and c.video_id=v.id order by c.created_at desc limit #{pages},#{limits}")
	List<UserCommentVO> selectCommentByUserId(@Param("id") Integer id, @Param("pages") Integer pages, @Param("limits") Integer limits);
}
