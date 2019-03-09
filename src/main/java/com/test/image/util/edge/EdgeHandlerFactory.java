package com.test.image.util.edge;

import com.test.image.util.edge.impl.ExtendEdgeHandler;
import com.test.image.util.edge.impl.MirrorEdgeHandler;

/**
 * Created by Thanos Mavroidis on 09/03/2019.
 */
public final class EdgeHandlerFactory {

    public static EdgeHandler mirrorInstance() {
        return new MirrorEdgeHandler();
    }

    public static EdgeHandler extendInstance() {
        return new ExtendEdgeHandler();
    }

    private EdgeHandlerFactory() {}
}
