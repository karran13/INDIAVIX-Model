import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VolatilityDriver {
	// public static String DATA_PATH = "C:\\Users\\Binki\\Documents\\Stock
	// Markets and Trading\\VIX\\Data";
	// public static String NEW_PATH = "C:\\Users\\Binki\\Documents\\Stock
	// Markets and Trading\\VIX\\Test Data Final\\new1.csv";
	public static Double MINUTES_IN_MONTH = 43200.00;
	public static Double MINUTES_IN_YEAR = 525600.00;
	static VolCalculations func = new VolCalculations();
	

	public double drive(String s, int a, int b , int c) {
		Double[] t1 = new Double[2];
		int hrs=0;
		int mins=0;
		int secs=0;
		hrs=a;
		mins=b;
		secs=c;
		Double[] v = new Double[2];
		File f = new File(s);
		File[] folder = f.listFiles();
		String[] fnames = f.list();
		String delims = "[_.]";
		for (int i = 0; i < 2; i++) {
			String[] t = fnames[i].split(delims); // Takes Date from file
			t1[i] = timeshift(t[1],hrs,mins,secs) + 0.00176940639; // Constant value
														// accounts for time
														// from 00:00 to
														// 3:30PM on expiry
														// day.
			func.readOption(folder[i].getPath());
			func.fixbasicsoptions(func.knotpoints(VolCalculations.option_data_main));
			v[i] = weightsum(VolCalculations.option_data_main, t1[i], VolCalculations.R);
			VolCalculations.option_data_main.clear();
			// System.out.print(v[i]);
			// System.out.println("\n");
		}
		Double NT2 = t1[1] * MINUTES_IN_YEAR;
		Double NT1 = t1[0] * MINUTES_IN_YEAR;
	//	v[0]=(2*v[0]-Math.pow(((5129/5100)-1),2))/0.02466;	//accuracy test
	//	v[1]=0.070942;										//accuracy test
	//	NT1=12960.00;										//accuracy test	
	//	NT2=53280.00;										//accuracy test
	//	t1[0]=0.02466;										//accuracy test
	//	t1[1]=0.10137;										//accuracy test
		Double vfinal = t1[0] * v[0] * (NT2 - MINUTES_IN_MONTH) / (NT2 - NT1)
				+ t1[1] * v[1] * (MINUTES_IN_MONTH - NT1) / (NT2 - NT1);
		Double vfinal2 = Math.sqrt(vfinal * MINUTES_IN_YEAR / MINUTES_IN_MONTH);
		return vfinal2;	
	}

	/**
	 * calculates final sigma squared for the given time takes overall option
	 * data with mid quotes, NSE MIBOR Interest rate for the expiry and time to
	 * expiry as input
	 */
	private Double weightsum(ArrayList<Option> O, Double t, Double r) {
		Double[] x = new Double[O.size()];
		Double sum = 0.00;
		Double sum1 = 0.00;
		for (int i = 0; i < O.size() - 1; i++) {
			if (i == 0 || i == (O.size() - 2)) {
				x[i] = O.get(i + 1).strike - O.get(i).strike;
			} else
				x[i] = (O.get(i + 1).strike - O.get(i - 1).strike) / 2;

			sum = sum + (x[i] / Math.pow(O.get(i).strike, 2)) * O.get(i).mid;
			if (O.get(i).knot == 3)
				sum1 = Math.pow(((VolCalculations.F / O.get(i).strike) - 1), 2.00);
		}
		//return sum*Math.exp(.02466*0.039); //accuracy test data, from VIX Paper
		return (2 * sum * Math.exp(t * r) - sum1) / t;
	}

	/**
	 * Calculates time from current time to 00:00 on date of expiry
	 * 24 hour format
	 */
	private Double timeshift(String s, int hrs,int mins, int secs) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date d = new Date();
		Double t1;
		Date date;
		Long diff;
		try {
			d.setHours(hrs);
			d.setMinutes(mins);
			d.setSeconds(secs);
			date = formatter.parse(s);
			diff = date.getTime() - d.getTime();
			t1 = diff.doubleValue() / 60000;
			return t1 / 525600.00;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1.00;
	}

}
