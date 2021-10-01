package com.groupc.game.utils;

import com.badlogic.gdx.physics.box2d.*;

import static com.groupc.game.utils.Constants.PPM;

public class BodyBuilder {
    public static Body createBox(World world, float x, float y, int width, int height, boolean isStatic, short aFilterBit, short bFilterBit) {
        Body pBody;
        BodyDef def = new BodyDef();

        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = aFilterBit;
        fixtureDef.filter.maskBits = bFilterBit;

        pBody = world.createBody(def).createFixture(fixtureDef).getBody();
        shape.dispose();
        return pBody;
    }
}
