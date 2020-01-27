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

    private boolean isMaxClaimPerType = false;

    // Default to 1500
    private double maxClaimAmount = 1500;

    private String mongodbConnectionUri;
    private String mongodbDatabaseName;
    private String mongodbCollection;
    private MongoDatabase mongoDatabase;

    // Specific Claim Type Max Values
    private double maxDisabilityClaim;
    private double maxIllnessClaim;
    private double maxLifeClaim;
    private double maxHospitalClaim;
    private double maxVisionClaim;
    private double maxAccidentClaim;
    private double maxDentalClaim;

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

        // Run with specific max claims per claim type?
        String maxClaimPerClaimType = (dotenv.get("MAX_AMOUNT_PER_CLAIM_TYPE"));
        if(maxClaimPerClaimType != null && maxClaimPerClaimType.length() > 0) {
            boolean tmpMaxClaimPerType = Boolean.parseBoolean(maxClaimPerClaimType);
            if(tmpMaxClaimPerType) {
                this.isMaxClaimPerType = true;
                log.info("*** Running in Max Claim Per Claim Type Mode ***");

                String envAccidentClaimString = (dotenv.get("ACCIDENT_MAX_CLAIM"));
                String envDentalClaimString = (dotenv.get("DENTAL_MAX_CLAIM"));
                String envDisabilityClaimString = (dotenv.get("DISABILITY_MAX_CLAIM"));
                String envHospitalClaimString = (dotenv.get("HOSPITAL_MAX_CLAIM"));
                String envIllnessClaimString = (dotenv.get("ILLNESS_MAX_CLAIM"));
                String envLifeClaimString = (dotenv.get("LIFE_MAX_CLAIM"));
                String envVisionClaimString = (dotenv.get("VISION_MAX_CLAIM"));

                try {

                    this.maxAccidentClaim = Double.parseDouble(envAccidentClaimString);
                    this.maxDentalClaim = Double.parseDouble(envDentalClaimString);
                    this.maxDisabilityClaim = Double.parseDouble(envDisabilityClaimString);
                    this.maxHospitalClaim = Double.parseDouble(envHospitalClaimString);
                    this.maxIllnessClaim = Double.parseDouble(envIllnessClaimString);
                    this.maxLifeClaim = Double.parseDouble(envLifeClaimString);
                    this.maxVisionClaim = Double.parseDouble(envVisionClaimString);

                } catch (Exception e) {
                    log.info("ERROR!  Error with parsing max claim amounts by type!!");
                    System.exit(0);
                }
            }
            else {
                log.info("*** Running in Default Mode ***");
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

        this.generateLoad();
    }

    /**
     *
     */
    private void generateLoad() {
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

                if(this.isMaxClaimPerType) {
                    mongoCollection.insertOne(this.getTestDocumentMaxPerClaimType());
                } else {
                    mongoCollection.insertOne(new MyTestDocument(this.maxClaimAmount));
                }

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
     * @return
     */
    private MyTestDocument getTestDocumentMaxPerClaimType() {
        MyTestDocument myTestDocument = new MyTestDocument();

        String claimType = myTestDocument.getClaimType();

        switch(claimType) {
            case Constants.ACCIDENT_CLAIM:
                myTestDocument.setClaimAmount(RandomDataGenerator.getRandomBigDecimalWithUpperBound(this.maxAccidentClaim));
                break;

            case Constants.DISABILITY_CLAIM:
                myTestDocument.setClaimAmount(RandomDataGenerator.getRandomBigDecimalWithUpperBound(this.maxDisabilityClaim));
                break;

            case Constants.ILLNESS_CLAIM:
                myTestDocument.setClaimAmount(RandomDataGenerator.getRandomBigDecimalWithUpperBound(this.maxIllnessClaim));
                break;

            case Constants.LIFE_CLAIM:
                myTestDocument.setClaimAmount(RandomDataGenerator.getRandomBigDecimalWithUpperBound(this.maxLifeClaim));
                break;

            case Constants.HOSPITAL_CLAIM:
                myTestDocument.setClaimAmount(RandomDataGenerator.getRandomBigDecimalWithUpperBound(this.maxHospitalClaim));
                break;

            case Constants.VISION_CLAIM:
                myTestDocument.setClaimAmount(RandomDataGenerator.getRandomBigDecimalWithUpperBound(this.maxVisionClaim));
                break;

            case Constants.DENTAL_CLAIM:
                myTestDocument.setClaimAmount(RandomDataGenerator.getRandomBigDecimalWithUpperBound(this.maxDentalClaim));
                break;
        }

        return myTestDocument;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        new MdbLoadGenerator();
    }
}
