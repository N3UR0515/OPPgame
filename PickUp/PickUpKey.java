package PickUp;

import java.io.Serializable;

// Pickup attribute
public class PickUpKey extends PickUp implements Serializable {
    @Override
    PickUp addPickup() {
        this.pickupCode = "Key";
        return this;
    }
}
