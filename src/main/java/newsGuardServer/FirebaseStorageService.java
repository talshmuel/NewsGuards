package newsGuardServer;

import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    private final String bucketName = "newsguardimages.appspot.com"; // Replace with your Firebase Storage bucket name

    public String uploadImage(String fileName, byte[] content) {
        String uniqueFileName = UUID.randomUUID() + "-" + fileName;
        BlobId blobId = BlobId.of(bucketName, uniqueFileName);
        Blob blob = storage.create(BlobInfo.newBuilder(blobId).build(), content);

        return "https://storage.googleapis.com/" + bucketName + "/" + uniqueFileName;
    }
}
