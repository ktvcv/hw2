package com.ithillel.service.watchservice;

import com.ithillel.service.ApplicationContext;
import com.ithillel.service.exceptions.LoadDataRuntimeException;

import java.io.IOException;
import java.nio.file.*;

public class WatchFileRuntime {

    public static void run(ApplicationContext context) throws InterruptedException {

        try {
            final WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(System.getProperty("path"));

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    final Path changed = (Path) event.context();
                    if (changed.endsWith("application.json")) {
                        context.initBeansConfig();
                        System.out.println(event + "textProcessor: " + context.getBean("textProcessor"));
                    }
                }
                key.reset();
            }
        } catch (IOException e) {
            throw new LoadDataRuntimeException("Impossible to initialize Watch Service for file" + e);
        }

    }
}
