package cz.csas.cscore.locker;

/**
 * The type O auth login activity options.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 09 /10/16.
 */
public class OAuthLoginActivityOptions {

    private boolean showLogo;
    private Integer navBarColor;

    /**
     * Instantiates a new O auth login activity options.
     *
     * @param showLogo    the show logo
     * @param navBarColor the nav bar color
     */
    public OAuthLoginActivityOptions(boolean showLogo, Integer navBarColor) {
        this.showLogo = showLogo;
        this.navBarColor = navBarColor;
    }

    /**
     * Sets show logo.
     *
     * @param showLogo the show logo
     */
    public void setShowLogo(boolean showLogo) {
        this.showLogo = showLogo;
    }

    /**
     * Sets nav bar color.
     *
     * @param navBarColor the nav bar color
     */
    public void setNavBarColor(Integer navBarColor) {
        this.navBarColor = navBarColor;
    }

    /**
     * Gets show logo.
     *
     * @return the show logo
     */
    public boolean getShowLogo() {
        return showLogo;
    }

    /**
     * Gets nav bar color.
     *
     * @return the nav bar color
     */
    public Integer getNavBarColor() {
        return navBarColor;
    }

    /**
     * The type Builder.
     */
    public static class Builder {

        private boolean showLogo = false;
        private Integer navBarColor = null;

        /**
         * Sets show logo.
         *
         * @return the show logo
         */
        public Builder setShowLogo(boolean showLogo) {
            this.showLogo = showLogo;
            return this;
        }

        /**
         * Sets nav bar color.
         *
         * @param navBarColor the nav bar color
         * @return the nav bar color
         */
        public Builder setNavBarColor(Integer navBarColor) {
            this.navBarColor = navBarColor;
            return this;
        }

        /**
         * Create o auth login activity options.
         *
         * @return the o auth login activity options
         */
        public OAuthLoginActivityOptions create() {
            return new OAuthLoginActivityOptions(showLogo, navBarColor);
        }

    }
}

