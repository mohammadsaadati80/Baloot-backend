package org.mm.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mm.Exceptions.*;
import org.mm.Utils.HTTPRequestHandler;
import org.mm.Entity.*;
import org.mm.Repository.*;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

@Service
public class Baloot {

    private static Baloot instance = null;
    static final String USERS_URL = "http://5.253.25.110:5000/api/users";
    static final String COMMODITIES_URL = "http://5.253.25.110:5000/api/v2/commodities";
    static final String PROVIDERS_URL = "http://5.253.25.110:5000/api/v2/providers";
    static final String COMMENTS_URL = "http://5.253.25.110:5000/api/comments";
    static final String DISCOUNT_URL = "http://5.253.25.110:5000/api/discount";
    private ObjectMapper mapper;
    private Map<String, User> users;
    private Map<Integer, Provider> providers;
    private Map<Integer, Commodity> commodities;
    private Map<Integer, Comment> comments;
    private Map<String, Discount> discounts;
    private String loginUsername;

    private User loginUser;

    private final ProviderRepository providerRepository;
    private final CommodityRepository commodityRepository;
    private final CommentRepository commentRepository;
    private final RateRepository rateRepository;
    private VoteRepository voteRepository;
    private final UserRepository userRepository;

    private DiscountRepository discountRepository;

    @Autowired
    private Baloot(ProviderRepository provider_rep, CommodityRepository commodity_rep, VoteRepository vote_repo,
                  CommentRepository comment_rep, RateRepository rate_rep, UserRepository user_rep, DiscountRepository discount_repo) {
        mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        users = new HashMap<>();
        providers = new HashMap<>();
        commodities = new HashMap<>();
        comments = new HashMap<>();
        discounts = new HashMap<>();
        loginUsername = "";

        providerRepository = provider_rep;
        commodityRepository = commodity_rep;
        commentRepository = comment_rep;
        rateRepository = rate_rep;
        userRepository = user_rep;
        voteRepository = vote_repo;
        discountRepository = discount_repo;
        setDB();
    }

