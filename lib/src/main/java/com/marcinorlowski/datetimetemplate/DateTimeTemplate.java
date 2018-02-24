package com.marcinorlowski.datetimetemplate;

/*
 *********************************************************************************
 *
 * @author Marcin Orlowski <mail (@) marcinorlowski (.) com>
 *
 *********************************************************************************
*/

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;

public class DateTimeTemplate {

	// supported Placeholder
	enum Placeholder {
		yy, y,
		MMM, MM, M, mm, m,
		DDD, DD, D,
		dd, d, dy, dw,
		wm, wy,

		hh, h, kk, k,
		ii, i,

		AA, A, aa, a
	}

	/**
	 * Processed given formatting string replacing all known Placeholder with its values.
	 *
	 * @param cal    Calendar object to use as date/time source
	 * @param format Formatting string
	 *
	 * @return processed date/time string
	 */
	public static String format(Calendar cal, String format) {
		return format(cal, format, false);
	}

	/**
	 * Processed given formatting string replacing all known Placeholder with its values.
	 *
	 * @param cal          Calendar object to use as date/time source
	 * @param format       Formatting string
	 * @param forceEnglish enforces use of English language instead of default locale (whenever applicable)
	 *
	 * @return processed date/time string
	 */
	@SuppressWarnings ("WeakerAccess")
	public static String format(Calendar cal, String format, @SuppressWarnings ("SameParameterValue") boolean forceEnglish) {

		Locale locale = Locale.ENGLISH;

		// checks if formatter supports current system locale or we need to fallback to English
		// forces english in string Placeholder like %MMM%. Also works around HTC Desire bug on Froyo
		// or in general workaround for locale not supported by formatter
		if (!forceEnglish) {
			String currentLanguage = Locale.getDefault().getLanguage();

			Locale[] availableLocales = SimpleDateFormat.getAvailableLocales();
			for (Locale availableLocale : availableLocales) {
				if (availableLocale.getLanguage().equals(currentLanguage)) {
					locale = availableLocale;
					break;
				}
			}
		}

		return format(cal, format, locale);
	}

