package in.workarounds.instimap.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import in.workarounds.instimap.models.Venue;

import java.lang.reflect.Type;

public class VenueDeserializer
        implements JsonDeserializer<Venue> {

    @Override
    public Venue deserialize(JsonElement json, Type type,
                              JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        Venue venue = gson.fromJson(json, Venue.class);
        venue.setDbId(venue.getId());
        venue.setId(null);
        return venue;
    }
}