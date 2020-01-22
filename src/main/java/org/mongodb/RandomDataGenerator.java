package org.mongodb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 */
public class RandomDataGenerator {

    // Claim Types
    private static List<String> claimTypes
            = Arrays.asList("Disability", "Illness", "Life", "Hospital", "Vision", "Accident", "Dental");

    private static Random rng = new Random();

    /**
     *
     * @return
     */
    public static BigDecimal getRandomBigDecimal() {
        BigDecimal bd = new BigDecimal(Double.toString(Math.random() * 1500));
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd;
    }

    /**
     *
     * @return
     */
    public static String getRandomClaimType() {
        int randomType = rng.nextInt(RandomDataGenerator.claimTypes.size());

        return RandomDataGenerator.claimTypes.get(randomType);
    }

    /**
     *
     * @return
     */
    public static java.util.Date getRandomDateSubmitted() {
        int randomMonth = (int)(Math.random() * 12); // 12 months
        int randomYear = (int)(Math.random() * 5); // 5 years
        int randomDay = (int)(Math.random() * 30); // 30 days

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime dateTimeSubmitted =
                localDateTime.minusMonths(randomMonth).minusDays(randomDay).minusYears(randomYear);

        return java.util.Date.from(dateTimeSubmitted.atZone( ZoneId.systemDefault()).toInstant());
    }

}
