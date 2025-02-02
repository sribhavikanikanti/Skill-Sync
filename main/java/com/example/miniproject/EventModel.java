package com.example.miniproject;

import java.util.List;

public class EventModel {
    private String eventId;
    private String eventName;
    private String description;
    private String registrationStart;
    private String registrationDeadline;
    private String duration;
    private String navigation;
    private String link;
    private List<String> applicableYears;

    // Default constructor required for Firestore
    public EventModel() {}

    // Constructor with all fields
    public EventModel(String eventId, String eventName, String description, String registrationStart,
                      String registrationDeadline, String duration, String navigation,
                      String link, List<String> applicableYears) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.description = description;
        this.registrationStart = registrationStart;
        this.registrationDeadline = registrationDeadline;
        this.duration = duration;
        this.navigation = navigation;
        this.link = link;
        this.applicableYears = applicableYears;
    }

    // Getters and Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(String registrationStart) {
        this.registrationStart = registrationStart;
    }

    public String getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(String registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getApplicableYears() {
        return applicableYears;
    }

    public void setApplicableYears(List<String> applicableYears) {
        this.applicableYears = applicableYears;
    }
}




/*package com.example.miniproject;

import java.util.List;

public class EventModel {

    private String eventId; // Add this field
    private String event_name;
    private String description;
    private String registration_Start;
    private String registration_Deadline;
    private String duration;
    private String navigation;
    private String link;
    private List<String> applicable_Years;*/

    // No-argument constructor
    /*public EventModel() {
        // Required for Firestore deserialization
    }

    // Constructor with all fields
    public EventModel(String eventId, String eventName, String description, String registrationStart, String registrationDeadline,
                      String duration, String navigation, String link, List<String> applicableYears) {
        this.eventId = eventId;
        this.event_name = eventName;
        this.description = description;
        this.registration_Start = registrationStart;
        this.registration_Deadline = registrationDeadline;
        this.duration = duration;
        this.navigation = navigation;
        this.link = link;
        this.applicable_Years = applicableYears;
    }

    // Getters and setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return event_name;
    }

    public void setEventName(String eventName) {
        this.event_name = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegistrationStart() {
        return registration_Start;
    }

    public void setRegistrationStart(String registrationStart) {
        this.registration_Start = registrationStart;
    }

    public String getRegistrationDeadline() {
        return registration_Deadline;
    }

    public void setRegistrationDeadline(String registrationDeadline) {
        this.registration_Deadline = registrationDeadline;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getApplicableYears() {
        return applicable_Years;
    }

    public void setApplicableYears(List<String> applicableYears) {
        this.applicable_Years = applicableYears;
    }
}*/
