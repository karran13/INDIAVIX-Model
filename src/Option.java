import java.lang.Math;

public class Option {

	double strike;
	double bid;
	double ask;
	double spread;
	double mid;
	int knot;

	public Option(Double a, Double b, Double c) {
		strike = a;
		bid = b;
		ask = c;
		if (ask + bid != 0)
			spread = 2 * ((ask - bid) / (ask + bid)) * 100.00;
		else
			spread = 0;

		if (Math.abs(spread) < 30) {
			mid = (bid + ask) / 2;
			knot = 1;

		} else
			knot = 0;

	}

}
