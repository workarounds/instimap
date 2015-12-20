package in.workarounds.instimap.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import in.workarounds.instimap.models.Notice;

import java.lang.reflect.Type;

public class NoticeDeserializer
        implements JsonDeserializer<Notice> {

    @Override
    public Notice deserialize(JsonElement json, Type type,
                              JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        Notice notice = gson.fromJson(json, Notice.class);
        notice.setDbId(notice.getId());
        notice.setId(null);

        JsonObject jsonElement = json.getAsJsonObject();
        String data = jsonElement.get("data").getAsString();
        String corners = jsonElement.get("corners").getAsString();

        notice.setDataJson(data);
        notice.setCornersJson(corners);
        return notice;
    }
}