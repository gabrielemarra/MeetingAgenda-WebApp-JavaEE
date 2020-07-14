package it.polimi.tiw.beans;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class Meeting {
    private int idMeeting;
    private String title;
    private int duration;
    private int maxParticipants;
    private int idCreator;
    private String creatorName;
    private LocalDate date;
    private LocalTime time;
    private LocalTime endTime;

    public Meeting() {
        super();
    }

    public Meeting(int idMeeting, String title, int duration, int maxParticipants, int idCreator, LocalDate date, LocalTime time) {
        super();
        this.idMeeting = idMeeting;
        this.title = title;
        this.duration = duration;
        this.maxParticipants = maxParticipants;
        this.idCreator = idCreator;
        this.date = date;
        this.time = time;
    }

    public Meeting(int idMeeting, String title, int duration, int maxParticipants, int idCreator, String dateTimeString) {
        super();
        this.idMeeting = idMeeting;
        this.title = title;
        this.duration = duration;
        this.maxParticipants = maxParticipants;
        this.idCreator = idCreator;

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDate localDate = dateTime.toLocalDate();
        LocalTime localTime = dateTime.toLocalTime();

        this.date = localDate;
        this.time = localTime;
        this.endTime = this.time.plus(Duration.ofMinutes(this.duration));

    }

    public Meeting(Meeting meeting) {
        super();
        this.idMeeting = meeting.idMeeting;
        this.title = meeting.title;
        this.duration = meeting.duration;
        this.maxParticipants = meeting.maxParticipants;
        this.idCreator = meeting.idCreator;
        this.date = meeting.date;
        this.time = meeting.time;
        this.creatorName = meeting.creatorName;
    }

    public Meeting(TempMeeting tempMeeting) {
        super();
        this.title = tempMeeting.getTitle();
        this.duration = tempMeeting.getDuration();
        this.maxParticipants = tempMeeting.getMaxParticipants();
        this.idCreator = tempMeeting.getIdCreator();
        this.date = tempMeeting.getLocalDate();
        this.time = tempMeeting.getLocalTime();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getIdMeeting() {
        return idMeeting;
    }

    public void setIdMeeting(int idMeeting) {
        this.idMeeting = idMeeting;
    }

    public void setDateAndTime(String dateTimeString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDate localDate = dateTime.toLocalDate();
        LocalTime localTime = dateTime.toLocalTime();

        date = localDate;
        time = localTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "idMeeting=" + idMeeting +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", maxParticipants=" + maxParticipants +
                ", idCreator=" + idCreator +
                ", creatorName='" + creatorName + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

    public String getFormattedDateTime (){
        String formattedString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        formattedString+=" ";
        formattedString += time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return formattedString;
    }
}
