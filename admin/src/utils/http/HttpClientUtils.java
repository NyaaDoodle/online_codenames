package utils.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.File;

public class HttpClientUtils extends ClientHttpClientUtils {
    private static final OkHttpClient HTTP_CLIENT = getHttpClient();

    public static void sendAddGameRequest(final File structureFile, final File dictionaryFile) {

    }

    private static Request buildAddGameRequest(final File structureFile, final File dictionaryFile) {

    }
}
