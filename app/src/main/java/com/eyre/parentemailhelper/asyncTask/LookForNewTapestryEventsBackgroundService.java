package com.eyre.parentemailhelper.asyncTask;

import static com.eyre.parentemailhelper.util.CalendarUtil.addEventToCalendar;
import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY;
import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY_BASE_PATH;
import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY_CHECKED;
import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY_CREDENTIALS;
import static com.eyre.parentemailhelper.util.DateUtil.approvedDateFormat;
import static com.eyre.parentemailhelper.util.DateUtil.datePattern;
import static com.eyre.parentemailhelper.util.DateUtil.dayPattern;
import static com.eyre.parentemailhelper.util.DisplayMessage.displayLong;
import static com.eyre.parentemailhelper.util.InternalStorage.read;
import static com.eyre.parentemailhelper.util.InternalStorage.write;
import static com.eyre.parentemailhelper.util.KeyStoreHelper.retrievePassword;
import static com.eyre.parentemailhelper.util.KeyStoreHelper.retrieveUsername;
import static com.eyre.parentemailhelper.util.Permissions.checkAndRequestPermissions;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eyre.parentemailhelper.pojo.CalenderEvent;
import com.eyre.parentemailhelper.pojo.Child;
import com.eyre.parentemailhelper.pojo.Paragraph;
import com.eyre.parentemailhelper.pojo.TapestryLoginCredentials;
import com.eyre.parentemailhelper.pojo.TapestrySession;
import com.eyre.parentemailhelper.util.DateUtil;
import com.eyre.parentemailhelper.util.RequestTapestry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;

public class LookForNewTapestryEventsBackgroundService {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    TapestrySession tapestrySession = TapestrySession.getInstance();

    private String error = "";
    private int progress = 0;

    private Context context;
    private ProgressBar progressBar;
    private TextView progressBarText;


    public void getNewTapestryEvents(Context context, ProgressBar progressBar, TextView progressBarText) {
        this.context = context;
        this.progressBar = progressBar;
        this.progressBarText = progressBarText;
        executor.execute(fullCheck);
    }

    public void getNewTapestryEvents(Context context) {
        this.context = context;
        executor.execute(fullCheck);
    }

