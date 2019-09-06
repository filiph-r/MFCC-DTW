package processing;

import java.util.ArrayList;

public class Word {

	public ArrayList<Double> Samples = new ArrayList<>();
	public ArrayList<Window> windows = new ArrayList<>();
	public double[][] FullBank;
	public String name = "";

	@Override
	public String toString() {
		return name;
	}
}
