package cz.csas.cscore.webapi;

import java.util.Map;

/**
 * The type Notes list parameters.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 26 /12/15.
 */
public class NotesListParameters extends Parameters {

    private final String PARAM_ANIMAL = "animal";

    private Animal animal;

    /**
     * Instantiates a new Notes list parameters.
     *
     * @param animal the animal
     */
    public NotesListParameters(Animal animal) {
        this.animal = animal;
    }

    @Override
    public Map<String, String> toDictionary() {
        Map<String, String> dictionary = super.toDictionary();
        if(animal != null)
            dictionary.put(PARAM_ANIMAL,animal.getValue());
        return dictionary;
    }
}
