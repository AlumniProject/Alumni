package Alumni.backend.module.service;

import Alumni.backend.module.domain.Board;
import Alumni.backend.module.repository.BoardRepository;
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
                    .description("모든 유저들이 사용 가능한 게시판")
                    .build();
            Board board2 = Board.builder()
                    .name("우리 대학")
                    .description("같은 대학교 소속 사람들과의 소통이 가능한 게시판")
                    .build();
            Board board3 = Board.builder()
                    .name("기술 Q&A")
                    .description("다양한 분야의 기술 관련 Q&A를 진행하는 게시판")
                    .build();
            Board board4 = Board.builder()
                    .name("공모전")
                    .description("IT 관련 다양한 공모전을 추천해주고, 팀원 모집까지 도와주는 게시판")
                    .build();
            Board board5 = Board.builder()
                    .name("취업&진로")
                    .description("IT 관련 취업시장과 진로에 대한 정보를 업로드하는 게시판")
                    .build();
            boardRepository.save(board1);
            boardRepository.save(board2);
            boardRepository.save(board3);
            boardRepository.save(board4);
            boardRepository.save(board5);
        }
    }
}
