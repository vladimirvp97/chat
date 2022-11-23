package ru.atom.chat;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)


public class ChatClient {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String PROTOCOL = "http://";
    private static final String HOST = "localhost";
    private static final String PORT = ":8080";

    @Autowired
    public static ChatController chatController;
    @Autowired
    public static TestRestTemplate testRestTemplate;


    public static Response login(String name) throws IOException {


        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, "name=" + name))
                .url(PROTOCOL + HOST + PORT + "/chat/login")
                .build();

        return client.newCall(request).execute();
    }


    public static Response viewChat() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/chat/chat")
                .addHeader("host", HOST + PORT)
                .build();

        return client.newCall(request).execute();
    }


    public static Response viewOnline() throws IOException {
        throw new UnsupportedOperationException();
    }

    public static Response say(String name, String msg) {
        throw new UnsupportedOperationException();
    }
}