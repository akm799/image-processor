package com.test.image.model;

import org.junit.Assert;
import org.junit.Test;

public class KernelTest {

    @Test
    public void shouldBuildKernel() {
        final String s = "1 2 3 \n4 5 6 \n7 8 9 \n";
        final int[][] k = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

        final Kernel underTest = new Kernel(k);
        Assert.assertEquals(s, underTest.toString());
    }
}
