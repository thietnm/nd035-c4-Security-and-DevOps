package com.example.demo;


import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SareetaApplication.class)
public class UserControllerTest {
    @Autowired
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setup() {
        userController = new UserController(userRepository, cartRepository, bCryptPasswordEncoder);
        User user = new User("username", "password", new Cart());
        user.setId(99L);
        given(userRepository.findByUsername(user.getUsername())).willReturn(user);
        given(userRepository.findById(99L)).willReturn(Optional.of(user));
        given(bCryptPasswordEncoder.encode("password")).willReturn("hashedValue");
    }

    @Test
    public void testCreateUserSuccess() {
        CreateUserRequest createUserRequest = new CreateUserRequest("username", "password", "password");
        ResponseEntity<User>  result = userController.createUser(createUserRequest);
        Assert.assertTrue(result.getStatusCode().value() == 200);
        Assert.assertTrue(Objects.equals(result.getBody().getUsername(), "username"));
        Assert.assertEquals(result.getBody().getPassword(), "hashedValue");
    }

    @Test
    public void testCreateUserFail() {
        CreateUserRequest createUserRequest = new CreateUserRequest("username", "xxxx", "xxxx");
        ResponseEntity<User>  result = userController.createUser(createUserRequest);
        Assert.assertTrue(result.getStatusCode().value() != 200);

        CreateUserRequest createUserRequest1 = new CreateUserRequest("username", "xxxxyyyy", "xxxxy");
        ResponseEntity<User>  result1 = userController.createUser(createUserRequest);
        Assert.assertTrue(result1.getStatusCode().value() != 200);

        ResponseEntity<User>  result3 = userController.createUser(null);
        Assert.assertTrue(result3.getStatusCode().value() != 200);
    }

    @Test
    public void testFindByIdSuccess() {
        ResponseEntity<User>  result = userController.findById(99L);
        Assert.assertTrue(result.getStatusCode().value() == 200);
        Assert.assertTrue(Objects.equals(result.getBody().getUsername(), "username"));

    }

    @Test
    public void testFindByIdFail() {
        ResponseEntity<User>  result = userController.findById(100L);
        Assert.assertTrue(result.getStatusCode().value() != 200);
    }

    @Test
    public void testFindByUsernameSuccessAndFail() {
        ResponseEntity<User>  result = userController.findByUserName("username");
        Assert.assertTrue(result.getStatusCode().value() == 200);
        Assert.assertTrue(Objects.equals(result.getBody().getUsername(), "username"));

        ResponseEntity<User>  result1 = userController.findByUserName("user");
        Assert.assertTrue(result1.getStatusCode().value() != 200);
    }
}
