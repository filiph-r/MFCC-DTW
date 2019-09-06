package calculator;

import java.util.ArrayList;

public class Calculator {

	public static Complex[] fft(double[] x1, double[] y1) {
		ArrayList<Double> tmp = new ArrayList<>();
		for (int i = 0; i < x1.length; i++)
			tmp.add(x1[i]);

		while (!isPowerOfTwo(tmp.size())) {
			tmp.add(0.0);
		}

		Complex[] x = new Complex[tmp.size()];
		for (int i = 0; i < tmp.size(); i++) {
			x[i] = new Complex(tmp.get(i), 0.0);
		}

		Complex[] y = FFT.fft(x);

		return y;
	}

	public static void dft(double[] x1, double[] y1) {
		int dir = 1;
		int i, k;
		double arg;
		double cosarg, sinarg;
		int m = x1.length;

		double[] x2 = new double[m];
		double[] y2 = new double[m];

		for (i = 0; i < m; i++) {
			x2[i] = 0;
			y2[i] = 0;
			arg = -dir * 2.0 * 3.141592654 * (double) i / (double) m;

			for (k = 0; k < m; k++) {
				cosarg = Math.cos(k * arg);
				sinarg = Math.sin(k * arg);
				x2[i] += (x1[k] * cosarg - y1[k] * sinarg);
				y2[i] += (x1[k] * sinarg + y1[k] * cosarg);
			}
		}

		if (dir == 1) {
			for (i = 0; i < m; i++) {
				x1[i] = x2[i] / (double) m;
				y1[i] = y2[i] / (double) m;
			}
		} else {
			for (i = 0; i < m; i++) {
				x1[i] = x2[i];
				y1[i] = y2[i];
			}
		}

	}

	public static double[] calcMagnitude(double[] x, double[] y) {
		double[] z = new double[x.length];
		for (int i = 0; i < z.length; i++) {

			z[i] = Math.sqrt(x[i] * x[i] + y[i] * y[i]);
		}

		return z;
	}

	public static boolean isPowerOfTwo(int n) {
		boolean isPower = false;
		int temp = n;

		while (temp >= 2) {
			if (temp % 2 == 0) {
				isPower = true;

			} else {
				isPower = false;
				break;
			}
			temp = temp / 2;
		}
		return isPower;
	}

	public static double[] HammingWindow(double[] signal_in) {
		int size = signal_in.length;
		for (int i = 0; i < size; i++) {

			signal_in[i] = (signal_in[i] * (0.54 - 0.46 * Math.cos(2.0 * Math.PI * i / size - 1)));
		}
		return signal_in;
	}

	public static double[] HanningWindow(double[] signal_in) {

		int size = signal_in.length;
		for (int i = 0; i < size; i++) {

			signal_in[i] = (signal_in[i] * 0.5 * (1.0 - Math.cos(2.0 * Math.PI * i / size - 1)));
		}
		return signal_in;
	}

	public static double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
		int r1 = firstMatrix.length;
		int c1 = firstMatrix[0].length;
		int c2 = secondMatrix[0].length;

		double[][] product = new double[r1][c2];
		for (int i = 0; i < r1; i++) {
			for (int j = 0; j < c2; j++) {
				for (int k = 0; k < c1; k++) {
					product[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}

		return product;
	}

	public static double[][] inverz(double a[][]) {
		int n = a.length;
		double x[][] = new double[n][n];
		double b[][] = new double[n][n];
		int index[] = new int[n];
		for (int i = 0; i < n; ++i)
			b[i][i] = 1;

		// Transform the matrix into an upper triangle
		gaussian(a, index);

		// Update the matrix b[i][j] with the ratios stored
		for (int i = 0; i < n - 1; ++i)
			for (int j = i + 1; j < n; ++j)
				for (int k = 0; k < n; ++k)
					b[index[j]][k] -= a[index[j]][i] * b[index[i]][k];

		// Perform backward substitutions
		for (int i = 0; i < n; ++i) {
			x[n - 1][i] = b[index[n - 1]][i] / a[index[n - 1]][n - 1];
			for (int j = n - 2; j >= 0; --j) {
				x[j][i] = b[index[j]][i];
				for (int k = j + 1; k < n; ++k) {
					x[j][i] -= a[index[j]][k] * x[k][i];
				}
				x[j][i] /= a[index[j]][j];
			}
		}
		return x;
	}

	public static void gaussian(double a[][], int index[]) {
		int n = index.length;
		double c[] = new double[n];

		// Initialize the index
		for (int i = 0; i < n; ++i)
			index[i] = i;

		// Find the rescaling factors, one from each row
		for (int i = 0; i < n; ++i) {
			double c1 = 0;
			for (int j = 0; j < n; ++j) {
				double c0 = Math.abs(a[i][j]);
				if (c0 > c1)
					c1 = c0;
			}
			c[i] = c1;
		}

		// Search the pivoting element from each column
		int k = 0;
		for (int j = 0; j < n - 1; ++j) {
			double pi1 = 0;
			for (int i = j; i < n; ++i) {
				double pi0 = Math.abs(a[index[i]][j]);
				pi0 /= c[index[i]];
				if (pi0 > pi1) {
					pi1 = pi0;
					k = i;
				}
			}

			// Interchange rows according to the pivoting order
			int itmp = index[j];
			index[j] = index[k];
			index[k] = itmp;
			for (int i = j + 1; i < n; ++i) {
				double pj = a[index[i]][j] / a[index[j]][j];

				// Record pivoting ratios below the diagonal
				a[index[i]][j] = pj;

				// Modify other elements accordingly
				for (int l = j + 1; l < n; ++l)
					a[index[i]][l] -= pj * a[index[j]][l];
			}
		}
	}

}
