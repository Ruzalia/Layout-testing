@import ..\commonData.spec
@script ..\js\allEven.js

@objects
    top-navigation                  id          navigationWrapper
        logo                            id          logo
            link                            css         a
        item-*                          css         ul li a
        normal-item-*                   css         ul li:not(.selected) a
        selected-item                   css         ul li.selected a


= Fork CMS Header =
    @on *
        top-navigation:
            on top left edge screen 0px left, 0px top
            "Color: Dark Cerulean Blue" css background-color matches "${dark_cerulean_blue}"

        top-navigation.item-#:
            inside top-navigation

        top-navigation.item-#:
            css font-family matches "${helvetica}"

        top-navigation.normal-item-#:
            "Color: Mabel Blue" css color matches "${mabel_blue}"

        @if ${isVisible("top-navigation.selected-item")}
            top-navigation.selected-item:
                "Color: White" css color matches "${white}"

    @on desktop, tablet
        top-navigation.logo:
            inside top-navigation

        top-navigation.item-#:
            css padding-top is "24px"
            css padding-bottom is "24px"
            css font-size is "14px"

        top-navigation.normal-item-#:
            color-scheme >=1% #c6e6e9

        @forEach [top-navigation.item-#] as menuItem, next as nextItem
            ${menuItem}:
                left-of ${nextItem} 0px

        top-navigation.logo.link:
            height ~66px
            css background-image contains "sprite.png"

        top-navigation.logo:
            left-of top-navigation.item-1

    @on desktop
         @forEach [top-navigation.item-#] as menuItem, next as nextItem
             ${nextItem}:
                 css padding-left is "40px"

         @forEach [top-navigation.item-#] as menuItem, prev as prevItem
             ${prevItem}:
                 css padding-right is "40px"

    @on tablet
         @forEach [top-navigation.item-#] as menuItem, next as nextItem
             ${nextItem}:
                 css padding-left is "25px"

         @forEach [top-navigation.item-#] as menuItem, prev as prevItem
             ${prevItem}:
                 css padding-right is "25px"

    @on mobile
        top-navigation.logo.link:
            absent

        top-navigation.item-#:
            css padding-top is "2px"
            css padding-bottom is "3px"
            css font-size is "12px"

        @for  [${allEven("top-navigation.item-#")}] as index
            top-navigation.item-${index}:
                right-of top-navigation.item-${index-1} 24px