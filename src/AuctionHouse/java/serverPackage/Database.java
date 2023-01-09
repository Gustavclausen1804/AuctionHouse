package serverPackage;

import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;

public class Database {
    Gson gson = new Gson();

    public void addUserToDB(User user) throws IOException {
        try (FileWriter writer = new FileWriter(
                "src/AuctionHouse/java/serverPackage/Database.json")) {
            gson.toJson(user, writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
