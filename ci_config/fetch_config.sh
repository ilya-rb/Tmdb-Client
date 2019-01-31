#!bin/bash

curl -X POST https://content.dropboxapi.com/2/files/download \
    --header "Authoriation: Bearer $DROPBOX_KEY" \
    --header "Dropbox-API-Arg: {\"path\": \"/$CONFIG_ARCHIVE_NAME\"}" \
    -o "./$CONFIG_ARCHIVE_NAME" \
    && unzip -o $CONFIG_ARCHIVE_NAME \
    && rm $CONFIG_ARCHIVE_NAME