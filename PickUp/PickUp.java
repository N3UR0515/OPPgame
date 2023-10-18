package PickUp;
// Pickup class
public abstract class PickUp implements Cloneable{
    String pickupCode;

    abstract void addPickup();

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
