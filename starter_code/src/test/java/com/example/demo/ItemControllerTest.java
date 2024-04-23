package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SareetaApplication.class)
public class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @Autowired
    private ItemController itemController;

    @Before
    public void setup() {
        itemController = new ItemController(itemRepository);

        Item item1 = new Item("item1", BigDecimal.ONE, "1");
        item1.setId(1L);
        Item item2 = new Item("item2", BigDecimal.TEN, "2");
        item2.setId(2L);
        Item item3 = new Item("item3", BigDecimal.TEN, "3");
        item3.setId(3L);
        ArrayList<Item> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        list.add(item3);
        itemRepository.saveAll(list);
        given(itemRepository.findAll()).willReturn(list);
        given(itemRepository.findById(item2.getId())).willReturn(Optional.of(item2));
        given(itemRepository.findByName(item2.getName())).willReturn(Collections.singletonList(item2));

    }

    @Test
    public void testGetItems() {
        List<Item> result = itemController.getItems().getBody();
        Assert.assertTrue(result.size() == 3);
    }

    @Test
    public void testGetItemById() {
        Item result = itemController.getItemById(2L).getBody();
        Assert.assertTrue(result.getName().equals("item2"));

        Item result2 = itemController.getItemById(4L).getBody();
        Assert.assertTrue(result2 == null);
    }

    @Test
    public void testGetItemsByName() {
        Item result = itemController.getItemsByName("item2").getBody().get(0);
        Assert.assertTrue(result.getName().equals("item2"));

        List<Item> result2 = itemController.getItemsByName("item4").getBody();
        Assert.assertTrue(result2 == null);
    }


}
