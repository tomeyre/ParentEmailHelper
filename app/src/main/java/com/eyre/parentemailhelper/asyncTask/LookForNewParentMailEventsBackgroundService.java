package com.eyre.parentemailhelper.asyncTask;

import static com.eyre.parentemailhelper.pojo.ParentMailSession.getSession;
import static com.eyre.parentemailhelper.util.DateUtil.approvedDateFormatParentMail;
import static com.eyre.parentemailhelper.util.DateUtil.datePattern;
import static com.eyre.parentemailhelper.util.DateUtil.dayPattern;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eyre.parentemailhelper.pojo.Attachment;
import com.eyre.parentemailhelper.pojo.CalenderEvent;
import com.eyre.parentemailhelper.pojo.Paragraph;
import com.eyre.parentemailhelper.pojo.ParentMailEmail;
import com.eyre.parentemailhelper.pojo.ParentMailSession;
import com.eyre.parentemailhelper.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LookForNewParentMailEventsBackgroundService {

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    ParentMailSession parentMailSession = getSession();

    private String error = "";
    private int progress = 0;

    private Context context;
    private ProgressBar progressBar;
    private TextView progressBarText;
    private WebView webView;
    ObjectMapper objectMapper = new ObjectMapper();

    public void getNewParentMailEvents(Context context, ProgressBar progressBar, TextView progressBarText, WebView webView) {
        this.context = context;
        this.progressBar = progressBar;
        this.progressBarText = progressBarText;
        this.webView = webView;
        fullCheck();
    }

    private void fullCheck() {
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setDatabaseEnabled(true);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            webView.getSettings().setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
//        }
//
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // Prevent links from opening in external browsers
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                if(request.getUrl().toString().contains("https://pmx.parentmail.co.uk/api/v1.9/feed?")){
//                    parentMailSession.setCurrentHeaders(request.getRequestHeaders());
//                    parentMailSession.setCookies(CookieManager.getInstance().getCookie(request.getUrl().toString()));
//                }
//                return null;
//            }
//        });
//
//        webView.loadUrl("https://pmx.parentmail.co.uk/#core/login");
//        webView.postDelayed(() -> {
//            // Call JavaScript to fill the username and password fields and click the login button
//            webView.loadUrl("javascript:" +
//                    "var usernameField = document.getElementById('username');" +
////                            "var passwordField = document.getElementById('password');" +
//                    "var continueButton = document.getElementById('continueButton');" +
//                    "if (usernameField && continueButton) {" +
//                    "  usernameField.value = '';" +
////                            "  passwordField.value = 'your_password';" +
//                    "  continueButton.click();" +
//                    "}"); // Adjust the delay as needed
//        }, 5000); // Adjust the delay as needed
//
//        webView.postDelayed(() -> {
//            // Call JavaScript to fill the username and password fields and click the login button
//            webView.loadUrl("javascript:document.querySelector('input[type=\"submit\"]').click()"); // Adjust the delay as needed
//        }, 10000); // Adjust the delay as needed
//
//        webView.postDelayed(() -> {
//            // Call JavaScript to fill the username and password fields and click the login button
//            webView.loadUrl("javascript:(function(){" +
//                    "  var inputField = document.getElementById('input52');" +
//                    "  if (inputField) {" +
//                    "    inputField.focus();" +
//                    "    inputField.value = '';" +
//                    "    inputField.dispatchEvent(new Event('input', { bubbles: true }));" +
//                    "    inputField.dispatchEvent(new Event('change', { bubbles: true }));" +
//                    "  }" +
//                    "})()"); // Adjust the delay as needed
//        }, 15000); // Adjust the delay as needed
//
//        webView.postDelayed(() -> {
//            // Call JavaScript to fill the username and password fields and click the login button
//            webView.loadUrl("javascript:document.querySelector('input[type=\"submit\"]').click()"); // Adjust the delay as needed
//        }, 16000); // Adjust the delay as needed

        executorService.schedule(new Runnable() {
            @Override
            public void run() {
//                extractEvents(new RequestParentMail().getJSONFromUrlUsingGet("https://pmx.parentmail.co.uk/api/v1.9/feed?max=20&offset=0&filters[]=Organisation::Email&device=ion-web/4.5.17"));
                extractEvents("");
            }
        }, 0, TimeUnit.SECONDS);
    }

    private void extractEvents(String json){
        try {
            List<ParentMailEmail> emails = objectMapper.readValue("[\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2684253805\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Morning,<\\/p><p>Adam\\u00a0\\u00a0has been to first aid today, for a head bump.<\\/p><p>The class teacher will be sending home a first aid slip but we like to notify parents if children have a head bump, in-case the slip is lost or unseen.<\\/p><p>The staff will continue to monitor\\u00a0Adam\\u00a0\\u00a0throughout the remainder of the school day.<\\/p><p><a href=\\\"https:\\/\\/www.what0-18.nhs.uk\\/professionals\\/gp-primary-care-staff\\/safety-netting-documents-parents\\/head-injury\\\">Head Injury - Advice for parents \\/ care<\\/a>r<\\/p><p>Best regards,<\\/p><p>Belinda Wheelhouse<\\/p><p>Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"First Aid Slip - Head Bump\",\n" +
                    "        \"createdTs\": \"2024-01-10T10:13:09+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Morning,<\\/p><p>Adam\\u00a0\\u00a0has been to first aid today, for a head bump.<\\/p><p>The class teacher<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2682969523\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good morning,\\u00a0<\\/p><p>Please see attached flyer for a local class starting soon.\\u00a0<\\/p><p>Kind regards,<\\/p><p>Mrs Mica Mealing\\u00a0<\\/p><p>School Administrator\\u00a0<\\/p><p>\\u00a0<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"ARTventurers class\",\n" +
                    "        \"createdTs\": \"2024-01-09T09:48:05+00:00\",\n" +
                    "        \"updatedTs\": \"2024-01-09T22:35:42+00:00\",\n" +
                    "        \"attachments\": [\n" +
                    "            {\n" +
                    "                \"name\": \"Jan Feb Timetable_20240108_075419_0000.pdf\",\n" +
                    "                \"link\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/27c71951362d838ea7539b598549b09331eokbpyuelomek9v4xyxfevvqkp7fxkeepaab6rpxxc4uti6pql7pm0c163vn3h2v10doglkzuezp68nesd2758t9o2hmd9886s.pdf\"\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"summary\": \"<p>Good morning,\\u00a0<\\/p><p>Please see attached flyer for a local class starting soon.\\u00a0<\\/p><p>Kind reg<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 1,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2681689961\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good morning,\\u00a0<\\/p><p>Due to the devastating news of one of our children being diagnosed with\\u00a0Leukaemia before Christmas, we share the attached fundraising page with all of our school community, to offer support to the family at this difficult time.\\u00a0<\\/p><h1><span style=\\\"font-size:14px;\\\"><a href=\\\"https:\\/\\/www.gofundme.com\\/f\\/s4wvn-noah-slatems-fight-against-leukaemia\\\">Noah Slatem's fight against Leukaemia<\\/a><\\/span><\\/h1><p>This is being shared with the support of the family and the school felt it was appropriate to share the page and to show full support from the school community.\\u00a0<\\/p><p>Our thoughts, prayers and well wishes remain with the family during this time.\\u00a0<\\/p><p>Kind regards,\\u00a0<\\/p><p>Mrs Mica Mealing\\u00a0<\\/p><p>School Administrator\\u00a0<\\/p><p>\\u00a0<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Fundraising Page-Noah\",\n" +
                    "        \"createdTs\": \"2024-01-08T10:56:44+00:00\",\n" +
                    "        \"updatedTs\": \"2024-01-09T20:53:19+00:00\",\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good morning,\\u00a0<\\/p><p>Due to the devastating news of one of our children being diagnosed with\\u00a0Le<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 1,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2681610769\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Dear Parents\\/Carers,<\\/p><p>Due to a recent increase in reports of head lice, we would like parents\\/carers to be aware of a campaign called 'Once a Week Take a Peek'. This emphasises the importance of checking the family's hair for head lice every week. It provides information about getting rid of lice or nits if they are found. You can find out about the campaign on this link:<\\/p><p><a href=\\\"https:\\/\\/www.onceaweektakeapeek.com\\/\\\">https:\\/\\/www.onceaweektakeapeek.com\\/<\\/a><\\/p><p>Head lice are a part of childhood. Despite this, they can be a stressful and frustrating problem for parents to deal with. While responsibility sits with parents\\/carers, schools can help by sharing accurate and reliable information that parents\\/carers can use to guide them through the process. We hope you find this campaign is helpful in combating head lice so that staff and pupils can be head lice free.<\\/p><p>There is no requirement to keep your child at home if they have head lice as long as parents\\/carers are addressing the problem. Please note that one case of head lice will need multiple attempts to rid the hair of lice and nits due to the life cycle of the lice. One check and one attempt to clear will not be enough. Once the hair is lice free, continue to check weekly.<\\/p><p>It would be wonderful if we could make this campaign a success and get rid of the nuisance of head lice. Please take the time to read the information included in this message and get into a routine of taking a peek every week.\\u00a0<\\/p><p>To avoid spending money on lotions, we would advise using the wet comb method described. This can also be found on the NHS website: <a href=\\\"https:\\/\\/www.nhs.uk\\/conditions\\/head-lice-and-nits\\/\\\">https:\\/\\/www.nhs.uk\\/conditions\\/head-lice-and-nits\\/<\\/a><\\/p><p>Kind regards<\\/p><p>Catherine Jollands<\\/p><p>Headteacher\\u00a0<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Once a Week Take a Peek\",\n" +
                    "        \"createdTs\": \"2024-01-08T10:25:48+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Dear Parents\\/Carers,<\\/p><p>Due to a recent increase in reports of head lice, we would like parent<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2681594058\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Morning,<\\/p><p>Just a reminder that the deadline for applying for a Junior School place is <strong>Monday 15th January, 2024.<\\/strong><\\/p><p>Please do not hesitate to contact the school office if you require any further information.<\\/p><p>Best regards,<\\/p><p>Belinda Wheelhouse<\\/p><p>Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Applying for a Junior School Place - Deadline Reminder\",\n" +
                    "        \"createdTs\": \"2024-01-08T10:21:16+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Morning,<\\/p><p>Just a reminder that the deadline for applying for a Junior School place is <\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2680678913\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Afternoon,<\\/p><p><span style=\\\"font-size:small;\\\"><span style=\\\"color:#222222;\\\"><span style=\\\"font-family:Arial, Helvetica, sans-serif;\\\"><span style=\\\"font-style:normal;\\\"><span><span style=\\\"font-weight:400;\\\"><span style=\\\"white-space:normal;\\\"><span style=\\\"background-color:#ffffff;\\\"><span><span><span>Happy New Year!<\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/p><p><span style=\\\"font-size:small;\\\"><span style=\\\"color:#222222;\\\"><span style=\\\"font-family:Arial, Helvetica, sans-serif;\\\"><span style=\\\"font-style:normal;\\\"><span><span style=\\\"font-weight:400;\\\"><span style=\\\"white-space:normal;\\\"><span style=\\\"background-color:#ffffff;\\\"><span><span><span>I have been asked to forward to parents the information below from the Family Hubs who are offering FREE online courses for parents and carers.\\u00a0<\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/p><p><span style=\\\"font-size:small;\\\"><span style=\\\"color:#222222;\\\"><span style=\\\"font-family:Arial, Helvetica, sans-serif;\\\"><span style=\\\"font-style:normal;\\\"><span><span style=\\\"font-weight:400;\\\"><span style=\\\"white-space:normal;\\\"><span style=\\\"background-color:#ffffff;\\\"><span><span><span>These courses have been made available through a paid partnership between LCC and the Solihull approach.\\u00a0<\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/p><p><span style=\\\"font-size:small;\\\"><span style=\\\"color:#222222;\\\"><span style=\\\"font-family:Arial, Helvetica, sans-serif;\\\"><span style=\\\"font-style:normal;\\\"><span><span style=\\\"font-weight:400;\\\"><span style=\\\"white-space:normal;\\\"><span style=\\\"background-color:#ffffff;\\\"><span><span><span>The courses for parents are designed to provide support through all stages of bringing up children, from pregnancy and birth to caring for toddlers up to teenagers. They include specific courses for teenagers, and for parents and carers of children with additional needs.\\u00a0 Parents\\u2019 courses can be accessed\\u00a0\\u00a0<a href=\\\"https:\\/\\/inourplace.co.uk\\/lincolnshire\\/\\\"><span style=\\\"color:#0000FF;\\\">inourplace.co.uk\\/lincolnshire\\/<\\/span><\\/a><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/p><p><span style=\\\"font-size:small;\\\"><span style=\\\"color:#222222;\\\"><span style=\\\"font-family:Arial, Helvetica, sans-serif;\\\"><span style=\\\"font-style:normal;\\\"><span><span style=\\\"font-weight:400;\\\"><span style=\\\"white-space:normal;\\\"><span style=\\\"background-color:#ffffff;\\\"><span><span><span>Please do not hesitate to contact me for a chat, advice or information.<\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/p><p>Best regards,<\\/p><p><span style=\\\"font-size:small;\\\"><span style=\\\"color:#222222;\\\"><span style=\\\"font-family:Arial, Helvetica, sans-serif;\\\"><span style=\\\"font-style:normal;\\\"><span><span style=\\\"font-weight:400;\\\"><span style=\\\"white-space:normal;\\\"><span style=\\\"background-color:#ffffff;\\\"><span><span><span>Karen Lodge<\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/p><p><span style=\\\"font-size:small;\\\"><span style=\\\"color:#222222;\\\"><span style=\\\"font-family:Arial, Helvetica, sans-serif;\\\"><span style=\\\"font-style:normal;\\\"><span><span style=\\\"font-weight:400;\\\"><span style=\\\"white-space:normal;\\\"><span style=\\\"background-color:#ffffff;\\\"><span><span><span>SENDCo\\/Class Teacher<\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Parent Courses \\/ Information\",\n" +
                    "        \"createdTs\": \"2024-01-05T14:44:29+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Afternoon,<\\/p><p><span><span><span><span><span><span><span><span><span><span><span>Happy New<\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/span><\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2680255903\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Morning,<\\/p><p>RE: PRIMARY SCHOOL ADMISSIONS FOR SEPTEMBER 2024 \\u2013\\u00a0<strong>CHILDREN BORN BETWEEN THE DATES OF 01\\/09\\/2019 AND 31\\/08\\/2020<\\/strong><\\/p><p>Please find attached a letter to inform you about how to apply for a school place for September 2024.\\u00a0 Please note the cut off date for applications is <strong>15th January, 2024.\\u00a0\\u00a0<\\/strong><\\/p><p>This is also the cut off date for applying for a <strong>Junior school<\\/strong> place.<\\/p><p>Please do share this information with anyone who may find it useful.<\\/p><p>Best regards,<\\/p><p>Belinda Wheelhouse<\\/p><p>Administrator<\\/p><p>\\u00a0<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Applying for a School Place - September 2024\",\n" +
                    "        \"createdTs\": \"2024-01-05T10:56:31+00:00\",\n" +
                    "        \"updatedTs\": \"2024-01-05T19:25:10+00:00\",\n" +
                    "        \"attachments\": [\n" +
                    "            {\n" +
                    "                \"name\": \"Primary Intake Letter  Sept 24.pdf\",\n" +
                    "                \"link\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/283ea49378653326a3b614353f53f972qi92ur6e6w83rnvm7qtuz4hnz2fjo57bnqqh1yxx11a7vnj5shw0z855zjit4c2rcqv5bz0rnei8vou2s8wpf1rjq63qdlmk6fd5.pdf\"\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"summary\": \"<p>Good Morning,<\\/p><p>RE: PRIMARY SCHOOL ADMISSIONS FOR SEPTEMBER 2024 \\u2013\\u00a0<strong>CHILDREN BORN B<\\/strong><\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 1,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2680101763\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Morning,<\\/p><p>A small amount of money has been found outside school this morning.<\\/p><p>If you think it may belong to you, please contact the school office.<\\/p><p>Thank you.<\\/p><p>Kind regards,<\\/p><p>Belinda Wheelhouse<\\/p><p>Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Item Found Outside School\",\n" +
                    "        \"createdTs\": \"2024-01-05T09:16:47+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Morning,<\\/p><p>A small amount of money has been found outside school this morning.<\\/p><p>If <\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2679740565\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good afternoon,\\u00a0<\\/p><p>RE:Phillip\\u00a0Eyre\\u00a0<\\/p><p>Happy New Year to you all!\\u00a0<\\/p><p>1) Please can we remind all parents carers that on PE days, children should not have any Jewellery on. Taping over ear piercings may offer a measure of protection in some physical activity situations. However, the amount of tape used needs to be sufficient to prevent the piercing penetrating, for example, the bone behind the ear. Long hair should always be tied back to prevent entanglement in apparatus and to prevent vision being obscured.\\u00a0<\\/p><p>2) The Charlotte Mowbray School of Dance LTD will be running an Acrobatic Arts, After School Club at Nettleham Infants Mondays 3:15-4:15pm, starting on Monday 8th January 2024. This club is open to children in KS1 only. If you would like to confirm a space for your child, please contact Charlotte directly on\\u00a0<a href=\\\"mailto:info@charlottemowbraydance.co.uk\\\">info@charlottemowbraydance.co.uk<\\/a>\\u00a0\\u00a0<\\/p><p>Many thanks,<\\/p><p>Mrs Mica Mealing<\\/p><p>School Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Reminders\",\n" +
                    "        \"createdTs\": \"2024-01-04T13:52:11+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good afternoon,\\u00a0<\\/p><p>RE:Phillip\\u00a0Eyre\\u00a0<\\/p><p>Happy New Year to you all!\\u00a0<\\/p><p>1) Please can<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2678793638\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Morning,<\\/p><p>I hope that you have had a lovely Christmas break.<\\/p><p>The Aikido instructor has confirmed that the club will restart this afternoon.<\\/p><p>Kind regards,<\\/p><p>Belinda Wheelhouse<\\/p><p>Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Aikido Club\",\n" +
                    "        \"createdTs\": \"2024-01-03T09:15:51+00:00\",\n" +
                    "        \"updatedTs\": \"2024-01-09T20:53:03+00:00\",\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Morning,<\\/p><p>I hope that you have had a lovely Christmas break.<\\/p><p>The Aikido instructo<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 1,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2678488830\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Morning,<\\/p><p>I hope that you have had a lovely Christmas break.<\\/p><p>The numbers for the Kidz Kitchen hot lunch orders for this week are fewer than normal.<\\/p><p>If you normally order a hot meal and haven't yet done so, please remember to send in a packed lunch from home.<\\/p><p>Thank you.<\\/p><p>Kind regards,<\\/p><p>Belinda Wheelhouse<\\/p><p>Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Kidz Kitchen - Lunch Orders\",\n" +
                    "        \"createdTs\": \"2024-01-02T09:55:39+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Morning,<\\/p><p>I hope that you have had a lovely Christmas break.<\\/p><p>The numbers for the <\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2676355812\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good afternoon,<\\/p><p>Please see attached newsletter from Miss Jollands for your information.\\u00a0<\\/p><p>Have a wonderful Christmas and we look forward to seeing the children back on Wednesday 3rd January 2024.\\u00a0<\\/p><p>Many thanks,\\u00a0<\\/p><p>Mrs Mica Mealing\\u00a0<\\/p><p>School Administrator\\u00a0<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"December Newsletter\",\n" +
                    "        \"createdTs\": \"2023-12-20T14:32:36+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [\n" +
                    "            {\n" +
                    "                \"name\": \"newsletter DEC 23 (1).pdf\",\n" +
                    "                \"link\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/0e7f7cf0461aa875502469eb34d4d23f54lc3t0tfqtm9ehq3l37v2fbw9qsn683iei7eppp414ohhx47bwmqz2etx65bcs5r722lne0xm42671am43af5xfscrzpejdmbme.pdf\"\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"summary\": \"<p>Good afternoon,<\\/p><p>Please see attached newsletter from Miss Jollands for your information.\\u00a0<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2675758867\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Morning,<\\/p><p>Just a reminder that the school day ends at 14:30 today.\\u00a0<\\/p><p>If you have a space booked for the p.m session at The Treehouse Club, the club will close at 4.30pm, in-line with the school early finish time.<\\/p><p>Have a lovely Christmas break and best wishes to you all for 2024.\\u00a0<\\/p><p>Kind regards,<\\/p><p>Mrs Mica Mealing\\u00a0<\\/p><p>School Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Wednesday 20th December 2023 - Early Finish\",\n" +
                    "        \"createdTs\": \"2023-12-20T10:25:21+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Morning,<\\/p><p>Just a reminder that the school day ends at 14:30 today.\\u00a0<\\/p><p>If you have <\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2673889516\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Afternoon,<\\/p><p>We have been made aware of a case of head lice within our school community.<\\/p><p>Please can all parents ensure that they thoroughly examine their child's hair as soon as possible and treat if any infestations are clear. The below NHS link outlines the how to spot and treat head lice.<\\/p><p><a href=\\\"http:\\/\\/www.nhs.uk\\/conditions\\/Head-lice\\/Pages\\/Introduction.aspx\\\">http:\\/\\/www.nhs.uk\\/conditions\\/Head-lice\\/Pages\\/Introduction.aspx<\\/a><\\/p><p>They're largely harmless, but can live in the hair for a long time if not treated and can be irritating and frustrating to deal with.\\u00a0<strong>Please support the school by tackling this at home with your children to reduce possible spreading at school.<\\/strong><\\/p><p>Please could we also request that girls with long hair have it tied up at school, inline with school policy.<\\/p><p>Kind regards,<\\/p><p>Belinda Wheelhouse<\\/p><p>Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Head Lice \\/ Nits\",\n" +
                    "        \"createdTs\": \"2023-12-18T15:50:57+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Afternoon,<\\/p><p>We have been made aware of a case of head lice within our school community.<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2673854561\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Dear Parent\\/Carers\\u00a0<\\/p><p>We are very excited for our trip tomorrow, please see below for some important reminders regarding our trip.\\u00a0<\\/p><p>- We will be leaving school between 9.15 - 9.30, please make sure your child is on time so they can use the toilet and be registered before going.\\u00a0<\\/p><p>- If your child is having a school dinner it has automatically\\u00a0been changed to a pack up, otherwise please make sure your child has their packup with them.\\u00a0<\\/p><p>- Please make sure your child has a water bottle, there will be somewhere to refill this at the museum, if required.\\u00a0<\\/p><p>- Children will not need to bring any money with them tomorrow.\\u00a0<\\/p><p>- Children should wear their school uniform and sensible\\u00a0footwear.\\u00a0<\\/p><p>- If your child suffers from travel sickness please ensure that they have taken appropriate medication prior to setting off.\\u00a0<\\/p><p>- We will arrive back at school between 14.00 - 14.30, collection will be at normal time from the Y2 gate.<\\/p><p>We hope the children are looking forward to the trip!<\\/p><p>\\u00a0<\\/p><p>Many thanks,\\u00a0<\\/p><p>Miss Jollands and the Y2 team.\\u00a0<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"School Trip - Information\",\n" +
                    "        \"createdTs\": \"2023-12-18T15:38:46+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Dear Parent\\/Carers\\u00a0<\\/p><p>We are very excited for our trip tomorrow, please see below for some i<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2673228591\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good morning,\\u00a0<\\/p><p>Please see attached an update from Miss Jollands.\\u00a0<\\/p><p>Kind regards,\\u00a0<\\/p><p>Mrs Mica Mealing\\u00a0<\\/p><p>School Administrator\\u00a0<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Fundraising Project- Update\",\n" +
                    "        \"createdTs\": \"2023-12-18T11:45:57+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [\n" +
                    "            {\n" +
                    "                \"name\": \"fund raising project update autumn.pdf\",\n" +
                    "                \"link\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/2f1167a004f463878d2a5699a121919cvv9753x9jtz233f9ubj8y6ipat22ulnrl3ehpyqqlu79f1zwqthacj23mu2umyng7bqcljry5flfy3mtebl6crc5pcfpab6cbo88.pdf\"\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"summary\": \"<p>Good morning,\\u00a0<\\/p><p>Please see attached an update from Miss Jollands.\\u00a0<\\/p><p>Kind regards,\\u00a0<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2670903124\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Morning,<\\/p><p>Adam\\u00a0\\u00a0has been to first aid today, for a head bump.<\\/p><p>The class teacher will be sending home a first aid slip but we like to notify parents if children have a head bump, in-case the slip is lost or unseen.<\\/p><p>The staff will continue to monitor\\u00a0Adam\\u00a0\\u00a0throughout the remainder of the school day.<\\/p><p><a href=\\\"https:\\/\\/www.what0-18.nhs.uk\\/professionals\\/gp-primary-care-staff\\/safety-netting-documents-parents\\/head-injury\\\">Head Injury - Advice for parents \\/ care<\\/a>r<\\/p><p>Best regards,<\\/p><p>Belinda Wheelhouse<\\/p><p>Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"First Aid Slip - Head Bump\",\n" +
                    "        \"createdTs\": \"2023-12-15T11:50:54+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Morning,<\\/p><p>Adam\\u00a0\\u00a0has been to first aid today, for a head bump.<\\/p><p>The class teacher<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2669529824\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Afternoon,<\\/p><p>We hope that all of the children enjoyed their term at multi-skills club.<\\/p><p>The club won't be running next term due to timetable issues. We already have another club using the hall and the Winter weather means that we cannot guarantee use of the playground after school.<\\/p><p>We are hoping to be able to offer the club again at a later date.<\\/p><p>Thank you for your support.<\\/p><p>Best regards,<\\/p><p>Belinda Wheelhouse<\\/p><p>Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Multi-skills Club - Information\",\n" +
                    "        \"createdTs\": \"2023-12-14T12:39:18+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Afternoon,<\\/p><p>We hope that all of the children enjoyed their term at multi-skills club.<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2669330401\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Morning,<\\/p><p>Just a reminder for Aikido Club attendees that due to the early finish on the last day of term, the final session of Aikido will be held on\\u00a0<strong>Monday 18th December, 2023.<\\/strong><\\/p><p>Best regards,<\\/p><p>Belinda Wheelhouse<\\/p><p>Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Aikido Club - Change of Day - Reminder\",\n" +
                    "        \"createdTs\": \"2023-12-14T11:31:54+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Morning,<\\/p><p>Just a reminder for Aikido Club attendees that due to the early finish on the<\\/p>\",\n" +
                    "        \"authorId\": 250007410,\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"_id\": \"128707620_organisations-email_2668835143\",\n" +
                    "        \"type\": \"organisations-email\",\n" +
                    "        \"msg\": \"<p>Good Morning,<\\/p><p>This is just a reminder that last week was the final session for the Multi-skills Club and there is no session running today.<\\/p><p>&nbsp;<\\/p><p>Best regards,<\\/p><p>Belinda Wheelhouse<\\/p><p>Administrator<\\/p>\",\n" +
                    "        \"regarding\": \"\",\n" +
                    "        \"subject\": \"Reminder - No Multi-skills Club Session Today\",\n" +
                    "        \"createdTs\": \"2023-12-14T08:00:06+00:00\",\n" +
                    "        \"updatedTs\": null,\n" +
                    "        \"attachments\": [],\n" +
                    "        \"summary\": \"<p>Good Morning,<\\/p><p>This is just a reminder that last week was the final session for the Multi-sk<\\/p>\",\n" +
                    "        \"authorId\": \"250007410\",\n" +
                    "        \"authorName\": \"Nettleham Infant School\",\n" +
                    "        \"authorImg\": \"https:\\/\\/pmx.parentmail.co.uk\\/download\\/99c8008016302a3280daa3936815539fpr6lef5ejfi5lqqnu34vjjbpu6p31p57ho5rioi7zim9etqu3a8v2yw21qdkenbtnoyinthlel0sh69sd29ivfr6h6a4gj3hinef.jpg\",\n" +
                    "        \"read\": 0,\n" +
                    "        \"starred\": 0,\n" +
                    "        \"archived\": 0,\n" +
                    "        \"newItem\": false\n" +
                    "    }\n" +
                    "]", new TypeReference<List<ParentMailEmail>>(){});

            checkForCalendarEvents(emails);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

//    private void displayErrors(Document doc, Context context) {
//        if (doc.getElementsByClass("alert-danger").size() > 0) {
//            for (Element element : doc.getElementsByClass("alert-danger")) {
//                error += element.select("div").text().substring(0, element.select("div").text().indexOf(".") + 1).toString() + "\n";
//            }
//            if (!error.replaceAll("\n", "").isEmpty()) {
//                displayLong(error, context);
//            }
//        }
//    }


    private List<CalenderEvent> checkForCalendarEvents(List<ParentMailEmail> emails) {
        List<CalenderEvent> calenderEvents = new ArrayList<>();

        for(ParentMailEmail email : emails) {
            LocalDateTime localDateApproved = LocalDateTime.parse(email.getCreatedTs().substring(0, 19), approvedDateFormatParentMail);
            String[] lines = checkAttachmentsForText(email).split("</p>");
            List<Paragraph> paragraphs = new ArrayList<>();
            Paragraph paragraph = new Paragraph();
            for (String line : lines) {
                line = findAndReturnAnyLinks(line);
                line = line.replaceAll("\\<[^>]*>","");
                if (!line.isEmpty() && !line.contains("\n")) {
                    paragraph.setText(paragraph.getText() + line + ' ');
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
                        calenderEvent.setChildName("Parent Mail");
                        calenderEvent.setApprovedBy(email.getAuthorName());
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
                        calenderEvent.setChildName("Parent Mail");
                        calenderEvent.setApprovedBy(email.getAuthorName());
                        String result = line.substring(start, end);
                        calenderEvent.setDatePlanned(localDateApproved.with(TemporalAdjusters.next(DayOfWeek.of(parseDayOfWeek(result.split(" ")[1].toUpperCase()) - 1))).toLocalDate());
                        calenderEvents.add(calenderEvent);
                    }
                }
            }
        }
        return calenderEvents;
    }

    private String checkAttachmentsForText(ParentMailEmail email){
        if(!email.getAttachments().isEmpty()){
            for(Attachment attachment : email.getAttachments()){
                extractPDF();
            }
        }
        return email.getMsg();
    }

    private void extractPDF() {
        try {
            // creating a string for
            // storing our extracted text.
            String extractedText = "";

            // creating a variable for pdf reader
            // and passing our PDF file in it.
            PdfReader reader = new PdfReader(context.getAssets().open("newsletterDEC23.pdf"));

            // below line is for getting number
            // of pages of PDF file.
            int n = reader.getNumberOfPages();

            // running a for loop to get the data from PDF
            // we are storing that data inside our string.
            for (int i = 0; i < n; i++) {
                extractedText = extractedText + PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n";
                // to extract the PDF content from the different pages
            }

            // after extracting all the data we are
            // setting that string value to our text view.
            System.out.println(extractedText);

            // below line is used for closing reader.
            reader.close();
        } catch (Exception e) {
            // for handling error while extracting the text file.
            System.out.println("Error found is : \n" + e);
        }
    }

    private String findAndReturnAnyLinks(String html){
        String regex = "<a href\\s?=\\s?\"([^\"]+)\">";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        int index = 0;
        while (matcher.find(index)) {
            String link = matcher.group(1); // just the link
            index = matcher.end();
            return link + " " + html.substring(index, html.length()-1);
        }
        return html;
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