	/**
	 * Processed given formatting string replacing all known Placeholder with its values.
	 *
	 * @param cal    Calendar object to use as date/time source
	 * @param format Formatting string
	 * @param locale Locale to use for month, day names
	 *
	 * @return processed date/time string
	 */
	@SuppressWarnings ("WeakerAccess")
	public static String format(Calendar cal, String format, Locale locale) {

		EnumMap<Placeholder, String> map = new EnumMap<>(Placeholder.class);

		Date date = new java.util.Date(cal.getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("", locale);
		formatter.setTimeZone(cal.getTimeZone());

		// this valid for 2012 only, where 1st is on sunday
		// some tweaks to make WY work correctly and show "1" instead of "52" as used in 2012
		// http://stackoverflow.com/questions/7299621/android-calendar-problem-with-day-of-the-week
		Calendar calendar = formatter.getCalendar();
		calendar.setMinimalDaysInFirstWeek(1);
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


		// %Y% - long year (2010)
		// %y% - short year (10)
		map.put(Placeholder.yy, Integer.toString(cal.get(Calendar.YEAR)));
		map.put(Placeholder.y, map.get(Placeholder.yy).substring(2));

		// %MMM% - long month name (January)
		// %MM%	 - abbreviated month name (Jan)
		// %M%	 - first letter of month name (J)
		formatter.applyLocalizedPattern("MMMM");
		map.put(Placeholder.MMM, formatter.format(date));

		formatter.applyLocalizedPattern("MMM");
		map.put(Placeholder.MM, formatter.format(date));
		map.put(Placeholder.M, map.get(Placeholder.MM).substring(0, 1));

		// %mm%	- zero prefixed 2 digit month number (02 for Feb, 12 for Dec)
		// %m%	- month number as is (2 for Feb, 12 for Dec)
		formatter.applyLocalizedPattern("MM");
		map.put(Placeholder.mm, formatter.format(date));

		formatter.applyLocalizedPattern("M");
		map.put(Placeholder.m, formatter.format(date));


		// %DDD%	- full day name (Saturday, Monday etc)
		// %DD%		- abbreviated day name (Sat, Mon)
		// %D%		- one letter, abbreviated day name (S, M)
		formatter.applyLocalizedPattern("EEEE");
		map.put(Placeholder.DDD, formatter.format(date));

		formatter.applyLocalizedPattern("EEE");
		String abrvDayName = formatter.format(date);
		map.put(Placeholder.DD, abrvDayName);
		map.put(Placeholder.D, abrvDayName.substring(0, 1).toUpperCase());

		// %dd%		- zero prefixed 2 digit day number (01 for 1st, 27 for 27th)
		// %d%		- day number as is (1 for 1st, 27 for 27th)
		// %dy%		- day number of the year (i.e. 250)
		// %dw%		- day number in week (i.e. 1 for Monday if weeks start on Mondays)
		formatter.applyLocalizedPattern("dd");
		map.put(Placeholder.dd, formatter.format(date));

		formatter.applyLocalizedPattern("d");
		map.put(Placeholder.d, formatter.format(date));

		formatter.applyLocalizedPattern("D");
		map.put(Placeholder.dy, formatter.format(date));

		map.put(Placeholder.dw, Integer.toString(cal.get(Calendar.DAY_OF_WEEK)));


		// %wm%		- week number of current month (3 for 3rd week)
		// %wy%		- week number of the year (3 for 3rd week, 47 for 47th)
		formatter.applyLocalizedPattern("W");
		map.put(Placeholder.wm, formatter.format(date));

		formatter.applyLocalizedPattern("w");
		map.put(Placeholder.wy, formatter.format(date));


		// %hh%		- current hour, zero prefixed, 24hrs clock (i.e. "01", "16")
		// %h%		- current hour, 24hrs clock (i.e. "1", "16")
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		map.put(Placeholder.hh, (hour < 10) ? "0" + hour : Integer.toString(hour));
		map.put(Placeholder.h, Integer.toString(hour));

		// %kk%		- current hour, zero prefixed, 12hrs clock (i.e. "01", "11")
		// %k%		- current hour, 12hrs clock (i.e. "1", "11")
		hour = cal.get(Calendar.HOUR);
		if (hour == 0) {
			hour = 12;
		}
		map.put(Placeholder.kk, (hour < 10) ? "0" + hour : Integer.toString(hour));
		map.put(Placeholder.k, Integer.toString(hour));

		// %ii%		- current minute, zero prefixed (i.e. "01", "35")
		// %i%		- current minute, zero prefixed (i.e. "1", "35")
		int minute = cal.get(Calendar.MINUTE);
		map.put(Placeholder.ii, (minute < 10) ? "0" + minute : Integer.toString(minute));
		map.put(Placeholder.i, Integer.toString(minute));

		// %AA%		- upper-cased AM/PM marker (i.e. "AM")
		// %A%		- upper-cased abbreviated AM/PM marker. "A" for "AM", "P" for "PM"
		// %aa%		- lower-cased am/pm marker (i.e. "am")
		// %a%		- lower-cased abbreviated AM/PM marker. "a" for "am", "p" for "pm"
		int ampm = cal.get(Calendar.AM_PM);
		map.put(Placeholder.AA, (ampm == Calendar.AM) ? "AM" : "PM");
		map.put(Placeholder.A, (ampm == Calendar.AM) ? "A" : "P");
		map.put(Placeholder.aa, map.get(Placeholder.AA).toLowerCase());
		map.put(Placeholder.a, map.get(Placeholder.A).toLowerCase());


		// placeholder substitution...
		String pattern;
		String result = format;
		for (Placeholder placeholderKey : Placeholder.values()) {
			pattern = "%" + placeholderKey.name() + "%";
			result = result.replaceAll(pattern, map.get(placeholderKey));
		}

		// done
		return result;
	}
}
