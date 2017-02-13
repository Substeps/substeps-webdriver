Tags: @non-visual

Feature: A feature to test iframe and window related step impls
    
Scenario: Test iframe and window step implementations
    NavigateTo url property "iframe.test.page"

    FindById outer-div-id and text = "iframe page"

    Switch to new frame by name "iframe-name"
    FindById iframed-div-id and text = "iframed div"

    Switch to default content
    FindById outer-div-id and text = "iframe page"

    Switch to new frame by CSS selector ".iframe-class"
    FindById iframed-div-id and text = "iframed div"

    Switch to default content
    FindByCssSelector ".iframe-class"
    SwitchFrameToCurrentElement
    FindById iframed-div-id and text = "iframed div"


    Switch to default content
    ClickById open-new-window-button

    Switch to new window
    FindById new-window-div-id and text = "new window div"

    Close new window
    FindById outer-div-id and text = "iframe page"

