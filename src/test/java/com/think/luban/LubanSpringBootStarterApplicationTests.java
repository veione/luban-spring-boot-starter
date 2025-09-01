package com.think.luban;

import cfg.Item;
import cfg.Tables;
import cfg.TbItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootTest
public class LubanSpringBootStarterApplicationTests {
    private LuBanTableProperties properties;
    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setProperties(LuBanTableProperties properties) {
        this.properties = properties;
    }

    @Test
    public void test() {
        TbItem repository = applicationContext.getBean(TbItem.class);
        List<Item> all1 = repository.findAll();
        System.out.println("all1 = " + all1);
        Item item = repository.findById(3008);
        System.out.println("item: " + item);

        System.out.println("count: " + repository.count(p -> true));

        Tables tables = applicationContext.getBean(Tables.class);
        List<Item> all = tables.findAll(Item.class);
        System.out.println("all: " + all);
        Item byId = tables.findById(Item.class, 1);
        System.out.println("item: " + byId);
    }

}
