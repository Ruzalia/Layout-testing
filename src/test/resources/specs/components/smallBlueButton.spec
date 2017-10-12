@import ..\commonData.spec

= Small blue button =
    @on *
        self:
            height 26px
            css font-family matches "${arial}"
            css font-size is "12px"
            css background-image matches "${blue_button_gradient}"
            css border-bottom-left-radius is "14px"
            css border-bottom-right-radius is "14px"
            css padding-left is "13px"
            css padding-right is "13px"
            css padding-top is "4px"
            css padding-bottom is "4px"