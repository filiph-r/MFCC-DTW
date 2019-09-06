package view;

import java.io.File;
import java.util.ArrayList;

import actions.FileAnalyzeAction;
import actions.RecAnalyzerAction;
import actions.Recording;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import processing.Word;

public class MainScreen extends Form {

	private static MainScreen instance = null;

	public TabPane TP;
	public Tab Database;
	public Tab Search;

	public Button browse;
	public Label fileDir;
	public Button rec;
	public Label reclabel;

	public Button analyzeFile;
	public Button analyzeRecodring;

	// ------------------------------------------
	public Button Sbrowse;
	public Label SfileDir;
	public Button Srec;
	public Label Sreclabel;

	public Button SanalyzeFile;
	public Button SanalyzeRecodring;

	public ListView<String> Slist;
	public ObservableList<String> SwordsInList;
	public ArrayList<Word> Swords; // SVE RECI KOJE SMO NASLI
	// ------------------------------------------

	public ListView<String> list;
	public ObservableList<String> wordsInList;
	public ArrayList<Word> words;

	public ComboBox<String> comboBox;

	public Label KoefLabel;
	public TextField NumOfKoef;
	public Label MfccWinLabel;
	public TextField MfccWindow;

	public Label NameLabel;
	public TextField Name;

	public static MainScreen get() {
		if (instance == null)
			new MainScreen();
		return instance;
	}

	public MainScreen() {
		super("Prepoznavanje Govora Domaci3");
		instance = this;

		int sirina = 400;
		initVariables();

		this.setWidth(800);
		this.setHeight(500);
		this.setResizable(false);

		HBox Hpane = new HBox();
		Hpane.setStyle("-fx-background-color: #c1c1c1;" + "-fx-background-radius: 30 30 30 30;\r\n"
				+ "    -fx-border-radius: 30 30 30 30;");
		VBox Vpane1 = new VBox();
		VBox Vpane2 = new VBox();

		GridPane pane1 = new GridPane();
		pane1.setVgap(20);
		pane1.setHgap(20);
		pane1.setMaxWidth(sirina);
		pane1.setMaxHeight(200);
		pane1.setPadding(new Insets(40, 0, 0, 45));
		// pane1.setGridLinesVisible(true);

		pane1.add(browse, 0, 0);
		pane1.add(fileDir, 1, 0);
		pane1.add(rec, 0, 1);
		pane1.add(reclabel, 1, 1);
		pane1.add(comboBox, 0, 2);

		GridPane pane2 = new GridPane();
		pane2.setPadding(new Insets(0, 0, 0, 45));
		pane2.setVgap(20);
		pane2.setHgap(20);
		pane2.setMaxWidth(sirina);
		pane2.setMaxHeight(200);
		// pane2.setGridLinesVisible(true);

		pane2.add(KoefLabel, 0, 2);
		pane2.add(NumOfKoef, 1, 2);
		pane2.add(MfccWinLabel, 0, 3);
		pane2.add(MfccWindow, 1, 3);
		pane2.add(NameLabel, 0, 4);
		pane2.add(Name, 1, 4);
		pane2.add(analyzeFile, 0, 6);
		pane2.add(analyzeRecodring, 1, 6);

		Vpane2.setMinWidth(400);
		list.setMaxWidth(350);
		list.setMaxHeight(380);
		list.setMinHeight(380);

		Vpane2.setPadding(new Insets(40, 0, 0, 50));
		Vpane2.getChildren().add(list);

		Vpane1.getChildren().add(pane1);
		Vpane1.getChildren().add(pane2);
		Hpane.getChildren().add(Vpane1);
		Hpane.getChildren().add(Vpane2);

		Database = new Tab("Database");

		Hpane.setMaxWidth(795);
		Hpane.setMinWidth(795);
		Hpane.setMinHeight(435);
		Hpane.setMaxHeight(435);
		Database.setContent(Hpane);

		Search = new Tab("Search      ");

		HBox SHpane = createSearch();
		SHpane.setMaxWidth(795);
		SHpane.setMinWidth(795);
		SHpane.setMinHeight(435);
		SHpane.setMaxHeight(435);
		Search.setContent(SHpane);

		TP = new TabPane(Database, Search);
		TP.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		Pane p = new Pane();
		p.setStyle("-fx-background-color: #848484;");
		p.getChildren().add(TP);
		setPane(p);
	}

