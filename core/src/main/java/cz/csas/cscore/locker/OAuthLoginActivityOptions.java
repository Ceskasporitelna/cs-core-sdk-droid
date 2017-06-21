package cz.csas.cscore.locker;

import cs.cz.cscore.R;

/**
 * The type O auth login activity options.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 09 /10/16.
 */
public class OAuthLoginActivityOptions {

    private boolean showLogo;
    private CsNavBarColor navBarColor;
    private int customNavBarColor;


    /**
     * Instantiates a new O auth login activity options.
     *
     * @param showLogo    the show logo
     * @param navBarColor the nav bar color
     */
    public OAuthLoginActivityOptions(boolean showLogo, CsNavBarColor navBarColor, int customNavBarColor) {
        this.showLogo = showLogo;
        this.navBarColor = navBarColor;
        this.customNavBarColor = customNavBarColor;
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
    public void setNavBarColor(CsNavBarColor navBarColor) {
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
    public CsNavBarColor getNavBarColor() {
        return navBarColor;
    }

    /**
     * Gets custom nav bar color
     * @return the custom nav bar color
     */
    public int getCustomNavBarColor() {
        return customNavBarColor;
    }

    /**
     * Sets custom nav bar color
     * @param customNavBarColor the custom nav bar color
     */
    public void setCustomNavBarColor(int customNavBarColor) {
        this.customNavBarColor = customNavBarColor;
    }

    /**
     * The type Builder.
     */
    public static class Builder {

        private boolean showLogo = false;
        private CsNavBarColor navBarColor = CsNavBarColor.DEFAULT;
        private int customNavbarColor = R.color.csasColorNavBar;

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
        public Builder setNavBarColor(CsNavBarColor navBarColor) {
            this.navBarColor = navBarColor;
            return this;
        }

        /**
         * Sets custom nav bar color
         * @param customNavBarColor the nav bar color
         * @return the nav bar color
         */
        public Builder setCustomNavBarColor(int customNavBarColor) {
            this.customNavbarColor = customNavBarColor;
            return this;
        }

        /**
         * Create o auth login activity options.
         *
         * @return the o auth login activity options
         */
        public OAuthLoginActivityOptions create() {
            return new OAuthLoginActivityOptions(showLogo, navBarColor, customNavbarColor);
        }

    }
}

