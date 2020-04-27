package per.chao.lifeshow.utils.m4autildependency;

/**
 * Description:
 *
 * @author W.Chao
 * @date 2020/4/5 10:35
 **/
public abstract class AudioInfo {
	protected String brand;			// brand, e.g. "M4A", "ID3", ...
	protected String version;		// version, e.g. "0", "2.3.0", ...

	protected long duration;		// track duration (milliseconds)

	protected String title;			// track title
	protected String artist;		// track artist
	protected String albumArtist;	// album artist
	protected String album;			// album title
	protected short year;			// year...
	protected String genre;			// genre name
	protected String comment;		// comment...
	protected short track;			// track number
	protected short tracks;			// number of tracks
	protected short disc;			// disc number
	protected short discs;			// number of discs
	protected String copyright;		// copyright notice
	protected String composer;		// composer name
	protected String grouping;		// track grouping
	protected boolean compilation;	// compilation flag
	protected String lyrics;		// song lyrics
	protected byte[] cover;			// cover image data

	public String getBrand() {
		return brand;
	}

	public String getVersion() {
		return version;
	}

	public long getDuration() {
		return duration;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getAlbumArtist() {
		return albumArtist;
	}

	public String getAlbum() {
		return album;
	}

	public short getYear() {
		return year;
	}

	public String getGenre() {
		return genre;
	}

	public String getComment() {
		return comment;
	}

	public short getTrack() {
		return track;
	}

	public short getTracks() {
		return tracks;
	}

	public short getDisc() {
		return disc;
	}

	public short getDiscs() {
		return discs;
	}

	public String getCopyright() {
		return copyright;
	}

	public String getComposer() {
		return composer;
	}

	public String getGrouping() {
		return grouping;
	}

	public boolean isCompilation() {
		return compilation;
	}

	public String getLyrics() {
		return lyrics;
	}

	public byte[] getCover() {
		return cover;
	}
}
