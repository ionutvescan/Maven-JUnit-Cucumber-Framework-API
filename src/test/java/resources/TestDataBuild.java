package resources;

import pojoClasses.*;

import java.util.ArrayList;
import java.util.List;

public class TestDataBuild {
    public AddPlace addPlacePayload(String name, String address, String website){
        AddPlace addPlace = new AddPlace();
        addPlace.setAccuracy(50);
        addPlace.setAddress(address);
        addPlace.setLanguage("English");
        addPlace.setPhone_number("(+91) 983 893 3937");
        addPlace.setWebsite(website);
        addPlace.setName(name);

        List<String> typeList = new ArrayList<String>();
        typeList.add("shoe park");
        typeList.add("shop");
        addPlace.setTypes(typeList);

        Location location = new Location();
        location.setLat(-38.383494);
        location.setLng(33.427362);
        addPlace.setLocation(location);
        return addPlace;
    }
    public String updatePlacePayload(String placeId, String name, String address, String website){
        return "{\n" +
                "\"place_id\":\"\"+placeId+\"\""+placeId+"\",\n" +
                "\"name\":\""+name+"\",\n" +
                "\"address\":\""+address+"\",\n" +
                "\"website\":\""+website+"\",\n" +
                "\"key\":\"qaclick123\"\n" +
                "}\n";
    }
    public String deletePlacePayload(String placeId) {
        return "{\r\n    \"place_id\":\""+placeId+"\"\r\n}";
    }

    public LoginRequest loginRequest(String email, String password){
        LoginRequest login = new LoginRequest();
        login.setUserEmail(email);
        login.setUserPassword(password);
        return login;
    }

    public Orders getOrder(String productId){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCountry("Romania");
        orderDetail.setProductOrderedId(productId);

        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        orderDetailList.add(orderDetail);

        Orders orders = new Orders();
        orders.setOrders(orderDetailList);
        return orders;
    }
}
