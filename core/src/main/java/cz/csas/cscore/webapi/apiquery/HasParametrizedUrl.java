package cz.csas.cscore.webapi.apiquery;

import cz.csas.cscore.webapi.Parameters;

/**
 * Marks Resource or Entity that supports .url(Parameters) Verb.
 *
 * @param <T> the type parameter
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public interface HasParametrizedUrl<T extends Parameters> {

    /**
     * Returns an url for given resource
     *
     * @param parameters the parameters
     * @return the string
     */
    public String url(T parameters);
}
