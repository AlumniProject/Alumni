package Alumni.backend.module.service.community;

import Alumni.backend.TestData;
import Alumni.backend.infra.exception.FormalValidationException;
import Alumni.backend.module.domain.community.*;
import Alumni.backend.module.domain.registration.Member;
import Alumni.backend.module.domain.registration.University;
import Alumni.backend.module.dto.community.CommentRequestDto;
import Alumni.backend.module.dto.community.PostCreateRequestDto;
import Alumni.backend.module.dto.community.PostModifyRequestDto;
import Alumni.backend.module.dto.community.PostResponseDto;
import Alumni.backend.module.repository.community.*;
import Alumni.backend.module.repository.community.comment.CommentRepository;
import Alumni.backend.module.repository.community.post.PostRepository;
import Alumni.backend.module.repository.registration.MemberRepository;
import Alumni.backend.module.repository.registration.UniversityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostServiceTest {

    @Autowired
    TestData testData;
    @Autowired private PostTagRepository postTagRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired private UniversityRepository universityRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private TagRepository tagRepository;

    @Autowired private PostLikeRepository postLikeRepository;

    @Autowired private CommentRepository commentRepository;
    @Autowired private CommentLikeRepository commentLikeRepository;
    @AfterEach
    void tearDown() {
        postTagRepository.deleteAllInBatch();
        postLikeRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    //@Rollback(value = false)
    public void 전체_게시글_조회_테스트() throws Exception {
        testData.SetUp();

        Member m1 = memberRepository.findByEmail("1").get();
        List<PostResponseDto> postResponseDtos = postService.findAllPosts(m1);

        //then
//        Assertions.assertEquals(5, postResponseDtos.size());
        assertThat(postResponseDtos).hasSize(5);
    }

//    @Test
//    public void 게시글_검색_테스트() throws Exception {
//        testData.SetUp();
//
//        PostSearch postSearch = new PostSearch();
//        postSearch.setId(1L);
//        Member m1 = memberRepository.findByEmail("1").get();
//
//        PostSearchResponse<?> search = postService.search(m1, postSearch);
//
//        //then
//        Assertions.assertEquals(1, search.getCount());
//    }

    @DisplayName("게시글을 등록한다")
    @Test
    void createPost(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember = memberRepository.save(member);

        PostCreateRequestDto postCreateRequestDto = createPostCreateRequestDto(1L, "제목입니다", "내용입니다", List.of());

        //when
        postService.postCreate(saveMember, postCreateRequestDto);

        //then
        List<Post> post = postRepository.findAll();
        assertThat(post).hasSize(1)
                .extracting("title", "content")
                .contains(tuple("제목입니다", "내용입니다"));
    }

    @DisplayName("게시글 등록시 해시태그가 5개 이상이면 예외가 발생한다.")
    @Test
    void createPostManyHashTags(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member1 = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember = memberRepository.save(member1);

        PostCreateRequestDto postCreateRequestDto = createPostCreateRequestDto(3L, "제목입니다", "내용입니다", Arrays.asList("python", "Java", "AI", "Javascript", "Node.js", "Vue.js"));

        //when //then
        assertThatThrownBy(() -> postService.postCreate(saveMember, postCreateRequestDto))
                .isInstanceOf(FormalValidationException.class)
                .hasMessage("해시태그가 5개 이상입니다.");

    }

    @DisplayName("게시글을 수정한다")
    @Test
    void modifyPost1(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember = memberRepository.save(member);

        Board board = boardRepository.findById(3L).get();

        Post post = Post.createPost(saveMember, board, "title", "content");
        Post savePost = postRepository.save(post);

        PostModifyRequestDto postModifyRequestDto =  createPostModifyRequestDto("수정된 제목", "수정된 내용", List.of());

        //when
        postService.postModify(saveMember, savePost.getId(), postModifyRequestDto);

        //then
        Post modifyPost = postRepository.findById(post.getId()).get();
        assertThat(modifyPost)
                .extracting("title", "content")
                .contains("수정된 제목", "수정된 내용");
    }

    @DisplayName("기술 게시글 수정시 기존 해시태그는 삭제되고 새로운 해시태그가 등록되어야 한다.")
    @Test
    void modifyPost2(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember = memberRepository.save(member);

        Board board = boardRepository.findById(3L).get();//기술 게시판

        Post post = Post.createPost(saveMember, board, "title", "content");
        Post savePost = postRepository.save(post);

        Tag tag1 = tagRepository.findByName("python").get();
        Tag tag2 = tagRepository.findByName("Java").get();
        Tag tag3 = tagRepository.findByName("AI").get();

        PostTag postTag1 = PostTag.createPostTag(savePost, tag1);
        PostTag postTag2 = PostTag.createPostTag(savePost, tag2);
        PostTag postTag3 = PostTag.createPostTag(savePost, tag3);

        postTagRepository.saveAll(List.of(postTag1, postTag2, postTag3));

        PostModifyRequestDto postModifyRequestDto =  createPostModifyRequestDto("수정된 제목", "수정된 내용", Arrays.asList("CSS", "C"));

        //when
        postService.postModify(saveMember, savePost.getId(), postModifyRequestDto);

        //then
        List<PostTag> postTags = postTagRepository.findByPostId(savePost.getId());
        assertThat(postTags).hasSize(2);

        List<String> tagNames = postTags.stream().map(postTag -> postTag.getTag().getName()).collect(Collectors.toList());
        assertThat(tagNames).contains("CSS", "C");
    }

    @DisplayName("게시글 삭제")
    @Test
    void postDelete1(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember = memberRepository.save(member);

        Board board = boardRepository.findById(3L).get();//기술 게시판

        Post post = Post.createPost(saveMember, board, "title", "content");
        Post savePost = postRepository.save(post);

        //when
        postService.postDelete(saveMember,savePost.getId());

        //then
        Optional<Post> findPost = postRepository.findById(savePost.getId());
        assertTrue(findPost.isEmpty());
    }

    @DisplayName("게시글 삭제 시 게시글에 달린 좋아요도 삭제한다")
    @Test
    void postDelete(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member1 = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember1 = memberRepository.save(member1);
        Member member2 = Member.createMember("soeun2@yu.ac.kr", "닉네임2", "20", "정보통신공학과", university, "1");
        Member saveMember2 = memberRepository.save(member2);
        Member member3 = Member.createMember("soeun3@yu.ac.kr", "닉네임3", "20", "정보통신공학과", university, "1");
        Member saveMember3 = memberRepository.save(member3);


        Board board = boardRepository.findById(3L).get();//기술 게시판

        Post post = Post.createPost(saveMember1, board, "title", "content");
        Post savePost = postRepository.save(post);

        PostLike postLike1 = PostLike.createPostLike(savePost, saveMember2);
        PostLike postLike2 = PostLike.createPostLike(savePost, saveMember3);

        postLikeRepository.saveAll(List.of(postLike1, postLike2));

        //when
        postService.postDelete(saveMember1, savePost.getId());

        //then
        List<PostLike> postLikeList = postLikeRepository.findByPostId(savePost.getId());
        assertThat(postLikeList).isEmpty();
    }

    @DisplayName("게시글 삭제 시 달린 댓글도 삭제된다")
    @Test
    void postDelete3(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member1 = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember1 = memberRepository.save(member1);
        Member member2 = Member.createMember("soeun2@yu.ac.kr", "닉네임2", "20", "정보통신공학과", university, "1");
        Member saveMember2 = memberRepository.save(member2);


        Board board = boardRepository.findById(1L).get();//기술 게시판

        Post post = Post.createPost(saveMember1, board, "title", "content");
        Post savePost = postRepository.save(post);

        Comment comment = Comment.createComment(saveMember2, "댓글1");
        comment.setPost(savePost);

        //when
        postService.postDelete(saveMember1, savePost.getId());

        //then
        List<Comment> findComment = commentRepository.findByPostId(savePost.getId());
        assertThat(findComment).isEmpty();
    }

    @DisplayName("게시글 삭제 시 댓글에 달린 좋아요도 삭제한다")
    @Test
    void postDelete4(){
        //given
        University university = universityRepository.findById(1L).get();
        Member member1 = Member.createMember("soeun1@yu.ac.kr", "닉네임1", "20", "정보통신공학과", university, "1");
        Member saveMember1 = memberRepository.save(member1);
        Member member2 = Member.createMember("soeun2@yu.ac.kr", "닉네임2", "20", "정보통신공학과", university, "1");
        Member saveMember2 = memberRepository.save(member2);


        Board board = boardRepository.findById(1L).get();//기술 게시판

        Post post = Post.createPost(saveMember1, board, "title", "content");
        Post savePost = postRepository.save(post);

        Comment comment = Comment.createComment(saveMember2, "댓글1");
        comment.setPost(savePost);
        Comment saveComment = commentRepository.save(comment);

        CommentLike commentLike = CommentLike.createCommentLike(saveComment, saveMember1);
        commentLikeRepository.save(commentLike);

        //when
        postService.postDelete(saveMember1, savePost.getId());
        //then
        List<CommentLike> likes = commentLikeRepository.findByCommentId(saveComment.getId());
        assertThat(likes).isEmpty();
    }

    @Test
    public void 게시글_등록_테스트2() throws Exception {
        testData.SetUpOnlyMember();
        Member m1 = memberRepository.findByEmail("1").get();

        PostCreateRequestDto postCreateRequestDto = createPostCreateRequestDto(5L, "title1", "content1", List.of());

        postService.postCreate(m1, postCreateRequestDto);

        Post post = postRepository.findByTitle("title1").get();

        Set<PostTag> postTags = post.getPostTags();

        //Assertions.assertEquals(0, postTags.size());
        assertThat(postTags).hasSize(0);
    }

    @Test
    @DisplayName("게시글 수정 - 해시태그 삭제 시 post의 hashTag set에서도 삭제되는지 확인")
    public void 게시글_수정_테스트() throws Exception {
        testData.SetUpOnlyMember();
        Member m1 = memberRepository.findByEmail("1").get();

        PostCreateRequestDto postCreateRequestDto = createPostCreateRequestDto(3L, "title1", "content1", Arrays.asList("python", "Java", "AI"));

        postService.postCreate(m1, postCreateRequestDto);

        Post post = postRepository.findByTitle("title1").get();

        PostModifyRequestDto postModifyRequestDto = new PostModifyRequestDto();
        postModifyRequestDto.setTitle("t1");
        postModifyRequestDto.setContent("c1");
        postModifyRequestDto.setHashTag(List.of("Java"));
        postService.postModify(m1, post.getId(), postModifyRequestDto);

        postRepository.findByTitle("t1")
                .orElseThrow(() -> new IllegalArgumentException("e"));
        Set<PostTag> postTags = post.getPostTags();
//        Assertions.assertEquals(1, postTags.size());
        assertThat(postTags).hasSize(1);
    }

    private static PostCreateRequestDto createPostCreateRequestDto(long boardId, String title, String content, List<String> hasTags) {
        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto();
        postCreateRequestDto.setBoardId(boardId);
        postCreateRequestDto.setTitle(title);
        postCreateRequestDto.setContent(content);
        postCreateRequestDto.setHashTag(hasTags);

        return postCreateRequestDto;
    }

    private PostModifyRequestDto createPostModifyRequestDto(String title, String content, List<String> hasTags) {
        PostModifyRequestDto postModifyRequestDto = new PostModifyRequestDto();

        postModifyRequestDto.setTitle(title);
        postModifyRequestDto.setContent(content);
        postModifyRequestDto.setHashTag(hasTags);

        return postModifyRequestDto;
    }

}