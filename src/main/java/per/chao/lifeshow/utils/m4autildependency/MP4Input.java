package per.chao.lifeshow.utils.m4autildependency;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/5 10:36
 **/
public final class MP4Input extends MP4Box<PositionInputStream> {
	/**
	 * Create new MP4 input.
	 * @param delegate stream
	 */
	public MP4Input(InputStream delegate) {
		super(new PositionInputStream(delegate), null, "");
	}

	/**
	 * Search next child atom matching the type expression.
	 * @param expectedTypeExpression regular expression
	 * @return matching child atom
	 * @throws IOException IO exception
	 */
	public MP4Atom nextChildUpTo(String expectedTypeExpression) throws IOException {
		while (true) {
			MP4Atom atom = nextChild();
			if (atom.getType().matches(expectedTypeExpression)) {
				return atom;
			}
		}
	}

	public String toString() {
		return "mp4[pos=" + getPosition() + "]";
	}
}
