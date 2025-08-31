package cfg;

import com.think.luban.loader.JsonTableLoader;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        Tables tables = new Tables(new JsonTableLoader());

        tables.register(Rewards.class, null);
        List<Rewards> all = tables.findAll(Rewards.class);
        Rewards byId = tables.findById(Rewards.class, 1);
        CfgTbRewardsRepository repository = tables.getRepository(Rewards.class);

        List<Rewards> indexes = repository.findIndexes("type", 1);
        indexes = repository.findIndexes(Rewards::getTypeId, 1);
    }
}
