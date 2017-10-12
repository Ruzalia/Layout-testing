@import ..\commonData.spec

@objects
    action-section                  css         .mod#action
        headline                        css         h3

= Action section =
    @on *
        action-section:
            css background-image contains "background_love.png"
            component downloadAndTryButtons.spec

        action-section.headline:
            inside action-section
            css text-align is "center"
            css font-family matches "${geometr}"
            "Color: Pelorous Blue" css color matches "${pelorous_blue}"