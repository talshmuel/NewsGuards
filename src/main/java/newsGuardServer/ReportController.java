package newsGuardServer;
//import data.transfer.object.report.CommentDTO;
//import data.transfer.object.report.NewReportDTO;
//import logic.engine.Engine;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.NoSuchElementException;
//
//@CrossOrigin(origins = "https://news-guard.vercel.app")
//
//// ReportController.java
//@RestController
//@RequestMapping("/report")
//public class ReportController {
//    Engine engine;
//
//    private final String uploadDir = "images/"; // GON: Directory to save uploaded images
//    @Autowired
//    public  ReportController(Engine engine)
//    {
//        this.engine = engine;
//    }
//
//    @PostMapping("/add-new-report")
//    public ResponseEntity<String> createReport(@RequestBody NewReportDTO newReportDTO) {
//        try {
//            System.out.println(newReportDTO.getIsAnonymousReport());
//            engine.addNewReportAndStartVerificationProcess(newReportDTO);
//            return ResponseEntity.status(HttpStatus.CREATED).body("Report created successfully");
//        }catch (NoSuchElementException e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//
//    @PostMapping("/upload-image")
//    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "File is empty"));
//            }
//
//            // Save the file to the local directory
//            String fileName = file.getOriginalFilename();
//            Path filePath = Paths.get(uploadDir + fileName);
//            Files.createDirectories(filePath.getParent());
//            Files.write(filePath, file.getBytes());
//
//            // Construct the URL for the image
//            String imageUrl = "https://news-guard-c0fjanc7ethue7dn.eastus-01.azurewebsites.net/images/" + fileName; //כאן צריך לשנות ךכתובת של הסרבר שבענן!!
//
//            // Return the URL as part of the response
//            Map<String, String> response = new HashMap<>();
//            response.put("imageUrl", imageUrl);
//
//            return ResponseEntity.ok(response);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Collections.singletonMap("message", "Image upload failed"));
//        }
//    }
//
//
//    @PostMapping("/add-comment")
//    public ResponseEntity<String> comment(@RequestBody CommentDTO commentDTO) {
//        try {
//            engine.addCommentToReport(commentDTO);
//            return ResponseEntity.ok("Comment successfully added");
//        }catch (NoSuchElementException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//    @PutMapping("/like-or-unlike")
//    public ResponseEntity<String> likeOrUnlikeReport(
//            @RequestParam("reportID") int reportID,
//            @RequestParam("userID") int userID) {
//        try {
//            engine.addOrRemoveLikeToReport(reportID, userID);
//            return ResponseEntity.ok("Like added / removed successfully");
//        }catch (NoSuchElementException e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//
//    }
//}



import com.azure.storage.blob.BlobClient;
        import com.azure.storage.blob.BlobContainerClient;
        import com.azure.storage.blob.BlobServiceClient;
        import com.azure.storage.blob.BlobServiceClientBuilder;
        import com.azure.storage.blob.models.BlobHttpHeaders;
import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import logic.engine.Engine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.multipart.MultipartFile;

        import java.util.Collections;
        import java.util.HashMap;
        import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/report")//
@CrossOrigin(origins = "https://news-guard.vercel.app")
public class ReportController {
    Engine engine;
    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")////
    private String containerName;

    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "File is empty"));
            }

            // Initialize BlobServiceClient and BlobContainerClient
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Upload the file to Azure Blob Storage
            String fileName = file.getOriginalFilename();
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            blobClient.upload(file.getInputStream(), file.getSize(), true);

            // Set the content type for the blob
            BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(file.getContentType());
            blobClient.setHttpHeaders(headers);

            // Construct the Blob URL
            String blobUrl = blobClient.getBlobUrl();

            // Return the Blob URL as part of the response
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", blobUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Image upload failed"));
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


}




