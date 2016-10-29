package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.Database;
import minebustergame.MineField;

public class Serialization {
    
    public static void serialize(Object o, String filename) {
        Gson encoder = new GsonBuilder().setPrettyPrinting().create();
        Database.save(filename, encoder.toJson(o));
    }
    
    public static Object deserialize(String filename) {
        Gson decoder = new Gson();        
        return decoder.fromJson(Database.getSave(filename), MineField.class);
    }
    
    public static String serializeToString(Object o) {
        Gson encoder = new Gson();
        return encoder.toJson(o);
    }
    
    public static Object deserializeFromString(String str) {
        Gson decoder = new Gson();
        return decoder.fromJson(str, MineField.class);
    }
}
