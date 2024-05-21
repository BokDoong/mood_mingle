package uni.capstone.moodmingle.common.log.advice;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.SneakyThrows;
import org.springframework.util.StreamUtils;

import java.io.*;

/**
 * local, dev 프로파일에서 RequestBody 로깅을 위해 캐싱 목적으로 HttpServletRequestWrapper 를 상속한 클래스
 * InputStream 을 이용하여 cachedBody 에 Body 를 복사(캐싱)하여 저장
 *
 * @author ijin
 */
public class CachingRequestWrapper extends HttpServletRequestWrapper {

    /**
     * CachedBodyServletInputStream 생성을 위해 RequestBody 를 담을 변수
     */
    private byte[] cachedBody;

    /**
     * getInputStream()을 이용하여 RequestBody 를 복사(캐싱)하는 생성자
     *
     * @param request HTTP Request Body
     */
    public CachingRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
    }

    /**
     * 다른 클래스에서 RequestBody 를 담은 CachedBodyServletInputStream 객체를 반환
     *
     * @return CachedBodyServletInputStream 객체
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedRequestServletInputStream(this.cachedBody);
    }

    /**
     * ByteArrayInputStream 으로 RequestBody 를 반환
     *
     * @return 인코딩된 RequestBody 를 담은 BufferReader 객체 반환
     */
    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream, "UTF-8"));
    }

    /**
     * RequestBody 를 담아 다른 곳에서 사용할 클래스
     */
    private class CachedRequestServletInputStream extends ServletInputStream {

        /**
         * RequestBody 를 담을 변수
         */
        private InputStream cachedBodyInputStream;

        /**
         * RequestBody 를 받는 생성자
         *
         * @param cachedBody 상위 클래스에서 받을 RequestBody 가 담긴 변수
         */
        public CachedRequestServletInputStream(byte[] cachedBody) {
            this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
        }

        @SneakyThrows
        @Override
        public boolean isFinished() {
            return cachedBodyInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }

        @Override
        public int read() throws IOException {
            return cachedBodyInputStream.read();
        }
    }
}
