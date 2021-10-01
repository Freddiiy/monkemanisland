package com.groupc.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.groupc.game.utils.Constants.*;

public class TileObject {

    public static void parseTileObject(World world, MapObjects objects) {
        for (MapObject object : objects) {
            Shape shape;
            System.out.println("outside instance");
            if(object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject) object);
                System.out.println("inside instance");
            } else {continue;}

            Body body;
            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(def);
            body.createFixture(shape, 1f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            fixtureDef.isSensor = false;
            fixtureDef.filter.categoryBits = BIT_WALL;
            fixtureDef.filter.maskBits = BIT_PLAYER | BIT_ENEMY;
            fixtureDef.filter.groupIndex = 1;
            body.createFixture(fixtureDef);
            shape.dispose();
        }
    }

    private static ChainShape createPolyLine(PolylineMapObject polyline) {
        float[] vert = polyline.getPolyline().getTransformedVertices();
        Vector2[] worldVert = new Vector2[vert.length/2];

        for (int i = 0; i < worldVert.length; i++) {
            worldVert[i] = new Vector2(vert[i*2] / Constants.PPM, vert[i*2+1] / Constants.PPM);
        }
        System.out.println("test" + worldVert[1]);
        ChainShape chain = new ChainShape();
        chain.createChain(worldVert);
        return chain;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject)
    {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / Constants.PPM,
                (rectangle.y + rectangle.height * 0.5f) / Constants.PPM);
        polygon.setAsBox(rectangle.width * 0.5f / Constants.PPM,
                rectangle.height * 0.5f / Constants.PPM,
                size, 0.0f);
        return polygon;
    }
}
