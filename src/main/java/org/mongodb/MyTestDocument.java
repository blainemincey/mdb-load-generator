package org.mongodb;

import com.github.javafaker.Faker;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.Date;

public class MyTestDocument {
    private ObjectId id;

    private String name;
    private String streetAddress;
    private String city;
    private String state;
    private String zip;
    private String employer;
    private String title;
    private String maritalStatus;
    private String gender;
    private String email;
    private String phoneNumber;
    private String healthProvider;

    // claim data
    private String claimType;
    private java.util.Date dateClaimSubmitted;
    private BigDecimal claimAmount;

    /**
     *
     */
    public MyTestDocument() {
        Faker faker = new Faker();

        this.setName(faker.name().fullName());
        this.setStreetAddress(faker.address().streetAddress());
        this.setCity(faker.address().cityName());
        this.setState(faker.address().stateAbbr());
        this.setZip(faker.address().zipCode());
        this.setEmployer(faker.company().name());
        this.setTitle(faker.job().title());
        this.setMaritalStatus(faker.demographic().maritalStatus());
        this.setGender(faker.demographic().sex());
        this.setEmail(faker.internet().safeEmailAddress());
        this.setPhoneNumber(faker.phoneNumber().cellPhone());
        this.setHealthProvider(faker.medical().hospitalName());
        this.setClaimType(RandomDataGenerator.getRandomClaimType());
        this.setDateClaimSubmitted(RandomDataGenerator.getRandomDateSubmitted());
        this.setClaimAmount(RandomDataGenerator.getRandomBigDecimal());
    }

    /**
     *
     * @param maxClaimAmount
     */
    public MyTestDocument(double maxClaimAmount) {
        Faker faker = new Faker();

        this.setName(faker.name().fullName());
        this.setStreetAddress(faker.address().streetAddress());
        this.setCity(faker.address().cityName());
        this.setState(faker.address().stateAbbr());
        this.setZip(faker.address().zipCode());
        this.setEmployer(faker.company().name());
        this.setTitle(faker.job().title());
        this.setMaritalStatus(faker.demographic().maritalStatus());
        this.setGender(faker.demographic().sex());
        this.setEmail(faker.internet().safeEmailAddress());
        this.setPhoneNumber(faker.phoneNumber().cellPhone());
        this.setHealthProvider(faker.medical().hospitalName());
        this.setClaimType(RandomDataGenerator.getRandomClaimType());
        this.setDateClaimSubmitted(RandomDataGenerator.getRandomDateSubmitted());
        this.setClaimAmount(RandomDataGenerator.getRandomBigDecimalWithUpperBound(maxClaimAmount));

    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHealthProvider() {
        return healthProvider;
    }

    public void setHealthProvider(String healthProvider) {
        this.healthProvider = healthProvider;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public Date getDateClaimSubmitted() {
        return dateClaimSubmitted;
    }

    public void setDateClaimSubmitted(Date dateClaimSubmitted) {
        this.dateClaimSubmitted = dateClaimSubmitted;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    @Override
    public String toString() {
        return "MyTestDocument{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", employer='" + employer + '\'' +
                ", title='" + title + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", healthProvider='" + healthProvider + '\'' +
                ", claimType='" + claimType + '\'' +
                ", dateClaimSubmitted=" + dateClaimSubmitted +
                ", claimAmount=" + claimAmount +
                '}';
    }

    /**
     * Smoke test
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(new MyTestDocument(1500));
    }
}
