package per.chao.lifeshow.service;

import java.util.LinkedList;
import java.util.List;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/26 23:53
 **/
public interface IStatService {
	List<LinkedList<String>> music();

	List<LinkedList<String>> users();

	List<LinkedList<String>> videos();

	List<LinkedList<String>> user();

	List<LinkedList<String>> video();

	void incrAccess(long value);
}
