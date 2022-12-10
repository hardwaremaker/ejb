package com.lp.util.report;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;
import java.util.function.Function;

import com.lp.server.util.Validator;

public class KalenderRechner {
	private final Calendar calendar;
	private final ZoneId zoneId;
	
	public KalenderRechner() {
		calendar = Calendar.getInstance();
		zoneId = ZoneId.systemDefault();
	}
	
	public KalenderRechner(Timestamp timestamp) {
		Validator.notNull(timestamp, "timestamp");
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timestamp.getTime());
		calendar = c;
		zoneId = ZoneId.systemDefault();
	}
	
	public KalenderRechner(Timestamp timestamp, Locale locale) {
		Validator.notNull(timestamp, "timestamp");
		Validator.notNull(locale, "locale");
		
		Calendar c = Calendar.getInstance(locale);
		c.setTimeInMillis(timestamp.getTime());
		calendar = c;		
		zoneId = ZoneId.systemDefault();
	}
	
	public KalenderRechner(long timeInMillis) {
		Validator.notNull(timeInMillis, "timeInMillis");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeInMillis);
		calendar = c;
		zoneId = ZoneId.systemDefault();
	}

	public KalenderRechner(long timeInMillis, Locale locale) {
		Validator.notNull(timeInMillis, "timeInMillis");
		Validator.notNull(locale, "locale");
		Calendar c = Calendar.getInstance(locale);
		c.setTimeInMillis(timeInMillis);
		calendar = c;
		zoneId = ZoneId.systemDefault();
	}

	public KalenderRechner(KalenderRechner other) {
		Validator.notNull(other, "other");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(other.asCalendar().getTimeInMillis());
		calendar = c;
		zoneId = ZoneId.systemDefault();
	}
	
	public KalenderRechner(KalenderRechner other, Locale locale) {
		Validator.notNull(other, "other");
		Validator.notNull(locale, "locale");
		Calendar c = Calendar.getInstance(locale);
		c.setTimeInMillis(other.asCalendar().getTimeInMillis());
		calendar = c;
		zoneId = ZoneId.systemDefault();
	}

	public static KalenderRechner createFromTimeStamp(Timestamp timestamp) {
		Validator.notNull(timestamp, "timestamp");
		return new KalenderRechner(timestamp);
	}
	
	public static KalenderRechner createFromUtilDate(java.util.Date date) {
		Validator.notNull(date, "date");
		return new KalenderRechner(date.getTime());
	}
	
	public static KalenderRechner createFromSqlDate(java.sql.Date date) {
		Validator.notNull(date, "date");
		return new KalenderRechner(date.getTime());
	}
	
	/**
	 * Die Anzahl an Jahren addieren
	 * 
	 * @param value kann auch negativ sein
	 * @return
	 */
	public KalenderRechner addYear(int value) {
		calendar.add(Calendar.YEAR, value);
		return this;
	}
	
	/** 
	 * Die Anzahl an Monaten addieren
	 * <p>Durch die Addition/Subtraktion &auml;ndert 
	 * sich auch das Feld Jahr</p> 
	 * 
	 * @param value kann auch negativ sein
	 * @return
	 */
	public KalenderRechner addMonth(int value) {
		calendar.add(Calendar.MONTH, value);
		return this;
	}
	
	/** 
	 * Die Anzahl an Wochen addieren</br>
	 * <p>Durch die Addition/Subtraktion &auml;ndern 
	 * sich auch die Felder Monat und Jahr</p> 
	 * 
	 * @param value kann auch negativ sein
	 * @return
	 */
	public KalenderRechner addWeek(int value) {
		calendar.add(Calendar.WEEK_OF_YEAR, value);
		return this;
	}
	
	/** 
	 * Die Anzahl an Tagen addieren</br>
	 * <p>Durch die Addition/Subtraktion &auml;ndern 
	 * sich auch die Felder Monat und Jahr</p> 
	 * 
	 * @param value kann auch negativ
	 * @return
	 */
	public KalenderRechner addDay(int value) {
		calendar.add(Calendar.DAY_OF_MONTH, value);
		return this;
	}
	
	/** 
	 * Die Anzahl an Stunden addieren</br>
	 * <p>Der Tag wird als Tag mit 24h betrachtet</p>
	 * <p>Durch die Addition/Subtraktion &auml;ndern 
	 * sich auch die Felder Tag, Monat und Jahr</p> 
	 * 
	 * @param value kann auch negativ
	 * @return
	 */	
	public KalenderRechner addHour(int value) {
		calendar.add(Calendar.HOUR_OF_DAY, value);
		return this;
	}

	public KalenderRechner add12hHour(int value) {
		calendar.add(Calendar.HOUR, value);
		return this;
	}
	
	/** 
	 * Die Anzahl an Minuten addieren</br>
	 * <p>Durch die Addition/Subtraktion &auml;ndern 
	 * sich auch die Felder Stunden, Tag, Monat und Jahr</p> 
	 * 
	 * @param value kann auch negativ
	 * @return
	 */	
	public KalenderRechner addMinute(int value) {
		calendar.add(Calendar.MINUTE, value);
		return this;
	}
	
	/** 
	 * Die Anzahl an Sekunden addieren</br>
	 * <p>Durch die Addition/Subtraktion &auml;ndern 
	 * sich auch die Felder Minuten Stunden, Tag, Monat und Jahr</p> 
	 * 
	 * @param value kann auch negativ
	 * @return
	 */	
	public KalenderRechner addSecond(int value) {
		calendar.add(Calendar.SECOND, value);
		return this;
	}
	
	public KalenderRechner addMillisecond(int value) {
		calendar.add(Calendar.MILLISECOND, value);
		return this;
	}

	/**
	 * Alle Zeitfelder auf 0 setzen
	 * 
	 * @return
	 */
	public KalenderRechner clearTime() {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return this;
	}
	
	/**
	 * Das Jahr explizit setzen</br>
	 * <p>Andere Felder werden nicht beeinflusst.</p>
	 * @param year
	 * @return
	 */
	public KalenderRechner setYear(int year) {
		calendar.set(Calendar.YEAR, year);
		return this;
	}

	/**
	 * Den Monat setzen 1 ... 12
	 * @param value 1 ... 12
	 * @return
	 */
	public KalenderRechner setMonth(int value) {
		calendar.set(Calendar.MONTH, value - 1);
		return this;
	}
	
	/**
	 * Den Tag (1 - max 31) explizit setzen</br>
	 * <p>Ist der gew&uuml;nschte Tag f&uuml;r den Monat nicht
	 * verf&uuml;gbar, wird auf den folgenden Monat weitergerechnet</p>
	 * @param year
	 * @return
	 */
	public KalenderRechner setDay(int value) {
		calendar.set(Calendar.DAY_OF_MONTH, value);
		return this;
	}
	
	/**
	 * Die Stunde im 24h Modus setzen (0 ... 23).</br>
	 * <p>Wird mehr als 23 angegeben, &auml;ndert das auch den Tag</p>
	 * 
	 * @param value
	 * @return
	 */
	public KalenderRechner setHour(int value) {
		calendar.set(Calendar.HOUR_OF_DAY, value);
		return this;
	}
	
	/**
	 * Die Minuten setzen (0 ... 59).</br>
	 * <p>Wird mehr als 59 angegeben, &auml;ndert das auch die Stunde</p>
	 * 
	 * @param value
	 * @return
	 */
	public KalenderRechner setMinute(int value) {
		calendar.set(Calendar.MINUTE, value);
		return this;
	}
	
	public KalenderRechner setSecond(int value) {
		calendar.set(Calendar.SECOND, value);
		return this;
	}
	
	public KalenderRechner setMillisecond(int value) {
		calendar.set(Calendar.MILLISECOND, value);
		return this;
	}

	/**
	 * Etwas direkt in Java machen. Die aufgerufene Funktion 
	 * muss ein KalenderRechner-Objekt zur&uuml;ckliefern</br>
	 * 
	 * @param f
	 * @return
	 */
	public KalenderRechner eval(
			Function<KalenderRechner, KalenderRechner> f) {
		return f.apply(this);
	}	
	
	/**
	 * Ermitteln, ob das Datum ein bestimmter Wochentag ist.
	 * Die Konstanten sind aus java.util.Calendar.SUNDAY..SATURDAY
	 * zu verwenden</p>
	 * @param calendarWeekday ist der gesuchte Wochentag in Calendar-Notation
	 * @return
	 */
	public boolean isWeekday(int calendarWeekday) {
		return calendar.get(Calendar.DAY_OF_WEEK) == calendarWeekday;	
	}
	
	public boolean isMonday() {
		return isWeekday(Calendar.MONDAY);
	}

	public boolean isTuesday() {
		return isWeekday(Calendar.TUESDAY);
	}

	public boolean isWednesday() {
		return isWeekday(Calendar.WEDNESDAY);
	}

	public boolean isThursday() {
		return isWeekday(Calendar.THURSDAY);
	}
	
	public boolean isFriday() {
		return isWeekday(Calendar.FRIDAY);
	}

	public boolean isSaturday() {
		return isWeekday(Calendar.SATURDAY);
	}

	public boolean isSunday() {
		return isWeekday(Calendar.SUNDAY);
	}

	/**
	 * Ist der gesuchte Tag ein Samstag bzw. Sonntag?
	 * 
	 * @return
	 */
	public boolean isWeekend() {
		return isSaturday() || isSunday();
	}

	/**
	 * Das Jahr ermitteln
	 */
	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * Den Monat ermitteln. Januar beginnt mit 1.
	 * 
	 * @return
	 */
	public int getMonth() {
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * Die Kalenderwoche ermitteln 1 -53</br>
	 * <p>Wie bei Kalenderwochen m&ouml;glich, kann ein Jahr zwei mal
	 * die gleiche Woche haben</p>
	 * @return
	 */
	public int getWeek() {
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	public int getDay() {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public int getHour() {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	public int get12Hour() {
		return calendar.get(Calendar.HOUR);
	}
	
	public int getAmPm() {
		return calendar.get(Calendar.AM_PM);
	}
	
	public int getMinute() {
		return calendar.get(Calendar.MINUTE);
	}
	
	public int getSecond() {
		return calendar.get(Calendar.SECOND);
	}
	
	public int getMillisecond() {
		return calendar.get(Calendar.MILLISECOND);
	}

	/**
	 * Den darunterliegenden "GregorianCalendar" ermitteln
	 * @return
	 */
	public Calendar asCalendar() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(calendar.getTimeInMillis());
		return c;
	}
	
	
	public Timestamp asTimestamp() {
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	public java.util.Date asUtilDate() {
		return new java.util.Date(calendar.getTimeInMillis());
	}
	
	public java.sql.Date asSqlDate() {
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	public long weeksBetween(KalenderRechner otherDate) {
		Validator.notNull(otherDate, "otherDate");
		return daysBetween(otherDate) / 7;
/*		
		return ChronoUnit.WEEKS.between(
				calendar.toInstant(), otherDate.calendar.toInstant());
*/				
	}
	
	public long daysBetween(KalenderRechner otherDate) {
		Validator.notNull(otherDate, "otherDate");
		
		LocalDateTime d0 = LocalDateTime.ofInstant(
				calendar.toInstant(), zoneId);
		LocalDateTime d1 = LocalDateTime.ofInstant(
				otherDate.calendar.toInstant(), zoneId);
		
//		long oldDifference = ChronoUnit.DAYS.between(
//				calendar.toInstant(), otherDate.calendar.toInstant());
		return ChronoUnit.DAYS.between(d0, d1);
	}

	public long hoursBetween(KalenderRechner otherDate) {
		Validator.notNull(otherDate, "otherDate");
		LocalDateTime d0 = LocalDateTime.ofInstant(
				calendar.toInstant(), zoneId);
		LocalDateTime d1 = LocalDateTime.ofInstant(
				otherDate.calendar.toInstant(), zoneId);
		
//		long oldDiff = ChronoUnit.HOURS.between(
//				calendar.toInstant(), otherDate.calendar.toInstant());

		return ChronoUnit.HOURS.between(d0, d1);
	}
	
	public long minutesBetween(KalenderRechner otherDate) {
		Validator.notNull(otherDate, "otherDate");
		LocalDateTime d0 = LocalDateTime.ofInstant(
				calendar.toInstant(), zoneId);
		LocalDateTime d1 = LocalDateTime.ofInstant(
				otherDate.calendar.toInstant(), zoneId);
		return ChronoUnit.HOURS.between(d0, d1);
//		return ChronoUnit.MINUTES.between(
//				calendar.toInstant(), otherDate.calendar.toInstant());
	}
}
