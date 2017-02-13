Tags: @non-visual

Feature: A feature to test Assertion based Step implementations

Scenario: Test Assertion step impls
    Given I go to the self test page
    FindById span-id-with-regex
    AssertCurrentElement text contains "xyzabc"

    FindById input-value-assertion-id
    AssertCurrentInput value="123456"

    AssertNotPresent text="discombobulation"

    RememberForScenario textFrom "remember-div" as "context.name"
    AssertSame rememberedValue "context.name" compareToElement "to-compare-to-matching-div"
    AssertDifferent rememberedValue "context.name" compareToElement "to-compare-to-not-matching-div"




