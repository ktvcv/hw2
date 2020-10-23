package com.ithillel.service.watchservice;

import com.ithillel.service.ApplicationContext;

import java.io.IOException;
import java.nio.file.*;

public class WatchFileRuntime {
    private static WatchService watchService;

    public static void run(ApplicationContext context) throws IOException, InterruptedException {
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    }
}
