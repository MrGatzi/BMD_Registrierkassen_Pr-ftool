import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;

import at.asitplus.regkassen.common.util.CashBoxUtils;

import java.security.interfaces.ECPrivateKey;

public class JWSadder {
	 protected JWSSigner jwsSigner;

	    @Override
	    public String signMachineCodeRepOfReceipt(String machineCodeRepOfReceipt, boolean signatureDeviceIsDamaged) {

	        ECPrivateKey key = (ECPrivateKey) openSystemSignatureModule.getSigningKey();
	        try {
	            this.jwsSigner = new ECDSASigner(key);
	        } catch (JOSEException e) {
	            e.printStackTrace();
	        }

	        //FOR DEMONSTRATION PURPOSES
	        //if damage occurs, the signature value is replaced with the term "Sicherheitseinrichtung ausgefallen"
	        if (signatureDeviceIsDamaged) {
	            String jwsHeader = "eyJhbGciOiJFUzI1NiJ9";  //ES256 Header for JWS
	            String jwsPayload = CashBoxUtils.base64Encode(machineCodeRepOfReceipt.getBytes(), true); //get payload
	            String jwsSignature = CashBoxUtils.base64Encode("Sicherheitseinrichtung ausgefallen".getBytes(), true);  //create damaged signature part
	            String jwsCompactRep = jwsHeader + "." + jwsPayload + "." + jwsSignature;
	            return jwsCompactRep;
	        }


	        try {
	            // Creates the JWS object with payload
	            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.parse(RKSuite.R1_AT0.getJwsSignatureAlgorithm())), new Payload(machineCodeRepOfReceipt));

	            // Compute the EC signature
	            jwsObject.sign(jwsSigner);

	            // Serialize the JWS to compact form
	            return jwsObject.serialize();
	        } catch (JOSEException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
}
