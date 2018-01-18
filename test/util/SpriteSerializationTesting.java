package util;

import engine.Bank;
import engine.game_elements.GameElement;
import engine.authoring_engine.FactoryTesting;
import util.io.SerializationUtils;

import java.util.*;

public class SpriteSerializationTesting {

    public static void main(String[] args) {
        FactoryTesting factoryTesting = new FactoryTesting();
        GameElement gameElement = factoryTesting.generateSingleTestSprite();
        gameElement.setX(50000000);
        SerializationUtils serializationUtils = new SerializationUtils();
        String ss = serializationUtils.serializeLevelData("bleh", new HashMap<>(), new Bank(),
                new HashMap<>(), Arrays.asList(gameElement), new HashSet<>(), 1,1, 1, 1);
        Map<Integer, String> map = new HashMap<>();
        map.put(1, ss);
        String sssss = serializationUtils.serializeLevelsData(map);
        System.out.println(sssss);
        List<GameElement> ls = serializationUtils.deserializeGameSprites(sssss, 1);
        ls.get(0).setX(10000000);
        /*GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setExclusionStrategies(new AnnotationExclusionStrategy());
        gsonBuilder.serializeSpecialFloatingPointValues();
        gsonBuilder.setLenient();
        String ss = gsonBuilder.create().toJson(gameElement);
        System.out.println(ss);
        GameElement ds = gsonBuilder.create().fromJson(ss, GameElement.class);*/
    }
}
