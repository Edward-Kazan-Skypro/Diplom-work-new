package pro.sky.finalprojectsky.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import pro.sky.finalprojectsky.dto.AdsCommentDto;
import pro.sky.finalprojectsky.dto.AdsDto;
import pro.sky.finalprojectsky.dto.CreateAdsDto;
import pro.sky.finalprojectsky.dto.FullAdsDto;
import pro.sky.finalprojectsky.entity.Ads;
import pro.sky.finalprojectsky.entity.AdsComment;
import pro.sky.finalprojectsky.entity.User;
import pro.sky.finalprojectsky.mapper.AdsCommentMapper;
import pro.sky.finalprojectsky.mapper.AdsMapper;
import pro.sky.finalprojectsky.repository.AdsCommentRepository;
import pro.sky.finalprojectsky.repository.AdsRepository;
import pro.sky.finalprojectsky.repository.UserRepository;
import pro.sky.finalprojectsky.service.AdsService;
import pro.sky.finalprojectsky.service.ImageService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service

public class AdsServiceImpl implements AdsService {
    Logger logger = LoggerFactory.getLogger(AdsServiceImpl.class);

    private final AdsRepository adsRepository;

    private final AdsCommentRepository adsCommentRepository;

    private final UserRepository userRepository;

    private final ImageService imagesService;

    private final AdsMapper adsMapper;

    private final AdsCommentMapper adsCommentMapper;

    @Override
    public AdsDto createAds(CreateAdsDto createAdsDto, MultipartFile imageFile) throws IOException {
        //logger.info("Was invoked method for create ad");
        User user = userRepository.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName()).orElseThrow();

        Ads ads = adsMapper.toEntity(createAdsDto);
        ads.setAuthor(user);
        ads.setImage(imagesService.uploadImage(imageFile, adsRepository.save(ads)));
        //logger.info("ad created");
        return adsMapper.toDto(adsRepository.save(ads));
    }

    @Transactional(readOnly = true)
    @Override
    public Ads getAds(Integer id) {
        //logger.info("Was invoked method for get ad by id");
        return adsRepository.findById(id).orElseThrow(() -> new NotFoundException("Объявление с id " + id + " не найдено!"));
    }

    @Transactional(readOnly = true)
    @Override
    public FullAdsDto getFullAdsDto(Integer id) {
        logger.info("Was invoked method for get full ad dto");
        return adsMapper.toFullAdsDto(adsRepository.findById(id).orElseThrow(() -> new NotFoundException("Объявление с id " + id + " не найдено!")));
    }

    @Override
    public List<AdsDto> getAllAds() {
        logger.info("Was invoked method for get all ads");
        return adsMapper.toDto(adsRepository.findAll());
    }

    @Override
    public boolean removeAds(Integer id, Authentication authentication) throws IOException {
        logger.info("Was invoked method for delete ad by id");
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Объявление с id " + id + " не найдено!"));
        logger.warn("Ad by id {} not found", id);
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        if (ads.getAuthor().getEmail().equals(user.getEmail()) || user.getRole().getAuthority().equals("ADMIN")) {
            List<Integer> adsComments = adsCommentRepository.findAll().stream()
                    .filter(adsComment -> adsComment.getAds().getId() == ads.getId())
                    .map(AdsComment::getId)
                    .collect(Collectors.toList());
            adsCommentRepository.deleteAllById(adsComments);
            imagesService.removeImage(ads.getImage().getId());
            adsRepository.delete(ads);
            logger.info("ad deleted");
            return true;
        }
        logger.warn("ad not deleted");
        return false;
    }

    @Override
    public AdsDto updateAds(Integer id, AdsDto updateAdsDto, Authentication authentication) {
        logger.info("Was invoked method for update ad by id");
        Ads updatedAds = adsRepository.findById(id).orElseThrow(() -> new NotFoundException("Объявление с id " + id + " не найдено!"));
        logger.warn("Ad by id {} not found", id);
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        if (updatedAds.getAuthor().getEmail().equals(user.getEmail()) || user.getRole().getAuthority().equals("ADMIN")) {
            updatedAds.setTitle(updateAdsDto.getTitle());
            updatedAds.setDescription(updateAdsDto.getDescription());
            updatedAds.setPrice(updateAdsDto.getPrice());
            adsRepository.save(updatedAds);
            return adsMapper.toDto(updatedAds);
        }
        logger.info("ad updated");
        return updateAdsDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AdsDto> getAdsMe() {
        logger.info("Was invoked method for get all my ads");
        User user = userRepository.findByEmail(SecurityContextHolder.getContext()
                .getAuthentication().getName()).orElseThrow();
        List<Ads> adsList = adsRepository.findAllByAuthorId(user.getId());
        return adsMapper.toDto(adsList);
    }
}