package processing;

import java.io.File;
import java.util.ArrayList;

public class WavFileReader {

	private ArrayList<Double> frames;
	private long sampleRate;
	private int numChannels;
	private int bytesPerSample;

	public WavFileReader(String input) throws Exception {
		frames = new ArrayList<>();
		sampleRate = 0;

		WavFile wavFile = WavFile.openWavFile(new File(input));
		numChannels = wavFile.getNumChannels();
		sampleRate = wavFile.getSampleRate();
		bytesPerSample = wavFile.getBytsPerSample();

		int[] buffer = new int[100 * numChannels];
		int framesRead;

		do {
			// Read frames into buffer
			framesRead = wavFile.readFrames(buffer, 100);

			for (int s = 0; s < framesRead * numChannels; s++) {
				frames.add((double) buffer[s]);
			}
		} while (framesRead != 0);

		wavFile.close();

	}

	public ArrayList<Double> getFrames() {
		return frames;
	}

	public long getSampleRate() {
		return sampleRate;
	}

	public int getNumChannels() {
		return numChannels;
	}

	public int getBytesPerSample() {
		return bytesPerSample;
	}

}
