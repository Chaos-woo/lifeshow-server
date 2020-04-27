package per.chao.lifeshow.exception;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/2/28 23:00
 **/
public class DataNotExistException extends RuntimeException {
	public DataNotExistException() {
	}

	public DataNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataNotExistException(Throwable cause) {
		super(cause);
	}
}
