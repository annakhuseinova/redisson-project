package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.Restaurant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class RestaurantUtil {

    public static List<Restaurant> getRestaurants(){
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = RestaurantUtil.class.getClassLoader().getResourceAsStream("restaurant.json");
        try {
            return mapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
