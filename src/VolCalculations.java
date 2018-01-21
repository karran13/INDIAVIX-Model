
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.ArrayList;

import csvFileHandling.CSVReader;

public class VolCalculations {

	public static Double F = 0.00;// Default values changed on the basis of data
	public static Double R = 0.00;
	public static ArrayList<Option> option_data_main = new ArrayList<Option>();

	/**
	 * 
	 * 
	 *            Path containing the data Reads option data from files of
	 *            format FILENAME_DD-MM-YYYY Data needs to be ordered by Strike
	 *            Price Ordering of Data in-file is
	 *            "STRIKEPRICE,CALLBID,CALLASK,PUTBID,PUTASK,
	 *            NIFTY_FUTURES_LTP_FOR_EXPIRY_DATE,NSE_MIBOR_RATE_FOR
	 *            EXPIRY_DATE_TENURE" Data should contain One row of File headers
	 */
	public void readOption(String s) {

		try {
			Double obj1, obj2, obj3, obj5, obj6;
			obj2 = 0.00;
			obj3 = 0.00;
			obj5 = 0.00;
			obj6 = 0.00;
			int i = 0;
			CSVReader reader = new CSVReader(new FileReader(s), ',');
			String[] nextLine;
			nextLine = reader.readNext();
			while ((nextLine = reader.readNext()) != null) {
		//		if (Double.parseDouble(nextLine[0]) % 100 == 50) // remove the
																	// 50s

			//		continue;
				obj1 = Double.parseDouble(nextLine[0]);
			//	 if (obj1 <7300 || obj1 > 9000) // Set highly traded area
			//	 continue;
				F = Double.parseDouble(nextLine[5]);
				R = Double.parseDouble(nextLine[6]);
				if (obj1 > F) {
					obj2 = NumberFormat.getNumberInstance(java.util.Locale.US).parse(nextLine[1]).doubleValue();
					obj3 = NumberFormat.getNumberInstance(java.util.Locale.US).parse(nextLine[2]).doubleValue();
				} else {
					obj2 = NumberFormat.getNumberInstance(java.util.Locale.US).parse(nextLine[3]).doubleValue();
					obj3 = NumberFormat.getNumberInstance(java.util.Locale.US).parse(nextLine[4]).doubleValue();
					obj5 = NumberFormat.getNumberInstance(java.util.Locale.US).parse(nextLine[1]).doubleValue();
					obj6 = NumberFormat.getNumberInstance(java.util.Locale.US).parse(nextLine[2]).doubleValue();
					if (obj2 != 0 && obj3 != 0)
						i++;
				}
				if (obj3 == 0 || obj2 == 0)
					continue;
				option_data_main.add(new Option(obj1, obj2, obj3));
				// System.out.println(nextLine[0]);
			}
			if (i == 0) {
				option_data_main.get(i).knot = 3;
				option_data_main.get(
						i).mid = ((option_data_main.get(i).bid + option_data_main.get(i).ask) / 2 + (obj5 + obj6) / 2)
								/ 2;
			} else {
				option_data_main.get(i - 1).knot = 3;
				option_data_main
						.get(i - 1).mid = ((option_data_main.get(i - 1).bid + option_data_main.get(i - 1).ask) / 2
								+ (obj5 + obj6) / 2) / 2;
			}
			// Close object
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calculates Option Mid quote for non-knot points x is Strike price of
	 * option m, h are M,h as per VIX Paper O is arraylist of knot point options
	 */
	public double spline(Double x, Double m[], Double h[], ArrayList<Option> O) {

		Double a, b, c;
		int i = 0;
		m[0] = 0.00;
		while (O.get(i).strike < x) {
			i++;
			if (i == O.size() || i == O.size() - 1) { // M(O.size()-1),
														// basically the last
														// element isn't
														// defined, and M(i+1)
														// is being used.
				return -1;
			}
		}
		i--;
		if (i == -1 || m[i] == null)
			return -1;
		a = (m[i + 1] - m[i]) / (6 * h[i]);
		b = m[i] / 2;
		c = ((O.get(i + 1).mid - O.get(i).mid) / h[i]) - (2 * h[i] * m[i] + h[i] * m[i + 1]) / 6;
		double y = a * Math.pow(x - O.get(i).strike, 3) + b * Math.pow(x - O.get(i).strike, 2)
				+ c * (x - O.get(i).strike) + O.get(i).mid;
		return y;
	}

	/**
	 * Thomas algorithm solves system of equations with Tridiagonal Matrix form
	 * a,b,c represent constants of linear equation system, going from 1 to
	 * size-2
	 */
	public Double[] thomas(Double c[], Double d[], int size) {

		int n = size;
		Double[] a = new Double[n];
		Double[] b = new Double[n];
		Double id = 0.00;
		Double[] x = new Double[n];
		for (int k = 1; k < size - 1; k++) {
			b[k] = 2 * (c[k - 1] + c[k]);
			a[k + 1] = c[k];
		}
		a[1] = 0.00;
		c[1] = c[1] / b[1];
		d[1] = d[1] / b[1];
		for (int i = 2; i < n - 1; i++) {
			id = 1.0 / (b[i] - c[i - 1] * a[i]);
			c[i] = c[i] * id;
			d[i] = (d[i] - a[i] * d[i - 1]) * id;
		}
		x[n - 2] = d[n - 2] / b[n - 2];
		for (int k = n - 3; k > 0; k--)
			x[k] = (d[k] - c[k] * x[k + 1]);
		return x;

	}

	/**
	 * 
	 * @param O
	 *            Option Arraylist
	 * @return option arraylist containing just the knot points from O
	 */
	public ArrayList<Option> knotpoints(ArrayList<Option> O) {
		int i = 0;
		ArrayList<Option> O1 = new ArrayList<Option>();
		for (i = 0; i < O.size(); i++)
			if (O.get(i).knot == 1 || O.get(i).knot == 3) {
				O1.add(O.get(i));
			}
		return O1;
	}

	/**
	 * Driver function for finding mid quotes of non knot points calls thomas
	 * and spline functions Also removes those data points which do not have
	 * required sufficient knot points for calculation.
	 */
	public void fixbasicsoptions(ArrayList<Option> O)

	{
		Double[] h = new Double[O.size()];
		Double[] k = new Double[O.size()];
		Double[] j = new Double[O.size()];
		Double[] g = new Double[O.size()];
		Double[] b = new Double[O.size()];
		for (int i = 0; i < O.size() - 1; i++) {
			h[i] = O.get(i + 1).strike - O.get(i).strike;
			if (i > 0)
				k[i] = 6 * (((O.get(i + 1).mid - O.get(i).mid) / h[i])
						- ((O.get(i).mid - O.get(i - 1).mid) / h[i - 1]));
		}
		k[0] = 0.00;
		for (int u = 0; u < O.size() - 1; u++) {
			g[u] = h[u];
			b[u] = k[u];
		}
		j = thomas(g, b, O.size());
		for (int r = 0; r < option_data_main.size(); r++) {
			if (option_data_main.get(r).knot == 0) {
				option_data_main.get(r).mid = spline(option_data_main.get(r).strike, j, h, O);
				if (option_data_main.get(r).mid == -1) {

					// System.out.println("Removing data point "+r);
					option_data_main.remove(r);
					r--;
				}
			}
		}
	}

}
