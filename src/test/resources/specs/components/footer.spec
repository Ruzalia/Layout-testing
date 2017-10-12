@import ..\commonData.spec
@script ..\js\allEven.js

@objects
    footer                          id              footer
        logo                            id              footerLogo
            link                            css             a
        navigation                      css             #footerNavigation
            item-*                          css             ul li a
            normal-item-*                   css             ul li:not(.selected) a
            selected-item                   css             ul li.selected a
        meta-navigation                 id              footerMetaNavigation
            item-*                          css             ul li a


= Fork CMS Footer =
    @on *
        footer:
            on bottom left edge screen 0px left
            "Color: Persian Blue" css background-color matches "${persian_blue}"

        footer.navigation, footer.meta-navigation:
            inside footer

        footer.navigation:
            above footer.meta-navigation

        footer.navigation.item-#:
            css font-family matches "${helvetica}"

        footer.navigation.normal-item-#:
            "Color: Mabel Blue" css color matches "${mabel_blue}"

        @if ${isVisible("footer.navigation.selected-item")}
            footer.navigation.selected-item:
                "Color: White" css color matches "${white}"

        footer.meta-navigation.item-#:
            css font-family matches "${arial}"
            "Color: Mabel Blue" css color matches "${mabel_blue}"

    @on desktop, tablet
        footer.logo:
            inside footer

        footer.navigation.item-#:
            css padding-top is "24px"
            css padding-bottom is "24px"
            css font-size is "14px"

        footer.meta-navigation.item-#:
            css font-size is "11px"

        @forEach [footer.navigation.item-#] as menuItem, next as nextItem
            ${menuItem}:
                left-of ${nextItem} 0px

        footer.logo.link:
            height ~66px
            css background-image contains "sprite.png"

        footer.logo:
            left-of footer.navigation.item-1

    @on desktop
        @forEach [footer.navigation.item-#] as menuItem, next as nextItem
            ${nextItem}:
                css padding-left is "30px"

        @forEach [footer.navigation.item-#] as menuItem, prev as prevItem
            ${prevItem}:
                css padding-right is "30px"

    @on tablet
        @forEach [footer.navigation.item-#] as menuItem, next as nextItem
            ${nextItem}:
                css padding-left is "13px"

        @forEach [footer.navigation.item-#] as menuItem, prev as prevItem
            ${prevItem}:
                css padding-right is "13px"

    @on mobile
        footer.logo.link:
            absent

        footer.meta-navigation.item-#:
            css font-size is "11px"

        footer.navigation.item-#:
            css padding-top is "2px"
            css padding-bottom is "3px"
            css font-size is "12px"