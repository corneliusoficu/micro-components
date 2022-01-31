package nl.vu.dynamicplugins.authhandler;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBHandler {
    private static final String HOST = "localhost";
    private static final int PORT = 27017;

    private static final String DATABASE = "mc-db";
    private static final String USERS_COLLECTION = "user";

    private static MongoDBHandler mongoDBHandler = null;

    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private MongoDBHandler() {
        mongoClient = new MongoClient(HOST, PORT);
        database = mongoClient.getDatabase(DATABASE);
    }

    public static MongoDBHandler getConnection() {
        if(mongoDBHandler == null) {
            mongoDBHandler = new MongoDBHandler();
        }
        return mongoDBHandler;
    }

    public Document getUser(String email) {
        MongoCollection<Document> usersCollection = database.getCollection(USERS_COLLECTION);
        return usersCollection.find(new Document("email", email)).first();
    }

    public void createUser(String email, String firstName, String lastName, String hashedPassword) {
        MongoCollection<Document> usersCollection = database.getCollection(USERS_COLLECTION);
        Document newUserDocument = new Document();
        newUserDocument.put("email", email);
        newUserDocument.put("first_name", firstName);
        newUserDocument.put("last_name", lastName);
        newUserDocument.put("hashed_password", hashedPassword);
        usersCollection.insertOne(newUserDocument);
    }
}
