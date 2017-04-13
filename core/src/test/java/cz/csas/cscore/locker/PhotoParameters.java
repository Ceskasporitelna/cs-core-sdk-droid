package cz.csas.cscore.locker;

import java.util.Map;

import cz.csas.cscore.webapi.Parameters;

/**
 * The type Photo parameters.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 18 /04/16.
 */
public class PhotoParameters extends Parameters {
    private final String PARAM_BLACK_AND_WHITE = "blackAndWhite";
    private final String PARAM_SIZE = "size";

    private Boolean blackAndWhite;
    private String size;

    /**
     * Instantiates a new Photo parameters.
     *
     * @param blackAndWhite the black and white
     * @param size          the size
     */
    public PhotoParameters(Boolean blackAndWhite, String size) {
        this.blackAndWhite = blackAndWhite;
        this.size = size;
    }

    @Override
    public Map<String, String> toDictionary() {
        Map<String, String> dictionary = super.toDictionary();
        if (blackAndWhite != null)
            dictionary.put(PARAM_BLACK_AND_WHITE, blackAndWhite.toString());
        if (size != null)
            dictionary.put(PARAM_SIZE, size.toString());
        return dictionary;
    }

}