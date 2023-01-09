package clientPackage;
import org.jspace.*;

public class User {

    String userId;
    String userName;
    String userAddress;
    long userTimeStamp;
    Space userSpace;


    public User(String userName, String userAddress, long userTimeStamp) {
//        this.userId = userId;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userTimeStamp = userTimeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public long getUserTimeStamp() {
        return userTimeStamp;
    }

    public void setUserTimeStamp(long userTimeStamp) {
        this.userTimeStamp = userTimeStamp;
    }

    public Space getUserSpace() {
        return userSpace;
    }

    public void setUserSpace(Space userSpace) {
        this.userSpace = userSpace;
    }

    public void putUsertoServer(RemoteSpace space) { // put UserInfo to its own repository. space should be addresse/userID)
        Object[] userInfo = {userName, userAddress, userTimeStamp};

        try {
            space.put(userInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }








}
