package com.github.radiantai.blogpostfetcher;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.radiantai.blogpostfetcher.datasources.cache.Cache;
import com.github.radiantai.blogpostfetcher.datasources.postsdatasource.PostsDatasource;
import com.github.radiantai.blogpostfetcher.entities.Post;
import com.github.radiantai.blogpostfetcher.requests.PostsRequest;
import com.github.radiantai.blogpostfetcher.responses.PostsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

@WebMvcTest
class BlogPostFetcherApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostsDatasource postsDatasource;

	@MockBean
	private RestTemplate restTemplate;

	@MockBean
	private Cache<List<Post>> postsCache;

	@Test
	public void testPing() throws Exception {
		mockMvc.perform(get("/api/ping")).andExpect(status().isOk())
				.andExpect(content().json("{'success':true}"));
	}

	@Test
	public void testEmptyTags1() throws Exception {
		mockMvc.perform(get("/api/posts")).andExpect(status().isBadRequest())
				.andExpect(content().json("{'error':'Tags parameter is required'}"));
	}

	@Test
	public void testInvalidTags1() throws Exception {
		mockMvc.perform(get("/api/posts?tags=,,,")).andExpect(status().isBadRequest())
				.andExpect(content().json("{'error':'Invalid list of tags'}"));
	}

	@Test
	public void testInvalidTags2() throws Exception {
		mockMvc.perform(get("/api/posts?tags=history,")).andExpect(status().isBadRequest())
				.andExpect(content().json("{'error':'Invalid list of tags'}"));
	}

	@Test
	public void testInvalidTags3() throws Exception {
		mockMvc.perform(get("/api/posts?tags=,history")).andExpect(status().isBadRequest())
				.andExpect(content().json("{'error':'Invalid list of tags'}"));
	}

	@Test
	public void testInvalidTags4() throws Exception {
		mockMvc.perform(get("/api/posts?tags= ")).andExpect(status().isBadRequest())
				.andExpect(content().json("{'error':'Invalid list of tags'}"));
	}

	@Test
	public void testInvalidTags5() throws Exception {
		mockMvc.perform(get("/api/posts?tags=")).andExpect(status().isBadRequest())
				.andExpect(content().json("{'error':'Invalid list of tags'}"));
	}

	@Test
	public void testValidTags() throws Exception {
		when(postsDatasource.getPosts(any())).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/api/posts?tags=history")).andExpect(status().isOk())
				.andExpect(content().json("{'posts':[]}"));
		mockMvc.perform(get("/api/posts?tags=history,politics")).andExpect(status().isOk())
				.andExpect(content().json("{'posts':[]}"));
	}

	@Test
	public void testInvalidDirection() throws Exception {
		mockMvc.perform(get("/api/posts?tags=history,politics&direction=ddfdf")).andExpect(status().isBadRequest())
				.andExpect(content().json("{'error':'direction parameter is invalid'}"));
	}

	@Test
	public void testValidDirection() throws Exception {
		when(postsDatasource.getPosts(any())).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/api/posts?tags=history,politics&direction=asc")).andExpect(status().isOk())
				.andExpect(content().json("{'posts':[]}"));
		mockMvc.perform(get("/api/posts?tags=history,politics&direction=desc")).andExpect(status().isOk())
				.andExpect(content().json("{'posts':[]}"));
	}

	@Test
	public void testInvalidSortBy() throws Exception {
		mockMvc.perform(get("/api/posts?tags=history,politics&direction=desc&sortBy=like"))
				.andExpect(status().isBadRequest())
				.andExpect(content().json("{'error':'sortBy parameter is invalid'}"));
	}

	@Test
	public void testValidSortBy() throws Exception {
		when(postsDatasource.getPosts(any())).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/api/posts?tags=history,politics&direction=asc&sortBy=likes")).andExpect(status().isOk())
				.andExpect(content().json("{'posts':[]}"));
		mockMvc.perform(get("/api/posts?tags=history,politics&direction=asc&sortBy=id")).andExpect(status().isOk())
				.andExpect(content().json("{'posts':[]}"));
	}

	@Test
	public void testGetPosts() throws Exception {
		when(postsDatasource.getPosts(any())).thenReturn(Arrays.asList(
				new Post(1, "testAuthor1", 2, 3, 4.0, 5, Arrays.asList("history", "politics")),
				new Post(2, "testAuthor2", 3, 4, 5.0, 6, Arrays.asList("economy", "science"))));
		mockMvc.perform(get("/api/posts?tags=test")).andExpect(status().isOk())
				.andExpect(content().json(
						"{'posts':[" +
								"{'id':1,'author':'testAuthor1','authorId':2,'likes':3,'popularity':4.0,'reads':5,tags:['history','politics']}," +
								"{'id':2,'author':'testAuthor2','authorId':3,'likes':4,'popularity':5.0,'reads':6,tags:['economy','science']}" +
								"]}"));
	}

	@Test
	public void testGetPostsSortById() throws Exception {
		when(restTemplate.getForObject(any(String.class), any(), any(String.class))).thenReturn(
				new PostsResponse(Arrays.asList(
						new Post(3, "testAuthor", 1, 1, 1.0, 1, List.of("history")),
						new Post(2, "testAuthor", 1, 1, 1.0, 1, List.of("history")),
						new Post(1, "testAuthor", 1, 1, 1.0, 1, List.of("history")),
						new Post(5, "testAuthor", 1, 1, 1.0, 1, List.of("history")),
						new Post(4, "testAuthor", 1, 1, 1.0, 1, List.of("history")))));
		when(postsCache.getFromCache(any(String.class), any(Callable.class))).thenAnswer(
				(Answer<List<Post>>) invocationOnMock -> ((Callable<List<Post>>) invocationOnMock.getArgument(1)).call());
		PostsDatasource postsDatasource = new PostsDatasource(restTemplate, postsCache);
		List<Post> posts = postsDatasource.getPosts(new PostsRequest("history", "id", "asc"));

		Assertions.assertTrue(posts.get(0).getId()==1 && posts.get(1).getId()==2 && posts.get(2).getId()==3
				&& posts.get(3).getId()==4 && posts.get(4).getId()==5);
	}

	@Test
	public void testGetPostsSortByLikesDesc() throws Exception {
		when(restTemplate.getForObject(any(String.class), any(), any(String.class))).thenReturn(
				new PostsResponse(Arrays.asList(
						new Post(3, "testAuthor", 1, 3, 1.0, 1, List.of("history")),
						new Post(2, "testAuthor", 1, 2, 1.0, 1, List.of("history")),
						new Post(1, "testAuthor", 1, 1, 1.0, 1, List.of("history")),
						new Post(5, "testAuthor", 1, 5, 1.0, 1, List.of("history")),
						new Post(4, "testAuthor", 1, 4, 1.0, 1, List.of("history")))));
		when(postsCache.getFromCache(any(String.class), any(Callable.class))).thenAnswer(
				(Answer<List<Post>>) invocationOnMock -> ((Callable<List<Post>>) invocationOnMock.getArgument(1)).call());
		PostsDatasource postsDatasource = new PostsDatasource(restTemplate, postsCache);
		List<Post> posts = postsDatasource.getPosts(new PostsRequest("history", "likes", "desc"));

		Assertions.assertTrue(posts.get(0).getLikes()==5 && posts.get(1).getLikes()==4 && posts.get(2).getLikes()==3
				&& posts.get(3).getLikes()==2 && posts.get(4).getLikes()==1);
	}

	@Test
	public void testGetDeduplication() throws Exception {
		when(restTemplate.getForObject(any(String.class), any(), eq("history"))).thenReturn(
				new PostsResponse(Arrays.asList(
						new Post(3, "testAuthor", 1, 3, 1.0, 1, List.of("history")),
						new Post(2, "testAuthor", 1, 2, 1.0, 1, List.of("history")),
						new Post(1, "testAuthor", 1, 1, 1.0, 1, List.of("history")),
						new Post(5, "testAuthor", 1, 5, 1.0, 1, List.of("history,politics")),
						new Post(4, "testAuthor", 1, 4, 1.0, 1, List.of("history,politics")))));
		when(restTemplate.getForObject(any(String.class), any(), eq("politics"))).thenReturn(
				new PostsResponse(Arrays.asList(
						new Post(6, "testAuthor", 1, 3, 1.0, 1, List.of("politics")),
						new Post(7, "testAuthor", 1, 2, 1.0, 1, List.of("politics")),
						new Post(8, "testAuthor", 1, 1, 1.0, 1, List.of("politics")),
						new Post(5, "testAuthor", 1, 5, 1.0, 1, List.of("history,politics")),
						new Post(4, "testAuthor", 1, 4, 1.0, 1, List.of("history,politics")))));
		when(postsCache.getFromCache(any(String.class), any(Callable.class))).thenAnswer(
				(Answer<List<Post>>) invocationOnMock -> ((Callable<List<Post>>) invocationOnMock.getArgument(1)).call());
		PostsDatasource postsDatasource = new PostsDatasource(restTemplate, postsCache);
		List<Post> posts = postsDatasource.getPosts(new PostsRequest("history,politics", "id", "desc"));

		Assertions.assertEquals(8, posts.size());
	}

	@Test
	public void testGetPostsSortByLikesRealCache() throws Exception {
		when(restTemplate.getForObject(any(String.class), any(), any(String.class))).thenReturn(
				new PostsResponse(Arrays.asList(
						new Post(3, "testAuthor", 1, 3, 1.0, 1, List.of("history")),
						new Post(2, "testAuthor", 1, 2, 1.0, 1, List.of("history")),
						new Post(1, "testAuthor", 1, 1, 1.0, 1, List.of("history")),
						new Post(5, "testAuthor", 1, 5, 1.0, 1, List.of("history")),
						new Post(4, "testAuthor", 1, 4, 1.0, 1, List.of("history")))));
		PostsDatasource postsDatasource = new PostsDatasource(restTemplate, new Cache<>(2000));
		List<Post> posts = postsDatasource.getPosts(new PostsRequest("history", "likes", "desc"));

		Assertions.assertTrue(posts.get(0).getLikes()==5 && posts.get(1).getLikes()==4 && posts.get(2).getLikes()==3
				&& posts.get(3).getLikes()==2 && posts.get(4).getLikes()==1);
	}

	@Test
	public void testCacheHit() throws Exception {
		when(restTemplate.getForObject(any(String.class), any(), any(String.class))).thenReturn(
				new PostsResponse());
		PostsDatasource postsDatasource = new PostsDatasource(restTemplate, new Cache<>(2000));
		postsDatasource.getPosts(new PostsRequest("history", "likes", "desc"));
		postsDatasource.getPosts(new PostsRequest("history", "likes", "desc"));
		verify(restTemplate, times(1)).getForObject(any(String.class), any(), any(String.class));
	}
}
