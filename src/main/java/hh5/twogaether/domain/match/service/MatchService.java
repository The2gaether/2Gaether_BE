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
    public MatchDogResponseDto getRandomMatchedOne(User me) {
        checkIsExistMyDog(me.getId());
        List<Match> matches = matchRepository.findAllNotPassedByCreatedBy(me.getId());
        if (matches.isEmpty()) {
            matchRepository.deleteAllByCreatedBy(me.getId());
            createMatches(me);
        }
        matches = getShuffledMatches(me.getId());
        return getOne(matches);
    }

    private void checkIsExistMyDog(Long id) {
        List<Dog> myDogs = dogRepository.findAllNotDeletedByCreatedByDog(id);
        if (myDogs.isEmpty()) {
            throw new IllegalArgumentException("강아지를 추가 후 이용해주세요");
        }
    }

    private List<Match> getShuffledMatches(Long id) {
        List<Match> matches = matchRepository.findAllNotPassedByCreatedBy(id);
        shuffle(matches);
        log.info("size = {}",matches.size());
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("매칭 상대가 없습니다");
        }
        return matches;
    }

    private MatchDogResponseDto getOne(List<Match> matches) {
        int distance = matches.get(0).getDistance();
        Long dogId = matches.get(0).getDogId();
        Dog dog = dogRepository.findById(dogId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
        MatchDogResponseDto result = new MatchDogResponseDto(dog, distance);
        return result;
    }

    @Transactional
    public void loveUser(Long dogId, Long myId) {
        Match opponent = matchRepository.findByDogIdAndCreatedBy(dogId, myId);
        Pass foundPass = passRepository.findByCreatedByAndDogId(myId, dogId);
        if (foundPass == null) {
            passRepository.save(new Pass(opponent.getOpponentId(),dogId,true));
        }
        opponent.passUser();
    }

    @Transactional
    public void passUser(Long dogId, Long myId) {
        Match opponent = matchRepository.findByDogIdAndCreatedBy(dogId, myId);
        Pass foundPass = passRepository.findByCreatedByAndDogId(myId, dogId);
        if (foundPass == null) {
            passRepository.save(new Pass(opponent.getOpponentId(),dogId,false));
        }
        opponent.passUser();
    }

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
