package hh5.twogaether.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
@RequiredArgsConstructor
public class AwsConfig {

    @Value("${cloud.aws.credentials.accessKey}")
    private String awsAccessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String awsSecretKey;

    @Bean // 빈 등록해서 의존성 주입
    public StaticCredentialsProvider getAwsBasicCredentials() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(awsAccessKey, awsSecretKey)
        );
    }

    @Bean // 빈 등록해서 의존성 주입
    public S3Client s3Client(){
        return S3Client.builder()
                .credentialsProvider(getAwsBasicCredentials())
                .region(Region.AP_NORTHEAST_2)
                .build();
    }
}


