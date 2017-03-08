package com.marcinorlowski.datetimetemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;

public class DateTimeTemplate {

	// supported pulldown view template placeholders
	enum placeholders {
		yy, y,
		MMM, MM, M, mm, m,
		DDD, DD, D,
		dd, d, dy, dw,
		wm, wy,

		hh, h, kk, k,
		ii, i,

		AA, A, aa, a
	}

	public static String format(Calendar c, String format) {
		return format(c, format, false);
	}

	public static String format(Calendar cal, String template, Boolean forceEnglish) {

		Date date = new java.util.Date(cal.getTimeInMillis());

		// http://en.wikipedia.org/wiki/ISO_week_date
		//
		// extended view template supported placeholders:
		//
		// %Y% 		- long year (2010)
		// %y% 		- short year (10)
		// %MMM%	- long month name (January)
		// %MM%		- abbreviated month name (Jan)
		// %M%		- first letter of month name (J)
		// %mm%		- zero prefixed 2 digit month number (02 for Feb but 12 for Dec)
		// %m%		- month number as is (2 for Feb, 12 for Dec)
		// %DDD%	- full day name (Saturday, Monday etc)
		// %DD%		- abbreviated day name (Sat, Mon)
		// %D%		- one letter day name (S, M)
		// %dd%		- zero prefixed 2 digit day number (01 for 1st, 27 for 27th)
		// %d%		- day number as is (1 for 1st, 27 for 27th)
		// %dy%		- day number of the year (i.e. 250)
		// %dw%		- day number in week (i.e. 1 for Monday if weeks start on Mondays)
		// %wm%		- week number of current month (3 for 3rd week)
		// %wy%		- week number of the year (3 for 3rd week, 47 for 47th)
		//
		// %hh%		- current hour, zero prefixed, 24hrs clock (i.e. "01", "16")
		// %h%		- current hour, 24hrs clock (i.e. "1", "16")
		// %kk%		- current hour, zero prefixed, 12hrs clock (i.e. "01", "11")
		// %k%		- current hour, 12hrs clock (i.e. "1", "11")
		// %ii%		- current minute, zero prefixed (i.e. "01", "35")
		// %i%		- current minute, zero prefixed (i.e. "1", "35")
		// %AA%		- uppercased AM/PM marker (i.e. "AM")
		// %A%		- uppercased abbreviated AM/PM marker. "A" for "AM", "P" for "PM"
		// %aa%		- lowercased am/pm marker (i.e. "am")
		// %a%		- lowercased abbreviated AM/PM marker. "a" for "am", "p" for "pm"

		// other text remains unaltered, so you can mix placeholders with own text.
		// example:
		// Today is: %DDD%, %yy% %MMM% %dd%


		EnumMap<placeholders, String> map = new EnumMap<placeholders, String>(placeholders.class);

		String result = "";

		// checking if formatter supports system set locale.
		// If not, we fall back to english
		//
		// forces english in string placeholders like %MMMM%.
		// Also works around HTC Desire bug on Froyo or in general
		// workaround for locale not supported by formatter
		if (forceEnglish == false) {
			String currentLanguage = Locale.getDefault().getLanguage();

			Locale[] availableLocales = SimpleDateFormat.getAvailableLocales();
			Boolean isLanguageSupported = false;
			for (int i = 0; i < availableLocales.length; i++) {
				if (availableLocales[i].getLanguage().equals(currentLanguage)) {
					isLanguageSupported = true;
					break;
				}
			}

			if (isLanguageSupported == false) {
				forceEnglish = true;
			}
		}

		SimpleDateFormat formatter;
		if (forceEnglish == true) {
			formatter = new SimpleDateFormat("", Locale.ENGLISH);
		} else {
			formatter = new SimpleDateFormat();
		}

		// this valid for 2012 ONLY!!!!! where 1st is on sunday
		// FIXME FIXME FIXME
		// some tweaks to make WY work correctly and show "1" instead of "52" as used in 2012
		Calendar calendar = formatter.getCalendar();
//        WebnetLog.i( "getMinimalDaysInFirstWeek: " + calendar.getMinimalDaysInFirstWeek() );
//        WebnetLog.i( "getFirstDayOfWeek: " + calendar.getFirstDayOfWeek() );
		calendar.setMinimalDaysInFirstWeek(1);
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		// http://stackoverflow.com/questions/7299621/android-calendar-problem-with-day-of-the-week


		// preparing placeholders

		// %Y% 	- long year (2010)
		// %y% 	- short year (10)
		map.put(placeholders.yy, Integer.toString(cal.get(Calendar.YEAR)));
		map.put(placeholders.y, map.get(placeholders.yy).substring(2));

		// %MMM%	- long month name (January)
		// %MM%		- abbreviated month name (Jan)
		// %M%		- first letter of month name (J)
		formatter.applyLocalizedPattern("MMMM");
		map.put(placeholders.MMM, formatter.format(date));

		formatter.applyLocalizedPattern("MMM");
		map.put(placeholders.MM, formatter.format(date));
		map.put(placeholders.M, map.get(placeholders.MM).substring(0, 1));

		// %mm%		- zero prefixed 2 digit month number (02 for Feb but 12 for Dec)
		// %m%		- month number as is (2 for Feb, 12 for Dec)
		formatter.applyLocalizedPattern("mm");
		map.put(placeholders.mm, formatter.format(date));

		formatter.applyLocalizedPattern("M");
		map.put(placeholders.m, formatter.format(date));


		// %DDD%	- full day name (Saturday, Monday etc)
		// %DD%		- abbreviated day name (Sat, Mon)
		// %D%		- one letter, abbreviated day name (S, M)
		formatter.applyLocalizedPattern("EEEE");
		map.put(placeholders.DDD, formatter.format(date));

		formatter.applyLocalizedPattern("EEE");
		String abrvDayName = formatter.format(date);
		map.put(placeholders.DD, abrvDayName);
		map.put(placeholders.D, abrvDayName.substring(0, 1).toUpperCase());

		// %dd%		- zero prefixed 2 digit day number (01 for 1st, 27 for 27th)
		// %d%		- day number as is (1 for 1st, 27 for 27th)
		// %dy%		- day number of the year (i.e. 250)
		// %dw%		- day number in week (i.e. 1 for Monday if weeks start on Mondays)
		formatter.applyLocalizedPattern("dd");
		map.put(placeholders.dd, formatter.format(date));

		formatter.applyLocalizedPattern("d");
		map.put(placeholders.d, formatter.format(date));

		formatter.applyLocalizedPattern("D");
		map.put(placeholders.dy, formatter.format(date));

		map.put(placeholders.dw, Integer.toString(cal.get(Calendar.DAY_OF_WEEK)));


		// %wm%		- week number of current month (3 for 3rd week)
		// %wy%		- week number of the year (3 for 3rd week, 47 for 47th)
		formatter.applyLocalizedPattern("W");
		map.put(placeholders.wm, formatter.format(date));

		// FIXME: we need a fix here, not a workaroud.
		// It looks that "w" for Jan 1st, 2012 returns 53 instead of 1
		// Java docs does not say it's correct. We enforce 1 day
		// for 1st week though but 1.1.2011 is wrong w/o this workaroud
		formatter.applyLocalizedPattern("w");
		String currentWY = formatter.format(date);
//        Integer tmp = Integer.parseInt( currentWY );
//    	if( (calendar.get( Calendar.MONTH ) == Calendar.JANUARY ) && ( tmp > 5 ) ) {
//       		currentWY = "1";
//        }
		map.put(placeholders.wy, currentWY);


		// %hh%		- current hour, zero prefixed, 24hrs clock (i.e. "01", "16")
		// %h%		- current hour, 24hrs clock (i.e. "1", "16")
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		map.put(placeholders.hh, (hour < 10) ? "0" + hour : Integer.toString(hour));
		map.put(placeholders.h, Integer.toString(hour));

		// %kk%		- current hour, zero prefixed, 12hrs clock (i.e. "01", "11")
		// %k%		- current hour, 12hrs clock (i.e. "1", "11")
		hour = cal.get(Calendar.HOUR);
		map.put(placeholders.kk, (hour < 10) ? "0" + hour : Integer.toString(hour));
		map.put(placeholders.k, Integer.toString(hour));

		// %ii%		- current minute, zero prefixed (i.e. "01", "35")
		// %i%		- current minute, zero prefixed (i.e. "1", "35")
		int minute = cal.get(Calendar.MINUTE);
		map.put(placeholders.ii, (minute < 10) ? "0" + minute : Integer.toString(minute));
		map.put(placeholders.i, Integer.toString(minute));

		// %AA%		- uppercased AM/PM marker (i.e. "AM")
		// %A%		- uppercased abbreviated AM/PM marker. "A" for "AM", "P" for "PM"
		// %aa%		- lowercased am/pm marker (i.e. "am")
		// %a%		- lowercased abbreviated AM/PM marker. "a" for "am", "p" for "pm"
		int ampm = cal.get(Calendar.AM_PM);
		map.put(placeholders.AA, (ampm == Calendar.AM) ? "AM" : "PM");
		map.put(placeholders.A, (ampm == Calendar.AM) ? "A" : "P");

		map.put(placeholders.aa, map.get(placeholders.AA).toLowerCase());
		map.put(placeholders.a, map.get(placeholders.A).toLowerCase());


		// placeholder substitution...
		String pattern = "";

		result = template;
		for (placeholders placeholderKey : placeholders.values()) {
			pattern = "%" + placeholderKey.name() + "%";
			result = result.replaceAll(pattern, map.get(placeholderKey));
		}

		// done
		return result;
	}

}
