package processing;

import java.util.ArrayList;

import calculator.DTW;
import calculator.MFCC;
import view.MainScreen;

public class SoundAnalyzer {

	public ArrayList<Double> samples = new ArrayList<>();
	public ArrayList<Word> words = new ArrayList<>();
	public ArrayList<Window> windows = new ArrayList<>();
	public long frequency = 0;
	public double granica = 0;
	public double ZCTgranica = 0;

	int X = 6, Y = 12, Z = 25;

	public void analyze() {
		findWords();
		for (Word w : words) { // radimo MFCC za svaku rec (bice samo jedna rec)
			double window = Double.parseDouble(MainScreen.get().MfccWindow.getText());
			double koefPerWin = Double.parseDouble(MainScreen.get().NumOfKoef.getText());
			MFCC.calculateMFCC(frequency, window, koefPerWin, w);

			ArrayList<double[]> lib = new ArrayList<>();
			for (Window win : w.windows) {
				lib.add(win.FilterBank);
			}
			
			double[][] FullBank = new double[lib.size()][lib.get(0).length];
			for (int i = 0; i < FullBank.length; i++)
				FullBank[i] = lib.get(i);

			w.FullBank = FullBank;
		}
	}

	public void search() {
		findWords();
		for (Word w : words) { // radimo MFCC za svaku rec (bice samo jedna rec)
			double window = Double.parseDouble(MainScreen.get().MfccWindow.getText());
			double koefPerWin = Double.parseDouble(MainScreen.get().NumOfKoef.getText());
			MFCC.calculateMFCC(frequency, window, koefPerWin, w);

			ArrayList<double[]> lib = new ArrayList<>();
			for (Window win : w.windows) {
				lib.add(win.FilterBank);
			}
			
			double[][] FullBank = new double[lib.size()][lib.get(0).length];
			for (int i = 0; i < FullBank.length; i++)
				FullBank[i] = lib.get(i);

			w.FullBank = FullBank;
		}
	}

	public void findWords() {
		words = new ArrayList<>();
		windows = new ArrayList<>();
		granica = 0;
		ZCTgranica = 0;

		granica = findGranica(0.5, 0, true);

		windows = generateWindowList(0.01);

		windows = smoothing(windows, X, Y, Z);

		// ZTC
		ZCTgranica = findZCTGranica(0.1);

		ZCTwindows(25);
		System.out.println(ListToStr(windows));

		collectWords();
		System.out.println("Words found: " + words.size());
	}

