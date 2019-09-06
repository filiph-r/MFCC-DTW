package actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import processing.RecThread;
import view.MainScreen;

public class Recording implements EventHandler<ActionEvent> {
	boolean rec = false;
	RecThread thread;

	@Override
	public void handle(ActionEvent arg0) {
		rec = !rec;

		if (rec) {
			MainScreen.get().analyzeFile.setDisable(true);
			MainScreen.get().analyzeRecodring.setDisable(true);
			if (MainScreen.get().TP.getSelectionModel().getSelectedItem() == MainScreen.get().Database) {
				MainScreen.get().rec.setText("Stop");
			} else {
				MainScreen.get().Srec.setText("Stop");
			}
			MainScreen.get().reclabel.setText("Recording...");
			thread = new RecThread();
			thread.start();

		} else {
			MainScreen.get().analyzeFile.setDisable(false);
			MainScreen.get().analyzeRecodring.setDisable(false);
			if (MainScreen.get().TP.getSelectionModel().getSelectedItem() == MainScreen.get().Database) {
				MainScreen.get().rec.setText("Rec");
			} else {
				MainScreen.get().Srec.setText("Rec");
			}
			thread.finish();
			thread.stop();
			MainScreen.get().reclabel.setText("Recording saved!");
		}

	}
}
