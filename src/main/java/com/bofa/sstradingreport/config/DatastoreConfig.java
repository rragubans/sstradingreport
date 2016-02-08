
package com.bofa.sstradingreport.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatastoreConfig {

    public static final String BIKING_PICTURES_DIRECTORY = "data/sstradingReport";

    /**
     * Configures a file based datastore for storing large objects.
     *
     * @param datastoreBaseDirectoryPath
     * @return
     */

    @Bean
    public File datastoreBaseDirectory(final @Value("${sstradingreport.datastore-base-directory:${user.dir}/var/dev}") String datastoreBaseDirectoryPath) {
        final File rv = new File(datastoreBaseDirectoryPath);
        if (!(rv.isDirectory() || rv.mkdirs())) {
            throw new RuntimeException(String.format("Could not initialize '%s' as base directory for datastore!", rv.getAbsolutePath()));
        }

        new File(rv, BIKING_PICTURES_DIRECTORY).mkdirs();
        return rv;
    }
}
