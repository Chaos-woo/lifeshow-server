package per.chao.lifeshow.utils.m4autildependency;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/5 10:37
 **/
public class PositionInputStream extends FilterInputStream {
	private long position;
	private long positionMark;

	public PositionInputStream(InputStream delegate) {
		this(delegate, 0L);
	}

	public PositionInputStream(InputStream delegate, long position) {
		super(delegate);
		this.position = position;
	}

	@Override
	public synchronized void mark(int readlimit) {
		positionMark = position;
		super.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		super.reset();
		position = positionMark;
	}

	public int read() throws IOException {
		int data = super.read();
		if (data >= 0) {
			position++;
		}
		return data;
	}

	public int read(byte[] b, int off, int len) throws IOException {
		long p = position;
		int read = super.read(b, off, len);
		if (read > 0) {
			position = p + read;
		}
		return read;
	}

	@Override
	public final int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	public long skip(long n) throws IOException {
		long p = position;
		long skipped = super.skip(n);
		position = p + skipped;
		return skipped;
	}

	public long getPosition() {
		return position;
	}
}
