package lab9;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Task {
	double speed = 20;
	double workinghours = 8;
	final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

	Date date2 = null, flow = null;

	public Date getDateFromString(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM");
		Date result = null;
		try {
			result = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Date formatDate(String pattern, String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Date changeDateFormat(String pattern, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return formatDate(pattern, dateFormat.format(date));
	}

	public int checkHoliday(Date tem, Date flow) {
		String[] dates = { "Jan 26", "Aug 15", "May 01", "Jan 01" };
		for (String i : dates) {
			// System.out.println(tem + " " + i + " " + tem.equals(i));
			if (flow.toString().contains(i) || flow.toString().contains("Sun")) {
				return 1;
			}
		}
		return 0;
	}

	public Date predictAccurate(double dist, Date flow) {
		double milli = (dist * 1000) / ((((speed * 1000) / 60) / 60) / 1000);
		Date fin = new Date(flow.getTime() + (long) milli);
		if (checkHoliday(changeDateFormat("dd-mm", fin), fin) == 1) {
			fin = new Date(fin.getTime() + MILLIS_IN_A_DAY);
		}
		System.out.println("Expected Delivery time = " + fin);
		return fin;
	}

	@SuppressWarnings("deprecation")
	public Date predictDate(String date, String tym, double dist) {
		date2 = formatDate("dd-MM-yyyy hh:mm:ss", date + " " + tym);
		System.out.println("consignment from : " + date2);
		flow = date2;
		String compFormat = "dd-mm";
		Date tem = changeDateFormat(compFormat, flow);
		// dateFormat.parse(dateFormat.format(flow));
		int flag = checkHoliday(tem, flow);
		if (flag == 0) {
			if (flow.getHours() >= 16) {
				int value = ((((23 - flow.getHours()) * 60) * 60) + ((60 - flow.getMinutes()) * 60)
						+ (60 - flow.getSeconds())) * 1000;
				// System.out.println(value+" "+value/60+" "+(value/60)/60);
				flow = new Date(flow.getTime() + value);
				double dpm = ((((speed * 1000) / 60) / 60) / 1000) * value;
				// System.out.println(spm/1000);
				dist = dist - dpm / 1000;
			}
		}
		while (dist > 0) {
			tem = changeDateFormat(compFormat, flow);
			flag = checkHoliday(tem, flow);
			if (flag == 0) {
				if (dist - (speed * workinghours) > 0) {
					dist = dist - (speed * workinghours);
				} else {
					flow = predictAccurate(dist, flow);
					return flow;
				}
			}
			flow = new Date(flow.getTime() + MILLIS_IN_A_DAY);
			// flow=new Date("Wed Jan 20 19:00:00 IST 2021");
			System.out.println(dist + "km left   :   " + flow + " " + flag);
		}
		return flow;
	}

	public static String Check(String date, String tym, double dist) {

		Task o = new Task();

		return o.predictDate(date, tym, dist).toString();

	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.print("\nenter date(dd-mm-yyyy) : ");
		String date = sc.next();

		System.out.print("\nenter time (hh:mm:ss) : ");
		String tym = sc.next();

		System.out.print("\nenter Distance in km : ");
		double dist = sc.nextDouble();
		System.out.println(Check(date, tym, dist));
	}

}

//25-01-2021 20:59:59 1000