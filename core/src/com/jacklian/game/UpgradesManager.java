package com.jacklian.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

/**
 * Created by jacklian on 13/03/2016.
 */
public class UpgradesManager implements TableClickHandlerInterface {

    Integer upgradesNumCount, maxUpgradesNumCount;
    Array<Integer> upgrades;
    Array<Integer> upgradesLevel;
    Array<Integer> upgradesCost;
    Array<String> upgradesTitle;

    Skin uiSkin;
    Table scrollTable;
    MainCharacter mainCharacter;
    ScrollPane upgradesScrollPane;
    Array<UpgradeListing> upgradesListingArray;

    MyGdxGame game;

    public ScrollPane scrollPaneForUpgrades()
    {
        return upgradesScrollPane;
    }

    public UpgradesManager(MainCharacter mainCharacter, MyGdxGame game)
    {
        this.mainCharacter = mainCharacter;
        this.game = game;

        upgradesNumCount = 0;
        maxUpgradesNumCount = 3;

        upgrades = new Array<Integer>();

        upgradesLevel = new Array<Integer>();
        upgradesLevel.add(0);
        upgradesLevel.add(0);
        upgradesLevel.add(0);

        upgradesCost = new Array<Integer>();
        upgradesCost.add(0);
        upgradesCost.add(0);
        upgradesLevel.add(0);

        upgradesTitle = new Array<String>();
        upgradesTitle.add("Add Damage");
        upgradesTitle.add("Special Attack");
        upgradesTitle.add("Sheild");

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));


        //Scrollpane
        upgradesListingArray = new Array<UpgradeListing>();

        scrollTable = new Table();

        scrollTable.row();
        Actor actor = actorForRow(0);
        scrollTable.add(actor).width(560).height(80).pad(20);

        upgradesListingArray.add((UpgradeListing) actor);

        scrollTable.row();
        Actor actor2 = actorForRow(1);
        scrollTable.add(actor2).width(560).height(80).pad(20);

        upgradesListingArray.add((UpgradeListing) actor2);

        upgradesScrollPane = new ScrollPane(scrollTable, uiSkin);
        upgradesScrollPane.setScrollingDisabled(true, false);

    }

    @Override
    public void didClickItemOnRow(Integer row) {
        UpgradeListing upgradeListing = upgradesListingArray.get(row);
        Double price = upgradeListing.getPrice();
        if(game.moneyManager.getTotal() >= price) {
            game.moneyManager.setTotal(game.moneyManager.getTotal() - price.intValue());

            upgradeListing.increaseLevel();

            upgradesLevel.set(row, upgradesLevel.get(row) + 1);
            Double newDamage = (Double)1.0 * Math.pow((Double)1.15, upgradesLevel.get(0).doubleValue());
            mainCharacter.damage = new Double(Math.ceil(newDamage)).intValue();
        }
    }

    @Override
    public Actor actorForRow(final Integer row) {
//        final TextButton button = new TextButton("Upgrade " + row, uiSkin);
//        button.addListener(new ClickListener() {
//            public void clicked (InputEvent event, float x, float y) {
//                didClickItemOnRow(row);
//                button.setText("Upgrade " + upgradesLevel.get(row).toString());
//                mainCharacter.damage = row;
//            }
//        });
//        return button;

        UpgradeListing upgradeListing = new UpgradeListing(row, new Runnable() {
            @Override
            public void run() {
                didClickItemOnRow(row);
            }
        });
        return upgradeListing;
    }

    @Override
    public Integer numberOfRows() {
        return maxUpgradesNumCount;
    }
}
