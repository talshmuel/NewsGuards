package newsGuardServer;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import logic.engine.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.Bucket;
//import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
//import software.amazon.awssdk.services.s3.model.S3Exception;
import java.util.List;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;


@CrossOrigin(origins = "https://news-guard.vercel.app")
//@CrossOrigin(origins = "http://localhost:5173")
// ReportController.java
@RestController
@RequestMapping("/report")
public class ReportController {
    Engine engine;

    @Autowired
    public  ReportController(Engine engine)
    {
        this.engine = engine;
    }

    @PostMapping("/add-new-report")
    public ResponseEntity<String> createReport(@RequestBody NewReportDTO newReportDTO) {
        try {
            System.out.println(newReportDTO.getIsAnonymousReport());
            engine.addNewReportAndStartVerificationProcess(newReportDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Report created successfully");
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private final String uploadDir = "images/"; // GON: Directory to save uploaded images
    private final String bucketName = "images-news-guard";
    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
//            if (file.isEmpty()) {
//                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "File is empty"));
//            }
//
//            // Save the file to the local directory
            String fileName = file.getOriginalFilename();
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_NORTH_1).build();
            s3.putObject(bucketName,fileName,file.getInputStream(),new ObjectMetadata());
            String imageUrl = "https://images-news-guard.s3.eu-north-1.amazonaws.com/" + fileName;
//            Path filePath = Paths.get(uploadDir + fileName);
//            Files.createDirectories(filePath.getParent());
//            Files.write(filePath, file.getBytes());
//
//            // Construct the URL for the image
//            String imageUrl = "https://news-guard-c0fjanc7ethue7dn.eastus-01.azurewebsites.net/images/" + fileName; //כאן צריך לשנות ךכתובת של הסרבר שבענן!!
//
//            // Return the URL as part of the response


            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Image upload failed"));
        }
    }


    @PostMapping("/add-comment")
    public ResponseEntity<String> comment(@RequestBody CommentDTO commentDTO) {
        try {
            engine.addCommentToReport(commentDTO);
            return ResponseEntity.ok("Comment successfully added");
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/like-or-unlike")
    public ResponseEntity<String> likeOrUnlikeReport(
            @RequestParam("reportID") int reportID,
            @RequestParam("userID") int userID) {
        try {
            engine.addOrRemoveLikeToReport(reportID, userID);
            return ResponseEntity.ok("Like added / removed successfully");
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}





