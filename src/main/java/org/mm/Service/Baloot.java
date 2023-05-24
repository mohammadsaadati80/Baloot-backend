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

import java.text.SimpleDateFormat;
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
            providerRepository.saveAll(providers);
            commodityRepository.saveAll(commodities);
            for (User user : users) {
                userRepository.save(user);
            }
            commentRepository.saveAll(comments);
            discountRepository.saveAll(discounts);
        } catch (Exception ignored) {
            System.out.print(ignored.getMessage());
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

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<Integer, Provider> getProviders() {
        return providers;
    }

    public Map<Integer, Commodity> getCommodities() {
        return commodities;
    }

    public Map<String, Discount> getDiscounts() { return discounts; }

    public void addUser(User user) throws Exception {
        if (!user.isValidCommand())
            throw new InvalidCommandError();
        else if (user.haveSpecialCharacter())
            throw new UsernameShouldNotContainSpecialCharactersError();
        else {
            if (users.containsKey(user.getUsername())) {
                users.get(user.getUsername()).update(user);
            }
            else {
                users.put(user.getUsername(), user);
            }
        }
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

    public void addComment(Comment comment) throws Exception {
        comment.setUsername(getLoginUsername());
        commentRepository.save(comment);
        Commodity commodity = getCommodityById(comment.getCommodityId());
        commodity.addComment(comment);
        commodityRepository.save(commodity);
    }

    public void addDiscount(Discount discount) throws Exception {
        if (!discount.isValidCommand())
            throw new InvalidCommandError();
        else if (!discount.isValidDiscount())
            throw new InvalidDiscount();
        else {
            if (discounts.containsKey(discount.getDiscountCode()))
                discounts.get(discount.getDiscountCode()).update(discount);
            else
                discounts.put(discount.getDiscountCode(), discount);
        }
    }

    public List<Commodity> getCommoditiesList() throws Exception {
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
        Optional<Commodity> m = commodityRepository.findById(id);
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

        return (List<Commodity>) user.get().getBuyList();
    }

    public void addCredit(String username, Integer credit) throws Exception {
        if (username==null || credit==null || credit==0.0f)
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(username))
                throw new UserNotFoundError();
            else if (credit < 0)
                throw new InvalidCreditValue();
            else {
                users.get(username).addCredit(credit);
            }
        }
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
        if (commodityId==null || commodityId==0.0f)
            throw new InvalidCommandError();
        else {
            List<Comment> commentList = new ArrayList<>();
            for (Map.Entry<Integer, Comment> entry : comments.entrySet())
                if (entry.getValue().getCommodityId().equals(commodityId))
                    commentList.add(entry.getValue());
            return commentList;
        }
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
    }

    public User getUserById(String username) throws Exception {
        if (username==null)
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(username))
                throw new UserNotFoundError();
            else {
                return users.get(username);
            }
        }
    }

    public Provider getProviderById(Integer id) throws Exception {
        Optional<Provider> provider = providerRepository.findById(id);

        return provider.get();
    }

    public boolean isLogin() {
        return (loginUsername != null && loginUsername != "");
    }

    public void login(String username, String password) throws Exception{
        Optional<User> user = userRepository.findById(username);

        if (password.equals(user.get().getPassword())) {
            loginUser = user.get();
            loginUsername = loginUser.getUsername();
        }

    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void logout() {
        loginUsername = "";
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

    public List<Commodity> getCommoditiesByCategoriesSortByName(String genre) {
        return commodityRepository.findByCategoriesOrderByNameDesc(genre);
    }

    public List<Commodity> getCommoditiesByCategoriesSortByPrice(String genre) {
        return commodityRepository.findByCategoriesOrderByPriceDesc(genre);
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

    public List<Commodity> getSuggestedCommodities(Commodity currentCommodity) throws Exception { //TODO
        List<Commodity> commoditiesList = new ArrayList<>();
        for (Map.Entry<Integer, Commodity> entry : commodities.entrySet()) {
            if (!Objects.equals(entry.getValue().getId(), currentCommodity.getId())) {
                entry.getValue().updateScore(currentCommodity.getCategories());
                commoditiesList.add(entry.getValue());
            }
        }
        commoditiesList = commoditiesList.stream().sorted(Comparator.comparing(Commodity::getScore).reversed()).toList();
        return commoditiesList.stream().limit(5).toList();
    }

    public void applyDiscountCode(String username, String discountCode) throws Exception {
        if (username==null || discountCode==null || username=="" || discountCode=="")
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(username))
                throw new UserNotFoundError();
            else if (!discounts.containsKey(discountCode))
                throw new DiscountCodeNotFoundError();
            else if (users.get(username).isUsedDiscountCode(discountCode))
                throw new DiscountCodeUsedError();
            else {
                users.get(username).addDiscountCode(discounts.get(discountCode));
            }
        }
    }

    public void registerUser(String _username, String _password, String _email, Date _birthDate, String _address) throws Exception {
        User newUser = new User();
        newUser.updateValue(_username, _password, _email, _birthDate, _address);
        addUser(newUser);
    }
}
