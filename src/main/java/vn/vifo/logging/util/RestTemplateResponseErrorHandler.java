package vn.vifo.logging.util;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler
    implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    // Ignore 4xx, 5xx errors
    return false;
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    // Do nothing since 4xx errors are ignored
  }
}