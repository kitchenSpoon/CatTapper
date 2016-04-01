package com.jacklian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jacklian on 15/02/2016.
 */
public class HeroManager extends Actor implements  TableClickHandlerInterface {
    Array<Hero> heroArray;
    MonsterManager monsterManager;
    ProjectileManager projectileManager;
    MoneyManager moneyManager;
    Stage stage;
    Skin uiSkin;
    Table scrollTable;
    ScrollPane warriorsScrollPane;
    MyGdxGame game;

    Array<HeroListing> herosListingArray;

    Integer currentHeroCount;
    HerosConfig herosConfig;


    public HeroManager() {
        super();
        this.game = game;
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        heroArray = new Array<Hero>();
        herosListingArray = new Array<HeroListing>();

        scrollTable = new Table();
        scrollTable.row();
        Actor actor = actorForRow(0);
        scrollTable.add(actor).width(560).height(80).pad(20);

        herosListingArray.add((HeroListing) actor);

        warriorsScrollPane = new ScrollPane(scrollTable, uiSkin);
        warriorsScrollPane.setScrollingDisabled(true, false);

        currentHeroCount = 0;

        readHeroFile();
    }

    public HeroManager(MonsterManager monsterManager, Stage stage, ProjectileManager projectileManager, MoneyManager moneyManager, MyGdxGame game){
        this();
        this.projectileManager = projectileManager;
        this.moneyManager = moneyManager;
        this.monsterManager = monsterManager;
        this.stage = stage;
        this.game = game;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for(Hero hero : heroArray) {
            hero.act(delta);
//            monsterManager.currentMonster.takeDamage(hero.getDamage());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for(Hero hero : heroArray) {
            hero.draw(batch, parentAlpha);
        }
    }

    public void addHero(int level)
    {
        int maxX = 160, minX = 20, maxY = 800, minY = 600;
        Random random = new Random();
        int x = random.nextInt(maxX - minX + 1) + minX;
        int y = random.nextInt(maxY - minY + 1) + minY;
        heroArray.add(new Hero(level, x, y, this.stage, projectileManager, game));
    }

    @Override
    public void didClickItemOnRow(Integer row) {

        if(row == herosListingArray.size - 1) {
            Integer price = (row + 1) * 10;
            if(moneyManager.getTotal() >= price) {
                moneyManager.setTotal(moneyManager.getTotal() - price);

                HeroListing heroListing = herosListingArray.get(row);
                heroListing.increaseLevel();

                addHero(row + 1);

                Actor actor = actorForRow(row + 1);

                herosListingArray.add((HeroListing) actor);

                scrollTable.add(actor).width(560).height(80).pad(20);
                scrollTable.row();
//        warriorsScrollPane.;
                warriorsScrollPane.setWidget(scrollTable);
            }
        } else {
            HeroListing heroListing = herosListingArray.get(row);
            Double price = heroListing.getPrice();
            if(moneyManager.getTotal() >= price) {
                moneyManager.setTotal(moneyManager.getTotal() - price.intValue());

                heroListing.increaseLevel();

                Hero hero = heroArray.get(row);
                hero.increaseLevel();
            }
        }


    }

    @Override
    public Actor actorForRow(final Integer row) {
//        final TextButton button = new TextButton("Hero " + row, uiSkin);
//        button.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                didClickItemOnRow(row);
//                button.setText("Hero " + row.toString());
//            }
//        });
//        return button;
        HeroListing heroListing = new HeroListing(row, new Runnable() {
            @Override
            public void run() {
                didClickItemOnRow(row);
            }
        });
        return heroListing;
    }

    @Override
    public Integer numberOfRows() {
        return currentHeroCount + 1;
    }

    public ScrollPane scrollPaneForHeroes()
    {
        return warriorsScrollPane;
    }



    public void readHeroFile()
    {
//        JsonValue json = new JsonReader().parse(Gdx.files.internal("heroData.json"));
//        Gdx.app.log("jsonreading","damage: " + json.get("hero").get(1).getInt("damage"));

        Json json = new Json();
        herosConfig = json.fromJson(HerosConfig.class, Gdx.files.internal("heroData.json"));
        Gdx.app.log("jsonreading", "damage: " + herosConfig.heros.get(1).damage);
    }

    public static class HerosConfig {
        private ArrayList<HeroConfig> heros;
    }

    public static class HeroConfig {
        private Integer damage;
    }
}