    private Runnable fullCheck = new Runnable() {
        @Override
        public void run() {
            if(progressBar != null) {
                setVisibility(progressBar, progressBarText, View.VISIBLE);
            }
            int count = 0;
            if (tapestrySession.getCookie() == null) {
                //get initial cookie
                String json = new RequestTapestry().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/");
                if(progressBar != null) {
                    setProgress(progressBar);
                    setProgressText(progressBarText, "Logging in...");
                }

                // get _token using jsoup
                Document doc = Jsoup.parse(json);

                String token = "";
                for (Element element : doc.getElementsByTag("input")) {
                    if (element.attr("name").equals("_token")) {
                        token = element.attr("value");
                    }
                }

                ObjectMapper objectMapper = new ObjectMapper();
                String tapestryChecked = read(context, TAPESTRY_CHECKED, TAPESTRY);
                List<String> urls = new ArrayList<>();
                try {
                    if (!tapestryChecked.isEmpty()) {
                        urls = objectMapper.readValue(tapestryChecked, TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                HashMap<String, String> params = new HashMap<>();
                params.put("email", retrieveUsername(context));
                params.put("password", retrievePassword(context));
                params.put("login_redirect_url", "");
                params.put("login_redirect_school", "");
                params.put("oauth", "");
                params.put("oauth_login_url", "");
                params.put("_token", token);
                //login
                json = new RequestTapestry().getJSONFromUrlUsingPost(TAPESTRY_BASE_PATH + "/login", params);
                doc = Jsoup.parse(json);

                if (doc.getElementsByClass("alert-danger").size() > 0) {
                    displayErrors(doc, context);
                } else {
                    new RequestTapestry().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/observations");
                    displayErrors(doc, context);
                    if(progressBar != null) {
                        setProgress(progressBar);
                        setProgressText(progressBarText, "Getting information...");
                    }
                    json = new RequestTapestry().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/children");
                    displayErrors(doc, context);
                    if(progressBar != null) {
                        setProgress(progressBar);
                    }
                    doc = Jsoup.parse(json);
                    for (int i = 0; i < doc.getElementsByClass("fa-child").size(); i++) {
                        Child child = new Child();
                        child.setID(doc.getElementsByClass("fa-child").get(i).parent().attr("href").substring(doc.getElementsByClass("fa-child").get(i).parent().attr("href").lastIndexOf("/") + 1,
                                doc.getElementsByClass("fa-child").get(i).parent().attr("href").length()));
                        child.setName(doc.getElementsByClass("fa-caret-down").get(i).parent().text());
                        tapestrySession.addChildren(child);
                    }
                    for (Child child : tapestrySession.getChildren()) {
                        new RequestTapestry().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/child/" + child.getID());
                        displayErrors(doc, context);
                        if(progressBar != null) {
                            setProgress(progressBar);
                            setProgressText(progressBarText, "Getting additional information...");
                        }
                        json = new RequestTapestry().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/observations?children%5Bchild_id%5D=" + child.getID());
                        displayErrors(doc, context);
                        if(progressBar != null) {
                            setProgress(progressBar);
                            setProgressText(progressBarText, "Getting list of observations...");
                        }
                        doc = Jsoup.parse(json);
                        for (int i = 0; i < doc.getElementsByClass("media-heading").size(); i++) {
                            String url = doc.getElementsByClass("media-heading").get(i).child(0).attr("href");
                            System.out.println(url);
                            if(progressBar != null) {
                                setProgressText(progressBarText, "Checking observations for dates ...");
                            }
                            if (!urls.contains(url)) {

                                json = new RequestTapestry().getJSONFromUrlUsingGet(url);
                                displayErrors(doc, context);
                                Document innerDoc = Jsoup.parse(json);
                                innerDoc.outputSettings(new Document.OutputSettings().prettyPrint(false));
                                List<CalenderEvent> calenderEvents = checkForCalendarEvents(innerDoc, url, child.getName());
                                if (checkAndRequestPermissions(context)) {
                                    for (CalenderEvent event : calenderEvents) {
                                        count++;
                                        addEventToCalendar(context, event, true, false);
                                    }
                                }
                                urls.add(url);
                                try {
                                    write(context, TAPESTRY_CHECKED, objectMapper.writeValueAsString(urls), TAPESTRY);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            if(progressBar != null) {
                                setProgress(progressBar);
                            }
                        }
                        if(progressBar != null) {
                            setProgress(progressBar);
                        }
                    }
                    if(progressBar != null) {
                        setProgressText(progressBarText, "Finished ...");
                    }
                }
            }
            displayLong("Found: " + count + " new events", context);
            if(progressBar != null) {
                setVisibility(progressBar, progressBarText, View.INVISIBLE);
            }
        }
    };

    public Runnable jobCheck = new Runnable() {
        @Override
        public void run() {
            String json = new RequestTapestry().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/");

            // get _token using jsoup
            Document doc = Jsoup.parse(json);

            String token = "";
            for (Element element : doc.getElementsByTag("input")) {
                if (element.attr("name").equals("_token")) {
                    token = element.attr("value");
                }
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String creds = read(context, TAPESTRY_CREDENTIALS, TAPESTRY);
            String tapestryChecked = read(context, TAPESTRY_CHECKED, TAPESTRY);
            TapestryLoginCredentials tapestryLoginCredentials = new TapestryLoginCredentials();
            List<String> urls = new ArrayList<>();
            try {
                if (!creds.isEmpty()) {
                    tapestryLoginCredentials = objectMapper.readValue(creds, TapestryLoginCredentials.class);
                }
                if (!tapestryChecked.isEmpty()) {
                    urls = objectMapper.readValue(tapestryChecked, TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("email", tapestryLoginCredentials.getUsername());
            params.put("password", tapestryLoginCredentials.getPassword());
            params.put("login_redirect_url", "");
            params.put("login_redirect_school", "");
            params.put("oauth", "");
            params.put("oauth_login_url", "");
            params.put("_token", token);
            //login
            json = new RequestTapestry().getJSONFromUrlUsingPost(TAPESTRY_BASE_PATH + "/login", params);
            doc = Jsoup.parse(json);
            if (doc.getElementsByClass("alert-danger").size() > 0) {
                return;
            } else {
                new RequestTapestry().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/observations");
                json = new RequestTapestry().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/children");
                doc = Jsoup.parse(json);
                for (int i = 0; i < doc.getElementsByClass("fa-child").size(); i++) {
                    Child child = new Child();
                    child.setID(doc.getElementsByClass("fa-child").get(i).parent().attr("href").substring(doc.getElementsByClass("fa-child").get(i).parent().attr("href").lastIndexOf("/") + 1,
                            doc.getElementsByClass("fa-child").get(i).parent().attr("href").length()));
                    child.setName(doc.getElementsByClass("fa-caret-down").get(i).parent().text());
                    tapestrySession.addChildren(child);
                }
                for (Child child : tapestrySession.getChildren()) {
                    new RequestTapestry().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/child/" + child.getID());
                    json = new RequestTapestry().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/observations?children%5Bchild_id%5D=" + child.getID());
                    doc = Jsoup.parse(json);
                    for (int i = 0; i < doc.getElementsByClass("media-heading").size(); i++) {
                        String url = doc.getElementsByClass("media-heading").get(i).child(0).attr("href");
                        if (!urls.contains(url)) {

                            json = new RequestTapestry().getJSONFromUrlUsingGet(url);
                            Document innerDoc = Jsoup.parse(json);
                            innerDoc.outputSettings(new Document.OutputSettings().prettyPrint(false));
                            List<CalenderEvent> calenderEvents = checkForCalendarEvents(innerDoc, url, child.getName());
                            if (checkAndRequestPermissions(context)) {
                                for (CalenderEvent event : calenderEvents) {
                                    addEventToCalendar(context, event, true, false);
                                }
                            }
                            urls.add(url);
                            try {
                                write(context, TAPESTRY_CHECKED, objectMapper.writeValueAsString(urls), TAPESTRY);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
    };


    private void displayErrors(Document doc, Context context) {
        if (doc.getElementsByClass("alert-danger").size() > 0) {
            for (Element element : doc.getElementsByClass("alert-danger")) {
                error += element.select("div").text().substring(0, element.select("div").text().indexOf(".") + 1).toString() + "\n";
            }
            if (!error.replaceAll("\n", "").isEmpty()) {
                displayLong(error, context);
            }
        }
    }


    private List<CalenderEvent> checkForCalendarEvents(Document doc, String url, String childName) {
        List<CalenderEvent> calenderEvents = new ArrayList<>();
        String dateApproved = doc.getElementsByClass("js-obs-approved-metadata").text();
        String whoApproved = dateApproved.substring(12, dateApproved.indexOf(" on "));
        LocalDateTime localDateApproved = LocalDateTime.parse(dateApproved.substring(dateApproved.length() - 20, dateApproved.length()), approvedDateFormat);
        if (doc.getElementsByClass("page-note") == null || doc.getElementsByClass("page-note").isEmpty()) {
            return new ArrayList<>();
        }
        String content = doc.getElementsByClass("page-note").get(0).child(0).wholeText();

        String[] lines = content.split("\\.");
        List<Paragraph> paragraphs = new ArrayList<>();
        Paragraph paragraph = new Paragraph();
        for (String line : lines) {
            line = line.replaceAll(" +", " ").replaceAll("\n +", "\n").replaceAll("\n+", "\n");
            if (!line.isEmpty() && !line.contains("\n")) {
                line = line + ".";
                paragraph.setText(paragraph.getText() + line);
                paragraph.addLines(line);
            } else {
                boolean endsWithNewLine = line.endsWith("\n");
                boolean startsWithNewLine = line.startsWith("\n");
                if (startsWithNewLine) {
                    paragraphs.add(paragraph);
                    paragraph = new Paragraph();
                }
                String[] innerLines = line.split("\n");
                for (int i = 0; i < innerLines.length; i++) {
                    if (!innerLines[i].isEmpty()) {
                        paragraph.setText(paragraph.getText() + innerLines[i]);
                        paragraph.addLines(innerLines[i]);
                        if (i != (-1 + innerLines.length) || endsWithNewLine) {
                            paragraphs.add(paragraph);
                            paragraph = new Paragraph();
                        }
                    }
                }
            }
        }
        if (!paragraph.getText().isEmpty()) {
            paragraphs.add(paragraph);
        }
        for (Paragraph para : paragraphs) {
            for (String line : para.getLines()) {
                Matcher datePatternMatcher = datePattern.matcher(line);
                Matcher dayPatternMatcher = dayPattern.matcher(line);
                while (datePatternMatcher.find()) {
                    int start = datePatternMatcher.start(0);
                    int end = datePatternMatcher.end(0);
                    CalenderEvent calenderEvent = new CalenderEvent();
                    calenderEvent.setDateApproved(localDateApproved);
                    calenderEvent.setContent(para.getText());
                    calenderEvent.setTitle(line);
                    calenderEvent.setChildName(childName);
                    calenderEvent.setApprovedBy(whoApproved);
                    calenderEvent.setUrl(url);
                    for (DateTimeFormatter dtf : new DateUtil().dateTimeFormatterList) {
                        try {
                            calenderEvent.setDatePlanned(LocalDate.parse(line.substring(start, end), dtf));
                            calenderEvents.add(calenderEvent);
                        } catch (DateTimeParseException parseException) {
                        }
                    }
                }
                while (dayPatternMatcher.find()) {
                    int start = dayPatternMatcher.start(0);
                    int end = dayPatternMatcher.end(0);
                    CalenderEvent calenderEvent = new CalenderEvent();
                    calenderEvent.setDateApproved(localDateApproved);
                    calenderEvent.setContent(para.getText());
                    calenderEvent.setTitle(line);
                    calenderEvent.setChildName(childName);
                    calenderEvent.setApprovedBy(whoApproved);
                    calenderEvent.setUrl(url);
                    String result = line.substring(start, end);
                    calenderEvent.setDatePlanned(localDateApproved.with(TemporalAdjusters.next(DayOfWeek.of(parseDayOfWeek(result.split(" ")[1].toUpperCase()) - 1))).toLocalDate());
                    calenderEvents.add(calenderEvent);
                }
            }
        }
        return calenderEvents;
    }

    private void setVisibility(ProgressBar progressBar, TextView progressBarText, int visibility) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(visibility);
                progressBarText.setVisibility(visibility);
            }
        });
    }

    private void setProgress(ProgressBar progressBar) {
        progress++;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progress);
            }
        });
    }

    private void setProgressText(TextView progressBarText, String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBarText.setText(text);
            }
        });
    }

    private static int parseDayOfWeek(String day) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.ENGLISH);
        Date date = null;
        try {
            date = dayFormat.parse(day);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }
}
