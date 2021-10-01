package com.groupc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraController {

    static int cameraState = 2;
    public static void draw(Camera camera, Vector2 lockOn) {
        if (cameraState == 0) {
            lockOnSmooth(camera, lockOn);
        }
        if (cameraState == 1) {
            lockOnStiff(camera, lockOn);
        }
        if (cameraState == 2) {
            aimCamera(camera, lockOn);
        }
    }

    public static void lockOnSmooth (Camera camera, Vector2 lockOn) {
        Vector3 position = camera.position;
        position.x = camera.position.x + (lockOn.x - camera.position.x) * 0.08f;
        position.y = camera.position.y + (lockOn.y - camera.position.y) * 0.08f;
        camera.position.set(position);
        camera.update();
    }

    public static void lockOnStiff (Camera camera, Vector2 lockOn) {
        Vector3 position = camera.position;
        position.x = lockOn.x;
        position.y = lockOn.y;
        camera.position.set(position);
        camera.update();
    }

    public static void aimCamera (Camera camera, Vector2 lockOn) {
        float offset = 0.3f;
        Vector3 position = camera.position;
        Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());

        Vector3 mouseToWorldCords = new Vector3(camera.unproject(new Vector3(mouse.x, mouse.y, 0)));

        position.x = lockOn.x -(camera.position.x - mouseToWorldCords.x) * offset;
        position.y = lockOn.y -(camera.position.y - mouseToWorldCords.y) * offset;

        camera.position.set(position);
        camera.update();
    }
}
