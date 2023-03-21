package hh5.twogaether.domain.dog.controller;

import hh5.twogaether.domain.dog.dto.DogPatchRequestDto;
import hh5.twogaether.domain.dog.dto.DogResponseDto;
import hh5.twogaether.domain.dog.dto.DogSignupRequestDto;
import hh5.twogaether.domain.dog.entity.Dog;
import hh5.twogaether.domain.dog.service.DogService;
import hh5.twogaether.domain.image.service.ImageService;
import hh5.twogaether.domain.users.dto.ResponseMessageDto;
import hh5.twogaether.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/dogs") //공용으로 쓰는 주소
public class DogController {

    private final DogService dogService;
    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<Boolean> checkMyDog(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(dogService.isExistMyDog(userDetails.getUser().getId()), OK);
    }

    @PostMapping
    private ResponseEntity<ResponseMessageDto> addMyDog(@ModelAttribute DogSignupRequestDto dogRequestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        Dog savedDog = dogService.saveDog(dogRequestDto, userDetails.getUser());
        List<String> imgUrls = imageService.upload(dogRequestDto.getImages(),savedDog);
        log.info("imgUrls = " + imgUrls);
        return new ResponseEntity<>(new ResponseMessageDto(CREATED.value(), "강아지 정보 저장 완료"), CREATED);
    }

    @GetMapping("/{dogId}")
    private ResponseEntity<DogResponseDto> showDog(@PathVariable Long dogId){
        return new ResponseEntity<>(dogService.showDog(dogId),HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{dogId}")
    private ResponseEntity patchMyDog(@PathVariable Long dogId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody DogPatchRequestDto dogPatchRequestDto){
        dogService.patchMyDog(dogId,userDetails.getUser(), dogPatchRequestDto);
        return new ResponseEntity(202,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{dogId}")
    private ResponseEntity<ResponseMessageDto> deleteMyDog(@PathVariable Long dogId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        dogService.deleteMyDog(dogId,userDetails.getUser());
        return new ResponseEntity<>(new ResponseMessageDto(OK.value(), "강아지 정보 삭제 완료"), OK);
    }
}
