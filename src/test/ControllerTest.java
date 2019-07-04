package test;

import client.Controller;

public class ControllerTest {
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.parse();

        Controller controller2 = new Controller();
        controller2.parse();
    }
}
