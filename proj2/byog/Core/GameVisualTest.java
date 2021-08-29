package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameVisualTest {
    private Game g;

    @Before
    public void init() {
        g = new Game();
    }

    @Test
    public void testInputStringPlay() throws IOException, ClassNotFoundException {
        TETile[][] world1 = g.playWithInputString("N999SDDDWWWDDD");
        TETile[][] world2 = g.playWithInputString("N999SDDD:Q");
        world2 = g.playWithInputString("LWWWDDD");
        assertTrue(Arrays.deepEquals(world1, world2));
        //      TERenderer ter = new TERenderer();
//        ter.initialize(worldKey.length, worldKey[0].length);
//        ter.renderFrame(worldKey);
    }

    @Test
    public void testScreenPlay() throws IOException, ClassNotFoundException {
        g.playWithKeyboard();
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Game g = new Game();
        TETile[][] worldKey = g.playWithInputString("N500SDWA");
        TERenderer ter = new TERenderer();
        ter.initialize(worldKey.length, worldKey[0].length);
        ter.renderFrame(worldKey);
    }
}
