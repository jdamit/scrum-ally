package in.keepgrowing.scrumally.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.keepgrowing.scrumally.config.SecurityConfig;
import in.keepgrowing.scrumally.projects.model.ProjectRole;
import in.keepgrowing.scrumally.projects.viewmodel.ProjectMemberDto;
import in.keepgrowing.scrumally.security.CustomUserDetailsService;
import in.keepgrowing.scrumally.security.TokenProperties;
import in.keepgrowing.scrumally.user.model.User;
import in.keepgrowing.scrumally.user.model.UserCredentials;
import in.keepgrowing.scrumally.user.viewmodel.UserCredentialsDto;
import in.keepgrowing.scrumally.user.viewmodel.UserDto;
import in.keepgrowing.scrumally.user.viewmodel.UserEntityDtoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@Import({TokenProperties.class, BCryptPasswordEncoder.class, CustomUserDetailsService.class, SecurityConfig.class})
class UserControllerTest {

    private final String apiPath = "/api/users";

    private JacksonTester<UserDto> userJacksonTester;

    @MockBean
    private UserService userService;

    @MockBean
    private UserEntityDtoConverter converter;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void registersNewUser() throws Exception {
        var user = createTestUser();
        var dto = getUserDto();

        given(converter.toEntity(dto))
                .willReturn(user);
        given(userService.register(user))
                .willReturn(user);
        given(converter.toDto(user))
                .willReturn(dto);

        mvc.perform(post(apiPath)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJacksonTester.write(dto).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userCredentials.username", is(dto.getUserCredentials().getUsername())));
    }

    private User createTestUser() {
        UserCredentials credentials = new UserCredentials("user", "start", "");
        return new User(credentials);
    }

    private UserDto getUserDto() {
        var credentials = new UserCredentialsDto("username", "password", "role");
        return new UserDto(1L, credentials, getDtoMembers());
    }

    private Set<ProjectMemberDto> getDtoMembers() {
        return Set.of(new ProjectMemberDto(null, 1L, ProjectRole.GUEST));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void updates() throws Exception {
        var user = createTestUser();
        var dto = getUserDto();

        given(converter.toEntity(dto))
                .willReturn(user);
        given(userService.update(user, 1L))
                .willReturn(Optional.of(user));
        given(converter.toDto(user))
                .willReturn(dto);

        mvc.perform(put(apiPath + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJacksonTester.write(dto).getJson()))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    void updateGivesForbiddenStatusWhenGivenInvalidRole() throws Exception {
        mvc.perform(put(apiPath + "/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateGivesUnauthorisedStatusWhenUserNotAuthenticated() throws Exception {
        mvc.perform(put(apiPath + "/1")
                .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void statusNotFoundWhenUpdatingNonExistingUser() throws Exception {
        var dto = getUserDto();

        given(userService.findByUsername("user"))
                .willReturn(Optional.empty());

        mvc.perform(put(apiPath + "/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJacksonTester.write(dto).getJson()))
                .andExpect(status().isNotFound());
    }
}