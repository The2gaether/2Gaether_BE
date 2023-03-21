package hh5.twogaether.domain.match.service;

import hh5.twogaether.domain.dog.entity.Dog;
import hh5.twogaether.domain.dog.repository.DogRepository;
import hh5.twogaether.domain.match.dto.MatchDogResponseDto;
import hh5.twogaether.domain.match.dto.MatchDto;
import hh5.twogaether.domain.match.entity.Match;
import hh5.twogaether.domain.match.entity.Pass;
import hh5.twogaether.domain.match.repository.MatchQueryRepository;
import hh5.twogaether.domain.match.repository.MatchRepository;
import hh5.twogaether.domain.match.repository.PassRepository;
import hh5.twogaether.domain.users.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Collections.shuffle;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final DogRepository dogRepository;
    private final MatchRepository matchRepository;
    private final PassRepository passRepository;
    private final MatchQueryRepository matchQueryRepository;

    @Transactional
    public MatchDogResponseDto getMatches(User me) {

        //내 개가 있을때 로직 시작
        isExistMyDog(me.getId());

        //기존에 저장된 리스트가 있을 시 기존 정보 사용, 없을 시 새 리스트 저장
        if (matchRepository.findByCreatedBy(me.getId()).size() == 0) {
            createMatches(me);
        }
        //좋아요, 싫어요 하지 않은 목록 불러와서 셔플
        List<Match> matches = getAndShuffleMatches(me.getId());

        //다 넘겨서 남은 매칭상대가 없는 경우 Matches 다 지우고 다시 갱신
        if (matches.size() == 0) {
            matchRepository.deleteAllByCreatedBy(me.getId());
            createMatches(me);
            matches = getAndShuffleMatches(me.getId());
        }
        //예외처리
        if (matches.size() == 0) {
            throw new IllegalArgumentException("매칭 상대가 없습니다");
        }
        //matches 에서 1개 뽑아서 보여줌
        int distance = matches.get(0).getDistance();
        Long dogId = matches.get(0).getDogId();
        Dog dog = dogRepository.findById(dogId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
        return new MatchDogResponseDto(dog, distance);
    }

    private void isExistMyDog(Long id) {
        List<Dog> myDogs = dogRepository.findAllNotDeletedByCreatedByDog(id);
        if (myDogs.isEmpty()) {
            throw new IllegalArgumentException("강아지를 추가 후 이용해주세요");
        }
    }

    //좋아요, 싫어요 하지 않은 목록 불러와서 셔플
    private List<Match> getAndShuffleMatches(Long id) {
        List<Match> matches = matchRepository.findAllNotPassedByCreatedBy(id);
        shuffle(matches);
        log.info("size = {}",matches.size());
        return matches;
    }

    //좋아요
    @Transactional
    public void loveUser(Long dogId, Long myId) {
        Match opponent = matchRepository.findByDogIdAndCreatedBy(dogId, myId);
        Pass foundPass = passRepository.findByCreatedByAndDogId(myId, dogId);
        if (foundPass == null) {
            passRepository.save(new Pass(opponent.getOpponentId(),dogId,true));
        }
        opponent.passUser();
    }

    //싫어요
    @Transactional
    public void passUser(Long dogId, Long myId) {
        Match opponent = matchRepository.findByDogIdAndCreatedBy(dogId, myId);
        Pass foundPass = passRepository.findByCreatedByAndDogId(myId, dogId);
        if (foundPass == null) {
            passRepository.save(new Pass(opponent.getOpponentId(),dogId,false));
        }
        opponent.passUser();
    }

    //Match 리스트 저장
    @Transactional
    public void createMatches(User me) {
        List<MatchDto> matchDtos = matchQueryRepository.findAllNotDeletedAndNotPassedDog(me.getId());
        for (MatchDto dto : matchDtos) {
            double calculatedDistance = calculateDistance(me.getLatitude(), me.getLongitude(), dto.getLatitude(), dto.getLongitude());
            int roundDistance = roundDistance(calculatedDistance);
            if ( me.getRanges() >= roundDistance && !dto.getOpponentId().equals(me.getId()) ) {
                matchRepository.save(new Match(dto.getDogId(),dto.getOpponentId(),roundDistance));
            }
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        log.info("distance = "+earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2)));
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }

    //거리 반올림하기
    private int roundDistance(double distance) {
        if (Math.round(distance) < 1) {
            return 1;
        }
        return (int) Math.round(distance);
    }

    @Transactional
    public void resetRejects() {
        passRepository.deleteAllisNotLoved();
    }
}
