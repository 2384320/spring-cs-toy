package com.project.cs.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String savedFileName = getSavedFilename(originalFileName);
        String savedFileUrl = getSavedFileUrl(savedFileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFileName, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFileName).toString();
    }

    public String getSavedFilename(String originalFileName) {
        return UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf('.')); // .jpg
    }

    public String getSavedFileUrl(String savedFileName) {
        return new StringBuilder()
                .append("https://")
                .append(bucket)
                .append(".s3.")
                .append(region)
                .append(".amazonaws.com/")
                .append(new SimpleDateFormat("yyMMdd").format(new Date()))
                .append("/")
                .append(savedFileName)
                .toString();
    }

    public ResponseEntity<UrlResource> downloadImage(String originalFilename) {
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFilename));

        String contentDisposition = "attachment; filename=\"" + originalFilename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    public void deleteImage(String originalFilename) {
        amazonS3.deleteObject(bucket, originalFilename);
    }
}
