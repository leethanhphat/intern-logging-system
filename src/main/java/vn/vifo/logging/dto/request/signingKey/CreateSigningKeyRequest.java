package vn.vifo.logging.dto.request.signingKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSigningKeyRequest {
    private String keyCode;
    private String password;
    private String name;
    private String keySerial;
    private String keyStoreLocation;
    private String publicKey;
    private String issuedAt;
    private String expiredAt;
}
