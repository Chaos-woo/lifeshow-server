package per.chao.lifeshow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import per.chao.lifeshow.entity.pojo.Videos;
import per.chao.lifeshow.entity.vo.UserVideoVO;
import per.chao.lifeshow.entity.vo.UserWorksVO;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author W.Chao
 * @since 2020-02-28
 */
@Repository
public interface VideosMapper extends BaseMapper<Videos> {
	@Select("select v.id,v.video_title as title,v.cover_path as cover,v.created_at as createdAt,u.nickname as createdBy,vs.liked_count as likedCount,vs.shared_count as sharedCount,vs.favored_count as favoredCount,vs.comment_count as commentCount from tb_videos v,tb_video_stat vs,tb_user_info u where u.id=#{id} and (v.status_id=1 or v.status_id=2) and v.created_by=u.id and v.id=vs.video_id order by v.created_at asc limit #{pages},#{limits}")
	List<UserWorksVO> selectByUserId(@Param("id") Integer id, @Param("pages") Integer pages, @Param("limits") Integer limits);

	@Select("select count(*) from tb_videos v where v.created_by=#{id}")
	Integer countByUserId(@Param("id") Integer id);

	@Select("select v.id,v.video_title as title,v.cover_path as cover,v.created_at as createdAt,u.nickname as createdBy,vs.liked_count as likedCount,vs.shared_count as sharedCount,vs.favored_count as favoredCount,vs.comment_count as commentCount from tb_videos v,tb_video_stat vs,tb_user_info u where v.created_by=u.id and (v.status_id=1 or v.status_id=2) and v.id=vs.video_id and v.video_title like '%${keyword}%' order by v.created_at asc limit #{pages},#{limits}")
	List<UserWorksVO> selectByKeyword(@Param("keyword") String keyword, @Param("pages") Integer pages, @Param("limits") Integer limits);

	@Select("select count(*) from tb_videos v where v.video_title like '%${keyword}%'")
	Integer countByKeyword(@Param("keyword") String keyword);

	@Select("select v.id as videoId,v.video_title as title,v.cover_path as cover,v.video_desc as description,v.video_path as videoSrc,u.id as authorId,u.nickname as author,u.avatar as authorAvatar,a.activ_name as active, vs.liked_count as likedCount,vs.comment_count as commentCount,vs.favored_count favoredCount,vs.shared_count sharedCount from tb_videos v,tb_user_info u,tb_video_stat vs,tb_activs a where v.id=#{id} and (v.status_id=1 or v.status_id=2) and v.created_by=u.id and v.activ_id=a.id and vs.video_id=v.id")
	UserVideoVO selectOneVideo(@Param("id") Integer id);

	@Select("select v.id as videoId,v.video_title as title,v.cover_path as cover,v.video_desc as description,v.video_path as videoSrc,u.id as authorId,u.nickname as author,u.avatar as authorAvatar,a.activ_name as active, vs.liked_count as likedCount,vs.comment_count as commentCount,vs.favored_count favoredCount,vs.shared_count sharedCount from tb_videos v,tb_user_info u,tb_video_stat vs,tb_activs a where v.activ_id=#{id} and (v.status_id=1 or v.status_id=2) and (v.id >= ((select MAX(tb_videos.id) from tb_videos)-(select MIN(tb_videos.id) from tb_videos)) * RAND() + (select MIN(tb_videos.id)-1 from tb_videos)) and v.created_by=u.id and v.activ_id=a.id limit 1")
	UserVideoVO selectOneVideoByActiv(@Param("id") Integer activId);

	@Select("select * from tb_videos order by id asc limit 0,1")
	Videos first();

	@Select("select * from tb_videos order by id desc limit 0,1")
	Videos last();

	@Select("select v.id,v.video_title as title,v.cover_path as cover,v.created_at as createdAt,u.nickname as createdBy,vs.liked_count as likedCount,vs.shared_count as sharedCount,vs.favored_count as favoredCount,vs.comment_count as commentCount from tb_videos v,tb_video_stat vs,tb_user_info u where u.id=#{id} and (v.status_id=1 or v.status_id=2) and v.id=vs.video_id and v.id in (select video_id from tb_user_collected_videos usv where usv.user_id=#{id}) order by v.created_at asc limit #{pages},#{limits}")
	List<UserWorksVO> selectCollectedByUserId(@Param("id") Integer id, @Param("pages") Integer pages, @Param("limits") Integer limits);

	@Select("select v.id from tb_videos v where v.created_by=#{id}")
	List<Integer> selectIdsByUserId(@Param("id") Integer createdBy);
}
