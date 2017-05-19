#!/bin/sh
echo 'this file exists solely to interact with gpg-agent, it and the .asc file can be deleted' > mystique-gpg-cacher.txt
gpg2 -ab mystique-gpg-cacher.txt
rm -f mystique-gpg-cacher.txt mystique-gpg-cacher.txt.asc
