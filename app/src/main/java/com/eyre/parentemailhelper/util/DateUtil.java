package com.eyre.parentemailhelper.util;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {

    public static DateTimeFormatter approvedDateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a").withLocale(Locale.ENGLISH);
}
