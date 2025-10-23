## Luban Table integration with Spring Boot

### Ⅰ.简介

本组件是基于 *Luban* 配置表工具进行封装的，*Luban* 配置表工具是一个用于生成配置表工具，可以生成 *CSV*、*XLSX*、*JSON* 格式的配置表，并且支持配置表热更新。

### Ⅱ.背景和适用项目

- 适用于Web项目、游戏项目等;
- 简单易用，简单配置，优雅的代码;
- 配置表替换直接热更无需重启服务;

### Ⅲ.Maven依赖

- JDK版本要求JDK 21
- 添加依赖
```xml
<dependency>
    <groupId>com.think</groupId>
    <artifactId>luban-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Ⅳ.使用方法

- 配置文件
```yaml
luban:
  # 指定配置表的所在目录
  path: tables
  # 指定配置表的加载类型，目前Luban for Java提供了两种加载方式：byte和json
  type: byte
  # 配置表扫描的包
  scan-packages:
    - cfg
```

#### 使用方式

- 方式一：
```java
// 直接获取配置项
CfgItem config = TableManager.getConfig(CfgItem.class, 1);

// 获取属性值
String name = TableManager.getValue(CfgItem.class, 1, CfgItem::getName);

// 获取所有配置项
List<CfgItem> allConfigs = TableManager.getList(CfgItem.class);

// 检查存在性
boolean exists = TableManager.contains(CfgItem.class, 1);

// 获取配置项数量
int count = TableManager.size(CfgItem.class);

```
- 方式二(通过Spring注入的方式)：
```java
public class UserService{
    private final CfgItemTable cfgItemTable;
    
    public UserService(CfgItemTable cfgItemTable) {
        this.cfgItemTable = cfgItemTable;
    }
    
    public void useCase() {
        // 通过主键ID查找对应的配置项
        var cfgItem = cfgItemTable.findById(1001);

        // 通过条件查找对应的配置项
        Optional<CfgItem> cfgItemOption = cfgItemTable.findById(p -> p.type == 100);

        // 通过查找符合条件的配置项列表
        List<CfgItem> cfgItemList = cfgItemTable.findAll(p -> p.type == 100);

        // 获取所有配置项
        List<CfgItem> cfgItemList = cfgItemTable.findAll();

        // 通过条件查找匹配的配置项数量
        long count = cfgItemTable.count(p -> p.type == 100);

        // 检查是否存在
        boolean exists = cfgItemTable.exists(1001);

        // 检查是否存在
        boolean exists = cfgItemTable.exists(p -> p.type == 100);

        // 获取配置项数量
        int len = cfgItemTable.size();
    }
}

```

- 热更表事件监听

```java
@Slf4j
public class TableEventListener {
    
    @EventListener
    public void onTableReload(TableReloadEvent event) {
        log.info("Received table reload event: {} {}", event.getFileName(), event.getClazz());
        // do something business
    }
}
```

### Ⅴ.注意事项
- 配置表工具请使用项目中提供的 *Luban* 配置表工具进行生成，里面包含了项目修改的模板部分，否则使用官方自带的模板生成的配置表将无法使用以及被框架所识别和加载。

### Ⅵ.待做事项

- [ ] 添加多索引功能实现;
