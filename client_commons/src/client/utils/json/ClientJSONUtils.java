package client.utils.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ClientJSONUtils {
    private static final Gson gson = new Gson();

    public static Gson getGson() { return gson; }

    public static String toJson(final Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(final String json, final Class<T> tClass) throws JsonSyntaxException {
        return gson.fromJson(json, tClass);
    }
}
