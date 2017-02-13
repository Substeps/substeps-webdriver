Tags: @non-visual

Feature: A feature to test Form Step implementations
    
Scenario: Test Form step impls
    Given I go to the self test page
    ClearAndSendKeys "a test" to id input-value-assertion-id

    FindById input-value-assertion-id
    AssertCurrentInput value="a test"

    FindById select_id
    ChooseOption "number three option" in current element
    AssertSelect id="select_id" text="number three option" is currently selected

    Select "number one option" option in Id "select_id"
    AssertSelect id="select_id" text="number one option" is currently selected

    Select "number two option" option in class "select-marker-class"
    AssertSelect id="select_id" text="number two option" is currently selected

    FindById text-id
    SendKeys "abc123"
    FindById text-id
    AssertCurrentInput value="abc123"

    SendKey Key.BACK_SPACE
    AssertCurrentInput value="abc12"

    FindById text-id
    ClearAndSendKeys ""

    SendKeys pathOf property "test.filename" to current element
    AssertCurrentInput value contains "one.file"

    FindById text-id
    ClearAndSendKeys ""

    SendKeys pathOf property "test.filename2" to id "text-id"
    AssertCurrentInput value contains "two.file"

    FindById input-value-to-submit-id
    Submit
