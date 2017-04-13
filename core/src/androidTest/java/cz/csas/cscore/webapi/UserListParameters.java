package cz.csas.cscore.webapi;

import java.util.Map;

/**
 * The type User list parameters.
 *
 * @author Jan Hauser <jan.hauser@appligting.cz>
 * @since 26 /12/15.
 */
public class UserListParameters extends PaginatedParameters implements Sortable<UserSortableFields>  {

    private final String PARAM_GENDER = "gender";

    private SortParameter<UserSortableFields> sortBy;
    private Gender gender;

    /**
     * Instantiates a new User list parameters.
     *
     * @param pagination the pagination
     * @param sortBy     the sort by
     * @param gender     the gender
     */
    public UserListParameters(Pagination pagination, SortParameter<UserSortableFields> sortBy, Gender gender) {
        super(pagination);
        this.sortBy = sortBy;
        this.gender = gender;
    }

    @Override
    public Map<String, String> toDictionary() {
        Map<String, String> dictionary = super.toDictionary();
        if(gender != null)
            dictionary.put(PARAM_GENDER,gender.getValue());
        return dictionary;
    }

    @Override
    public SortParameter<UserSortableFields> getSortBy() {
        return sortBy;
    }

    @Override
    public void setSortBy(SortParameter<UserSortableFields> sort) {
        this.sortBy = sort;
    }

    /**
     * The type Builder.
     */
    public static class Builder {

        private Pagination pagination;
        private SortParameter<UserSortableFields> sortBy;
        private Gender gender;

        /**
         * Sets pagination.
         *
         * @param pagination the pagination
         * @return the pagination
         */
        public Builder setPagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        /**
         * Sets sort by.
         *
         * @param sortBy the sort by
         * @return the sort by
         */
        public Builder setSortBy(SortParameter<UserSortableFields> sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        /**
         * Set gender builder.
         *
         * @param gender the gender
         * @return the builder
         */
        public Builder setGender(Gender gender){
            this.gender = gender;
            return this;
        }

        /**
         * Create user list parameters.
         *
         * @return the user list parameters
         */
        public UserListParameters create(){
            return new UserListParameters(pagination,sortBy,gender);
        }
    }
}
