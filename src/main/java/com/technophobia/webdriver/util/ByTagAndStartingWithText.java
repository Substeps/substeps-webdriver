package com.technophobia.webdriver.util;

/**
 * By to locate elements by tag name whose text value starts with the specified string
 * @see WebDriverSubstepsBy#ByTagStartingWithText(String, String)
 */
class ByTagAndStartingWithText extends ByTagAndWithText {

    ByTagAndStartingWithText(final String tag, final String text) {
        super(tag, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildXPath(final StringBuilder xpathBuilder) {
        xpathBuilder.append(".//").append(this.tag).append("[starts-with(text(), '").append(this.text)
                .append("')]");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByTagAndStartingWithText{" +
                "tag='" + tag + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
