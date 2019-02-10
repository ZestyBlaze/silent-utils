package net.silentchaos512.utils.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.ConfigSpec;
import net.silentchaos512.utils.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConfigValue<T> {
    final CommentedConfig config;
    final ConfigSpec spec;
    final String path;

    ConfigValue(ConfigSpecWrapper wrapper, String path, Consumer<ConfigSpec> handleSpec, Consumer<CommentedConfig> handleConfig) {
        this.config = wrapper.config;
        this.spec = wrapper.spec;
        this.path = path;

        handleSpec.accept(this.spec);
        handleConfig.accept(this.config);
    }

    public T get() {
        return config.get(this.path);
    }

    public static <T> Builder<T> builder(ConfigSpecWrapper wrapper, String path) {
        return new Builder<>(wrapper, path);
    }

    public static class Builder<T> {
        private final ConfigSpecWrapper wrapper;
        private final String path;
        private final Collection<Consumer<CommentedConfig>> handleConfig = new ArrayList<>();
        private final Collection<Consumer<ConfigSpec>> handleSpec = new ArrayList<>();

        public Builder(ConfigSpecWrapper wrapper, String path) {
            this.wrapper = wrapper;
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public Builder<T> comment(String comment) {
            return handleConfig(c -> c.setComment(path, comment));
        }

        public Builder<T> comment(String firstLine, String... rest) {
            StringBuilder builder = new StringBuilder(firstLine);
            for (String str : rest) builder.append("\n").append(str);
            return comment(builder.toString());
        }

        public Builder<T> handleSpec(Consumer<ConfigSpec> handler) {
            this.handleSpec.add(handler);
            return this;
        }

        public Builder<T> handleConfig(Consumer<CommentedConfig> handler) {
            this.handleConfig.add(handler);
            return this;
        }

        public BooleanValue define(boolean defaultValue) {
            handleSpec(spec -> spec.define(path, defaultValue, o -> o instanceof Boolean));
            return new BooleanValue(wrapper, path, this::doSpec, this::doConfig);
        }

        public DoubleValue defineInRange(double defaultValue, double min, double max) {
            return defineInRange(() -> defaultValue, min, max);
        }

        public DoubleValue defineInRange(Supplier<Double> defaultValue, double min, double max) {
            handleSpec(spec -> spec.defineInRange(path, defaultValue, min, max));
            return new DoubleValue(wrapper, path, this::doSpec, this::doConfig);
        }

        public IntValue defineInRange(int defaultValue, int min, int max) {
            return defineInRange(() -> defaultValue, min, max);
        }

        public IntValue defineInRange(Supplier<Integer> defaultValue, int min, int max) {
            handleSpec(spec -> spec.defineInRange(path, defaultValue, min, max));
            return new IntValue(wrapper, path, this::doSpec, this::doConfig);
        }

        public LongValue defineInRange(long defaultValue, long min, long max) {
            return defineInRange(() -> defaultValue, min, max);
        }

        public LongValue defineInRange(Supplier<Long> defaultValue, long min, long max) {
            handleSpec(spec -> spec.defineInRange(path, defaultValue, min, max));
            return new LongValue(wrapper, path, this::doSpec, this::doConfig);
        }

        public ColorValue defineColor(int defaultValue) {
            handleSpec(spec -> spec.define(path, Color.format(defaultValue), o ->
                    o instanceof String && Color.validate((String) o)));
            return new ColorValue(defaultValue, wrapper, path, this::doSpec, this::doConfig);
        }

        public IntValue defineColorInt(int defaultValue) {
            handleSpec(spec -> spec.define(path, Color.format(defaultValue), o ->
                    o instanceof String && Color.validate((String) o)));
            return new IntValue(wrapper, path, this::doSpec, this::doConfig) {
                @Override
                public Integer get() {
                    return Color.parse(config.get(path)).getColor();
                }
            };
        }

        public <E extends Enum<E>> EnumValue<E> defineEnum(E defaultValue) {
            EnumSet<E> validValues = EnumSet.allOf(defaultValue.getDeclaringClass());
            return defineEnum(defaultValue, validValues);
        }

        public <E extends Enum<E>> EnumValue<E> defineEnum(E defaultValue, Set<E> validValues) {
            handleSpec(spec -> spec.define(path, defaultValue.name(), o ->
                    validateEnum(defaultValue.getDeclaringClass(), validValues, o)));
            return new EnumValue<>(defaultValue, wrapper, path, this::doSpec, this::doConfig);
        }

        private static <E extends Enum<E>> boolean validateEnum(Class<E> clazz, Set<E> validValues, Object o) {
            if (!(o instanceof String)) return false;

            String name = (String) o;
            for (E e : validValues) {
                if (e.name().equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        }

        public ConfigValue<T> define(T defaultValue) {
            return define(() -> defaultValue, o ->
                    o != null && defaultValue.getClass().isAssignableFrom(o.getClass()));
        }

        public ConfigValue<T> define(T defaultValue, Predicate<Object> validator) {
            return define(() -> defaultValue, validator);
        }

        public ConfigValue<T> define(Supplier<T> defaultValue, Predicate<Object> validator) {
            handleSpec(spec -> spec.define(path, defaultValue, validator));
            return new ConfigValue<>(wrapper, path, this::doSpec, this::doConfig);
        }

        private void doSpec(ConfigSpec spec) {
            handleSpec.forEach(h -> h.accept(spec));
        }

        private void doConfig(CommentedConfig config) {
            handleConfig.forEach(h -> h.accept(config));
        }
    }

    public static final Predicate<Object> IS_STRING = o -> o instanceof String;
    public static final Predicate<Object> IS_NONEMPTY_STRING = o -> o instanceof String && !((String) o).isEmpty();
}
