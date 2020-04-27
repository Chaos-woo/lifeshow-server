package per.chao.lifeshow.exception;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/2/28 22:37
 **/
public class NotFoundException extends RuntimeException {
	public NotFoundException() {
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}
}
