package PickUp;

import java.util.HashMap;
import java.util.Map;
// Class that gives cloned attribute
public class PickUpStore {
    private static Map<String, PickUp> pickUpMap = new HashMap<String, PickUp>();

    static
    {
        pickUpMap.put("Key", new PickUpKey());
        pickUpMap.put("Heal", new PickUpHeal());
    }

    public static PickUp getPickUp(String pickUpName)
    {
        return (PickUp) pickUpMap.get(pickUpName).clone();
    }
}
