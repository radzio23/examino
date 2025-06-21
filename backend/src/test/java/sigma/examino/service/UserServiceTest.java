package sigma.examino.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sigma.examino.model.Role;
import sigma.examino.model.User;
import sigma.examino.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void registerUser_shouldCreateNewUserWithHashedPassword() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        User savedUser = new User();
        savedUser.setUsername("testuser");
        savedUser.setPassword("hashedpassword");
        savedUser.setRole(Role.STUDENT);
        when(userRepository.save(Mockito.any())).thenReturn(savedUser);

        User result = userService.registerUser("testuser", "plaintext", Role.STUDENT);

        assertEquals("testuser", result.getUsername());
        assertEquals(Role.STUDENT, result.getRole());
        assertNotEquals("plaintext", result.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowExceptionWhenUserExists() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () ->
                userService.registerUser("admin", "pass", Role.ADMIN));
    }

    @Test
    void loginUserReturnUser_shouldReturnNullWhenUserNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        User result = userService.loginUserReturnUser("ghost", "pass");
        assertNull(result);
    }
}
