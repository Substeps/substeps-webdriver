package org.substeps.webdriver;

import com.technophobia.webdriver.substeps.runner.DriverType;

/**
 * A Key class to be used in lookups to denote a particular Driver factory type and the factory class used to create the actual driver
 */
public final class DriverFactoryKey implements DriverType {
    private final String key;
    private final boolean visual;

    private final transient Class<? extends DriverFactory> factory;

    /**
     * standard constructor
     *
     * @param key     the identifier for this DriverFactory
     * @param visual  is the driver represented by this factory visual
     * @param factory the factory class that will instantiate the driver
     */
    public DriverFactoryKey(String key, boolean visual, Class<? extends DriverFactory> factory) {
        this.key = key;
        this.visual = visual;
        this.factory = factory;
    }

    /**
     * @return the factory class that is used to instantiate this driver type
     */
    public Class<? extends DriverFactory> getFactory() {
        return factory;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DriverFactoryKey that = (DriverFactoryKey) o;

        return key.equals(that.key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * return the key that represents the Driver factory
     *
     * @return the key associated with this driver factory
     */
    public String getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVisual() {
        return visual;
    }
}
