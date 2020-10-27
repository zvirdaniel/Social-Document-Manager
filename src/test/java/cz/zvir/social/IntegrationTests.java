package cz.zvir.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zvir.social.models.Document;
import cz.zvir.social.models.Like;
import cz.zvir.social.models.User;
import cz.zvir.social.repositories.DocumentRepository;
import cz.zvir.social.repositories.UserRepository;
import cz.zvir.social.requests.DocumentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = { IntegrationTests.Initializer.class })
@ActiveProfiles("test")
public class IntegrationTests {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Container
	private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
			.withDatabaseName("social-integration-tests")
			.withUsername("sa")
			.withPassword("sa");

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of(
					"spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
					"spring.datasource.username=" + postgreSQLContainer.getUsername(),
					"spring.datasource.password=" + postgreSQLContainer.getPassword()
			).applyTo(configurableApplicationContext.getEnvironment());
		}
	}

	@Test
	public void createUserTest() throws Exception {
		final String name = "Karel";
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users").param("name", name))
		            .andDo(print())
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.name").value(name));
	}

	@Test
	public void deleteUserTest() throws Exception {
		final User josef = userRepository.save(new User("Josef"));
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/users/id/" + josef.getId()))
		            .andDo(print())
		            .andExpect(status().isOk());
		assertFalse(userRepository.existsById(josef.getId()));
	}

	@Test
	public void renameUserTest() throws Exception {
		final User josef = userRepository.save(new User("Josef"));
		final String newName = "Jan";
		this.mockMvc.perform(MockMvcRequestBuilders.patch("/users/id/" + josef.getId()).param("name", newName))
		            .andDo(print())
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.name").value(newName));
	}

	@Test
	public void createDocumentTest() throws Exception {
		final String docId = "ImportantArticle";
		final String content = "some text content";
		final User user = this.getNewUser();
		this.mockMvc.perform(MockMvcRequestBuilders.post("/documents/id/" + docId)
		                                           .contentType(MediaType.APPLICATION_JSON)
		                                           .content(asJsonString(new DocumentRequest(content, user.getId()))))
		            .andDo(print())
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.id").value(docId))
		            .andExpect(jsonPath("$.content").value(content))
		            .andExpect(jsonPath("$.user.id").value(user.getId()));
	}

	@Test
	public void likeDocumentTest() throws Exception {
		final Document document = this.getNewDocument("docID", "content", null);
		final User likerOne = this.getNewUser();
		final User likerTwo = this.getNewUser();
		this.mockMvc.perform(MockMvcRequestBuilders.patch("/documents/like/" + document.getId())
		                                           .param("userId", String.valueOf(likerOne.getId())))
		            .andDo(print())
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.id").value(document.getId()))
		            .andExpect(jsonPath("$.content").value(document.getContent()))
		            .andExpect(jsonPath("$.likes").isArray())
		            .andExpect(jsonPath("$.likes[0]").isNotEmpty())
		            .andExpect(jsonPath("$.likes[0].user.id").value(likerOne.getId()));

		this.mockMvc.perform(MockMvcRequestBuilders.patch("/documents/like/" + document.getId())
		                                           .param("userId", String.valueOf(likerTwo.getId())))
		            .andDo(print())
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.id").value(document.getId()))
		            .andExpect(jsonPath("$.content").value(document.getContent()))
		            .andExpect(jsonPath("$.likes").isArray())
		            .andExpect(jsonPath("$.likes[1]").isNotEmpty())
		            .andExpect(jsonPath("$.likes[1].user.id").value(likerTwo.getId()));
	}

	@Test
	public void unlikeDocumentTest() throws Exception {
		final User likerOne = this.getNewUser();
		final Document document = this.likeDocument(this.getNewDocument("docId", "content", this.getNewUser()), likerOne);
		this.mockMvc.perform(MockMvcRequestBuilders.patch("/documents/unlike/" + document.getId())
		                                           .param("userId", String.valueOf(likerOne.getId())))
		            .andDo(print())
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.id").value(document.getId()))
		            .andExpect(jsonPath("$.content").value(document.getContent()))
		            .andExpect(jsonPath("$.likes").isArray())
		            .andExpect(jsonPath("$.likes.length()").value(0));
	}

	@Test
	public void documentLikeCountTest() throws Exception {
		final Document document = this.likeDocument(this.getNewDocument("docId", "content", this.getNewUser()),
		                                            this.getNewUser(), this.getNewUser(), this.getNewUser());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/documents/id/" + document.getId()))
		            .andDo(print())
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.id").value(document.getId()))
		            .andExpect(jsonPath("$.content").value(document.getContent()))
		            .andExpect(jsonPath("$.likeCount").value(3));
	}

	@Test
	public void getLikedDocumentsTest() throws Exception {
		// This user will like the documents
		final User liker = this.getNewUser();

		// Documents liked by the user, each document is created by new user (not by the liking user)
		final Document[] likedDocuments = {
				this.likeDocument(this.getNewDocument("doc" + System.currentTimeMillis(), "content", this.getNewUser()), liker),
				this.likeDocument(this.getNewDocument("doc" + System.currentTimeMillis(), "content", this.getNewUser()), liker),
				this.likeDocument(this.getNewDocument("doc" + System.currentTimeMillis(), "content", this.getNewUser()), liker),
				this.likeDocument(this.getNewDocument("doc" + System.currentTimeMillis(), "content", this.getNewUser()), liker),
				this.likeDocument(this.getNewDocument("doc" + System.currentTimeMillis(), "content", this.getNewUser()), liker),
		};

		// Unrelated documents, each documents is created by new user and liked by another new user
		final Document[] unlikedDocuments = {
				this.likeDocument(this.getNewDocument("doc" + System.currentTimeMillis(), "content", this.getNewUser()), this.getNewUser()),
				this.likeDocument(this.getNewDocument("doc" + System.currentTimeMillis(), "content", this.getNewUser()), this.getNewUser()),
				this.likeDocument(this.getNewDocument("doc" + System.currentTimeMillis(), "content", this.getNewUser()), this.getNewUser()),
				this.likeDocument(this.getNewDocument("doc" + System.currentTimeMillis(), "content", this.getNewUser()), this.getNewUser()),
				this.likeDocument(this.getNewDocument("doc" + System.currentTimeMillis(), "content", this.getNewUser()), this.getNewUser())
		};

		final var actions = this.mockMvc.perform(MockMvcRequestBuilders.get("/documents/liked-documents").param("userId", String.valueOf(liker.getId())))
		                                .andDo(print())
		                                .andExpect(status().isOk())
		                                .andExpect(jsonPath("$").isArray())
		                                .andExpect(jsonPath("$.length()").value(5));

		// Check that liked documents are returned
		for (final var likedDocument : likedDocuments) {
			actions.andExpect(jsonPath("$[?(@.id == '" + likedDocument.getId() + "')]").exists());
		}

		// Check that unrelated documents are not returned
		for (final var unlikedDocument : unlikedDocuments) {
			actions.andExpect(jsonPath("$[?(@.id == '" + unlikedDocument.getId() + "')]").doesNotExist());
		}
	}

	private Document likeDocument(final Document document, final User... users) {
		if (users != null && users.length > 0) {
			for (final var user : users) {
				document.getLikes().add(new Like(document, user));
			}
		}
		return documentRepository.saveAndFlush(document);
	}

	private Document getNewDocument(final String id, final String content, final User user) {
		return documentRepository.saveAndFlush(new Document(id, content, Optional.ofNullable(user).orElseGet(this::getNewUser)));
	}

	private User getNewUser() {
		return userRepository.saveAndFlush(new User("User " + System.currentTimeMillis()));
	}

	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}