package com.technophobia.webdriver.util;

/**
 * By to match by tag name and where the element contains the specified text
 * @see WebDriverSubstepsBy#ByTagContainingText(String, String)
 */
public class ByTagAndContainingText extends ByTagAndWithText {

    ByTagAndContainingText(final String tag, final String text) {
        super(tag, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildXPath(final StringBuilder xpathBuilder) {
        xpathBuilder.append(".//").append(this.tag).append("[contains(text(), '").append(this.text).append("')]");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByTagAndContainingText{" +
                "tag='" + tag + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
