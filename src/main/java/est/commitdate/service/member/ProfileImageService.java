package est.commitdate.service.member;

import est.commitdate.entity.Member;
import est.commitdate.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileImageService {

    private final MemberRepository memberRepository;
    //application.yml 에서 가져올 이미지 저장 경로
    @Value("${image.upload-dir}")
    private String uploadDir;

    private final int VALID_MAX_WIDTH = 500;
    private final int VALID_MAX_HEIGHT = 500;

    private final int MAX_WIDTH = 48;
    private final int MAX_HEIGHT = 48;

    private final int MAX_SIZE = 2 * 1024 * 1024; //2MB

    private final MemberService memberService;

    public ResponseEntity<String> uploadProfileImage(MultipartFile file, HttpSession session,Long userId) {

        Member member = memberService.getLoggedInMember(session);

        if (member == null) {
            return ResponseEntity.badRequest().body("로그인되지 않았습니다.");
        }

        Member requestMember = memberRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        if(!member.equals(requestMember)) {
            return ResponseEntity.badRequest().body("올바르지못한 요청");
        }

        if (file.isEmpty()) {//빈파일이면
            return ResponseEntity.badRequest().body("파일 없음");
        }
        if (file.getSize() > MAX_SIZE) {
            return ResponseEntity.badRequest().body("이미지 용량 초과(최대 500KB)");
        }


        try {

            //이미지 크기 편집할 수 있게끔 만들기
            BufferedImage image = ImageIO.read(file.getInputStream());

            //
            if (image == null) {
                return ResponseEntity.badRequest().body("파일이 유효하지 않음");
            }

            //파일이 너무 과하게 클 경우
            if (image.getWidth() > VALID_MAX_WIDTH || image.getHeight() > VALID_MAX_HEIGHT) {
                return ResponseEntity.badRequest().body("이미지 크기가 초과됨. (최대 500* 500");
            }

            //이미지 크롭 & 리사이징
            BufferedImage processDoneImage = cropAndResizeImage(image, MAX_WIDTH, MAX_HEIGHT);

            //업로드 경로 표시(application.yml 에 있음.)
            Path uploadPath = Paths.get(uploadDir);

            //저장디렉토리가 없으면 생성함.
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }


            String fileName = member.getId() + member.getNickname() + ".jpg";
//                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))

            log.info(fileName);

            //폴더경로와 파일이름 합치기.
            Path filePath = uploadPath.resolve(fileName);
            log.info(filePath.toString());

            //이미지 저장
            ImageIO.write(processDoneImage, "jpg", filePath.toFile());

            //파일 이름 업데이트하기.
            requestMember.profileImageUrlUpdate(fileName);
//            memberRepository.save(member);


//            file.transferTo(filePath.toFile());
            log.info("저장 완료");
            return ResponseEntity.ok("/image/profiles/" + fileName);
        } catch (
                IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("알 수 없는 오류 발생!");
        }
    }

    public BufferedImage cropAndResizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {

        //원본 이미지이이ㅣ이이ㅠ
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        //작은 쪽 기준으로 1:1 크롭
        int cropSize = Math.min(originalWidth, originalHeight);

        //크롭 센터 따기
        int cropX = (originalWidth - cropSize) / 2;
        int cropY = (originalHeight - cropSize) / 2;

        //크롭 진행 구문(x 지점, y지점, 가로사이즈, 세로사이즈)
        BufferedImage croppedImage = originalImage.getSubimage(cropX, cropY, cropSize, cropSize);

        // 리사이징
        //아까 크롭한 이미지(croppedImage)를 리사이징 해줌.(가로길이, 세로길이, 스케일힌트)
        Image resizedImage = croppedImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

        //(가로, 세로, 이미지 타입)틀 정의 tip:TYPE_INT_RGB 는 24bit 임)
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

        //outputImage 에 그림그릴 툴 준비.(어떤 버퍼드 이미지의 그래픽스 객체준비)
        Graphics2D g2d = outputImage.createGraphics();

        //리사이징까지 끝난 이미지를 장전함.
        g2d.drawImage(resizedImage, 0, 0, null);

        //그림그려라 이 때, outputImage 에  그림이 그려짐.
        g2d.dispose();

        //반환.
        return outputImage; //g2d를 안보내는 이유는 g2d의 메서드로 작업을 하면 최초 생성했던 outputImage 에서 작업이 이루어지므로
    }


    public ResponseEntity<String> defaultProfileImage(HttpSession session, Long userId) {

        Member member = memberService.getLoggedInMember(session);

        if (member == null) {
            return ResponseEntity.badRequest().body("로그인되지 않았습니다.");
        }

        Member requestMember = memberRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        if (!member.equals(requestMember)) {
            return ResponseEntity.badRequest().body("올바르지못한 요청");
        }

        requestMember.defaultProfileImageUpdate();
        log.info(requestMember.getProfileImage());

        return ResponseEntity.ok("기본이미지로 변경했습니다.");
    }

}