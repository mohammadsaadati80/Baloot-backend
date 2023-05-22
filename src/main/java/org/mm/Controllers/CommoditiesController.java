package org.mm.Controllers;

import org.mm.Entity.*;

import org.mm.Service.Baloot;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CommoditiesController  {
    private Baloot baloot;

    public CommoditiesController(){
        baloot = Baloot.getInstance();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/commodities", method = RequestMethod.GET)
    public Map<Integer, Commodity> getCommodities(){
        return baloot.getCommodities();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/commodities/sort",method = RequestMethod.GET)
    public List<Commodity> sortCommodities(
            @RequestParam("sortMethod") String sort_method,
            @RequestParam(value = "searchMethod", required = false) String search_method,
            @RequestParam(value = "searchedTxt", required = false) String searched_txt){
        List<Commodity> commoditiesList = new ArrayList<>();

        try {
            if(search_method != null && !searched_txt.equals("")) {
                if (search_method.equals("commodityName")) {
                    commoditiesList = baloot.getCommoditiesByName(searched_txt);
                } else if (search_method.equals("category")) {
                    commoditiesList = baloot.getCommoditiesByCategory(searched_txt);
                }
            }
            else {
                commoditiesList = baloot.getCommoditiesList();
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        if(sort_method.equals("sort_by_price")) {
            commoditiesList = commoditiesList.stream().sorted(Comparator.comparing(Commodity::getPrice).reversed()).toList();
        }

        return commoditiesList;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/commodities/search",method = RequestMethod.GET)
    public List<Commodity> searchCommodities(
            @RequestParam(value = "searchMethod", required = false) String search_method,
            @RequestParam(value = "searchedTxt", required = false) String searched_txt){
        List<Commodity> commoditiesList = new ArrayList<>();
        try {
            if(search_method == null || searched_txt.equals("")) {
                return baloot.getCommoditiesList();
            }
            if(search_method.equals("commodityName")) {
                commoditiesList = baloot.getCommoditiesByName(searched_txt);
            } else if (search_method.equals("category")) {
                commoditiesList = baloot.getCommoditiesByCategory(searched_txt);
            }
            return commoditiesList;
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return commoditiesList;
    }
}