package vn.vifo.logging.dto.request.esign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsignRequest {
    public MultipartFile file;
    public String type;
    public String filename;
    public String signingKeyCode;


}
