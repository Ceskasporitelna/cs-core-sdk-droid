package cz.csas.cscore.webapi.signing;

import cz.csas.cscore.utils.csjson.annotations.CsExpose;

/**
 * The type Sign info.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 06 /04/16.
 */
public class SignInfo {

    @CsExpose
    private SigningState state;

    @CsExpose
    private String signId;


    /**
     * Instantiates a new Sign info.
     *
     * @param state  the state
     * @param signId the sign id
     */
    public SignInfo(SigningState state, String signId) {
        this.state = state;
        this.signId = signId;
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(SigningState state) {
        this.state = state;
    }

    /**
     * Sets sign id.
     *
     * @param signId the sign id
     */
    public void setSignId(String signId) {
        this.signId = signId;
    }

    /**
     * Get state.
     *
     * @return the state
     */
    public SigningState getState() {
        return state;
    }

    /**
     * Get sign id.
     *
     * @return the sign id
     */
    public String getSignId() {
        return signId;
    }

    @Override
    public String toString() {
        return "SignInfo{" +
                "state=" + state +
                ", signId='" + signId + '\'' +
                '}';
    }
}
