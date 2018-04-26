package umm3601.database.journal;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Iterator;
import java.util.Map;

import java.util.Date;

import static com.mongodb.client.model.Filters.eq;

public class JournalController {

    private final Gson gson;
    private MongoDatabase database;
    private final MongoCollection<Document> journalCollection;

    /**
     * journalController constructor
     *
     * @param database
     */
    public JournalController(MongoDatabase database) {
        gson = new Gson();
        this.database = database;
        journalCollection = database.getCollection("journals");
    }

    public String getJournal(String id) {
        FindIterable<Document> jsonUsers
            = journalCollection
            .find(eq("_id", new ObjectId(id)));

        Iterator<Document> iterator = jsonUsers.iterator();
        if (iterator.hasNext()) {
            Document user = iterator.next();
            return user.toJson();
        } else {
            // We didn't find the desired journal
            return null;
        }
    }

    public String getJournals(Map<String, String[]> queryParams) {

        Document filterDoc = new Document();

        //Filter by userID
        //If there is no userID provided, return an empty result
        if (queryParams.containsKey("userID")) {
            String targetContent = (queryParams.get("userID")[0]);
            Document contentRegQuery = new Document();
            contentRegQuery.append("$regex", targetContent);
            contentRegQuery.append("$options", "i");
            filterDoc = filterDoc.append("userID", contentRegQuery);
        } else {
            System.out.println("It had no userID");
            return JSON.serialize("[ ]");
        }

        if (queryParams.containsKey("subject")) {
            String targetContent = (queryParams.get("subject")[0]);
            Document contentRegQuery = new Document();
            contentRegQuery.append("$regex", targetContent);
            contentRegQuery.append("$options", "i");
            filterDoc = filterDoc.append("subject", contentRegQuery);        }

        if (queryParams.containsKey("body")) {
            String targetContent = (queryParams.get("body")[0]);
            Document contentRegQuery = new Document();
            contentRegQuery.append("$regex", targetContent);
            contentRegQuery.append("$options", "i");
            filterDoc = filterDoc.append("body", contentRegQuery);
        }

        //FindIterable comes from mongo, Document comes from Gson
        FindIterable<Document> matchingJournals = journalCollection.find(filterDoc);

        return JSON.serialize(matchingJournals);
    }

    public String addNewJournal(String userID, String subject, String body) {
        Document newJournal = new Document();
        newJournal.append("userID", userID);
        newJournal.append("subject",subject);
        newJournal.append("body",body);

        Date now = new Date();
        newJournal.append("date", now.toString());

        try {
            journalCollection.insertOne(newJournal);
            ObjectId id = newJournal.getObjectId("_id");
            System.err.println("Successfully added new journal [_id=" + id + ", subject=" + subject + ", body=" + body + ", date=" + now + ']');
            return JSON.serialize(id);
        } catch(MongoException me) {
            me.printStackTrace();
            return null;
        }
    }

    public String editJournal(String id, String subject, String body){

        Document newJournal = new Document();
        newJournal.append("subject", subject);
        newJournal.append("body", body);
        Document setQuery = new Document();
        setQuery.append("$set", newJournal);
        Document searchQuery = new Document().append("_id", new ObjectId(id));
        System.out.println(searchQuery + " the search");


        try {
            //System.out.println(journalCollection.find());
            journalCollection.updateOne(searchQuery, setQuery);
            System.out.println(journalCollection.find());
            ObjectId id1 = searchQuery.getObjectId("_id");
            System.err.println("Successfully updated journal [_id=" + id1 + ", subject=" + subject + ", body=" + body + ']');
            return JSON.serialize(id1);
        } catch(MongoException me) {
            me.printStackTrace();
            return null;
        }
    }

    public void deleteJournal(String id){
        Document searchQuery = new Document().append("_id", new ObjectId(id));
        System.out.println("Journal id: " + id);
        try {
            journalCollection.deleteOne(searchQuery);
            ObjectId theID = searchQuery.getObjectId("_id");
            System.out.println("Succesfully deleted journal with ID: " + theID);

        } catch(MongoException me) {
            me.printStackTrace();
            System.out.println("error");
        }
    }

}
