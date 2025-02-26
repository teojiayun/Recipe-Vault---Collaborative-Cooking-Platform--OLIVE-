// package com.recipevault.service;

// import java.io.IOException;
// import java.nio.file.Paths;
// import java.util.UUID;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
// import software.amazon.awssdk.regions.Region;
// import software.amazon.awssdk.services.s3.S3Client;
// import software.amazon.awssdk.services.s3.model.PutObjectRequest;

// @Service
// public class ImageService {
//     @Value("${olive-recipe-vault}")
//     private String bucketName;

//     private final S3Client s3Client = S3Client.builder()
//             .region(Region.AP_SOUTHEAST_2)
//             .credentialsProvider(DefaultCredentialsProvider.create())
//             .build();

//     public String uploadImage(MultipartFile file) throws IOException {
//         String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

//         s3Client.putObject(
//             PutObjectRequest.builder()
//                     .bucket(bucketName)
//                     .key(fileName)
//                     .acl("public-read")
//                     .build(),
//             Paths.get(file.getOriginalFilename())
//         ); 

//         return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
//     }
// }
