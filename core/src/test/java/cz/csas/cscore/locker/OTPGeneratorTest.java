package cz.csas.cscore.locker;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import cz.csas.cscore.client.crypto.CryptoManager;
import cz.csas.cscore.client.crypto.CryptoManagerImpl;
import cz.csas.cscore.client.crypto.OTPGenerator;
import cz.csas.cscore.client.crypto.OTPGeneratorImpl;

/**
 * The type Otp generator test.
 *
 * @author Jan Hauser <hauseja3@gmail.com>
 * @since 06 /12/15.
 */
public class OTPGeneratorTest extends TestCase {

    private final String DFP = "C6F0D156-8F29-43C6-AF59-166F86953F84";
    private final String CID = "c0f9fe23-21ea-493c-9286-6d3f0d7826b0";
    private final String OTPK = "0eaBoyBkUdlZ8X0T/xrntoxK5/MHi2vFt8ui6Zd7SmY=";
    private OTPGenerator mOTPGenerator;

    @Before
    public void setUp(){
        mOTPGenerator = new OTPGeneratorImpl();
    }

    /**
     * Test one time password payload.
     */
    @Test
    public void testOneTimePasswordPayload(){
        assertEquals("48274530c0f9fe23-21ea-493c-9286-6d3f0d7826b0C6F0D156-8F29-43C6-AF59-166F86953F84", mOTPGenerator.constructPayload(1449246020,CID+DFP));
    }

    /**
     * Test one time password generator.
     */
    @Test
    public void testOneTimePasswordGenerator(){
        CryptoManager cryptoManager = new CryptoManagerImpl();
        String oneTimePassword = mOTPGenerator.generatePassword( CID+DFP, 4, cryptoManager.decodeBase64(OTPK), 1449246020);
        assertEquals("7217358",oneTimePassword);
    }
}
