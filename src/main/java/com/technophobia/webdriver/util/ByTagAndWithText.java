package com.technophobia.webdriver.util;

/**
 * By to locate an element using tag name and a with expected text
 *
 * @see WebDriverSubstepsBy#ByTagAndWithText(String, String)
 */
public class ByTagAndWithText extends XPathBy {

    protected final String tag;
    protected final String text;

    ByTagAndWithText(final String tag, final String text) {
        this.tag = tag;
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildXPath(final StringBuilder xpathBuilder) {
        xpathBuilder.append(".//").append(this.tag).append("[")
                .append(WebDriverSubstepsBy.equalsIgnoringCaseXPath("text()", "'" + this.text.toLowerCase() + "'")).append("]");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByTagAndWithText that = (ByTagAndWithText) o;

        if (!tag.equals(that.tag)) return false;
        return text.equals(that.text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = tag.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByTagAndWithText{" +
                "tag='" + tag + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
