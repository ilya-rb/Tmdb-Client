#!/bin/bash

curl -X POST https://content.dropboxapi.com/2/files/download_zip \
    --header "Authorization: Bearer $DROPBOX_KEY" \
    --header "Dropbox-API-Arg: {\"path\": \"/$CONFIG_ARCHIVE_NAME\"}" \
    -o "./code/$CONFIG_ARCHIVE_NAME.zip" \
    && unzip -o "$CONFIG_ARCHIVE_NAME.zip" \
    && rm "$CONFIG_ARCHIVE_NAME.zip"