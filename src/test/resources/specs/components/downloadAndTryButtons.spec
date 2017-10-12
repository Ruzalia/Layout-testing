@import ..\commonData.spec

@objects
    download-btn                    css         .button.yellowButton
    try-demo-btn                    css         .button:not(.yellowButton)

= Big buttons: Download Fork and Try the demo =
    @on desktop, tablet
        download-btn, try-demo-btn:
            height 40px
            css font-family matches "${arial}"
            css font-size is "16px"
            css padding-left is "21px"
            css padding-right is "21px"
            css padding-top is "11px"
            css padding-bottom is "11px"

        download-btn:
            left-of try-demo-btn