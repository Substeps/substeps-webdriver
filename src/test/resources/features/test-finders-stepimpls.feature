Tags: @non-visual

Feature: A feature to test finder step implementations description
    
Scenario: Test finder step implementations
    Given I go to the self test page

    FindByTagAndAttributesWithText tag="div" attributes=[data-reactid="12345"] with text="div1 with tag and attributes"# go to doesn't work
    FindByTagAndAttributesWithText tag="div" attributes=[data-reactid="12345", aria-label="a-label"] with text="div2 with tag and attributes"

    FindById find-child-by-name-parent
    FindChild ByName name="child1"
    AssertCurrentElement text="child 1"

    FindById find-child-by-name-parent
    FindChild ByTagAndAttributes tag="div" attributes=[data-reactid="12345"] with text="child div1 with tag and attributes"

    FindFirstByTagAndAttributes tag="div" attributes=[data-reactid="54321"]
    AssertCurrentElement text="first"

    FindById find-child-by-name-parent
    FindFirstChild ByTagAndAttributes tag="div" attributes=[data-reactid="6789"]
    AssertCurrentElement text="first child"

    FindById find-child-by-name-parent
    FindFirstTagElementStartingWithText tag="div" text="beginning with"
    AssertCurrentElement text="beginning with first"

    # intellij plugin doesn't go to this
    FindNthByTagAndAttributes n="2" tag="div" attributes=[data-reactid="54321"]
    AssertCurrentElement text="second"

    FindByXpath //li[a/i[contains(@class, "NOT_RUN")]]
    AssertCurrentElement text="target"

    FindById ul-parent-id
    FindFirstChildElementContainingText xpath="li//a" text="decoy"

    FindByTag "div" with text "some text"
    FindByTag "span" containing text "xyzabc"

    FindById "span-id-with-regex" with text matching regex \w* xyzabc.*
    AssertCurrentElement text="blah xyzabc found"

    # shouldn't result in failure
    NotImplemented

    FindById visible-div
    AssertCurrentElement is visible
    ClickButton "hide something visible"
    WaitFor id "visible-div" to hide

    ClickButton "make something visible"
    WaitFor id "visible-div" to be visible

    FindById not-visible-div
    AssertCurrentElement is invisible

    FindById not-visible-hidden-div
    AssertCurrentElement is invisible

    FindBy xpath with token replacement $x("//li[a/i[contains(@class, '%s')]]") "FAILED"
    AssertCurrentElement text="target2"

    FindById id-for-partial-text-match containing text="jarlsberg"

