package Main;

public class FactoryProducer {
    public static AbstractFactory getFactory(Boolean isMutant) {
        if(isMutant){
            return new MutantZombieFactory();
        } else {
            return new ZombieFactory();
        }
    }
}
