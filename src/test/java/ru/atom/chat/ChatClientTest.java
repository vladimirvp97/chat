package ru.atom.chat;

import okhttp3.Response;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ChatClientTest {
    private static final Logger log = LoggerFactory.getLogger(ChatClientTest.class);

    private static String MY_NAME_IN_CHAT = "I_AM_STUPID";
    private static String MY_MESSAGE_TO_CHAT = "KILL_ME_SOMEONE";
    @Autowired
    public ChatController chatController;
    @Autowired
    public TestRestTemplate testRestTemplate;

    @Test
    public void login() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", "stars");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/chat/login", HttpMethod.POST, entity, String.class); //postForEntity("/chat/login", "Vova", String.class);
        Assert.assertTrue(response.getStatusCodeValue() == 200);
//        Response response = ChatClient.login(MY_NAME_IN_CHAT);
//        log.info("[" + response + "]");
//        String body = response.body().string();
//        log.info(body);
//        Assert.assertTrue(response.code() == 200 || body.equals("Already logged in:("));
    }

    @Test
    public void viewChat() throws IOException {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/chat/chat", String.class);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void viewOnline() throws IOException {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/chat/online", String.class);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void say() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", "stars");
        map.add("msg", "message");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = testRestTemplate.exchange("/chat/say", HttpMethod.POST, entity, String.class);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void save() throws IOException {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/chat/save", String.class);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

}
