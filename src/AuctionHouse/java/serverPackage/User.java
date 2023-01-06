package serverPackage;

import org.jspace.*;

import java.net.URI;

public class User {


    String userId;
    String userName;
    String userAddress;
    long userTimeStamp;
    Space userSpace;


    public User(String userId, String userName, String userAddress, long userTimeStamp, SpaceRepository mainRepository, URI serverURI ) {
        this.userId = getUserIDfromServer(mainRepository);
        this.userName = userName;
        this.userAddress = userAddress;
        this.userTimeStamp = userTimeStamp;
        this.userSpace = createUserSpace(mainRepository, serverURI);
    }


    public Space createUserSpace(SpaceRepository mainRepository, URI serverURI) {
        // Create a userSpace for the user
        SequentialSpace userSpace = new SequentialSpace();

        // Add the space to the repository
        mainRepository.add(userId, userSpace);

        // Open a gate
        String gateUri = "tcp://" + serverURI.getHost() + ":" + serverURI.getPort() +  "?keep" ;
        System.out.println("Opening repository gate at " + gateUri + "...");
        mainRepository.addGate(gateUri);
        return userSpace;

    }

    public String getUserIDfromServer(SpaceRepository mainRepository) {
     return "" + mainRepository.size();
    }

    }







