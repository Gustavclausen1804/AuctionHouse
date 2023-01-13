package serverPackage;

import org.apache.commons.lang3.ObjectUtils;
import org.jspace.*;

import java.net.URI;

public class User {


    String userId;
    String userName;
    String userAddress;
    long userTimeStamp;
    Space userSpace;
    int wallet;


    public User(String userName, String userAddress, long userTimeStamp, SpaceRepository mainRepository, Space usersSpace) throws InterruptedException {
        this.userId = getUserIDfromServer(usersSpace);
        this.userName = userName;
        this.userAddress = userAddress;
        this.userTimeStamp = userTimeStamp;
        this.userSpace = createUserSpace(mainRepository);
    }


    public Space createUserSpace(SpaceRepository mainRepository) throws InterruptedException {
        // Create a userSpace for the user
        SequentialSpace userSpace = new SequentialSpace();
        // Add the space to the repository
        mainRepository.add("user" + userId, userSpace);


        return userSpace;

    }

    public String getUserIDfromServer(Space usersSpace) throws InterruptedException {


        Object[] usersCount = usersSpace.getp(new FormalField(String.class), new FormalField(Integer.class));
        if (usersCount != null) {

            int count = (int) usersCount[1] + 1;
            usersSpace.put(usersCount[0], count);
            return "" + count;
        } else {

            usersSpace.put("users", 1);
            return "" + 1;


        }
    }

    public void depositToWallet(int deposit) {
        wallet += deposit;
    }


}







