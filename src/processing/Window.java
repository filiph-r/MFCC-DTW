package processing;

import java.util.ArrayList;

public class Window {

	public int repr;
	public int N;
	public ArrayList<Double> windowSamples = new ArrayList<>();
	public double[] FilterBank; // ovde cuvamo cepstral koef
	public double[] z;

	public Window(ArrayList<Double> samples, int startPos, int N) {
		this.repr = -1;

		this.N = N;

		for (int i = startPos; i < startPos + N; i++) {
			windowSamples.add(samples.get(i));
		}

	}

	public Window(int repr, ArrayList<Double> samples, int startPos, int N) {
		this.repr = repr;
		this.N = N;

		for (int i = startPos; i < startPos + N; i++) {
			windowSamples.add(samples.get(i));
		}
	}

	@Override
	public String toString() {
		return repr + "";
	}

}
