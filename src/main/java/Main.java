

import utils.Controller;

import java.io.IOException;

class Main {
    public static void main(String args[]) throws IOException {
        utils.Controller controller = utils.Controller.getInstance();
        controller.run();
    }
}