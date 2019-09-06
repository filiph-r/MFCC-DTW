package actions;

import java.io.File;
import java.util.ArrayList;

import calculator.DTW;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import processing.FileAnalyzer;
import processing.WavFile;
import processing.Word;
import view.MainScreen;

public class FileAnalyzeAction implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent arg0) {
		String file;
		if (MainScreen.get().TP.getSelectionModel().getSelectedItem() == MainScreen.get().Database) {
			file = MainScreen.get().fileDir.getText();
			FileAnalyzer analyzer = new FileAnalyzer(file);
			new File("D:/Saved Words").mkdirs();

			for (Word word : analyzer.words) {
				word.name = MainScreen.get().Name.getText();

				saveWord(analyzer, word, "Word " + (MainScreen.get().wordsInList.size() + 1));

				MainScreen.get().wordsInList.add(word.toString());
				MainScreen.get().words.add(word);
			}

		} else {
			file = MainScreen.get().SfileDir.getText();
			FileAnalyzer analyzer = new FileAnalyzer(file);

			for (Word word : analyzer.words) {
				saveWord(analyzer, word, " SEARCH Word " + (MainScreen.get().SwordsInList.size() + 1));

				double min = Double.MAX_VALUE;
				String similar = "";
				ArrayList<Word> DatabaseWords = MainScreen.get().words;
				for (Word dWord : DatabaseWords) {

					DTW dtw = new DTW(word.FullBank, dWord.FullBank);
					System.out.println(word.toString() + " -> " + dWord.toString() + " = " +  dtw.getDistance());
					if (min > dtw.getDistance()) {
						min = dtw.getDistance();
						similar = dWord.toString();
					}
				}

				MainScreen.get().SwordsInList.add(similar);
			}
		}
	}

	public void saveWord(FileAnalyzer analyzer, Word word, String file) {
		try {
			int sampleRate = (int) analyzer.frequency;

			// Calculate the number of frames required for specified duration
			long numFrames = (long) word.Samples.size() + 1;

			// Create a wav file with the name specified as the first argument
			WavFile wavFile = WavFile.newWavFile(new File("D:/Saved Words/" + file + ".wav"), 1, numFrames, 16,
					sampleRate);

			// Create a buffer of 100 frames

			// Initialise a local frame counter
			long frameCounter = 0;

			// Loop until all frames written
			for (int i = 0; i < word.Samples.size(); i++) {
				double[][] buffer = new double[2][100];
				double tmp = (double) word.Samples.get(i);
				int toWrite = (int) tmp;
				buffer[0][0] = toWrite;
				wavFile.writeFrames(buffer, i, 1);
			}

			// Close the wavFile
			wavFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
