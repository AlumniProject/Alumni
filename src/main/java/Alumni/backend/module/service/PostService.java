package Alumni.backend.module.service;

import Alumni.backend.module.domain.*;
import Alumni.backend.module.dto.requestDto.PostCreateRequestDto;
import Alumni.backend.module.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    public void PostCreate(Member member, PostCreateRequestDto postCreateRequestDto){
        if(!memberRepository.existsMemberById(member.getId()))//존재하지 않는 회원인 경우
            throw new IllegalArgumentException("Bad Request");

        Board board = boardRepository.findById(postCreateRequestDto.getBoardId()).orElseThrow(() -> new IllegalArgumentException("Bad Request"));

        Post createPost = Post.createPost(member, board, postCreateRequestDto.getTitle(), postCreateRequestDto.getContent());
        if(postCreateRequestDto.getBoardId() == 2) //우리대학 게시판인 경우
            createPost.setUniversityId(member.getUniversity().getId());//대학Id 넣어주기
        Post post = postRepository.save(createPost);

        //태그가 있는 경우
        for(int i =0; i<postCreateRequestDto.getHashTag().size(); i++){
            Tag tag;
            String hashTag = postCreateRequestDto.getHashTag().get(i);

            if(tagRepository.findByName(hashTag).isPresent()){//이미 있는 태그인 경우
                tag = tagRepository.findByName(hashTag).get();
                tag.setCount(tag.getCount()+1);//count 증가
            }else{//새로운 태그인 경우
                tag = tagRepository.save(Tag.createTag(hashTag));
            }

            postTagRepository.save(PostTag.createPostTag(post, tag));
        }
    }
}
