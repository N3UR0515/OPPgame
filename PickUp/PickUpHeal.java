package PickUp;

import java.io.Serializable;

// Pickup attribute
public class PickUpHeal extends PickUp implements Serializable {
    @Override
    PickUp addPickup() {
        this.pickupCode = "Heal";
        return this;
    }
}
