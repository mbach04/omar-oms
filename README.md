# omar-oms

[![Build Status](https://jenkins.radiantbluecloud.com/buildStatus/icon?job=omar-oms-dev)]()

### Required environment variable
- OMAR_COMMON_PROPERTIES

### Optional environment variables
Only required for Jenkins pipelines or if you are running Artifactory and/or Openshift locally

- OPENSHIFT_USERNAME
- OPENSHIFT_PASSWORD
- ARTIFACTORY_USER
- ARTIFACTORY_PASSWORD

## How to Install omar-oms-plugin locally

1. Git clone the following repos or git pull the latest versions if you already have them.
```
  git clone https://github.com/ossimlabs/omar-common.git
  git clone https://github.com/ossimlabs/omar-core.git
  git clone https://github.com/ossimlabs/omar-openlayers.git
  git clone https://github.com/ossimlabs/omar-oms.git
```

2. Set OMAR_COMMON_PROPERTIES environment variable to the omar-common-properties.gradle (it is part of the omar-common repo).

3. Install omar-core-plugin (it is part of the omar-core repo).
```
 cd omar-core/plugins/omar-core-plugin
 ./gradlew clean install
```

4. Install omar-openlayers-plugin
```
 cd omar-openlayers/plugins/omar-openlayers-plugin
 ./gradlew clean install
```

5. Install omar-oms-plugin
```
 cd omar-oms/plugin/omar-oms-plugin
 ./gradlew clean install
``
