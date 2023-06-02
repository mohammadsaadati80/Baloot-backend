package org.mm.Controllers;

import org.mm.Service.Baloot;
import org.mm.Entity.User;
import org.mm.Exceptions.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import static javax.xml.bind.DatatypeConverter.parseDateTime;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.io.IOException;
import java.lang.runtime.ObjectMethods;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;

//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

@CrossOrigin(origins = "http://localhost:3000",exposedHeaders = "*")
@RestController
public class LoginController {
    @Autowired
    private Baloot baloot;


    @RequestMapping(value = "/get_userss", method = RequestMethod.GET)
    public String getUsername(){
        try {
            return baloot.getLoginUsername();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    protected ResponseEntity<Void> login(@RequestBody Map<String, String> user_info){
        try {
            String jwtToken = baloot.login(user_info.get("username"), user_info.get("password"));
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("token", jwtToken);
            map.add("username",user_info.get("username"));
//            ResponseEntity<Void> x = new ResponseEntity<>(map,HttpStatus.OK);
            return new ResponseEntity<>(map,HttpStatus.OK);
        } catch (UserNotFoundError ignored) {
            System.out.println(ignored.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    protected ResponseEntity<Void> signup(@RequestBody Map<String, String> user_info){
        String username = user_info.get("username");
        String email = user_info.get("email");
        String password = user_info.get("password");
        String address = user_info.get("address");
        String birthDate = user_info.get("birthDate");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(birthDate);
            baloot.signup(username, email, password, date, address);
        } catch (UserAlreadyExistsError e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            String jwt_token = baloot.login(username, password);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("token", jwt_token);
            map.add("username",user_info.get("username"));
            return new ResponseEntity<>(map,HttpStatus.OK);
        } catch (UserNotFoundError ignored) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "کاربر با موفقیت از سامانه خارج شد.")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    protected void logout(){
        baloot.logout();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/login/user",method = RequestMethod.GET)
    protected User getLoggedInUser(){
        try {
            return baloot.getUserById(baloot.getLoginUsername());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "کاربر با موفقیت ثبت نام شد.")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    protected void registerUser(@RequestBody Map<String, String> user_info){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

//        Date date = new Date(user_info.get("birthDate"));
        try {
            Date date = format.parse(user_info.get("birthDate"));
            baloot.registerUser(user_info.get("username"), user_info.get("password"), user_info.get("email"), date, user_info.get("address"));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping(value = "/callback",method = RequestMethod.POST)
    protected ResponseEntity<Void> callbackLogin(@RequestParam(value = "code", required = true) String code) throws IOException, InterruptedException, ParseException {
        if(baloot.getLoginUser() != null) {
            String jwt_token = baloot.createJwtToken(baloot.getLoginUser().getUsername());
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("token", jwt_token);
            map.add("username",baloot.getLoginUser().getUsername());
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
        String client_id = "3e057dd17148426a419e";
        String client_secret = "11fea6c722a8208114a9bdc747f16e232df22344";
        String access_token_url = String.format(
                "https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s",
                client_id, client_secret, code
        );
        HttpClient client = HttpClient.newHttpClient();
        URI access_token_uri = URI.create(access_token_url);
        HttpRequest.Builder access_token_builder = HttpRequest.newBuilder().uri(access_token_uri);
        HttpRequest access_token_request = access_token_builder
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> access_token_result = client.send(access_token_request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> result_body = mapper.readValue(access_token_result.body(),HashMap.class);
        String access_token = (String) result_body.get("access_token");
        URI user_data_uri = URI.create("https://api.github.com/user");
        HttpRequest.Builder user_data_builder = HttpRequest.newBuilder().uri(user_data_uri);
        HttpRequest request = user_data_builder.GET()
                .header("Authorization", String.format("token %s",access_token))
                .build();
        HttpResponse<String> user_data_result = client.send(request,HttpResponse.BodyHandlers.ofString());
        HashMap data_body = mapper.readValue(user_data_result.body(),HashMap.class);
        String email = (String) data_body.get("email");
        String username = (String) data_body.get("login");
        String address = "";
        String bday = (String) data_body.get("created_at");
        Calendar cal = parseDateTime(bday);
        cal.add(Calendar.YEAR, -18);
        Date date = cal.getTime();
        String birthdate = new SimpleDateFormat("yyyy-MM-dd").format(date).toString();
        String password = "null";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthDate = format.parse(birthdate);
            baloot.signup(username, email, password, birthDate, address);
        } catch (UserAlreadyExistsError e) {
            Date birthDate = format.parse(birthdate);
            baloot.updateUser(username,email,password,birthDate, address);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        baloot.loginWithJwtToken(username);
        String jwt_token = baloot.createJwtToken(email);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token", jwt_token);
        map.add("username",username);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
}
