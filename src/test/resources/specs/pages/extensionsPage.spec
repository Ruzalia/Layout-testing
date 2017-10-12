@import ..\commonData.spec

@import ..\components\header.spec
@import ..\components\actionSection.spec
@import ..\components\footer.spec

@objects
    title-container                 id          titleWrapper

    sub-header                      css         #subHeader
        links                           css         p

    small-button-*                  css         a.button:not(.bigButton)

    download-block-*                css         section.mod:not(#action)
    action-section                  css         .mod#action

= Fork CMS extensions page =

    = Page structure =
        @on *
            top-navigation:
                above title-container 0px

            download-block-2:
                above action-section


        @on desktop, tablet
            title-container:
                above sub-header 0px

            download-block-1:
                left-of download-block-2
                aligned horizontally all download-block-2

            download-block-1:
                below sub-header

        @on mobile
            title-container.actions, sub-header:
                absent

            download-block-1:
                above download-block-2
                aligned vertically all download-block-2

            download-block-1:
                below title-container

    = Properties of components =
        @on *
            download-block-#:
                component ..\components\extensionsBlock.spec
