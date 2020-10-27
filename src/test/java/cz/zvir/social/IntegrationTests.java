package cz.zvir.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zvir.social.models.User;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = { IntegrationTests.Initializer.class })
@ActiveProfiles("test")
public class IntegrationTests {
	@Autowired
	private UserRepository userRepository;

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
		            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name));
	}

	@Test
	public void deleteUserTest() throws Exception {
		final User josef = userRepository.save(new User("Josef"));
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/users/id/" + josef.getId()))
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
		            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newName));
	}

	@Test
	public void createDocumentTest() throws Exception {
		final String docId = "ImportantArticle";
		final String content = "some text content";
		final User user = this.getFreshUser();
		this.mockMvc.perform(MockMvcRequestBuilders.post("/documents/id/" + docId)
		                                           .contentType(MediaType.APPLICATION_JSON)
		                                           .content(asJsonString(new DocumentRequest(content, user.getId()))))
		            .andDo(print())
		            .andExpect(status().isOk())
		            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(docId))
		            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(content))
		            .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(user.getId()));
	}

	private User getFreshUser() {
		return userRepository.saveAndFlush(new User("Karel " + System.currentTimeMillis()));
	}

	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}