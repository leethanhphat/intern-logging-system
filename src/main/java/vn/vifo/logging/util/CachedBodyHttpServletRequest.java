package vn.vifo.logging.util;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import java.io.*;

@Slf4j
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
    private byte[] cacheBody;
    private static final Logger logger = LoggerFactory.getLogger(CachedBodyHttpServletRequest.class);

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        if (this.isFormPost()) {
            // to copy parameters to request body (make them available for
            // getInputStream() method)
            this.getRequest().getParameterMap();
        }

        // cache our input stream as byte array
        this.cacheBody = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // create new input stream from cached body
        return new CachedBodyServletInputStream(cacheBody);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        // create new reader from cached body
        final var stream = new ByteArrayInputStream(this.cacheBody);
        return new BufferedReader(new InputStreamReader(stream));
    }

    private boolean isFormPost() {
        final var contentType = this.getContentType();
        return contentType != null &&
                contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE) &&
                HttpMethod.POST.matches(this.getMethod());
    }

    /*
     * Our implementation of input stream that is created from cached body.
     * Based on extension of ServletInputStream abstract class
     */
    private class CachedBodyServletInputStream extends ServletInputStream {
        private InputStream cachedBodyInputStream;

        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public int read() throws IOException {
            return this.cachedBodyInputStream.read();
        }

        @Override
        public boolean isFinished() {
            try {
                return this.cachedBodyInputStream.available() == 0;
            } catch (IOException e) {
                log.error("Can not read number of available bytes {}", e);
                return false;
            }
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException();
        }
    }
}
