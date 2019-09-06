package processing;

import view.MainScreen;

public class FileAnalyzer extends SoundAnalyzer {

	public FileAnalyzer(String file) {
		WavFileReader reader = null;
		try {
			reader = new WavFileReader(file);
		} catch (Exception e) {
			System.out.println("greska prilikom citanja wav fajla");
			return;
		}
		samples = reader.getFrames();
		frequency = reader.getSampleRate();
		// System.out.println("Number of chanels: " + reader.getNumChannels());

		if (MainScreen.get().TP.getSelectionModel().getSelectedItem() == MainScreen.get().Database) {
			analyze();
		} else {
			search();
		}
	}

}
