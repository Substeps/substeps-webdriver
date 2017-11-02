package com.technophobia.webdriver.util;

/**
 * A By to find an element by id and containing the specified string
 * @see WebDriverSubstepsBy#ByIdContainingText(String, String)
 */
class ByIdContainingText extends XPathBy {

    protected final String text;
    protected final String id;

    ByIdContainingText(final String id, final String text) {
        this.id = id;
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildXPath(final StringBuilder xpathBuilder) {

        xpathBuilder.append(".//*[@id='").append(this.id).append("' and contains(text(), '").append(this.text)
                .append("')]");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByIdContainingText that = (ByIdContainingText) o;

        if (!text.equals(that.text)) return false;
        return id.equals(that.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByIdContainingText{" +
                "text='" + text + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
