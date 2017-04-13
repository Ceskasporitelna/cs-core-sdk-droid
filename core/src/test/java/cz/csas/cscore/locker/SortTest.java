package cz.csas.cscore.locker;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import cz.csas.cscore.utils.UrlUtils;
import cz.csas.cscore.webapi.Parameters;
import cz.csas.cscore.webapi.Sort;
import cz.csas.cscore.webapi.SortDirection;
import cz.csas.cscore.webapi.SortParameter;
import cz.csas.cscore.webapi.Sortable;

/**
 * The type Sort test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 08 /04/16.
 */
public class SortTest extends TestCase {

    private TrainParameters trainParameters;
    private TrainParameters trainParametersEmpty;

    @Before
    public void setUp() {

        trainParameters = new TrainParameters(Sort.by(Flag.DIRECTION, SortDirection.ASCENDING)
                .thenBy(Flag.other("test"), null)
                .thenBy(Flag.other("testString"), SortDirection.DESCENDING), 6, Direction.BRNO);

        trainParametersEmpty = new TrainParameters(null, 4, Direction.BRNO);
    }

    @Test
    public void testSort() {

        assertTrue(UrlUtils.toQueryString(trainParameters.toDictionary()).contains("coaches=6"));
        assertTrue(UrlUtils.toQueryString(trainParameters.toDictionary()).contains("sort=DIRECTION,test,testString"));
        assertTrue(UrlUtils.toQueryString(trainParameters.toDictionary()).contains("order=asc,asc,desc"));
        assertTrue(UrlUtils.toQueryString(trainParameters.toDictionary()).contains("direction=" + Direction.BRNO.getValue()));

        assertTrue(UrlUtils.toQueryString(trainParametersEmpty.toDictionary()).contains("coaches=4"));
        assertTrue(UrlUtils.toQueryString(trainParametersEmpty.toDictionary()).contains("direction=" + Direction.BRNO.getValue()));
    }

    private class TrainParameters extends Parameters implements Sortable<Flag> {

        private final String PARAM_COACHES = "coaches";
        private final String PARAM_DIRECTION = "direction";
        private SortParameter<Flag> sortBy;
        private Integer numberOfCoaches;
        private Direction direction;

        /**
         * Instantiates a new Train parameters.
         *
         * @param sortBy          the sort by
         * @param numberOfCoaches the number of coaches
         * @param direction       the direction
         */
        public TrainParameters(SortParameter<Flag> sortBy, Integer numberOfCoaches, Direction direction) {
            this.sortBy = sortBy;
            this.numberOfCoaches = numberOfCoaches;
            this.direction = direction;
        }

        /**
         * Gets number of coaches.
         *
         * @return the number of coaches
         */
        public Integer getNumberOfCoaches() {
            return numberOfCoaches;
        }

        /**
         * Gets direction.
         *
         * @return the direction
         */
        public Direction getDirection() {
            return direction;
        }

        @Override
        public SortParameter<Flag> getSortBy() {
            return sortBy;
        }

        @Override
        public void setSortBy(SortParameter<Flag> sortBy) {
            this.sortBy = sortBy;
        }

        @Override
        public Map<String, String> toDictionary() {
            Map<String, String> dictionary = super.toDictionary();
            if (getNumberOfCoaches() != null)
                dictionary.put(PARAM_COACHES, getNumberOfCoaches().toString());
            if (getDirection() != null)
                dictionary.put(PARAM_DIRECTION, getDirection().getValue());
            return dictionary;
        }
    }
}

