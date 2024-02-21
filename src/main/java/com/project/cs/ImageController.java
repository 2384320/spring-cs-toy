package com.project.cs;

import com.project.cs.exception.ResultData;
import com.project.cs.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final S3Service s3Service;

    @PostMapping()
    public ResponseEntity<Object> saveImage(@RequestParam(value = "file") MultipartFile file) throws IOException {
        s3Service.saveFile(file);
        return new ResponseEntity<>(new ResultData(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Object> downloadImage(String savedFileName) {
        s3Service.downloadImage(savedFileName);
        return new ResponseEntity<>(new ResultData(), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteImage(String savedFileName) {
        s3Service.deleteImage(savedFileName);
        return new ResponseEntity<>(new ResultData(), HttpStatus.OK);
    }

}