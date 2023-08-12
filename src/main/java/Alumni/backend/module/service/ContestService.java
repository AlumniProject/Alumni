package Alumni.backend.module.service;

import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.module.domain.Contest;
import Alumni.backend.module.dto.ContestResponseDto;
import Alumni.backend.module.repository.Contest.ContestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;

    public List<ContestResponseDto> contestSearch(String content) {
        List<ContestResponseDto> contestResponseDtos = new ArrayList<>();

        List<Contest> contests = contestRepository.findByContent(content).
                orElseThrow(() -> new NoExistsException("검색 결과가 없음"));

        for (Contest contest : contests) {
            contestResponseDtos.add(ContestResponseDto.getContestResponseDto(contest));
        }

        return contestResponseDtos;
    }
}