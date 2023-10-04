public class FactoryProducer {
    public static AbstractFactory getFactory(String factory) {
        if(factory.equalsIgnoreCase("Soldier")){
            return new SoldierFactory();
        } else if (factory.equalsIgnoreCase("Medic")) {
            return new MedicFactory();
        } else if (factory.equalsIgnoreCase("Scout")) {
            return new ScoutFactory();
        }
        return null;
    }
}
