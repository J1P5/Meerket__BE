package org.j1p5.infrastructure.global.s3;

import static org.j1p5.infrastructure.global.s3.exception.S3ErrorCode.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.domain.product.service.ImageService;
import org.j1p5.infrastructure.global.exception.InfraException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Override
    public List<String> upload(List<File> images) {
        List<String> imageList = new ArrayList<>();
        if (images.isEmpty()) {
            throw new InfraException(EMPTY_FILE_EXCEPTION);
        }
        for (File image : images) {
            imageList.add(uploadImage(image));
        }
        return imageList;
    }

    private String uploadImage(File image) {
        ImageValidator.validateImageFile(image);
        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new InfraException(IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }
    }

    private String uploadImageToS3(File image) throws IOException {
        String originalFilename = image.getName(); // 원본 파일 명
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); // 확장자 명

        String s3FileName =
                UUID.randomUUID().toString().substring(0, 10) + originalFilename; // 변경된 파일 명

        // 파일 내용 읽기
        byte[] bytes;
        try (FileInputStream fileInputStream = new FileInputStream(image)) {
            bytes = IOUtils.toByteArray(fileInputStream);
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata);
            amazonS3.putObject(putObjectRequest); // put image to S3
        } catch (Exception e) {
            e.printStackTrace();
            throw new InfraException(PUT_OBJECT_EXCEPTION);
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void deleteImageFromS3(String imageAddress) {
        String key = getKeyFromImageAddress(imageAddress);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            throw new InfraException(IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8.name());
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new InfraException(IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }
}
