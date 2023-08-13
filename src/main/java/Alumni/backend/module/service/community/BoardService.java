package Alumni.backend.module.service.community;

import Alumni.backend.module.domain.community.Board;
import Alumni.backend.module.repository.community.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @PostConstruct
    public void initBoardData() {
        if (boardRepository.count() == 0) {
            Board board1 = Board.builder()
                    .name("자유")
                    .build();
            Board board2 = Board.builder()
                    .name("우리 대학")
                    .build();
            Board board3 = Board.builder()
                    .name("기술 Q&A")
                    .build();
            Board board4 = Board.builder()
                    .name("공모전")
                    .build();
            Board board5 = Board.builder()
                    .name("취업&진로")
                    .build();
            boardRepository.save(board1);
            boardRepository.save(board2);
            boardRepository.save(board3);
            boardRepository.save(board4);
            boardRepository.save(board5);
        }
    }
}
