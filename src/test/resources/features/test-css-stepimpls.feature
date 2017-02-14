Tags: @non-visual
    
Feature: A feature to test CSS Step implementations
    
Scenario: Test css step impls
    Given I go to the self test page
    FindByCssClass "markerClass-1"
    FindByCssClass "somethingElse" with text "Another heading"
    FindByCssClass "markerClass-2" containing text "Another"

    # #id .class
    FindByCssSelector "#parent_div_id"
    FindByCssSelector ".markerClass-1"

    AssertCssSelector ".not-present-button" is not present
    # shouldn't fail
    ClickByCssClass "not-present-button" if present

    ClickByCssClass "present-button" if present

    FindByCssSelector ".present-button" with text "clicked"

    AssertCssSelector ".two-divs" count is greater than "1"
    AssertCssSelector ".two-divs" count is "2" or less

    AssertCssSelector ".two-divs" is present
    FindByCssClass "two-divs" using timeout "500" containing "1"
    FindByCssClass "two-divs" using timeout "500" with text "2"

    FindById visible-div
    AssertCurrentElement is visible
    ClickButton "hide something visible"
    WaitFor CSS Selector "#visible-div" to be invisibile
    ClickButton "make something visible"
    WaitFor CSS Selector "#visible-div" to be visibile


    Find ByChained CSS selectors ".parent-div-class", ".nested-div-class"
    AssertCurrentElement text="expected"

    FindByCssSelector ".parent-div-class"
    FindChildByTag "div" with cssClassRegex "nested-div-class-\d{4}"
    AssertCurrentElement text="expected2"

    FindNthByTagAndText from parent by CSSSelector ".parent-div-class", tag name "span", tag number "2" containing text "two"

# TODO
# FindNthByTagAndText with parent css "([^"]*)", tag name "([^"]*)", tag number "(\d+)" containing text "([^"]*)"

