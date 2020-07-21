package it.polimi.tiw.beans;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TempMeeting {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private String title;
    private LocalDate localDate;
    private LocalTime localTime;
    private int idCreator;
    private int maxParticipants;
    private int duration;
    private int attempts;

    public TempMeeting() {

    }

    public TempMeeting(String title, LocalDate localDate, LocalTime localTime, int idCreator, int maxParticipants, int duration, int attempts) {
        this.title = title;
        this.localDate = localDate;
        this.localTime = localTime;
        this.idCreator = idCreator;
        this.maxParticipants = maxParticipants;
        this.duration = duration;
        this.attempts = attempts;
    }

    public TempMeeting(String title, String dateTimeString, int idCreator, int maxParticipants, int duration, int attempts) {
        this.title = title;
        this.idCreator = idCreator;
        this.maxParticipants = maxParticipants;
        this.duration = duration;
        this.attempts = attempts;

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.localDate = dateTime.toLocalDate();
        this.localTime = dateTime.toLocalTime();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(int idCreator) {
        this.idCreator = idCreator;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public static void checkInsertedMeeting(TempMeeting tempMeeting) throws InvalidParameterException {
        LocalDate now = LocalDate.now();

        //check if title is right

        if (tempMeeting.getTitle() == null) {
            throw new InvalidParameterException("Invalid Parameters. Please try again.");
        }
        if (tempMeeting.getTitle().length() < 4) {
            throw new InvalidParameterException("Meeting title is too short");

        } else if (tempMeeting.getTitle().length() >= 60) {
            throw new InvalidParameterException("Meeting title is too long");

        } else if (tempMeeting.getDuration() < 5) {
            throw new InvalidParameterException("Meeting duration is too short, at least 5 minutes");

        } else if (tempMeeting.getMaxParticipants() < 2 || tempMeeting.getMaxParticipants() > 250) {
            throw new InvalidParameterException("Invalid max partecipants number");

        } else if (tempMeeting.getLocalDate() == null) {
            throw new InvalidParameterException("Invalid Parameters. Please try again.");

        } else if (tempMeeting.getLocalDate().isBefore(now)) {
            throw new InvalidParameterException("Meeting duration is too short, at least 5 minutes");

        } else if (tempMeeting.getLocalTime() == null) {
            throw new InvalidParameterException("Invalid Parameters. Please try again.");

        } else if (tempMeeting.getLocalDate().isEqual(now) &&
                tempMeeting.getLocalTime().isBefore(LocalDateTime.now().toLocalTime())) {
            throw new InvalidParameterException("Please enter a later date");

        } else if (tempMeeting.getIdCreator() <= 0) {
            throw new InvalidParameterException("Please enter a later date");

        }
    }

    public String getFormattedDateTime() {
        String formattedString = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        formattedString += " ";
        formattedString += localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return formattedString;
    }

    public static void checkPrimaryKeySyntax(String title, LocalDate localDate, LocalTime localTime) throws InvalidParameterException {
        LocalDate now = LocalDate.now();

        if (title.length() < 4) {
            throw new InvalidParameterException("Meeting title is too short");
        } else if (localDate.isBefore(now)) {
            throw new InvalidParameterException("Meeting " +
                    "duration is too short, at least 5 minutes");
        } else if (localDate.isEqual(now) && localTime.isBefore(LocalDateTime.now().toLocalTime())) {
            throw new InvalidParameterException("Please enter a later date");
        }
    }
}
