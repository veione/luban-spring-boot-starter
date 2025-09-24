package com.think.luban;

import cfg.Item;
import com.think.luban.manager.TableManager;
import cfg.TbItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootTest(classes = LuBanTableProperties.class)
@EnableConfigurationProperties
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

        TableManager tableManager = applicationContext.getBean(TableManager.class);
        List<Item> all = tableManager.findAll(Item.class);
        System.out.println("all: " + all);
        Item byId = tableManager.findById(Item.class, 1);
        System.out.println("item: " + byId);
    }

}
