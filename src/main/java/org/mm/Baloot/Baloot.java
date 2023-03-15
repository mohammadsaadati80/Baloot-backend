package org.mm.Baloot;

import org.mm.Baloot.Exceptions.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

public class Baloot {

    private ObjectMapper mapper;
    private Map<String, User> users;
    private Map<Integer, Provider> providers;
    private Map<Integer, Commodity> commodities;
    private Map<Integer, Comment> comments;

    public Baloot() {
        mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        users = new HashMap<>();
        providers = new HashMap<>();
        commodities = new HashMap<>();
        comments = new HashMap<>();
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
        boolean isUserFound = false;
        if (!comment.isValidCommand())
            throw new InvalidCommandError();
        else if (!commodities.containsKey(comment.getCommodityId()))
            throw new CommodityNotFoundError();
        else {
            for (Map.Entry<String, User> entry : users.entrySet()) {
                if (entry.getValue().getEmail().equals(comment.getUserEmail())) {
                    isUserFound = true;
                    break;
                }
            }
        }
        if (isUserFound) {
            Integer key = comments.size();
            comment.addId(key+1);
            comments.put(comment.getId(), comment);
        }
        else
            throw new UserNotFoundError();
    }

    public List<Commodity> getCommoditiesList() throws Exception {
        List<Commodity> commodityList = new ArrayList<>();
        for (Map.Entry<Integer, Commodity> entry : commodities.entrySet()) {
            commodityList.add(entry.getValue());
        }
        return commodityList;
    }

    public void rateCommodity(Rate rate) throws Exception {
        if (rate==null || !rate.isValidCommand())
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(rate.getUsername()))
                throw new UserNotFoundError();
            else if (!commodities.containsKey(rate.getCommodityId()))
                throw new CommodityNotFoundError();
            else if (!rate.isValidScoreRange())
                throw new InvalidRateScoreError();
            else if (!rate.isValidScoreType())
                throw new InvalidRateScoreError();
            else {
                commodities.get(rate.getCommodityId()).addRate(rate);
            }
        }
    }

    public void addToBuyList(String username, Integer commodityId) throws Exception {
        if (username==null || commodityId==null || commodityId==0.0f)
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(username))
                throw new UserNotFoundError();
            else if (!commodities.containsKey(commodityId))
                throw new CommodityNotFoundError();
            else if (commodities.get(commodityId).getInStock() == 0)
                throw new CommodityNotInStuckError();
            else {
                if (users.get(username).isInBuyList((commodityId)))
                    throw new CommodityAlreadyInBuyListError();
                else {
                    users.get(username).addToBuyList(commodities.get(commodityId));
                }
            }
        }
    }

    public void removeFromBuyList(String username, Integer commodityId) throws Exception {
        if (username==null || commodityId==null || commodityId==0.0f)
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(username))
                throw new UserNotFoundError();
            else if (!commodities.containsKey(commodityId))
                throw new CommodityNotFoundError();
            else {
                if (! users.get(username).isInBuyList((commodityId)))
                    throw new CommodityIsNotInBuyListError();
                else {
                    users.get(username).removeFromBuyList(commodityId);
                }
            }
        }
    }

    public Commodity getCommodityById(Integer id) throws Exception {
        if (id==null || id==0.0f)
            throw new InvalidCommandError();
        else {
            if (!commodities.containsKey(id))
                throw new CommodityNotFoundError();
            else {
                return commodities.get(id);
            }
        }
    }

    public List<Commodity> getCommoditiesByCategory(String category) throws Exception {
        if (category==null || category=="")
            throw new InvalidCommandError();
        else {
            List<Commodity> commoditiesList = new ArrayList<>();
            for (Map.Entry<Integer, Commodity> entry : commodities.entrySet()) {
                if (entry.getValue().isInCategory(category)) {
                    commoditiesList.add(entry.getValue());
                }
            }
            return commoditiesList;
        }
    }

    public List<Commodity> getBuyList(String username) throws Exception {
        if (username==null)
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(username))
                throw new UserNotFoundError();
            else {
                HashMap<Integer, Commodity> userBuyList = users.get(username).getBuyList();
                List<Commodity> buyList = new ArrayList<>();
                for (Map.Entry<Integer, Commodity> entry : userBuyList.entrySet()) {
                    buyList.add(entry.getValue());
                }
                return buyList;
            }
        }
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

    public List<Commodity> getCommoditiesByPrice(Integer startPrice, Integer endPrice) throws Exception {
        if (startPrice==null || startPrice==0.0f || endPrice==null || endPrice==0.0f)
            throw new InvalidCommandError();
        else {
            if (endPrice < startPrice)
                throw new InvalidPriceRangeError();
            else {
                List<Commodity> commoditiesList = new ArrayList<>();
                for (Map.Entry<Integer, Commodity> entry : commodities.entrySet()) {
                    if (entry.getValue().isPriceInRange(startPrice, endPrice))
                        commoditiesList.add(entry.getValue());
                }
                return commoditiesList;
            }
        }
    }

    public void voteComment(String username, Integer commentId, Integer vote) throws Exception {
        if (username==null || commentId==null || commentId==0.0f)
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(username))
                throw new UserNotFoundError();
            else if (!comments.containsKey(commentId))
                throw new CommentNotFound();
            else if (!(vote==-1 || vote==0 || vote==1))
                throw new InvalidVoteScoreError();
            else
                comments.get(commentId).addVote(username, vote);
        }
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

    public void userBuyListPayment(String username) throws Exception {
        if (username==null)
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(username))
                throw new UserNotFoundError();
            else if (users.get(username).getBuyList().size() == 0)
                throw new UserBuyListIsEmptyError();
            else if (!users.get(username).haveEnoughCredit())
                throw new UserNotHaveEnoughCreditError();
            else {
                HashMap<Integer, Commodity> userBuyList = users.get(username).getBuyList();
                for (Map.Entry<Integer, Commodity> entry : userBuyList.entrySet())
                    commodities.get(entry.getValue().getId()).buy(1);
                users.get(username).buyListPayment();
            }
        }
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
        if (id==null || id==0.0f)
            throw new InvalidCommandError();
        else {
            if (!providers.containsKey(id))
                throw new ProviderNotFoundError();
            else {
                return providers.get(id);
            }
        }
    }

}
