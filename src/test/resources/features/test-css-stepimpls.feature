# new feature
# Tags: optional
    
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

#
#    # duplicate ?
#    AssertCssSelector "<s>" is present