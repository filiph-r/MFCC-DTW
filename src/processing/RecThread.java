package processing;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import view.MainScreen;

public class RecThread extends Thread {
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	File wavFile = new File("RecordAudio.wav");
	TargetDataLine line;

	@Override
	public void run() {
		record();
	}

	public void record() {
		try {
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not suported");
				return;
			}

			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();

			AudioInputStream ais = new AudioInputStream(line);
			AudioSystem.write(ais, fileType, wavFile);
			System.out.println("rec.wav saved");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void finish() {
		line.stop();
		line.close();
	}

	AudioFormat getAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		return format;
	}

}
