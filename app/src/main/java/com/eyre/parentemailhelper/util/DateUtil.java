package com.eyre.parentemailhelper.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    public static DateTimeFormatter approvedDateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a").withLocale(Locale.ENGLISH);

    public static Pattern datePattern = Pattern.compile("[0-9]{1,2}(st|nd|rd|th|)( | of )((j|J)an(uary)?|(f|F)eb(ruary)?|(m|M)ar(ch)?|(a|A)pr(il)?|(m|M)ay|(j|J)une?|(j|J)uly?|(a|A)ug(ust)?|(s|S)ep(t(ember)?)?|(o|O)ct(ober)?|(n|N)ov(ember)?|(d|D)ec(ember)?)( [0-9]{4})?");
    public static Pattern dayPattern = Pattern.compile("(on|this|next) ((m|M)on(day)?|(t|T)ue(s)?(day)?|(w|W)ed(nes)?(day)?|(t|T)hu(rs)?(day)?|(f|F)ri(day)?|(s|S)at(day)?|(s|S)un(day)?)");

    public static DateTimeFormatter dayLongMonthYear = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("d[d]['st']['nd']['rd']['th'][' of'] MMMM[ yyyy]")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter(Locale.ENGLISH);
    public static DateTimeFormatter dayShortMonthYear = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("d[d]['st']['nd']['rd']['th'][' of'] MMM[ yyyy]")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter(Locale.ENGLISH);
    public List<DateTimeFormatter> dateTimeFormatterList = new ArrayList<>();

    {
        dateTimeFormatterList.add(dayLongMonthYear);
        dateTimeFormatterList.add(dayShortMonthYear);
    }

}
