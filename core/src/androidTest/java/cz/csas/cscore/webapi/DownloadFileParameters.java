package cz.csas.cscore.webapi;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cz.csas.cscore.utils.TimeUtils;
import cz.csas.cscore.utils.UrlUtils;

/**
 * The type Download file parameters.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 26 /04/16.
 */
public class DownloadFileParameters extends Parameters {

    private final String FIELDS = "fields";
    private final String SHOW_EXPORT_NAME = "showExportName";
    private final String DATE_FROM = "dateFrom";
    private final String DATE_TO = "dateTo";

    private List<String> fields;
    private Boolean showExportName;
    private Date dateFrom;
    private Date dateTo;

    /**
     * Instantiates a new Download file parameters.
     *
     * @param fields         the fields
     * @param showExportName the show export name
     * @param dateFrom       the date from
     * @param dateTo         the date to
     */
    public DownloadFileParameters(List<String> fields, Boolean showExportName, Date dateFrom, Date dateTo) {
        this.fields = fields;
        this.showExportName = showExportName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    @Override
    public Map<String, String> toDictionary() {
        Map<String, String> dictionary = super.toDictionary();
        if (fields != null)
            dictionary.put(FIELDS, UrlUtils.joinStrings(fields));
        if (showExportName != null)
            dictionary.put(SHOW_EXPORT_NAME, String.valueOf(showExportName));
        if (dateFrom != null)
            dictionary.put(DATE_FROM, TimeUtils.getISO8601String(dateFrom));
        if (dateTo != null)
            dictionary.put(DATE_TO, TimeUtils.getISO8601String(dateTo));
        return dictionary;
    }
}
