package com.lovemesomecoding;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.lovemesomecoding.utils.DateTimeUtils;

@SpringBootApplication
public class SpringbootStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootStarterApplication.class, args);
	}

	@PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            // Display Environmental Useful Variables
            try {
                System.out.println("\n");
                Runtime runtime = Runtime.getRuntime();
                double mb = 1048576;// megabtye to byte
                double gb = 1073741824;// gigabyte to byte
                Environment env = ctx.getEnvironment();
                String timeZone = "America/Los_Angeles";

                System.out.println("************************ Lovemesomecoding - Starter ***************************");
                System.out.println("** Active Profile: " + Arrays.toString(env.getActiveProfiles()));
				System.out.println("** Port: " + env.getProperty("server.port"));
				System.out.println("** Build: " + "0.0.102");
				System.out.println("** Timezone: " + timeZone);

				System.out.println("** Internal Url: http://localhost:" + env.getProperty("server.port"));
				System.out.println("** External Url: http://" + InetAddress.getLocalHost().getHostAddress() + ":"
						+ env.getProperty("server.port"));

				System.out.println(
						"** Internal Swagger: http://localhost:" + env.getProperty("server.port") + "/swagger-ui.html");
                
                
                System.out.println("************************* Java - JVM *****************************************");
                System.out.println("** Number of processors: " + runtime.availableProcessors());
                String processName = ManagementFactory.getRuntimeMXBean().getName();
                System.out.println("** Process ID: "+processName.split("@")[0]);
                System.out.println("** Total memory: " + (double) (runtime.totalMemory() / mb) + " MB = " + (double) (runtime.totalMemory() / gb) + " GB");
                System.out.println("** Max memory: " + (double) (runtime.maxMemory() / mb) + " MB = " + (double) (runtime.maxMemory() / gb) + " GB");
                System.out.println("** Free memory: " + (double) (runtime.freeMemory() / mb) + " MB = " + (double) (runtime.freeMemory() / gb) + " GB");
                System.out.println("******************************************************************************");
            } catch (Exception e) {
                System.err.println("Exception, commandlineRunner -> " + e.getMessage());
            }
            System.out.println("\n");
        };
    }
}
