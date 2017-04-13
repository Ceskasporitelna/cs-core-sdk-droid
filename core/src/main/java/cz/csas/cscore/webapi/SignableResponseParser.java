package cz.csas.cscore.webapi;

import cz.csas.cscore.utils.csjson.CsJson;
import cz.csas.cscore.utils.csjson.JsonElement;
import cz.csas.cscore.utils.csjson.JsonParser;
import cz.csas.cscore.webapi.signing.SignInfo;

/**
 * The type Signable response parser provides {@link SignInfo} parsing from {@link WebApiEntity}
 * json response.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 09 /04/16.
 */
class SignableResponseParser {

    /**
     * Parse sign info from json
     *
     * @param json the json including the
     * @return the sign info
     */
    public static SignInfo parseSignInfoFromJson(String json) {
        JsonElement element = new JsonParser().parse(json);
        JsonElement signInfoElement = element.getAsJsonObject().get("signInfo");
        if (signInfoElement == null)
            return null;
        return new CsJson().fromJson(signInfoElement, SignInfo.class);
    }
}
