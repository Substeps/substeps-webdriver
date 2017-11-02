package com.technophobia.webdriver.util;

/**
 * A By to find an element by id and text, both case sensitive and insensitive
 *
 * @see WebDriverSubstepsBy#ByIdAndText(String, String)
 */
public class ByIdAndText extends XPathBy {

    /**
     * the id of the element to find
     */
    protected final String id;

    /**
     * the expected text value of the id element
     */
    protected final String text;

    /**
     * flag to indicate case insensitive match
     */
    protected final boolean caseSensitive;

    ByIdAndText(final String id, final String text) {
        this(id, text, false);
    }

    ByIdAndText(final String id, final String text, final boolean caseSensitive) {
        this.id = id;
        this.text = text;
        this.caseSensitive = caseSensitive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildXPath(final StringBuilder xpathBuilder) {

        xpathBuilder.append(".//*[@id='").append(this.id).append("' and ");

        if (caseSensitive) {
            xpathBuilder.append("text()='").append(this.text).append("'");
        } else {
            xpathBuilder.append(WebDriverSubstepsBy.equalsIgnoringCaseXPath("text()", "'" + this.text.toLowerCase() + "'"));
        }

        xpathBuilder.append("]");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByIdAndText that = (ByIdAndText) o;

        if (caseSensitive != that.caseSensitive) return false;
        if (!id.equals(that.id)) return false;
        return text.equals(that.text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + (caseSensitive ? 1 : 0);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByIdAndText{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", caseSensitive=" + caseSensitive +
                '}';
    }
}
