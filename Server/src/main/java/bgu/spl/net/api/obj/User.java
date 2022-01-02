package bgu.spl.net.api.obj;

public class User {
    private String username;
    private String pw;
    private String birthday;

    public User(String username, String pw, String birthday) {
        this.username = username;
        this.pw = pw;
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public String getPw() {
        return pw;
    }

    public String getBirthday() {
        return birthday;
    }
}
