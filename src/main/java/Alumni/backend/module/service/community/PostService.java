package Alumni.backend.module.service.community;

import Alumni.backend.infra.event.community.GptCommentCreateEvent;
import Alumni.backend.infra.exception.FormalValidationException;
import Alumni.backend.infra.exception.NoExistsException;
import Alumni.backend.infra.response.PostSearchResponse;
import Alumni.backend.module.domain.community.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.dto.community.*;
import Alumni.backend.module.repository.community.*;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.community.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void postCreate(Member member, PostCreateRequestDto postCreateRequestDto) {
        Board board = boardRepository.findById(postCreateRequestDto.getBoardId()).orElseThrow(() -> new IllegalArgumentException("Bad Request"));

        Post createPost = Post.createPost(member, board, postCreateRequestDto.getTitle(), postCreateRequestDto.getContent());
        Post post = postRepository.save(createPost);

        if (postCreateRequestDto.getBoardId() == 3) {//기술게시판인 경우
            List<String> hashTag = postCreateRequestDto.getHashTag();

            if (hashTag.size() > 5)//해시태그는 5개까지 가능
                throw new FormalValidationException("해시태그가 5개 이상입니다.");

            List<PostTag> postTagList = savePostTag(hashTag, post);

            postTagRepository.saveAll(postTagList);
        }
    }

    public void postModify(Member member, Long postId, PostModifyRequestDto postModifyRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Bad Request"));

        if (!post.getMember().getId().equals(member.getId())) //수정하는 사람이 작성한 글인지 확인
            throw new IllegalArgumentException("Bad Request");

        post.postModify(postModifyRequestDto.getTitle(), postModifyRequestDto.getContent());//수정글 update

        //기술 게시판인 경우
        if (post.getBoard().getId() == 3) {
            List<PostTag> findPostTag = postTagRepository.findByPostId(postId);

            //기존 태그 삭제
            findPostTag.forEach(postTag -> postTag.getPost().getPostTags().remove(postTag));
            postTagRepository.deleteAll(findPostTag);
            deleteTag(findPostTag);

            //새로운 태그 저장
            List<String> hashTag = postModifyRequestDto.getHashTag();
            List<PostTag> postTagList = savePostTag(hashTag, post);

            postTagRepository.saveAll(postTagList);
        }
    }

    public void postDelete(Member member, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Bad Request"));

        if (!post.getMember().getId().equals(member.getId())) //수정하는 사람이 작성한 글인지 확인
            throw new IllegalArgumentException("Bad Request");

        //기술 게시판인 경우 해시태그 삭제
        if (post.getBoard().getId() == 3) {
            List<PostTag> postTag = postTagRepository.findByPostId(postId);
            postTagRepository.deleteAll(postTag);
            deleteTag(postTag);
        }

        //달린 댓글 삭제
        List<Comment> commentList = commentRepository.findByPostId(postId);

        //댓글에 달린 좋아요 삭제
        for (Comment comment : commentList) {
            List<CommentLike> findCommentLike = commentLikeRepository.findByCommentId(comment.getId());
            commentLikeRepository.deleteAll(findCommentLike);
        }

        commentRepository.deleteAll(commentList);

        //좋아요 삭제
        List<PostLike> likeList = postLikeRepository.findByPostId(postId);
        postLikeRepository.deleteAll(likeList);

        postRepository.delete(post);
    }

    private List<PostTag> savePostTag(List<String> hashTag, Post post) {
        List<PostTag> postTagList = new ArrayList<>();

        for (String s : hashTag) {
            Tag tag = tagRepository.findByName(s).orElseThrow(() -> new NoExistsException("존재하지 않는 해시태그 입니다."));
            postTagList.add(PostTag.createPostTag(post, tag));
            tag.setCount(tag.getCount() + 1);//count 증가
        }

        return postTagList;
    }

    private void deleteTag(List<PostTag> postTag) {
        for (PostTag findPostTag : postTag) {
            Tag tag = findPostTag.getTag();
            tag.setCount(tag.getCount() - 1);
        }
    }

    @Transactional(readOnly = true)
    public PostSearchResponse<?> search(Member user, PostSearch postSearch) {
        List<Post> posts = postRepository.searchPost(postSearch);
        List<String> tagRankList = null;
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        HashMap<Long, Long> postMap = postLikeRepository.countPostLikesByPostId();
        HashMap<Long, Long> commentMap = commentRepository.countCommentsByPostId();

        if (postSearch.getId() == 2) {
            if (postSearch.getHashTag() != null) {
                throw new IllegalArgumentException("Bad Request");
            }
            for (Post post : posts) {
                if (!post.getMember().getUniversity().getId().equals(user.getUniversity().getId())) {
                    continue;
                }
                Long likes = postMap.get(post.getId());
                Long comments = commentMap.get(post.getId());

                PostResponseDto.getPostResponseDto(post, likes, comments);
            }
        } else if (postSearch.getId() == 3) {
            tagRankList = tagRank();
            for (Post post : posts) {
                if (postSearch.getHashTag() != null) { // 검색 해시태그 있는 경우
                    if (post.getPostTags().isEmpty()) {
                        continue;
                    }
                    List<String> postTagList = post.getPostTags().stream() // hashTag 문자열 리스트로 변환
                            .map(postTag -> postTag.getTag().getName())
                            .collect(Collectors.toList());
                    int size = postSearch.getHashTag().size();
                    for (String postSearchHashTag : postSearch.getHashTag()) {
                        if (postTagList.contains(postSearchHashTag)) {
                            size--;
                        }
                    }
                    if (size == 0) {
                        Long likes = postMap.get(post.getId());
                        Long comments = commentMap.get(post.getId());

                        PostResponseDto postResponseDto = PostResponseDto.getPostResponseDto(post, likes, comments);
                        postResponseDto.setHashTag(postTagList);
                        postResponseDtos.add(postResponseDto);
                    }
                } else { // 검색 해시태그 없는 경우
                    Long likes = postMap.get(post.getId());
                    Long comments = commentMap.get(post.getId());

                    PostResponseDto postResponseDto = PostResponseDto.getPostResponseDto(post, likes, comments);
                    if (!post.getPostTags().isEmpty()) {
                        postResponseDto.setHashTag(post.getPostTags().stream() // hashTag 문자열 리스트로 변환
                                .map(postTag -> postTag.getTag().getName())
                                .collect(Collectors.toList()));
                    }
                    postResponseDtos.add(postResponseDto);
                }
            }
        } else {
            if (postSearch.getHashTag() != null) {
                throw new IllegalArgumentException("Bad Request");
            }
            for (Post post : posts) {
                Long likes = postMap.get(post.getId());
                Long comments = commentMap.get(post.getId());

                postResponseDtos.add(PostResponseDto.getPostResponseDto(post, likes, comments));
            }
        }
        return new PostSearchResponse<>(postResponseDtos, tagRankList, "게시글 검색 결과 전송 완료");
    }

    /*private PostResponseDto getPostResponseDto(Post post, long likes, long comments) {
        return PostResponseDto.getPostResponseDto(post, likes, comments);
    }*/

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllPosts(Member user) {
        // post + member + image fetch join
        List<Post> posts = postRepository.findAllPosts();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        HashMap<Long, Long> postMap = postLikeRepository.countPostLikesByPostId();
        HashMap<Long, Long> commentMap = commentRepository.countCommentsByPostId();

        for (Post post : posts) {
            if (post.getBoard().getId() == 2
                    && !post.getMember().getUniversity().getId().equals(user.getUniversity().getId())) {
                continue;
            }

            Long likes = postMap.get(post.getId());
            Long comments = commentMap.get(post.getId());

            PostResponseDto postResponseDto = PostResponseDto.getPostResponseDto(post, likes, comments);

            // hashTag 확인
            if (post.getBoard().getId() == 3 && !post.getPostTags().isEmpty()) {
                postResponseDto.setHashTag(post.getPostTags().stream() // hashTag 문자열 리스트로 변환
                        .map(postTag -> postTag.getTag().getName())
                        .collect(Collectors.toList()));
            }
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }

    // 가장 많은 태그 리스트 조회
    @Transactional(readOnly = true)
    public List<String> tagRank() {
        return tagRepository.findTop5ByOrderByCountDesc().stream()
                .map(Tag::getName).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPostDetails(Long postId) {
        Post post = postRepository.findByIdFetchJoinMemberAndImage(postId);

        if (post == null) {
            throw new NoExistsException("존재하지 않는 게시글입니다");
        }

        long likes = postLikeRepository.countByPostId(post.getId());
        long comments = commentRepository.countByPostId(post.getId());

        PostResponseDto postResponseDto = PostResponseDto.getPostResponseDto(post, likes, comments);
        // hashTag 확인
        if (!post.getPostTags().isEmpty()) {
            postResponseDto.setHashTag(post.getPostTags().stream() // hashTag 문자열 리스트로 변환
                    .map(postTag -> postTag.getTag().getName())
                    .collect(Collectors.toList()));
        }
        // 댓글 확인
        List<CommentDto> commentDtos = new ArrayList<>();
        HashMap<Long, Long> commentMap = commentLikeRepository.countCommentLikesByCommentId();

        commentRepository.findByPostIdFetchJoinMemberAndImage(post.getId()).forEach(comment -> {
            if (comment.getParent() == null) { // 대댓글 아닌 경우만
                CommentDto commentDto = CommentDto.getCommentDto(comment, commentMap.get(comment.getId()));
                // recommentList 확인
                List<RecommentDto> recommentDtos = comment.getChildren().stream()
                        .map(rc -> RecommentDto.getRecommentDto(rc, commentMap.get(rc.getId())))
                        .collect(Collectors.toList());
                commentDto.setRecommentList(recommentDtos);
                commentDtos.add(commentDto);
            }
        });
        postResponseDto.setCommentList(commentDtos);
        return postResponseDto;
    }

    /*private Long getLikesByCommentId(List<Tuple> likeTuple, Long commentId) {

        //원하는 contestId에 해당하는 결과를 찾아 카운트 값을 반환
        return likeTuple.stream()
                .filter(tuple -> tuple.get(commentLike.comment.id).equals(commentId))
                .findFirst()
                .map(tuple -> tuple.get(1, Long.class)) // 두 번째 열의 값을 Long으로 변환
                .orElse(0L);
    }

    private Long getLikesByPostId(List<Tuple> likeTuple, Long postId) {

        //원하는 contestId에 해당하는 결과를 찾아 카운트 값을 반환
        return likeTuple.stream()
                .filter(tuple -> tuple.get(postLike.post.id).equals(postId))
                .findFirst()
                .map(tuple -> tuple.get(1, Long.class)) // 두 번째 열의 값을 Long으로 변환
                .orElse(0L);
    }

    private Long getCommentsByPostId(List<Tuple> commentTuple, Long postId) {

        //원하는 contestId에 해당하는 결과를 찾아 카운트 값을 반환
        return commentTuple.stream()
                .filter(tuple -> tuple.get(comment.post.id).equals(postId))
                .findFirst()
                .map(tuple -> tuple.get(1, Long.class)) // 두 번째 열의 값을 Long으로 변환
                .orElse(0L);
    }*/
}
