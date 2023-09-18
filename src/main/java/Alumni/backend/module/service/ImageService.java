package Alumni.backend.module.service;

import Alumni.backend.module.domain.Image;
import Alumni.backend.module.repository.ImageRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@DependsOn("chatGptService")
public class ImageService {
    private final FileService fileService;
    private final ImageRepository imageRepository;

    @Transactional
    public String saveProfileImage(MultipartFile file) {

        if (file.getSize() > 2097152)//2MB 보다 큰 경우
            throw new MaxUploadSizeExceededException(file.getSize());

        String storageImageName = fileService.uploadFile(file);
        imageRepository.save(new Image(file.getOriginalFilename(), storageImageName, fileService.getFileUrl(storageImageName)));
        return storageImageName;
    }
}
