@import ..\commonData.spec
@import ..\commonRules.gspec

@import ..\components\header.spec
@import ..\components\actionSection.spec
@import ..\components\footer.spec

@objects
    title-container                 id          titleWrapper
        title                           css         h2
        actions                         css         .alignButtons
            download-btn                    css         .button.yellowButton
            try-demo-btn                    css         .button:not(.yellowButton)

    sub-header                      css         #subHeader
        image                           css         img

    small-button-*                  css         a.button:not(.bigButton)
    list-*                          xpath       //*[@class='featureList' or @class='appList' or @class='linkedImage']
    feature-list                    css         .featureList
        item-*                          css         li
    extension-list                  css         .extensionsHome .appList
        item-*                          css         li

    action-section                  css         .mod#action

    download-release                id          doormat
        release-button                  css         .bd

= Fork CMS main page =
    = Page structure =
        @on *
            top-navigation:
                above title-container 0px

            | list-* are above each other

            | extension-list.item* are aligned horizontally next to each other with equal distance

            download-release:
                above footer 0px

            download-release.release-button:
                inside download-release

        @on desktop, tablet
            title-container:
                above sub-header 0px

            title-container.actions.download-btn, title-container.actions.try-demo-btn:
                inside title-container.actions

            sub-header:
                above feature-list

            | feature-list.item* are aligned horizontally next to each other with equal distance

        @on mobile
            title-container.actions, sub-header:
                absent

            title-container:
                above feature-list

            | feature-list.item* are aligned vertically next to each other with equal distance

    = Properties of components =
        @on *
            title-container.title:
                css background-image contains "title_home.png"

            @forEach [small-button-#] as smallBtn
                ${smallBtn}:
                    component ..\components\smallBlueButton.spec

            download-release:
                css padding-top is "30px"

        @on desktop, tablet
            title-container.actions:
                centered horizontally inside title-container