	public String ListToStr(ArrayList<Window> windows) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < windows.size(); i++)
			sb.append(windows.get(i).toString());

		return sb.toString();
	}

	public double findGranica(double ms, int startPos, boolean sum) {
		double srednjaVred = 0;
		double deviacija = 0;
		int N = (int) (frequency * ms);

		for (int i = startPos; i < startPos + N; i++) {
			srednjaVred += Math.abs(samples.get(i));
		}

		srednjaVred /= (double) N;

		if (sum == false)
			return srednjaVred;

		double e = 0;
		for (int i = 0; i < N; i++)
			e += Math.pow(Math.abs(samples.get(i)) - srednjaVred, 2);

		deviacija = Math.sqrt(e / (double) N);

		return srednjaVred + deviacija;
	}

	public ArrayList<Window> generateWindowList(double ms) {
		ArrayList<Window> windows = new ArrayList<>();

		int startPos = 0;
		int N10ms = (int) (frequency * ms);

		while ((startPos + N10ms) <= samples.size()) {
			if (granica < findGranica(ms, startPos, false))
				windows.add(new Window(1, samples, startPos, N10ms));
			else
				windows.add(new Window(0, samples, startPos, N10ms));

			startPos += N10ms;
		}

		return windows;
	}

	public ArrayList<Window> smoothing(ArrayList<Window> windows, int x, int y, int z) {
		int[] lista = new int[windows.size()];
		for (int i = 0; i < windows.size(); i++)
			lista[i] = windows.get(i).repr;

		// prvi prolaz
		for (int i = x; i < lista.length; i++) {
			boolean xNulls = false;
			for (int j = i - 1; j >= i - x; j--) {
				if (lista[j] == 1)
					xNulls = true;
			}

			if (lista[i] == 1 && lista[i - 1] == 0 && xNulls) { // Ako smo naišli na 1, i pre toga je bilo manje od X 0
				int[] tmp = lista;
				for (int k = i - 1;; k--) {
					if (tmp[k] == 0)
						tmp[k] = 1;
					else
						break;
				}

				boolean bA = true;
				boolean bB = true;
				int a = i;
				int b = i - 1;
				int duzina = 0;
				while (true) {
					if (a >= tmp.length)
						break;

					if (bA == false && bB == false)
						break;

					if (tmp[a] == 1 && bA) {
						duzina++;
						a++;
					} else
						bA = false;

					if (tmp[b] == 1 && bB) {
						duzina++;
						b--;
					} else
						bB = false;
				}
				if (duzina > y)
					lista = tmp;
			}
		}

		// drugi prolaz
		boolean counting = false;
		int counter = 0;
		lista[0] = 0;
		for (int i = 1; i < lista.length; i++) {
			if (counting && lista[i] == 1) {
				counter++;
				continue;
			}

			if (counting && lista[i] == 0) {
				if (counter < z) { // Ako naiđemo na niz 1 dužine manje od Z → te 1 spuštamo na 0.
					for (int j = i - 1; j >= i - counter; j--)
						lista[j] = 0;
				}
				counter = 0;
				counting = false;
			}

			if (lista[i] == 1 && lista[i - 1] == 0) {
				counting = true;
				counter++;
			}

		}

		for (int i = 0; i < lista.length; i++) {
			windows.get(i).repr = lista[i];
		}

		return windows;
	}

	public double findZCTGranica(double ms) {
		double srednjaVred = 0;
		double deviacija = 0;
		int N = (int) (frequency * ms);

		for (int i = 1; i < N; i++) {
			if (samples.get(i) < 0 && samples.get(i - 1) >= 0)
				srednjaVred++;
		}

		srednjaVred /= (double) N;

		double e = 0;
		for (int i = 0; i < N; i++)
			e += Math.pow(Math.abs(samples.get(i)) - srednjaVred, 2);

		deviacija = Math.sqrt(e / (double) N);

		return srednjaVred;// + 2 * deviacija;
	}

	public double ZCT(ArrayList<Double> list) {
		double srednjaVred = 0;

		for (int i = 1; i < list.size(); i++) {
			if (list.get(i) < 0 && list.get(i - 1) >= 0)
				srednjaVred++;
		}
		srednjaVred /= list.size();

		return srednjaVred;
	}

	public void ZCTwindows(int windowNum) {

		for (int i = 1; i < windows.size(); i++) {
			if (windows.get(i).repr == 1 && windows.get(i - 1).repr == 0) {// pre reci
				ArrayList<Double> l250 = new ArrayList<>();
				for (int j = i - windowNum; j < i && j > 0; j++) {
					for (Double d : windows.get(j).windowSamples)
						l250.add(d);
				}
				if (ZCT(l250) > ZCTgranica)
					for (int j = i - windowNum; j < i && j > 0; j++)
						windows.get(j).repr = 1;
			}

			if (windows.get(i).repr == 0 && windows.get(i - 1).repr == 1) {// posle reci
				ArrayList<Double> l250 = new ArrayList<>();
				for (int j = i; j < i + windowNum && j < windows.size(); j++) {
					for (Double d : windows.get(j).windowSamples)
						l250.add(d);
				}
				if (ZCT(l250) > ZCTgranica)
					for (int j = i; j < i + windowNum && j < windows.size(); j++)
						windows.get(j).repr = 1;
			}

		}

	}

	public void collectWords() {
		boolean speaking = false;
		Word word = new Word();
		for (Window w : windows) {
			if (w.repr == 1 && speaking == false) {
				speaking = true;
				word = new Word();
			}

			if (w.repr == 0 && speaking == true) {
				speaking = false;
				words.add(word);
			}

			if (speaking == true)
				word.Samples.addAll(w.windowSamples);
		}
	}
}