	private HBox createSearch() {
		int sirina = 400;
		HBox Hpane = new HBox();
		Hpane.setStyle("-fx-background-color: #c1c1c1;" + "-fx-background-radius: 30 30 30 30;\r\n"
				+ "    -fx-border-radius: 30 30 30 30;");
		VBox Vpane1 = new VBox();
		VBox Vpane2 = new VBox();

		GridPane pane1 = new GridPane();
		pane1.setVgap(20);
		pane1.setHgap(20);
		pane1.setMaxWidth(sirina);
		pane1.setMaxHeight(200);
		pane1.setPadding(new Insets(40, 0, 0, 45));
		// pane1.setGridLinesVisible(true);

		pane1.add(Sbrowse, 0, 0);
		pane1.add(SfileDir, 1, 0);
		pane1.add(Srec, 0, 1);
		pane1.add(Sreclabel, 1, 1);

		GridPane pane2 = new GridPane();
		pane2.setPadding(new Insets(0, 0, 0, 45));
		pane2.setVgap(20);
		pane2.setHgap(20);
		pane2.setMaxWidth(sirina);
		pane2.setMaxHeight(200);
		pane2.setMinWidth(415);
		// pane2.setGridLinesVisible(true);

		pane2.add(SanalyzeFile, 0, 6);
		pane2.add(SanalyzeRecodring, 1, 6);

		Vpane2.setMinWidth(400);
		Slist.setMaxWidth(350);
		Slist.setMaxHeight(380);
		Slist.setMinHeight(380);

		Vpane2.setPadding(new Insets(40, 0, 0, 0));
		Vpane2.getChildren().add(Slist);

		Vpane1.getChildren().add(pane1);
		Vpane1.getChildren().add(pane2);
		Hpane.getChildren().add(Vpane1);
		Hpane.getChildren().add(Vpane2);

		return Hpane;
	}

	private void initVariables() {
		browse = new Button("Browse");
		browse.setMinWidth(50);
		fileDir = new Label("No file chosen..");
		fileDir.setMinWidth(200);
		fileDir.setMaxWidth(200);
		rec = new Button("Rec");
		reclabel = new Label("No recording..");

		analyzeFile = new Button("Analyse File");
		analyzeFile.setMinWidth(110);
		analyzeRecodring = new Button("Analyse Recording");
		analyzeRecodring.setMaxSize(120, 20);

		// -------------------------------------------------------------------
		Sbrowse = new Button("Browse");
		Sbrowse.setMinWidth(50);
		SfileDir = new Label("No file chosen..");
		SfileDir.setMinWidth(200);
		SfileDir.setMaxWidth(200);
		Srec = new Button("Rec");
		Sreclabel = new Label("No recording..");

		SanalyzeFile = new Button("Analyse File");
		SanalyzeFile.setMinWidth(110);
		SanalyzeRecodring = new Button("Analyse Recording");
		SanalyzeRecodring.setMaxSize(120, 20);

		Slist = new ListView<String>();
		Swords = new ArrayList<>();
		SwordsInList = FXCollections.observableArrayList();
		Slist.setItems(SwordsInList);
		// --------------------------------------------------------------------

		list = new ListView<String>();
		words = new ArrayList<>();
		wordsInList = FXCollections.observableArrayList();
		list.setItems(wordsInList);

		ObservableList<String> options = FXCollections.observableArrayList("None", "Hanning", "Hamming");
		comboBox = new ComboBox<String>(options);
		comboBox.setValue("Hamming");
		comboBox.setMinWidth(100);

		KoefLabel = new Label("Num. koef. per window:");
		NumOfKoef = new TextField("12");
		NumOfKoef.setAlignment(Pos.CENTER);
		MfccWinLabel = new Label("MFCC window:");
		MfccWindow = new TextField("0.025");
		MfccWindow.setAlignment(Pos.CENTER);

		NameLabel = new Label("Name:");
		Name = new TextField("Name");
		Name.setAlignment(Pos.CENTER);

		final FileChooser fileChooser = new FileChooser();
		browse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(null);
				if (file != null) {
					fileDir.setText(file.getAbsolutePath());
				}
			}
		});

		analyzeFile.setOnAction(new FileAnalyzeAction());
		analyzeRecodring.setOnAction(new RecAnalyzerAction());
		rec.setOnAction(new Recording());

		Sbrowse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(null);
				if (file != null) {
					SfileDir.setText(file.getAbsolutePath());
				}
			}
		});

		SanalyzeFile.setOnAction(new FileAnalyzeAction());
		SanalyzeRecodring.setOnAction(new RecAnalyzerAction());
		Srec.setOnAction(new Recording());
	}

}
