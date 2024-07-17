package test;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class SendTest {
    private static final String BASE = "/home/doodle/Downloads/Java Resources/test_files/e2/";
    private static final String URL = "http://localhost:8080/server/test";
    public static void main(String[] args) {
        File structureFile = new File(BASE + "classic.xml");
        File dictionaryFile = new File(BASE + "haha.txt");
        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart("structureFile", structureFile.getName(), RequestBody.create(structureFile, MediaType.parse("text/xml")))
                .addFormDataPart("dictionaryFile", dictionaryFile.getName(), RequestBody.create(dictionaryFile, MediaType.parse("text/plain")))
                .build();
        Request req = new Request.Builder().url(URL).post(body).build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(req);
        try (Response res = call.execute()) {
            System.out.println(res.body().string());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
