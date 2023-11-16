package PickUp;

import java.io.Serializable;

// Pickup class
public abstract class PickUp implements Cloneable, Serializable {
    protected String pickupCode;

    abstract PickUp addPickup();

    public Object clone()
    {
        Object clone = null;
        try
        {
            clone = super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return clone;
    }

    public String getPickupCode() {
        return pickupCode;
    }
}
