package cz.csas.cscore.webapi;

/**
 * The enum Animal.
 */
public enum Animal implements HasValue {

    /**
     * Dog animal.
     */
    DOG("dog"),

    /**
     * Other animal.
     */
    OTHER(null);

    private String value;

    /**
     * Instantiates a new Animal.
     *
     * @param value the value
     */
    Animal(String value) {
        this.value = value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    /**
     * Other animal.
     *
     * @param value the value
     * @return the animal
     */
    public static Animal other(String value){
        Animal animal = Animal.OTHER;
        animal.setValue(value);
        return animal;
    }
}
