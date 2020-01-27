package org.mongodb;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    // Specific Claim Type Max Values
    private double maxDisabilityClaim = 25000;
    private double maxIllnessClaim = 10000;
    private double maxLifeClaim = 150000;
    private double maxHospitalClaim = 100000;
    private double maxVisionClaim = 15000;
    private double maxAccidentClaim = 75000;
    private double maxDentalClaim = 2500;

    /**
     * Rigorous Test :-)
     */
    @Test
    public void testDocument() {

        System.out.println("====== Generic Test Document ======");
        for(int idx = 0; idx < 2; idx++) {
            System.out.println(new MyTestDocument());
        }

        System.out.println("====== Max Per Claim Test Document ======");
        for(int idx = 0; idx < 2; idx++) {
            System.out.println(this.getTestDocumentMaxPerClaimType());
        }
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
}
