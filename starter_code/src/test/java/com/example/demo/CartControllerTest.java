package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SareetaApplication.class)
public class CartControllerTest {

    @Autowired
    private CartController cartController;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Before
    public void setup() {
        cartController = new CartController(userRepository, cartRepository, itemRepository);
        User user = new User("username", "password", new Cart());
        given(userRepository.findByUsername(user.getUsername())).willReturn(user);

        Item item = new Item("item", BigDecimal.ONE, "1");
        item.setId(1L);
        given(itemRepository.findById(item.getId())).willReturn(Optional.of(item));
    }

    @Test
    public void testAddToCartSuccess() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest("username", 1, 1);
        Cart result = cartController.addTocart(modifyCartRequest).getBody();

        Assert.assertTrue(result != null);
        Assert.assertTrue(result.getItems().size() == 1);
    }

    @Test
    public void testAddToCartError() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest("usernameError", 1, 1);

        Cart result = cartController.addTocart(modifyCartRequest).getBody();
        Assert.assertTrue(result == null);

        modifyCartRequest.setItemId(2);
        result = cartController.addTocart(modifyCartRequest).getBody();
        Assert.assertTrue(result == null);
    }

    @Test
    public void testRemoveFromCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest("username", 1, 1);
        cartController.addTocart(modifyCartRequest);

        Cart removeResult = cartController.removeFromcart(modifyCartRequest).getBody();
        Assert.assertTrue(removeResult != null);
        Assert.assertTrue(removeResult.getItems().isEmpty());
    }
}
