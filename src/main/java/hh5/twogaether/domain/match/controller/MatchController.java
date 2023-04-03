package hh5.twogaether.domain.match.controller;

import hh5.twogaether.domain.chat.service.ChatRoomService;
import hh5.twogaether.domain.loves.entity.Love;
import hh5.twogaether.domain.loves.service.LoveService;
import hh5.twogaether.domain.match.dto.MatchDogResponseDto;
import hh5.twogaether.domain.match.service.MatchService;
import hh5.twogaether.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;
    private final LoveService loveService;
    private final ChatRoomService chatRoomService;

    //매칭 상대 보기
    @GetMapping
    public ResponseEntity<MatchDogResponseDto> GetMatchedOne(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(matchService.getRandomMatchedOne(userDetails.getUser()), OK);
    }

    //좋아요
    @PostMapping("/love/{dogId}")
    public ResponseEntity loveAndGetNext(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable Long dogId) {
        //좋아요 로직
        Love love = loveService.loveUser(dogId, userDetails.getUser());
        if (love.getMatchCode() == 1) {
            chatRoomService.createChatRoom(love);
        }
        matchService.loveUser(dogId, userDetails.getUser().getId());
        return new ResponseEntity<>("좋아요 완료", CREATED);
    }

    //싫어요
    @PostMapping("/reject/{dogId}")
    public ResponseEntity rejectAndGetNext(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     @PathVariable Long dogId) {
        matchService.passUser(dogId,userDetails.getUser().getId());
        return new ResponseEntity<>("싫어요 완료", CREATED);
    }

    @PostMapping("/superpowerbeam")
    public String dayReset() {
        matchService.resetRejects();
        return "싫어요 리셋 완료";
    }
}
