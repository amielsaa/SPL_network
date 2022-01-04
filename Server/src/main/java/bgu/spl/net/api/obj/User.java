package bgu.spl.net.api.obj;

public class User {
    private String username;
    private String pw;
    private String birthday;
    private boolean connected;

    public User(String username, String pw, String birthday) {
        this.username = username;
        this.pw = pw;
        this.birthday = birthday;
        this.connected=false;
    }

    public void setConnected(boolean connect) {
        this.connected=connect;
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getPw() {
        return pw;
    }

    public String getBirthday() {
        return birthday;
    }
}
