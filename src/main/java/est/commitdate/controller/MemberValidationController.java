package est.commitdate.controller;


import est.commitdate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberValidationController {

        private final MemberRepository memberRepository;

        @GetMapping("/check-email")
        public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String value) {
            boolean exists = memberRepository.findByEmail(value).isPresent();
            return ResponseEntity.ok(Collections.singletonMap("exists", exists));
        }

        @GetMapping("/check-nickname")
        public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String value) {
            boolean exists = memberRepository.findByNickname(value).isPresent();
            return ResponseEntity.ok(Collections.singletonMap("exists", exists));
        }

        @GetMapping("/check-phone-number")
        public ResponseEntity<Map<String, Boolean>> checkPhoneNumber(@RequestParam String value) {
            boolean exists = memberRepository.findByPhoneNumber(value).isPresent();
            return ResponseEntity.ok(Collections.singletonMap("exists", exists));
        }

}