    public void setDB() {
        if (!commodityRepository.findAll().isEmpty())
            return;
        try {
            System.out.println("Importing Users...");
            List<User> users = importUsersFromWeb(USERS_URL);
            System.out.println("Importing Providers...");
            List<Provider> providers = importProvidersFromWeb(PROVIDERS_URL);
            System.out.println("Importing Commodities...");
            List<Commodity> commodities = importCommoditiesFromWeb(COMMODITIES_URL);
            System.out.println("Importing Comments...");
            List<Comment> comments = importCommentsFromWeb(COMMENTS_URL);
            System.out.println("Importing Discounts...");
            List<Discount> discounts = importDiscountsFromWeb(DISCOUNT_URL);

            for (Commodity commodity : commodities) {
                Integer providerId = commodity.getProviderId();
                for (Provider provider : providers) {
                    if (providerId.equals(provider.getId())) {
                        commodity.setProvider(provider);
                    }
                }
            }
            for (Provider provider: providers) {
                providerRepository.save(provider);
            }
            for (Commodity commodity: commodities)
            commodityRepository.save(commodity);
            for (User user : users) {
                userRepository.save(user);
            }
            hashUsersPasswords();
            for (Comment comment: comments) {
                boolean isUserFound = false;
                String username = "";
                for (User entry : users) {
                    if (entry.getEmail().equals(comment.getUserEmail())) {
                        isUserFound = true;
                        username = entry.getUsername();
                        break;
                    }
                }
                if (isUserFound) {
//                    Integer key = comments.size();
//                    comment.addId(key);
                    comment.addUsername(username);
                }
                try {
                    commentRepository.save(comment);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            for (Discount discount:discounts)
            discountRepository.save(discount);
        } catch (Exception ignored) {
            System.out.print(ignored.getMessage());
        }
    }

    private void hashUsersPasswords() {
        List<User> users = userRepository.findAll();
        for (User user: users) {
            String hashedPassword = String.valueOf(user.getPassword().hashCode());
            user.setPassword(hashedPassword);
            userRepository.save(user);
        }
    }

    private static List<User> importUsersFromWeb(String usersUrl) throws Exception{
        String UsersJsonString = HTTPRequestHandler.getRequest(usersUrl);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<User> users = gson.fromJson(UsersJsonString, new TypeToken<List<User>>() {}.getType());
        return users;
    }

    private static List<Commodity> importCommoditiesFromWeb(String commoditiesUrl) throws Exception{
        String CommoditiesJsonString = HTTPRequestHandler.getRequest(commoditiesUrl);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Commodity> commodities = gson.fromJson(CommoditiesJsonString, new TypeToken<List<Commodity>>() {}.getType());
        return commodities;
    }

    private static List<Provider> importProvidersFromWeb(String providersUrl) throws Exception{
        String ProvidersJsonString = HTTPRequestHandler.getRequest(providersUrl);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Provider> providers = gson.fromJson(ProvidersJsonString, new TypeToken<List<Provider>>() {}.getType());
        return providers;
    }

    private static List<Comment> importCommentsFromWeb(String commentsUrl) throws Exception{
        String CommentsJsonString = HTTPRequestHandler.getRequest(commentsUrl);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CommentsJsonString = CommentsJsonString.replaceAll("\"\"", "2024-01-01"); // TODO
        List<Comment> comments = gson.fromJson(CommentsJsonString, new TypeToken<List<Comment>>() {}.getType());
        return comments;
    }

    private static List<Discount> importDiscountsFromWeb(String discountsUrl) throws Exception{
        String DiscountsJsonString = HTTPRequestHandler.getRequest(discountsUrl);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Discount> discounts = gson.fromJson(DiscountsJsonString, new TypeToken<List<Discount>>() {}.getType());
        return discounts;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Provider> getProviders() {
        return providerRepository.findAll();
    }

    public Map<Integer, Commodity> getCommodities() {
        return commodities;
    }

    public Map<String, Discount> getDiscounts() { return discounts; }

    public void addUser(User user) throws Exception {
        userRepository.save(user);
    }

    public void addProvider(Provider provider) throws Exception {
        if (!provider.isValidCommand())
            throw new InvalidCommandError();
        else {
            if (providers.containsKey(provider.getId())) {
                providers.get(provider.getId()).update(provider);
            }
            else {
                providers.put(provider.getId(), provider);
            }
        }
    }

    public void addCommodity(Commodity commodity) throws Exception {
        if (!commodity.isValidCommand())
            throw new InvalidCommandError();
        else if (!providers.containsKey(commodity.getProviderId()))
            throw new ProviderNotFoundError();
        else {
            if (commodities.containsKey(commodity.getId())) {
                commodities.get(commodity.getId()).update(commodity);
                providers.get(commodity.getProviderId()).addCommodity(commodity);
            }
            else {
                commodities.put(commodity.getId(), commodity);
                providers.get(commodity.getProviderId()).addCommodity(commodity);
            }
        }
    }

    public void addComment(Comment comment) throws Exception { // TODO
//        comment.setUsername(getLoginUsername());
//        commentRepository.save(comment);
//        Commodity commodity = getCommodityById(comment.getCommodityId());
//        commodity.addComment(comment);
//        commodityRepository.save(commodity);
    }

    public void addDiscount(Discount discount) throws Exception {
        if (!discount.isValidCommand())
            throw new InvalidCommandError();
        else if (!discount.isValidDiscount())
            throw new InvalidDiscountError();
        else {
            if (discounts.containsKey(discount.getDiscountCode()))
                discounts.get(discount.getDiscountCode()).update(discount);
            else
                discounts.put(discount.getDiscountCode(), discount);
        }
    }

    public List<Commodity> getCommoditiesList()  {
        return commodityRepository.findAll();
    }

    public void rateCommodity(Rate rate) throws Exception {
        Rate _rate = rateRepository.findByUsernameAndCommodityId(getLoginUsername(), rate.getCommodityId());
        Optional<Commodity> m = commodityRepository.findById(rate.getCommodityId());
        Commodity commodity = m.get();
        if (_rate == null) {
            rateRepository.save(rate);
            commodity.addRate(rate);
            commodityRepository.save(commodity);
        } else {
            rateRepository.save(rate);
        }

    }

    public void addToBuyList(String username, Integer commodityId) throws Exception {
        Optional<Commodity> m = commodityRepository.findById(commodityId);
        Commodity commodity;
        try {
            commodity = m.get();
        } catch (Exception e) {
            return;
        }
        Optional<User> u = userRepository.findById(username);
        User user;
        try {
            user = u.get();
        } catch (Exception e) {
            return;
        }
        user.addToBuyList(commodity);
        userRepository.save(user);
    }

    public void removeFromBuyList(String username, Integer commodityId) throws Exception {
        Optional<User> user_DAO = userRepository.findById(username);
        Optional<Commodity> commodity_DAO = commodityRepository.findById(commodityId);
        User user = user_DAO.get();
        Commodity commodity = commodity_DAO.get();
        user.removeFromBuyList(commodity);
        userRepository.save(user);
    }

    public Commodity getCommodityById(Integer id) throws Exception {
        Optional <Commodity> m = commodityRepository.findById(id);
        Commodity commodity;
        try {
            commodity = m.get();
            return commodity;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Commodity> getCommoditiesByCategory(String category) throws Exception {
        return commodityRepository.findByCategories(category);
    }

    public List<Commodity> getBuyList(String username) throws Exception {
        Optional<User> user = userRepository.findById(username);

        return (new ArrayList<>(user.get().getBuyList())) ;
    }

    public void addCredit(String username, Integer credit) throws Exception {
        Optional<User> u = userRepository.findById(username);
        User user = u.get();
        user.addCredit(credit);
        userRepository.save(user);
    }

//    public List<Commodity> getCommoditiesByPrice(Integer startPrice, Integer endPrice) throws Exception {
//        return commodityRepository.findByPriceLike(startPrice + "/%");
//    }

    public void voteComment(String username, Integer commentId, Integer vote) throws Exception { //TODO
//        if (username==null || commentId==null || commentId==0.0f)
//            throw new InvalidCommandError();
//        else {
//            if (!users.containsKey(username))
//                throw new UserNotFoundError();
//            else if (!comments.containsKey(commentId))
//                throw new CommentNotFound();
//            else if (!(vote==-1 || vote==0 || vote==1))
//                throw new InvalidVoteScoreError();
//            else
//                comments.get(commentId).addVote(username, vote);
//        }


    }

    public List<Comment> getCommentByCommodity(Integer commodityId) throws Exception {
//        if (commodityId==null || commodityId==0.0f) TODO
//            throw new InvalidCommandError();
//        else {
//            List<Comment> commentList = new ArrayList<>();
//            for (Map.Entry<Integer, Comment> entry : comments.entrySet())
//                if (entry.getValue().getCommodityId().equals(commodityId))
//                    commentList.add(entry.getValue());
//            return commentList;
//        }
        return new ArrayList<Comment>();
    }

    public void userBuyListPayment(String username) throws Exception { //Todo
//        if (username==null)
//            throw new InvalidCommandError();
//        else {
//            if (!users.containsKey(username))
//                throw new UserNotFoundError();
//            else if (users.get(username).getBuyList().size() == 0)
//                throw new UserBuyListIsEmptyError();
//            else if (!users.get(username).haveEnoughCredit())
//                throw new UserNotHaveEnoughCreditError();
//            else {
//                List<Commodity> userBuyList = users.get(username).getBuyList();
//                for (Commodity entry : userBuyList)
//                    commodities.get(entry.getId()).buy(1);
//                users.get(username).buyListPayment();
//            }
//        }
        try {
            Optional<User> u = userRepository.findById(username);
            User user = u.get();
            user.buyListPayment();
            userRepository.save(user);
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public User getUserById(String username) throws Exception {
        Optional<User> u = userRepository.findById(username);
        return u.get();
    }

    public Provider getProviderById(Integer id) throws Exception {
        Optional<Provider> provider = providerRepository.findById(id);

        return provider.get();
    }

    public void updateUser(String username, String email, String password, Date birthDate, String address) {
        Optional<User> existingUser = userRepository.findById(username);

        String hashedPassword = String.valueOf(password.hashCode());
        User user = existingUser.get();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setAddress(address);
        user.setBirthDate(birthDate);
        userRepository.save(user);
    }

    public boolean isLogin() {
        return (loginUsername != null && loginUsername != "");
    }

    public String login(String username, String password) throws Exception{
        Optional<User> user = userRepository.findById(username);

        String hashedPassword = String.valueOf(password.hashCode());
        if (user.stream().findFirst().isEmpty())
            throw new UserNotFoundError();
        if (hashedPassword.equals(user.get().getPassword())) {
            loginUser = user.get();
            loginUsername = loginUser.getUsername();
        } else
            throw new UserNotFoundError();
        return createJwtToken(username);
    }

    public void loginWithJwtToken(String username) {
        Optional<User> user = userRepository.findById(username);
        this.loginUser = user.get();
    }

    public String createJwtToken(String username) {
        String signKey = "-----------Baloot2023-----------";

//        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(hmac_secret_key), SignatureAlgorithm.HS256.getJcaName());

        SecretKey signature_type = new SecretKeySpec(signKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        String jwt_token = Jwts.builder()
                .claim("username", username)
                .setId(UUID.randomUUID().toString())
                .setIssuer("BALOOT_SYSTEM")                                                      // iss claim
                .setIssuedAt(Date.from(Instant.now()))                                          // iat claim
                .setExpiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS))) // exp claim
                .signWith(signature_type)
                .compact();
        return jwt_token;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public User getLoginUser() { return loginUser;}

    public void signup(String username, String email, String password, Date birthDate, String address) throws UserAlreadyExistsError {
        Optional<User> existingUser = userRepository.findById(username);

        if (existingUser.stream().findFirst().isPresent())
            throw new UserAlreadyExistsError();
        User newUser = new User();
        String hashedPassword = String.valueOf(password.hashCode());
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setBirthDate(birthDate);
        newUser.setAddress(address);
        userRepository.save(newUser);
    }

    public void logout() {
        loginUsername = "";
        loginUser = null;
    }

    public List<Commodity> getCommoditiesByName(String name) throws Exception {
        return commodityRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Commodity> getCommoditiesByNameSortPrice(String name) {
        return commodityRepository.findByNameContainingIgnoreCaseOrderByPriceDesc(name);
    }

    public List<Commodity> getCommoditiesByNameSortByName(String name) {
        return commodityRepository.findByNameContainingIgnoreCaseOrderByNameDesc(name);
    }

    public List<Commodity> getCommoditiesByCategoriesSortByName(String categories) {
        return commodityRepository.findByCategoriesOrderByNameDesc(categories);
    }

    public List<Commodity> getCommoditiesByCategoriesSortByPrice(String categories) {
        return commodityRepository.findByCategoriesOrderByPriceDesc(categories);
    }

    public List<Commodity> getCommoditiesOrderByName() {
        return commodityRepository.findByOrderByNameDesc();
    }

    public List<Commodity> getCommoditiesOrderByPrice() {
        return commodityRepository.findByOrderByPriceDesc();
    }

    public String convertListOfStringsToString(String[] listOfItems) {
        StringBuilder itemsStr = new StringBuilder();
        for (String item: listOfItems)
            itemsStr.append(item).append(", ");
        if(itemsStr.length() > 0)
            itemsStr = new StringBuilder(itemsStr.substring(0, itemsStr.length() - 2));
        return itemsStr.toString();
    }

    public List<Commodity> getSuggestedCommodities(Commodity currentCommodity) throws Exception {
        List<Commodity> commoditiesList = new ArrayList<>();
        List<Commodity> _commodities = getCommoditiesList();
        for (Commodity entry : _commodities) {
            if (!Objects.equals(entry.getId(), currentCommodity.getId())) {
                entry.updateScore(currentCommodity.getCategories());
                commoditiesList.add(entry);
            }
        }
        commoditiesList = commoditiesList.stream().sorted(Comparator.comparing(Commodity::getScore).reversed()).toList();
        return commoditiesList.stream().limit(5).toList();
    }

    public void applyDiscountCode(String username, String discountCode) throws Exception {
        try {
            Optional<User> u = userRepository.findById(username);
            User user = u.get();
            Discount d = discountRepository.findByDiscountCode(discountCode);
            user.addDiscountCode(d);
            userRepository.save(user);

        } catch (Exception ignored) {
            System.out.print(ignored.getMessage());
        }


    }

    public void registerUser(String _username, String _password, String _email, Date _birthDate, String _address) throws Exception {
        User newUser = new User();
        newUser.updateValue(_username, _password, _email, _birthDate, _address);
        addUser(newUser);
    }
}
