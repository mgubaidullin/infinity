package one.entropy.infinity.aggregator;

import org.apache.camel.main.Main;

public class Application {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(AnalyticRoute.class);
        main.run();
    }
}
