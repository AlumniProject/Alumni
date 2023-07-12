package Alumni.backend.module.service;

import Alumni.backend.module.domain.*;
import Alumni.backend.module.dto.requestDto.PostCreateRequestDto;
import Alumni.backend.module.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    public void postCreate(Member member, PostCreateRequestDto postCreateRequestDto){
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

    public void postDelete(Member member, Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Bad Request"));

        if(post.getMember().getId() != member.getId()) //삭제하는 사람이 작성한 글인지 확인
            throw new IllegalArgumentException("Bad Request");

        //기술 게시판인 경우 해시태그 삭제
        if(post.getBoard().getId() == 4){
            List<PostTag> postTag = postTagRepository.findByPostId(postId);

            postTagRepository.deleteAll(postTag);

            for (PostTag findPostTag : postTag) {
                Tag tag = tagRepository.findById(findPostTag.getTag().getId()).orElseThrow(() -> new IllegalArgumentException("Bad Request"));

                if(tag.getCount() == 1)//마지막인 경우 tag 삭제
                    tagRepository.delete(tag);
                else
                    tag.setCount(tag.getCount() - 1);
            }
        }
        postRepository.delete(post);
    }
}
