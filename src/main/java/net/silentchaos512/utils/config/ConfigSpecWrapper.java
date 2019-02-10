package net.silentchaos512.utils.config;

import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import org.apache.logging.log4j.LogManager;

public class ConfigSpecWrapper {
    final CommentedFileConfig config;
    final ConfigSpec spec;

    public ConfigSpecWrapper(CommentedFileConfig config, ConfigSpec spec) {
        this.config = config;
        this.spec = spec;

        this.config.load();
    }

    public ConfigValue.Builder<?> builder(String path) {
        return ConfigValue.builder(this, path);
    }

    public void comment(String path, String comment) {
        config.setComment(path, comment);
    }

    public void comment(String path, String firstLine, String... rest) {
        StringBuilder builder = new StringBuilder(firstLine);
        for (String str : rest) builder.append("\n").append(str);
        comment(path, builder.toString());
    }

    public void validate() {
        if (!spec.isCorrect(config)) {
            String configName = config.getNioPath().toString();
            LogManager.getLogger().warn("Correcting config file {}", configName);
            spec.correct(config, (action, path, incorrectValue, correctedValue) ->
                    LogManager.getLogger().warn("  {}: {} -> {}", path, incorrectValue, correctedValue));
            config.save();
        }
//        config.close();
    }
}
