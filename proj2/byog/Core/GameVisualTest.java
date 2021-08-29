package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class GameVisualTest {
    private Game g;

    @Before
    public void init() {
        g = new Game();
    }

    @Test
    public void testInputStringPlay() throws IOException, ClassNotFoundException {
        TETile[][] worldKey=g.playWithInputString("n8772076153521736045sawsasdsadwwwwsa");
        TERenderer ter = new TERenderer();
//        ter.initialize(worldKey.length, worldKey[0].length);
//        ter.renderFrame(worldKey);
    }

    @Test
    public void testScreenPlay() throws IOException, ClassNotFoundException {
        g.playWithKeyboard();
    }


    public static void main(String[] args)throws IOException, ClassNotFoundException {
        Game g = new Game();
        TETile[][] worldKey = g.playWithInputString("N500SDWA");
        TERenderer ter = new TERenderer();
        ter.initialize(worldKey.length, worldKey[0].length);
        ter.renderFrame(worldKey);
    }
}
