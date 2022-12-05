package ru.atom.chat;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.security.Policy;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;


@Controller
@RequestMapping("chat")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private HashMap<String, Date> personalHistory = new HashMap<>();
    private Queue<Message> messages = new ConcurrentLinkedQueue<>();
    private Map<String, String> usersOnline = new ConcurrentHashMap<>();

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name) {
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name, sorry :(");
        }
        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry :(");
        }
        if (usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged in:(");
        }
        usersOnline.put(name, name);
        messages.add(new Message(new Date(), "[" + name + "] logged in", ""));
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, -3);
        personalHistory.put(name, calendar.getTime());
        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> chat() {
        StringJoiner str = new StringJoiner("\n");
        ArrayList<Message> list = new ArrayList<>(messages);
        for (int i = 0; i < list.size(); ++i) {
            str.add(sdf.format(list.get(i).getDate())  + " [" + list.get(i).getName() + "] " + list.get(i).getMessage());
        }
        return new ResponseEntity<String>(String.valueOf(str),
                HttpStatus.OK);
    }

    /**
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<String> online() {
        return new HttpEntity<String>(usersOnline.keySet().stream().map(Objects::toString).collect(Collectors.joining("\n")));
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity logout(@RequestParam("name") String name) {
        usersOnline.remove(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
            path = "register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity logout(@RequestParam("name") String name, @RequestParam("pass") String pass) {
        usersOnline.remove(name);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity say(@RequestParam("name") String name, @RequestParam("msg") String msg) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.ok(("You have not already login"));
        }
        if ((new Date().getTime() - personalHistory.get(name).getTime()) < 3000) {
            return new ResponseEntity<>("Don't spamming, one msg once in 3 sec", HttpStatus.NO_CONTENT);
        }
        messages.add(new Message(new Date(), Jsoup.clean(msg, Safelist.basic()), name));
        personalHistory.put(name, new Date());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(
            path = "save",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity save() throws FileNotFoundException {
        //String str = String.join("\n", messages);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:\\Users\\stars\\Desktop\\JavaCoding\\Repos\\web_hackaton\\src\\main\\java\\ru\\atom\\chat\\History"))) {
            //bufferedWriter.write(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
