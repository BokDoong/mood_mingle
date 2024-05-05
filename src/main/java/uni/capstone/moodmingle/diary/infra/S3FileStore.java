package uni.capstone.moodmingle.diary.infra;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uni.capstone.moodmingle.diary.domain.FileStore;
import uni.capstone.moodmingle.exception.FIleIOException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * S3 와 통신하여 파일을 업로드하고 삭제하기 위한 어댑터
 *
 * @author ijin
 */
@Component
@RequiredArgsConstructor
public class S3FileStore implements FileStore {

    private final AmazonS3 amazonS3;

    /**
     * S3 Bucket Name
     */
    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    /**
     * 허용된 파일 확장자
     */
    private static final List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    /**
     * 업로드
     *
     * @param file 이미지 파일
     * @return 이미지 URL
     */
    @Override
    public String upload(MultipartFile file) {
        // 입력된 파일이 비어있는지 검사
        if(file.isEmpty() || Objects.isNull(file.getOriginalFilename())){
            throw new FIleIOException(ErrorCode.INPUT_FILE_EMPTY);
        }

        // 업로드 -> URL 반환
        return uploadImage(file);
    }

    /**
     * 삭제
     *
     * @param imageUrl 삭제할 이미지 URL
     */
    @Override
    public void delete(String imageUrl) {
        // 파싱한 Key 를 이용하여 삭제
        String key = getKeyFromImageAddress(imageUrl);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new FIleIOException(ErrorCode.FAILED_DELETE_FILE);
        }
    }

    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new FIleIOException(ErrorCode.FAILED_DECODE_FILE_KEY);
        }
    }

    private String uploadImage(MultipartFile file) {
        // 확장자 유효성 검사
        validateImageExtension(file.getOriginalFilename());

        // 업로드
        try {
            return uploadFileToS3(file);
        } catch (IOException e) {
            throw new FIleIOException(ErrorCode.FAILED_IO_OPERATION);
        }
    }


    private void validateImageExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new FIleIOException(ErrorCode.NON_FILE_EXTENSION);
        }

        String extension = fileName.substring(lastDotIndex + 1).toLowerCase();
        if(!ALLOWED_FILE_EXTENSIONS.contains(extension)) {
            throw new FIleIOException(ErrorCode.INVALID_FILE_EXTENSION);
        }
    }

    private String uploadFileToS3(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); //원본 파일 명
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

        InputStream is = file.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try{
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest); // put image to S3
        }catch (Exception e){
            throw new FIleIOException(ErrorCode.FAILED_PUT_FILE);
        }finally {
            byteArrayInputStream.close();
            is.close();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }
}
