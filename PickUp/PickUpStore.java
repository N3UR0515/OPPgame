package PickUp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
// Class that gives cloned attribute
public class PickUpStore implements Serializable {
    private static Map<String, PickUp> pickUpMap = new HashMap<String, PickUp>();

    static
    {
        pickUpMap.put("Key", new PickUpKey().addPickup());
        pickUpMap.put("Heal", new PickUpHeal().addPickup());
    }

    public static PickUp getPickUp(String pickUpName)
    {
        return (PickUp) pickUpMap.get(pickUpName).clone();
    }
}
