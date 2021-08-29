package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import org.junit.Before;
import org.junit.Test;

public class GameVisualTest {
    private Game g;

    @Before
    public void init() {
        g = new Game();
    }

    @Test
    public void testKeyboardPlay() {
        TETile[][] worldKey = g.playWithInputString("N500SDWA");
        TERenderer ter = new TERenderer();
        ter.initialize(worldKey.length, worldKey[0].length);
        ter.renderFrame(worldKey);
    }

    @Test
    public void testScreenPlay() {
        g.playWithKeyboard();
    }


    public static void main(String[] args) {
        Game g = new Game();
        TETile[][] worldKey = g.playWithInputString("N500SDWA");
        TERenderer ter = new TERenderer();
        ter.initialize(worldKey.length, worldKey[0].length);
        ter.renderFrame(worldKey);
    }
}
