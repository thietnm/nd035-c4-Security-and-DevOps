package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SareetaApplication.class)
public class OrderControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Autowired
    private OrderController orderController;

    @Before
    public void setup() {
        orderController = new OrderController(userRepository, orderRepository);
        User user = new User("username", "password", new Cart());
        user.setId(1L);
        user.getCart().setId(1L);
        user.getCart().setUser(user);
        user.getCart().setTotal(BigDecimal.TEN);
        user.getCart().setItems(Collections.singletonList(new Item("item1", BigDecimal.TEN, "des")));
        given(userRepository.findByUsername(user.getUsername())).willReturn(user);
        UserOrder userOrder = new UserOrder();
        given(orderRepository.findByUser(user.getId())).willReturn(Collections.singletonList(userOrder));
    }

    @Test
    public void testSubmitSuccess() {
        UserOrder userOrder = orderController.submit("username").getBody();
        assert userOrder != null;
        Assert.assertTrue(Objects.equals(userOrder.getUser().getUsername(), "username"));
    }

    @Test
    public void testSubmitError() {
        UserOrder userOrder = orderController.submit("usernameNotFound").getBody();
        Assert.assertTrue(userOrder == null);
    }

    @Test
    public void testGetOrdersForUserSuccess() {
        UserOrder userOrder = orderController.submit("username").getBody();
        given(orderRepository.findByUser(1L)).willReturn(Collections.singletonList(userOrder));

        UserOrder userOrder1 =  orderController.getOrdersForUser("username").getBody().get(0);
        Assert.assertTrue(Objects.equals(userOrder1.getUser().getUsername(), "username"));
    }

    @Test
    public void testGetOrdersForUserFail() {
        UserOrder userOrder = orderController.submit("username").getBody();
        given(orderRepository.findByUser(1L)).willReturn(Collections.singletonList(userOrder));

        List<UserOrder> result =  orderController.getOrdersForUser("usernameNotFound").getBody();
        Assert.assertTrue(result == null);
    }

}
