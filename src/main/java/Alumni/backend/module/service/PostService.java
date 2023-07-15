package Alumni.backend.module.service;

import Alumni.backend.module.domain.*;
import Alumni.backend.module.dto.requestDto.PostCreateRequestDto;
import Alumni.backend.module.dto.requestDto.PostModifyRequestDto;
import Alumni.backend.module.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    public void postCreate(Member member, PostCreateRequestDto postCreateRequestDto){
        Board board = boardRepository.findById(postCreateRequestDto.getBoardId()).orElseThrow(() -> new IllegalArgumentException("Bad Request"));

        Post createPost = Post.createPost(member, board, postCreateRequestDto.getTitle(), postCreateRequestDto.getContent());
        Post post = postRepository.save(createPost);

        if(postCreateRequestDto.getBoardId() == 3){//기술게시판인 경우
            List<String> hashTag = postCreateRequestDto.getHashTag();

            if(hashTag.size() > 5)//해시태그는 5개까지 가능
                throw new IllegalArgumentException("해시태그가 5개 이상입니다.");

            List<PostTag> postTagList = savePostTag(hashTag, post);

            postTagRepository.saveAll(postTagList);
        }
    }

    @Transactional
    public void postModify(Member member, Long postId, PostModifyRequestDto postModifyRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Bad Request"));

        if(post.getMember().getId() != member.getId()) //수정하는 사람이 작성한 글인지 확인
            throw new IllegalArgumentException("Bad Request");

        post.postModify(postModifyRequestDto.getTitle(), postModifyRequestDto.getContent());//수정글 update

        //기술 게시판인 경우
        if(post.getBoard().getId() == 3){
            List<PostTag> findPostTag = postTagRepository.findByPostId(postId);

            //기존 태그 삭제
            postTagRepository.deleteAll(findPostTag);
            deleteTag(findPostTag);

            //새로운 태그 저장
            List<String> hashTag = postModifyRequestDto.getHashTag();
            List<PostTag> postTagList = savePostTag(hashTag, post);

            postTagRepository.saveAll(postTagList);
        }
    }

   public void postDelete(Member member, Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Bad Request"));

        if(post.getMember().getId() != member.getId()) //삭제하는 사람이 작성한 글인지 확인
            throw new IllegalArgumentException("Bad Request");

       //기술 게시판인 경우 해시태그 삭제
        if(post.getBoard().getId() == 3){
            List<PostTag> postTag = postTagRepository.findByPostId(postId);
            postTagRepository.deleteAll(postTag);
            deleteTag(postTag);
        }
        postRepository.delete(post);
    }

    private List<PostTag> savePostTag(List<String> hashTag, Post post) {
        List<PostTag> postTagList = new ArrayList<>();

        for (String s : hashTag) {
            Tag tag = tagRepository.findByName(s).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 해시태그 입니다."));
            postTagList.add(PostTag.createPostTag(post, tag));
            tag.setCount(tag.getCount() + 1);//count 증가
        }

        return postTagList;
    }

    private void deleteTag(List<PostTag> postTag) {
        for (PostTag findPostTag : postTag) {
            Tag tag = findPostTag.getTag();
            tag.setCount(tag.getCount()-1);
        }
    }
}
