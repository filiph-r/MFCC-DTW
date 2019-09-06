package calculator;

import java.util.ArrayList;
import java.util.Objects;

import processing.Window;
import processing.Word;
import view.MainScreen;

public class MFCC {

	public static void calculateMFCC(double sampleRate, double window, double koefPerWin, Word word) {
		int brSampl = (int) (sampleRate * window);
		while (!Calculator.isPowerOfTwo(brSampl))
			brSampl++;

		// Podeliti signal na kratke prozore
		ArrayList<Window> windows = new ArrayList<>();
		int start = 0;
		while (start + brSampl <= word.Samples.size()) {
			Window win = new Window(word.Samples, start, brSampl);
			windows.add(win);
			start += 160;// 10ms (160 samples) overlap
		}

		for (Window win : windows) {
			double[] bin = DFT(win);

			int numMelFilters = Integer.parseInt(MainScreen.get().NumOfKoef.getText());
			double T = brSampl / sampleRate;
			double lowerFilterFreq = 1 / T;
			double upperFilterFreq = sampleRate / 2.0;
			int samplesPerFrame = win.windowSamples.size();

			int cBin[] = indexiranje(numMelFilters, lowerFilterFreq, upperFilterFreq, sampleRate, samplesPerFrame);

			double fBank[] = melFilter(bin, cBin, numMelFilters);

			double f[] = logaritmovanje(fBank);

			win.FilterBank = DCTtransform(f);
			
			/*System.out.println("-------------");
			for(int i = 0 ; i < win.FilterBank.length; i++)
				System.out.println(win.FilterBank[i]);
			System.out.println("-------------");*/
		}

		word.windows = windows;// sve prozore cuvamo unutar reci
	}

	private static double[] DFT(Window window) {
		int brSampl = window.windowSamples.size();

		double[] x = new double[brSampl];
		double[] y = new double[brSampl];

		for (int i = 0; i < brSampl; i++) {
			x[i] = window.windowSamples.get(i);
			y[i] = 0;
		}

		if (MainScreen.get().comboBox.getValue().toString().equals("Hanning")) {
			x = Calculator.HanningWindow(x);
		}

		if (MainScreen.get().comboBox.getValue().toString().equals("Hamming")) {
			x = Calculator.HammingWindow(x);
		}

		int option = 2;

		if (option == 1)// DFT
			Calculator.dft(x, y);
		else { // FFT
			Complex[] k = Calculator.fft(x, y);
			x = new double[k.length];
			y = new double[k.length];

			for (int i = 0; i < k.length; i++) {
				x[i] = k[i].re();
				y[i] = k[i].im();
			}
		}

		double[] z = Calculator.calcMagnitude(x, y);

		double[] mag = new double[z.length / 2 + 1];
		for (int i = 1; i <= mag.length; i++)
			mag[i - 1] = z[i];

		return mag;
		//return z;
	}

	private static int[] indexiranje(int numMelFilters, double lowerFilterFreq, double upperFilterFreq,
			double sampleRate, int samplesPerFrame) {
		final int cBin[] = new int[numMelFilters + 2];
		cBin[0] = (int) Math.floor(lowerFilterFreq / sampleRate * samplesPerFrame);
		cBin[cBin.length - 1] = (samplesPerFrame / 2);
		for (int i = 1; i <= numMelFilters; i++) {
			final double fc = linearnoPlasiranje(i, lowerFilterFreq, upperFilterFreq, numMelFilters);
			cBin[i] = (int) Math.floor(fc / sampleRate * samplesPerFrame);
		}
		return cBin;
	}

	private static double linearnoPlasiranje(int i, double lowerFilterFreq, double upperFilterFreq, int numMelFilters) {
		final double melFLow = freqToMel(lowerFilterFreq);
		final double melFHigh = freqToMel(upperFilterFreq);
		final double temp = melFLow + ((melFHigh - melFLow) / (numMelFilters + 1)) * i;
		return melToFreq(temp);
	}

	private static double melToFreq(double x) {
		final double temp = Math.pow(Math.E, x / 1125) - 1;
		return 700 * (temp);
	}

	protected static double freqToMel(double freq) {
		return 1125 * Math.log(1 + freq / 700);
	}

	private static double[] melFilter(double bin[], int cBin[], int numMelFilters) {
		final double temp[] = new double[numMelFilters + 2];
		for (int k = 1; k <= numMelFilters; k++) {
			double br1 = 0.0, br2 = 0.0;
			for (int i = cBin[k - 1]; i <= cBin[k]; i++) {
				br1 += ((i - cBin[k - 1] + 1) / (cBin[k] - cBin[k - 1] + 1)) * bin[i];
			}

			for (int i = cBin[k] + 1; i <= cBin[k + 1]; i++) {
				br2 += (1 - ((i - cBin[k]) / (cBin[k + 1] - cBin[k] + 1))) * bin[i];
			}

			temp[k] = br1 + br2;
		}
		final double fBank[] = new double[numMelFilters];
		System.arraycopy(temp, 1, fBank, 0, numMelFilters); // shiftujemo za 1 unazad
		return fBank;
	}

	private static double[] logaritmovanje(double melFilBank[]) {
		double f[] = new double[melFilBank.length];
		final double minimum = -50;
		for (int i = 0; i < melFilBank.length; i++) {
			f[i] = Math.log(melFilBank[i]);
			if (f[i] < minimum) {
				f[i] = minimum;
			}
		}
		return f;
	}

	public static double[] DCTtransform(double[] vector) {
		Objects.requireNonNull(vector);
		double[] result = new double[vector.length];
		double factor = Math.PI / vector.length;
		for (int i = 0; i < vector.length; i++) {
			double sum = 0;
			for (int j = 0; j < vector.length; j++)
				sum += vector[j] * Math.cos((j + 0.5) * i * factor);
			result[i] = sum;
		}
		return result;
	}

}
