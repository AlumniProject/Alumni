package Alumni.backend.module.service;

import Alumni.backend.module.domain.Board;
import Alumni.backend.module.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @PostConstruct
    public void initBoardData() throws IOException{
        if(boardRepository.count() == 0){
            List<String> list = Arrays.asList("자유", "우리대학", "기술Q&A", "공모전", "취업&진로");
            List<Board> boards = list.stream()
                    .map(b -> Board.builder()
                            .name(b)
                            .build())
                    .collect(Collectors.toList());
            boardRepository.saveAll(boards);
        }
    }
}
