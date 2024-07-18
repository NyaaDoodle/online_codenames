package test;

import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class NewGameServletTest {
    // TODO does the xml file exist? is it even ".xml"?
    // TODO does the txt file exist? is it even ".txt"?
    private static final String TEST_DIR = "c:\\Users\\bmroo\\Downloads\\Java Resources\\test_files\\e2\\";
    //private static final String TEST_DIR = "/home/doodle/Downloads/Java Resources/test_files/e2/";
    private static final String BASE_URL = "http://localhost:8080/server";
    private static final String NEW_GAME_URI = "/new-game";
    private static final String STRUCTURE_FILE_PART_NAME = "structure-file";
    private static final String DICTIONARY_FILE_PART_NAME = "dictionary-file";
    public static void main(String[] args) {
        File structureFile = new File(TEST_DIR + "classic-mod.xml");
        File dictionaryFile = new File(TEST_DIR + "moby dick.txt");
        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart(STRUCTURE_FILE_PART_NAME, structureFile.getName(), RequestBody.create(structureFile, MediaType.parse("text/xml")))
                .addFormDataPart(DICTIONARY_FILE_PART_NAME, dictionaryFile.getName(), RequestBody.create(dictionaryFile, MediaType.parse("text/plain")))
                .build();
        Request req = new Request.Builder().url(BASE_URL + NEW_GAME_URI).post(body).build();
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
