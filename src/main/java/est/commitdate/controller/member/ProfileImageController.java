package est.commitdate.controller.member;

import est.commitdate.service.member.ProfileImageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    @PostMapping("/uploadProfileImage")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("file") @NotNull MultipartFile file,
            @RequestParam("userId") @NotNull Long userId,
            HttpSession session) {
        return profileImageService.uploadProfileImage(file,session, userId);
    }


    @PostMapping("/defaultProfileImage")
    public ResponseEntity<String> imgUploadTest(@RequestBody HashMap<String, Long> payload, HttpSession session){
        return profileImageService.defaultProfileImage(session, payload.get("userId"));
    }
}
