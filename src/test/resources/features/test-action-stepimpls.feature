
Feature: A feature to test Action Step implementations

Tags: @non-visual

Scenario: Test Action step impls
    Given I go to the self test page
    ClickButton containing "partially"
    FindById partial-text-button-div and text = "Partial text matched Button click successful"

    ClickById other-button-enabler
    FindById the-other-button
    ClickWhenClickable
    FindById the-other-button-div and text = "Other button clicked"

    ClickById trigger-alert
    WaitFor 200
    DismissAlert with message "Popup"


    ClickById trigger-page-title-change
    WaitForPageTitle "A new page title"

    # as of 7.02.2017 Double click doesn't work in Firefox
    FindById dbl-click-trigger
    PerformDoubleClick
    FindById dbl-click-button-div and text = "doubled clicked"

    FindById id-for-js-manipulation and text = "initial"
    ExecuteJavascript document.getElementById("id-for-js-manipulation").innerHTML = "js fiddled"
    FindById id-for-js-manipulation and text = "js fiddled"



    #######################################################
    # External site

    NavigateTo https://swisnl.github.io/jQuery-contextMenu/demo.html
    FindByCssClass "context-menu-one" with text "right click me"
    PerformContextClick

    FindByCssClass "context-menu-item" containing text "Edit"
    AssertCurrentElement is visible


##################################################################
	# these tests need to be last as they go off to google

    NavigateTo url property "external.content"
    And the raw README is loaded
    # Do not add more steps here


Tags: @visual

Scenario: Test Action step impls
    Given I go to the self test page
    TakeScreenshot with prefix "self-test"
    AssertScreenshotFileExists "self-test" something
