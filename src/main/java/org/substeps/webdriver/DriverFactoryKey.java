package org.substeps.webdriver;

import com.technophobia.webdriver.substeps.runner.DriverType;

/**
 * Created by ian on 19/12/16.
 */
public final class DriverFactoryKey implements DriverType{
    private final String key;
    private final boolean visual;

    private transient final Class<? extends DriverFactory> factory;

    public DriverFactoryKey(String key, boolean visual, Class<? extends DriverFactory> factory){
        this.key = key;
        this.visual = visual;
        this.factory = factory;
    }

    public Class<? extends DriverFactory> getFactory() {
        return factory;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DriverFactoryKey that = (DriverFactoryKey) o;

        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean isVisual() {
        return visual;
    }
}
