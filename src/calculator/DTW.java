package calculator;

public class DTW {

	int K;
	double wDistance;
	int[][] warpingPath;
	double[][] wo1;
	double[][] wo2;

	public DTW(double[][] word1, double[][] word2) {
		wDistance = 0.0;
		K = 1;
		wo1 = word1;
		wo2 = word2;

		warpingPath = new int[wo1.length + wo2.length][2];
		process();
	}

	public void process() {
		int visina = wo1.length;
		int sirina = wo2.length;

		double distance = 0.0;

		double[][] lokal = new double[visina][sirina];
		double[][] global = new double[visina][sirina];

		for (int i = 0; i < visina; i++) {
			for (int j = 0; j < sirina; j++) {
				lokal[i][j] = lengthBetween(wo1[i], wo2[j]);
			}
		}

		global[0][0] = lokal[0][0];

		for (int i = 1; i < visina; i++) {
			global[i][0] = lokal[i][0] + global[i - 1][0];
		}

		for (int j = 1; j < sirina; j++) {
			global[0][j] = lokal[0][j] + global[0][j - 1];
		}

		for (int i = 1; i < visina; i++) {
			for (int j = 1; j < sirina; j++) {
				distance = Math.min(Math.min(global[i - 1][j], global[i - 1][j - 1]), global[i][j - 1]);
				distance += lokal[i][j];
				global[i][j] = distance;
			}
		}
		distance = global[visina - 1][sirina - 1];

		int i = visina - 1;
		int j = sirina - 1;
		int minIndex = 1;

		warpingPath[K - 1][0] = i;
		warpingPath[K - 1][1] = j;

		while ((j + i) != 0) {
			if (i == 0) {
				j -= 1;
			} else if (j == 0) {
				i -= 1;
			} else {
				double[] array = { global[i - 1][j], global[i][j - 1], global[i - 1][j - 1] };
				minIndex = this.getIndexOfMinimum(array);

				if (minIndex == 0) {
					i -= 1;
				} else if (minIndex == 1) {
					j -= 1;
				} else if (minIndex == 2) {
					i -= 1;
					j -= 1;
				}
			}
			K += 1;
			warpingPath[K - 1][0] = i;
			warpingPath[K - 1][1] = j;
		}
		wDistance = distance / K;
		reverse();
	}

	protected void reverse() {
		int[][] path = warpingPath;
		int[][] newP = new int[K][2];
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < 2; j++) {
				newP[i][j] = path[K - i - 1][j];
			}
		}
		warpingPath = newP;
	}

	public double lengthBetween(double[] p1, double[] p2) {
		double rez = 0;
		for(int i = 0; i <p1.length; i++)
			rez += (p1[i] - p2[i]) * (p1[i] - p2[i]);
		
		return Math.sqrt(rez);
	}

	public int getIndexOfMinimum(double[] a) {
		int index = 0;
		double min = a[0];

		for (int i = 1; i < a.length; i++) {
			if (a[i] < min) {
				min = a[i];
				index = i;
			}
		}
		return index;
	}

	public double getDistance() {
		return wDistance;
	}

}
