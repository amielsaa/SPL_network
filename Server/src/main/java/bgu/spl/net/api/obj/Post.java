package bgu.spl.net.api.obj;

import java.util.List;

public class Post {

    private List<String> receivers;
    private String sender;
    private String content;
    private String type;

    public Post(String sender,String content,String type) {
        //this.receivers = receivers;
        this.sender = sender;
        this.content = content;
        this.type = type;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }
}
