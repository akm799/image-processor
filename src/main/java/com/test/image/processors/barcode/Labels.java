package com.test.image.processors.barcode;

import com.test.image.model.Location;

import java.util.LinkedList;
import java.util.Queue;

final class Labels {
    private final int w;
    private final boolean[] labels;
    private final Queue<Integer> queue = new LinkedList<>();

    Labels(int w, int h) {
        this.w = w;
        labels = new boolean[w*h];
    }

    boolean isLabelled(int x, int y) {
        return labels[x + y*w];
    }

    boolean isNotLabelled(Location pixel) {
        return !labels[pixel.x() + pixel.y()*w];
    }

    void add(int x, int y) {
        queue.add(x + y*w);
    }

    void add(Location pixel) {
        queue.add(pixel.x() + pixel.y()*w);
    }

    boolean removeAndLabel(Location pixel) {
        if (queue.isEmpty()) {
            pixel.clear();
            return false;
        }

        final int index = queue.remove();
        pixel.set(index%w, index/w);
        labels[index] = true;

        return true;
    }

    boolean hasPixels() {
        return !queue.isEmpty();
    }
}
