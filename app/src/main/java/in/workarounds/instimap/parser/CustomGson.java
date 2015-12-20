package in.workarounds.instimap.parser;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.workarounds.instimap.models.Notice;
import in.workarounds.instimap.models.Venue;

public class CustomGson {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson;

    public CustomGson() {
        this.gsonBuilder.registerTypeAdapter(Notice.class, new NoticeDeserializer());
        this.gsonBuilder.registerTypeAdapter(Venue.class, new VenueDeserializer());
        this.gson = this.gsonBuilder.create();
    }

    public Gson getGson() {
        return this.gson;
    }
}
