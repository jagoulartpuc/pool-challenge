package southsystem.votechallenge.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "pool")
public class Poll {

    @Id
    private String id;
    private String topicName;
    private int countYes;
    private int countNo;
    private boolean opened;
    private int duration;
    private List<String> employeesWhoVoted;

    public Poll(String id, String topicName, int duration) {
        this.id = id;
        this.topicName = topicName;
        this.countYes = 0;
        this.countNo = 0;
        this.opened = true;
        this.duration = duration;
        this.employeesWhoVoted = new ArrayList<>();
    }

    public List<String> getEmployeesWhoVoted() {
        return employeesWhoVoted;
    }

    public void setEmployeesWhoVoted(List<String> employeesWhoVoted) {
        this.employeesWhoVoted = employeesWhoVoted;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getCountYes() {
        return countYes;
    }

    public void setCountYes(int countYes) {
        this.countYes = countYes;
    }

    public int getCountNo() {
        return countNo;
    }

    public void setCountNo(int countNo) {
        this.countNo = countNo;
    }
}
