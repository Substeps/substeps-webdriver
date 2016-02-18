#!/bin/bash

if [[ $TRAVIS_BRANCH == 'master' ]] && [[ $TRAVIS_REPO_SLUG == 'Substeps/substeps-webdriver' ]]; then
   mvn deploy --settings settings.xml
else
  mvn verify
fi