package com.akurilo.weatherapi;

import akka.actor.ActorSystem;
import akka.util.Timeout;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import scala.concurrent.duration.Duration;

@SpringBootApplication
public class WeatherApiApplication {

    public static ActorSystem ACTOR_SYSTEM;
    public static final Timeout TIMEOUT_GET_MESSAGE = new Timeout(Duration.create(100, "seconds"));

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiApplication.class, args);

        if (args.length == 0)
            startup(new String[] { "2551" });
        else
            startup(args);
    }

    public static void startup(String[] ports) {
        for (String port : ports) {

            final Config config =
                    ConfigFactory.parseString(
                            "akka.remote.netty.tcp.port=" + port + "\n" +
                                    "akka.remote.artery.canonical.port=" + port)
                            //.withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]"))
                            .withFallback(ConfigFactory.load());
            ACTOR_SYSTEM = ActorSystem.create("ClusterSystem", config);
        }
    }
}
