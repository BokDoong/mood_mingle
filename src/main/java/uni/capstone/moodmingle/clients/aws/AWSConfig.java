package uni.capstone.moodmingle.clients.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// AWS 설정 파일
@Configuration
public class AWSConfig {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    // Credentials Provider 설정
    @Bean
    public AWSStaticCredentialsProvider awsCredentialsProvider() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return new AWSStaticCredentialsProvider(credentials);
    }

    // S3
    @Bean
    public AmazonS3 awsS3(AWSStaticCredentialsProvider awsCredentialsProvider) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }

    // KMS
    @Bean
    public AWSKMS awsKms(AWSStaticCredentialsProvider awsCredentialsProvider) {
        return AWSKMSClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }
}
