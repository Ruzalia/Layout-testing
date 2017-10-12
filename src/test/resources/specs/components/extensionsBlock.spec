@import ..\commonData.spec
@import ..\commonRules.gspec

@objects
    headline                        css         h3
    product-list                    css         .appListFull
        item-*                          css         li
    button                          css         .button


= Block with extensions =
    @on *
        headline:
            above product-list
            css font-family matches "${geometr}"

        product-list:
            above button

        product-list.item-#:
            height ~89px

        button:
            component smallBlueButton.spec

        | product-list.item-* are aligned vertically next to each other with equal distance

    @on desktop
        headline:
            css font-size is "54px"

        product-list.item-#:
            width 434px

    @on tablet
        headline:
            css font-size is "44px"

        product-list.item-#:
            width 342px

    @on mobile
        headline:
            css font-size is "28px"

        product-list.item-#:
            width 250px