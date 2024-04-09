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

    boolean isNotLabelled(int x, int y) {
        return !labels[x + y*w];
    }

    void add(int x, int y) {
        final int i = x + y*w;
        if (!queue.contains(i)) {
            queue.add(i);
        }
    }

    void add(Location pixel) {
        final int i = pixel.x() + pixel.y()*w;
        if (!queue.contains(i)) {
            queue.add(i);
        }
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
