substeps-webdriver [![Build Status](https://travis-ci.org/Substeps/substeps-webdriver.svg)](https://travis-ci.org/Substeps/substeps-webdriver) [![Maven Central](https://img.shields.io/maven-central/v/org.substeps/webdriver-substeps.png?label=webdriver-substeps)](https://maven-badges.herokuapp.com/maven-central/org.substeps/webdriver-substeps) 
==================

[![Join the chat at https://gitter.im/Substeps/substeps-webdriver](https://badges.gitter.im/Substeps/substeps-webdriver.svg)](https://gitter.im/Substeps/substeps-webdriver?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Webdriver based step implementations. 

Substeps documentation can be found [here](http://substeps.technophobia.com/ "Substeps documentation").  

There is also a [Substeps Google group](http://groups.google.com/group/substeps?hl=en-GB "Substeps Google group") if you have any queries and where new releases will ne announced.

Substeps.org Release Notes
==========================

1.0.7
-----
* Upgraded to Selenium 3.3.1 and Guava 21, requires some changes to webdriver wait methods to use java.util.function.Functions rather than the guava equivalents
* added options to maximise webdriver windows on startup (issue #25)
* simplified the shutdown / re-use flags, replacing the existing three confusing flags with a singular strategy value:
    `
    org.substeps.webdriver {
        reuse-strategy = "shutdown_and_create_new" 
    }
    `
    possible values include shutdown_and_create_new, reuse_unless_error_keep_visuals_in_error, reuse_unless_error, leave_and_create_new


1.0.5
-----
* Tests around some of the step impls added in 1.0.4
* Added a config flag (log.pagesource.onerror) to disable logging out the page source on failure - useful for js heavy sites
* Some step impl refactoring, 
  AssertTagElementContainsAttribute tag="<tag>" attributeName="<name>" attributeValue="<value>" has been replaced by
  FindByTagAndAttributes tag="<tag>" attributes=\[<name>="<value>"\]
* AssertEventuallyContains <id> "text" replaced by  FindById id containing text="abc"
* AssertEventuallyNotEmpty id="<id>"  replaced by FindById "<id>" with text matching regex .+
* AssertRadioButton name="radio_btn_name", text="text", checked="true" replaced by AssertRadioButton checked=true/false
* SetCheckBox name="accept", checked=true replaced by SetCheckedBox checked=true/false
* SetRadioButton name=opt_in, value=OFF, checked=true replaced by other finders + SetRadioButton checked=true/false
* SetRadioButton name="opt_in", text="radio button text" replaced by other finders + SetRadioButton checked=true/false
* Startup / Shutdown removed as no longer relevant / necessary

1.0.4
-----
* More step implementations...

1.0.3
-----
* Firefox webdriver starts with blank page rather than the 'learn more' page
* Updated to selenium-3.1.  Added geckodriver configuration path
* Added Remote webdriver capability and corresponding properties
* Modified tests to use hosted github page (to make testing with something like saucelabs possible)
* Moved config over to use Typesafe config
* Modified the Factory mechanism used to create webdrivers - to allow better extensibility and customisation
* Used WebdriverManager to handle downloading the appropriate drivers for chrome, firefox etc.  Will use latest unless specifc versions in localhost.conf.
  see Chrome/Firefox Driver Factories for examples.

1.0.2
-----
* Delegated the shutdown of webdrivers to the factory to allow for projects to override and provide specialisms
* refactored out some statics that weren't essential and making it difficult to test the setup and tear down
* Made some of the base By's accessible to implementing projects.
* Added chromedriver.path properties to config
* Selenium upgrade
* Made BaseBy public to allow extension outside of this project

1.0.0 / 1
-----
* Forked from G2G3.Digital as no longer being maintained.  Group renamed to org.substeps and version number reset to 1.0





com.technophobia release notes
------------------------------

1.1.4
-----
* Move to v2.0 of core, library update across the board
* tidied up some javadocs
* Requires JDK 8


1.1.3
-----
* addition of ExecutionListener config in the pom, set to default step logger implemenation.
* Add in proxy capabilities for other browsers [Peter Phillips]
* Enabled full screen mode for visual browsers
* Refactored MatchingElementResultHandler into it's own class
* Browser js logs now printed via enabling trace on WebDriverBrowserLogs
* Clicks now wait until an item is clickable
* Travis-CI integration


1.1.2
-----
* Catch StaleElementExceptions in FindByTagAndAttributes - elements located in the initial search can become detached from the DOM, such elements can be discared from the results. 
* Removed some dead substeps in the self tests. 

1.1.1
-----
* New step implementation, FindParentByTagAndAttributes.
* Refactored some methods out of this project into the api
* 1.1.1 core and api dependency
* selenium 2.35.0 dependency
* Ability to reset webdriver between scenarios rather than close and restart
* WebDriverFactory customisation to allow customisation of the creation and initialisation of the WebDriver instance
* Clarify some Assertions and Finders, we've decided to remove Asserts that are simply Finders, and also tighten the wording of some steps.  Changes to steps :
    AssertTagElementStartsWithText -> FindFirstTagElementStartingWithText
    AssertValue id msg_id text = "Hello World" -> FindById msg_id and text = "Hello World"
    AssertChildElementsContainText xpath="li//a" text = "Log Out" -> FindFirstChildElementContainingText xpath="li//a" text = "Log Out"
    AssertTagElementContainsText tag="ul" text="list item itext" ->  FindFirstTagElementContainingText tag="ul" text="list item itext"
    FindTagElementContainingText ... -> FindFirstTagElementContainingText ...
* AssertPageSourceContains can now handle checking for quoted strings, eg checking javascript values or return values from ReSTful calls.
* new step implementation FindFirstChild ByTagAndAttributes
* new step implementation Find nth ByTagAndAttributes

1.1.0
-----
* Upgraded dependency on webdriver-java to 2.28.0
* Some changes to work with api which has been split out of core
* Addition of Table row related functionality; FindTableRowWithColumnsThatContainText, FindElementInRow etc
* DismissAlert step implementation

1.0.2
-----
* Refactored some of the Webdriver locating code into new 'By' classes
* DoubleClick and Context click support
* removed Email steps from webdriver-susbteps - these were based on Dumbster and not reliable (caused deadlock)

1.0.0
-----
* Added a property to disable js with HTMLUnit.
* move to 1.0.0 versions of other substeps libraries

0.0.6
-----
* Increased version of substeps-core and substeps-runner for enhanced reporting

0.0.5
-----
* Modified StepImplementation classes to specify the required initialisation classes
* Changed the dependency on activation.jar to an available version

 
0.0.4
-----
* Changes as a result of core changes to Notifications.
* fixed the base url when specifying relative paths on Windows.
* enabled use of the IE driver.
* removed unused config property.
* BUG: fix around FindChildByTagAndAttributes - wasn't actually finding children, siblings were also being reported.
* inclusion of step implementation glossary info
* exposed the DriverType from WebDriverContext
* improved some of the step implementation docs
* BUG: NavigateTo now uses base.url property unless the url begins with file or http
