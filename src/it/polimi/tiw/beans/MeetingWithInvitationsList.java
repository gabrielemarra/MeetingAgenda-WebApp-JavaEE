package it.polimi.tiw.beans;

import java.util.ArrayList;
import java.util.List;

public class MeetingWithInvitationsList {
    private TempMeeting meeting;
    private List<User> participantsList;

    public MeetingWithInvitationsList() {
        super();
        participantsList = new ArrayList<>();
    }

    public MeetingWithInvitationsList(TempMeeting meeting, List<User> participantsList) {
        this.meeting = meeting;
        this.participantsList = participantsList;
    }

    public List<User> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(List<User> participantsList) {
        this.participantsList = participantsList;
    }

    public void addParticipant(User participant) {
        participantsList.add(participant);
    }

    public Meeting getRealMeeting() {
        Meeting realMeeting = new Meeting(meeting);
        return realMeeting;
    }

    public void setMeeting(TempMeeting meeting) {
        this.meeting = meeting;
    }
}
