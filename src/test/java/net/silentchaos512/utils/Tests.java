package net.silentchaos512.utils;

import org.junit.Assert;
import org.junit.Test;

public class Tests {
    @Test
    public void testColors() {
        Color red = new Color(0xFF0000);
        Color green = new Color(0x00FF00);
        // Check components
        Assert.assertEquals(0xFF, red.getRedInt());
        Assert.assertEquals(0x0, red.getGreenInt());
        Assert.assertEquals(0x0, red.getBlueInt());
        Assert.assertEquals(0xFF, red.getAlphaInt());

        // Check alpha and full int
        Assert.assertEquals(0xFFFF0000, red.getColor());
        Assert.assertEquals(0xFF00FF00, green.getColor());

        // Check blending
        Color blended = Color.blend(red, green);
        Assert.assertEquals(0xFF7F7F00, blended.getColor());
    }
}
