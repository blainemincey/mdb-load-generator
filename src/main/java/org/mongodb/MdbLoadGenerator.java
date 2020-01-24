package org.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 *
 */
public class MdbLoadGenerator {

    private static Logger log = Logger.getLogger(MdbLoadGenerator.class.getName());

    private int numDocsPerSecond = 25;

    // Default to 1500
    private double maxClaimAmount = 1500;

    private String mongodbConnectionUri;
    private String mongodbDatabaseName;
    private String mongodbCollection;

    private MongoDatabase mongoDatabase;

    /**
     *
     */
    public MdbLoadGenerator() {
        this.initialize();
    }

    /**
     * Load .env values into AppConfig
     */
    private void initialize() {
        log.info("Initialize from env props.");

        Dotenv dotenv = Dotenv.configure().load();
        this.mongodbConnectionUri = dotenv.get("MONGODB_CONNECTION_URI");
        this.mongodbDatabaseName = dotenv.get("MONGODB_DATABASE");
        this.mongodbCollection = dotenv.get("MONGODB_COLLECTION");

        // claim amount
        String envClaimAmountString = (dotenv.get("MAX_CLAIM_AMOUNT"));
        if(envClaimAmountString != null && envClaimAmountString.length() > 0) {
            try{
                double maxClaim = Double.parseDouble(envClaimAmountString);
                if(maxClaim != this.maxClaimAmount) {
                    this.maxClaimAmount = maxClaim;
                }

                log.info("Max Claim Amount: " + this.maxClaimAmount);

            } catch (Exception e) {
                log.info("ERROR!  Error with parsing claim amount. Using default of: " + this.maxClaimAmount);
            }
        }

        if(this.mongodbConnectionUri != null && this.mongodbDatabaseName != null && this.mongodbCollection != null) {
            this.dbinit();

        } else {
            log.info("Error!  Null values for properties!");
        }
    }

    /**
     *
     */
    private void dbinit() {
        log.info("Initialize database.");
        log.info("Database: " + this.mongodbDatabaseName + ", Collection: " + this.mongodbCollection);

        ConnectionString connectionString = new ConnectionString(this.mongodbConnectionUri);

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(connectionString)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        this.mongoDatabase = mongoClient.getDatabase(this.mongodbDatabaseName);

        this.generateload();
    }

    /**
     *
     */
    private void generateload() {
        log.info("Generate load.");

        MongoCollection<MyTestDocument> mongoCollection
                = this.mongoDatabase.getCollection(this.mongodbCollection,MyTestDocument.class);

        int totalInserted = 0;

        // For every 5 records, we will sleep for 2 seconds
        // Simply control-c to exit
        // TODO - More elegant approach later
        while(true) {

            int count = 0;

            while(count < this.numDocsPerSecond) {
                mongoCollection.insertOne(new MyTestDocument(this.maxClaimAmount));
                count++;
                totalInserted++;
            }

            if(count == this.numDocsPerSecond) {
                try{
                    log.info("Sleeping...");
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ie) {
                    log.info("Exception: " + ie);
                    Thread.currentThread().interrupt();
                }
            }

            // total inserted
            log.info("Total inserted: " + totalInserted);

        } // end while true
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        new MdbLoadGenerator();
    }
}
