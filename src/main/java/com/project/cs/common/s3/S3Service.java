package com.project.cs.common.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void saveFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String savedFileName = getSavedFilename(originalFileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, savedFileName, multipartFile.getInputStream(), metadata);
        amazonS3.getUrl(bucket, savedFileName);
    }

    public String getSavedFilename(String originalFileName) {
        return UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf('.')); // .jpg
    }

    public String downloadImage(String savedFileName) {
        return amazonS3.getUrl(bucket, savedFileName).toString();
    }

    public void deleteImage(String savedFileName) {
        amazonS3.deleteObject(bucket, savedFileName);
    }
}
