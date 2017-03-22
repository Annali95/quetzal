package com.example.quetzal.drawpanel;

import android.graphics.Path;

/**
 * Created by liguoying on 1/13/17.
 */

public class PathPoints {
        private Path path;
        // private Paint mPaint;
        private int color;
        public boolean pathcount = false;
        private int x, y;

        public PathPoints(Path path, int color) {
            this.path = path;
            this.color = color;
        }


        public Path getPath() {
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

}
