package com.eyre.parentemailhelper.asyncTask;

import static com.eyre.parentemailhelper.util.DateUtil.approvedDateFormat;

import android.content.Context;

import com.eyre.parentemailhelper.pojo.CalenderEvent;
import com.eyre.parentemailhelper.pojo.Paragraph;
import com.eyre.parentemailhelper.pojo.TapestrySession;

import org.jsoup.nodes.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LookForNewTapestryEventsBackgroundService {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    TapestrySession tapestrySession = TapestrySession.getInstance();

    private String error = "";

    public void getNewTapestryEvents(Context context) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
//                if (tapestrySession.getCookie() == null) {
//                    //get initial cookie
//                    String json = new Request().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/");
//
//                    // get _token using jsoup
//                    Document doc = Jsoup.parse(json);
//
//                    String token = "";
//                    for (Element element : doc.getElementsByTag("input")) {
//                        if (element.attr("name").equals("_token")) {
//                            token = element.attr("value");
//                        }
//                    }
//
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String creds = read(context, TAPESTRY_CREDENTIALS, TAPESTRY);
//                    TapestryLoginCredentials tapestryLoginCredentials = new TapestryLoginCredentials();
//                    try {
//                        tapestryLoginCredentials = objectMapper.readValue(creds, TapestryLoginCredentials.class);
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException(e);
//                    }
//                    HashMap<String, String> params = new HashMap<>();
//                    params.put("email", tapestryLoginCredentials.getUsername());
//                    params.put("password", tapestryLoginCredentials.getPassword());
//                    params.put("login_redirect_url", "");
//                    params.put("login_redirect_school", "");
//                    params.put("oauth", "");
//                    params.put("oauth_login_url", "");
//                    params.put("_token", token);
//                    //login
//                    json = new Request().getJSONFromUrlUsingPost(TAPESTRY_BASE_PATH + "/login", params);
//                    doc = Jsoup.parse(json);
//                    if (doc.getElementsByClass("alert-danger").size() > 0) {
//                        for (Element element : doc.getElementsByClass("alert-danger")) {
//                            error += element.select("div").text().substring(0, element.select("div").text().indexOf(".") + 1).toString() + "\n";
//                        }
//                        displayLong(error, context);
//                    } else {
//                        new Request().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/observations");
//                        json = new Request().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/children");
//                        doc = Jsoup.parse(json);
//                        for(Element element : doc.getElementsByClass("fa-child")) {
//                            Child child = new Child();
//                            child.setID(element.parent().attr("href").substring(element.parent().attr("href").lastIndexOf("/") + 1,
//                                    element.parent().attr("href").length()));
//                            child.setName(element.parent().attr("href").substring(element.parent().attr("href").lastIndexOf("/") + 1,
//                                    element.parent().attr("href").length()));
//                            tapestrySession.addChildren(child);
//                        }
//                        for(Child child : tapestrySession.getChildren()){
//                            new Request().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/child/" + child.getID());
//                            json = new Request().getJSONFromUrlUsingGet(TAPESTRY_BASE_PATH + "/s/nettleham-infant-and-nursery-school/observations?children%5Bchild_id%5D=" + child.getID());
//                            doc = Jsoup.parse(json);
//                            for(int i = 0; i < doc.getElementsByClass("media-heading").size(); i++ ) {
//                                String url = doc.getElementsByClass("media-heading").get(i).child(0).attr("href");
//                                json = new Request().getJSONFromUrlUsingGet(url);
//                                doc = Jsoup.parse(json);
//                                CalenderEvent calenderEvent = checkForCalendarEvent(doc, url);
//                                if(calenderEvent != null){
//
//                                }
//                            }
//                        }
//                    }
//                }
                getDateFromDoc("","");
            }
        });
    }

    private CalenderEvent checkForCalendarEvent(Document doc, String url){
        CalenderEvent calenderEvent = new CalenderEvent();
        String dateApproved = doc.getElementsByClass("js-obs-approved-metadata").text();
        calenderEvent.setDateApproved(dateApproved.substring(dateApproved.length() - 20, dateApproved.length()));
        String content = doc.getElementsByClass("page-note").get(0).child(0).text();
        String date = getDateFromDoc("",content);
        if(date == null){
            return calenderEvent;
        }
        return null;
    }

    private String getDateFromDoc(String dateApprove, String observationContent){
        String dateApproved = "29 Apr 2023 08:16 PM";

        LocalDateTime localDate = LocalDateTime.parse("29 Apr 2023 08:16 PM", approvedDateFormat);
        String content = "Welcome to the Weekly Warm Up\n" +
                "\n" +
                "We will be looking at the story of \"The Enormous Turnip\" for the last time next week. We will be innovating from the story to create a new tale but using the same story structure. The children will be encouraged to create their own stories too with puppets.\n" +
                "\n" +
                "Our maths work focuses on sorting and classifying objects. Children will be engaged in grouping objects according to different criteria and will also be asked to explain how they think a group has been sorted by describing what all the objects within a set have in common.\n" +
                "\n" +
                "Our phonic work continues next week with the letters g, o, c and k. We will also be introducing the tricky word \"is\" to the children within our phonic sessions. Further information on our phonics teaching and learning can be found on the attached file to the Weekly Warm Up from 2 weeks ago.\n" +
                "\n" +
                "We will be beginning reading groups and your child will bring home a book either with or without words to develop key reading skills as discussed. We will need book bags to be in school on Thursday morning and these will be sent home on Friday afternoon for you to keep at home until the following Thursday. Mrs Yeates' reading presentation slides were shared via Tapestry on 15th September and further information has been sent via Parentmail so that whether you were able to come to the reading meeting or not, all parents have access to the same information.\n" +
                "\n" +
                "Next week we will begin formal handwriting sessions with the children. The main objective at this stage in the year is a comfortable pencil grip so that adequate pressure can be applied from pencil to paper. If your child is mark making at home, it would be a huge help if you could ensure that they hold their pencil correctly. A file is attached to demonstrate this clearly.\n" +
                "\n" +
                "We want to spend as much time interacting with your children as possible whilst they are with us so you may notice that in the coming weeks there may be less observations on Tapestry. As we are getting to know your children better we will know what is a real “Wow!” moment where new, significant learning is being demonstrated by them and would wish to share those precious times with you but it is likely that the number of Tapestry posts you receive will be less. Your understanding is appreciated.\n" +
                "\n" +
                "Our parent consultations will be held soon and this is the perfect opportunity for us to discuss how your child has settled into school. Details to follow.\n" +
                "\n" +
                "As always if you have any questions or concerns, please do not hesitate to come and speak to one of the Reception Team.\n" +
                "\n" +
                "Kind regards\n" +
                "\n" +
                "Rachael Rideout\n" +
                "\n" +
                "We are coming up to assessment time again. We will be conducting our final assessment of reading in two weeks time and testing children's ability to read and spell common exception words in the next few days. Children should be able to read and spell these words to meet Year 1 National Curriculum expectations.\n" +
                "\n" +
                "Just a reminder for tomorrow to send in a named plastic tub to put your child’s pizza in!";
        String[] lines = content.split("[.\n]");
        List<Paragraph> paragraphs = new ArrayList<>();
        Paragraph paragraph = new Paragraph();
        for(String line : lines){
            if(!line.isEmpty()){
                paragraph.setText(paragraph.getText() + line);
                paragraph.addLines(line);
            }else{
                paragraphs.add(paragraph);
                paragraph = new Paragraph();
            }
        }
        for(Paragraph para : paragraphs){
            
        }
        return null;
    }
}
