package processing;

public class RecordingAnalyzer extends SoundAnalyzer {

	public RecordingAnalyzer() {
		FileAnalyzer analyzer = new FileAnalyzer("RecordAudio.wav");
		this.frequency = analyzer.frequency;
		this.granica = analyzer.granica;
		this.samples = analyzer.samples;
		this.windows = analyzer.windows;
		this.words = analyzer.words;
		this.ZCTgranica = analyzer.granica;
		this.X = analyzer.X;
		this.Y = analyzer.Y;
		this.Z = analyzer.Z;
	}

}
