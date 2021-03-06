# Maven Plugin for Azure App Service
[![Maven Central](https://img.shields.io/maven-central/v/com.microsoft.azure/azure-webapp-maven-plugin.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.microsoft.azure%22%20AND%20a%3A%22azure-webapp-maven-plugin%22)

The Maven Plugin for Azure App Service provides seamless integration into Maven projects, 
and makes it easier for developers to deploy to different kinds of Azure Web Apps:
  - [Web App on Windows](https://docs.microsoft.com/en-us/azure/app-service/app-service-web-overview)
  - [Web App on Linux](https://docs.microsoft.com/azure/app-service-web/app-service-linux-intro)
  - [Web App for Containers](https://docs.microsoft.com/en-us/azure/app-service/containers/tutorial-custom-docker-image)
  
#### Table of Content
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [More Samples](#more-samples)
- [Goals](#goals)
- [Authentication with Azure](#authentication-with-azure)
- [Azure App Service](#azure-app-service)
    - [Web App on Windows](#web-app-on-windows)
    - [Web App on Linux](#web-app-on-linux)
    - [Web App for Containers](#web-app-for-containers)
- [Deployment Type](#deployment-type)
    - [WAR Deployment](#war-deployment)
    - [JAR Deployment](#jar-deployment)
    - [AUTO Deployment](#auto-deployment)
    - [ZIP Deployment](#zip-deployment)
    - [FTP Deployment](#ftp-deployment)
    - [NONE Deployment](#none-deployment)
- [Advanced Configurations](#advanced-configurations)
- [Supported Regions](#supported-regions)
- [Supported Pricing Tiers](#supported-pricing-tiers)

## Prerequisites

Tool | Required Version
---|---
JDK | 1.7 or above
Maven | 3.0 or above

## Quick Start 

1. Create a new Azure App Service and choose Linux with built-in tomcat as the environment. 

2. To use this plugin in your Maven project, add the following settings for the plugin to your `pom.xml` file:

    ```xml
    <project>
       ...
       <packaging>war</packaging>
       ...
       <build>
          <pluginManagement>
             <plugins>
                <groupId>com.microsoft.azure</groupId>
                <artifactId>azure-webapp-maven-plugin</artifactId>
                <!-- check Maven Central for the latest version -->
                <version>1.3.0</version>
             </plugins>
          </pluginManagement>
          <plugins>
             <plugin>
                <groupId>com.microsoft.azure</groupId>
                <artifactId>azure-webapp-maven-plugin</artifactId>
                <configuration>

                    <!-- Web App information -->
                    <resourceGroup>your-resource-group</resourceGroup>
                    <appName>your-app-name</appName>

                    <!-- Java Runtime Stack for Web App on Linux-->
                    <linuxRuntime>tomcat 8.5-jre8</linuxRuntime>
                </configuration>
             </plugin>
             ...
          </plugins>
       </build>
    </project>
    ```
   
3. Use the following commands to deploy your project to Azure App Service. 

    ```
    $ mvn azure-webapp:deploy
    ```

<a name="more-samples"></a>
## More Samples	
A few typical usages of Maven Plugin for Azure App Service Web Apps are listed at [Web App Samples](../docs/web-app-samples.md). You can choose one to quickly get started.

<a name="goals"></a>
## Goals

The Maven Plugin for Azure App Service Web Apps has only one goal: `azure-webapp:deploy`. 

Goal | Description
--- | ---
`azure-webapp:deploy` | Deploy artifacts or docker container image to an Azure Web App based on your configuration.<br>If the specified Web App does not exist, it will be created.

## Authentication with Azure

You can use the Azure CLI 2.0 for authentication. More authentication methods can be found [here](../docs/common-configuration.md).  

1. Install the Azure CLI 2.0 by following the instructions in the [Install Azure CLI 2.0](https://docs.microsoft.com/cli/azure/install-azure-cli) article.

2. Run the following commands to log into your Azure subscription:

    ```
    $ az login
    $ az account set --subscription <subscription Id>
    ```

## Azure App Service
In this section, you can see how to integrate the Maven plugin with different kinds of Azure App Services.

### Web App on Windows

For `Web App on Windows`, only Java runtime stack is supported in our plugin.
You can use `<javaVersion>` and `<javaWebContainer>` to configure the runtime of your Web App.

#### Java Runtime
Use values from the following table to configure the JVM you want to use in your Web App.

Supported Value | Description
---|---
`1.7` | Java 7, Newest minor version
`1.7.0_51` | Java 7, Update 51
`1.7.0_71` | Java 7, Update 71
`1.8` | Java 8, Newest minor version
`1.8.0_25` | Java 8, Update 25
`1.8.0_60` | Java 8, Update 60
`1.8.0_73` | Java 8, Update 73
`1.8.0_111` | Java 8, Update 111
`1.8.0_92` | Azul's Zulu OpenJDK 8, Update 92
`1.8.0_102` | Azul's Zulu OpenJDK 8, Update 102

> Note: It is recommended to ignore the minor version number like the following example so that the latest supported JVM will be used in your Web App.

```xml
<plugin>
   <groupId>com.microsoft.azure</groupId>
   <artifactId>azure-webapp-maven-plugin</artifactId>
   <configuration>
      <javaVersion>1.8</javaVersion>
      ...
   </configuration>
</plugin>
```

#### Web Container

Use values from the following table to configure the Web Container in your Web App.

Supported Value | Description
---|---
`tomcat 7.0` | Newest Tomcat 7.0
`tomcat 7.0.50` | Tomcat 7.0.50
`tomcat 7.0.62` | Tomcat 7.0.62
`tomcat 8.0` | Newest Tomcat 8.0
`tomcat 8.0.23` | Tomcat 8.0.23
`tomcat 8.5` | Newest Tomcat 8.5
`tomcat 8.5.6` | Tomcat 8.5.6
`jetty 9.1` | Newest Jetty 9.1
`jetty 9.1.0.20131115` | Jetty 9.1.0.v20131115
`jetty 9.3` | Newest Jetty 9.3
`jetty 9.3.13.20161014` | Jetty 9.3.13.v20161014

> Note: It is recommended to ignore the minor version number like the following example so that the latest supported web container will be used in your Web App.

```xml
<plugin>
   <groupId>com.microsoft.azure</groupId>
   <artifactId>azure-webapp-maven-plugin</artifactId>
   <configuration>
      <javaWebContainer>tomcat 8.5</javaWebContainer>
      ...
   </configuration>
</plugin>
```

### Web App on Linux

#### Runtime Stacks
Use values from the following table to configure the runtime stack you want to use in your `Web App on Linux`.

Supported Value | Description
---|---
`tomcat 8.5-jre8` | OpenJDK 8, Tomcat 8.5
`tomcat 9.0-jre8` | OpenJDK 8, Tomcat 9.0
`jre8` | OpenJDK 8

```xml
<plugin>
   <groupId>com.microsoft.azure</groupId>
   <artifactId>azure-webapp-maven-plugin</artifactId>
   <configuration>
      <linuxRuntime>tomcat 8.5-jre8</linuxRuntime>
      ...
   </configuration>
</plugin>
```

### Web App for Containers

#### Container Setting

In the `<containerSettings>` element of your `pom.xml` file, you can specify which docker container image to deploy to your Web App. Typically, this image should be from a private container registry which is built from your app, but you can also use images from a docker hub.

You can specify the following properties within the `<containerSettings>` element:

Property | Required | Description
---|---|---
`<imageName>` | true | Specifies the Docker image name. Valid image name formats are listed as below.<br>- Docker Hub image: `[hub-user/]repo-name[:tag]`; `tag` is optional, default value is **latest**.<br>- Private registry image: `hostname/repo-name[:tag]`; `tag` is optional, default value is **latest**.
`<serverId>` | false | Specifies the credentials for private docker hub images or private container registry images. (Note: `serverId` should be from your Maven `setting.xml` file.)
`<registryUrl>` | false | Specifies the URL of private container registry images.

Check out samples at [Web App Samples](../docs/web-app-samples.md) for the configuration settings for different image sources.


## Deployment Type

### WAR Deployment
You can deploy your **WAR** file with WAR Deployment by setting the `<deploymentType>` to `war`. The plugin will find the artifact at `${project.build.directory}/${project.build.finalName}.war` and deploy it as the `ROOT` application of your web container. Meanwhile, there are some optional settings that you can configure for it:

Property | Description
---|---
`warFile` | Specify the war file location, optional if the war file location is: `${project.build.directory}/${project.build.finalName}.war`
`path` | Specify context path, optional if you want to deploy to ROOT.

### JAR Deployment
If the `<deploymentType>` is set to `jar`, the plugin will find the artifact at `${project.build.directory}/${project.build.finalName}.jar` and deploy it to `%HOME%\site\wwwroot\` of your Web App. Please note that for Windows Web App, we will generate a `web.config` file, you can find more details [here](../docs/web-config.md).

There is one optional setting that you can configure for it:

Property | Description
---|---
`jarFile` | Specify the jar file location, optional if the jar file location is: `${project.build.directory}/${project.build.finalName}.jar`

### AUTO Deployment
This is the default deployment type used by the plugin. It will inspect `<packaging>` field in the pom file to decide how to deploy the artifact. If the `<packaging>` is set to `war`, the plugin will use war deployment. If the `<packaging>` is set to `jar`, the plugin will use jar deployment.
Otherwise, the plugin will skip the deployment, which is the same as `NONE` deployment.

> Note: If you want the plugin to inspect the `<packaging>` field. Just not set `<deploymentType>` in the configuration. The plugin will use `AUTO` deployment as default.

### ZIP Deployment
ZIP deploy is intended for fast and easy deployments.
You can deploy your artifacts with ZIP Deployment by setting the `<deploymentType>` to `zip`. The following shows a sample configuration:

```xml
<plugin>
   <groupId>com.microsoft.azure</groupId>
   <artifactId>azure-webapp-maven-plugin</artifactId>
   <configuration>
      <deploymentType>zip</deploymentType>
      <resources>
         <resource>
             <directory>${project.basedir}/target</directory>
             <includes>
                 <include>*.jar</include>
             </includes>
             <excludes>
                 <exclude>*.xml</exclude>
             </excludes>
         </resource>
      </resources>
      ...
   </configuration>
</plugin>
```

### FTP Deployment
You can deploy your artifacts/resources to Web App via FTP. The following example shows all the configuration elements.

```xml
<plugin>
   <groupId>com.microsoft.azure</groupId>
   <artifactId>azure-webapp-maven-plugin</artifactId>
   <configuration>
      <deploymentType>ftp</deploymentType>
      <resources>
         <resource>
             <directory>${project.basedir}/target</directory>
             <targetPath>webapps</targetPath>
             <includes>
                 <include>*.war</include>
             </includes>
             <excludes>
                 <exclude>*.xml</exclude>
             </excludes>
         </resource>
      </resources>
      ...
   </configuration>
</plugin>
```

The detailed explanation of the `<resource>` element is listed in the following table.

Property | Description
---|---
`directory` | Specifies where the resources are stored.
`targetPath` | Specifies the target path where the resources will be deployed to.<br>This is a relative path to the `/site/wwwroot/` folder of FTP server in your Web App.
`includes` | A list of patterns to include, e.g. `**/*.war`.
`excludes` | A list of patterns to exclude, e.g. `**/*.xml`.

### NONE Deployment
If you do not want to deploy anything, just simply set the `<deploymentType>` to `NONE`.

### Deployment Slot
In the `<deploymentSlot>` element of your `pom.xml` file, you can specify which deployment slot to deploy your Web App.
It requires you to configure an existing web app. And the deployment slot configured will be created first if it does not exist.

You can specify the following properties within the `<deploymentSlot>` element:

Property | Required | Description
---|---|---
`<name>` | true | Specifies the slot name. Valid slot name must match this kind of regex expression: [a-zA-Z0-9-]{1, 60}. 
`<configurationSource>` | false | The configuration source used to create a deployment slot.

> Note the `<name>` is pure slot name, not the name with web app name as the prefix in the portal.

If you specify an existing deployment slot to do the deploy, `<configurationSource>` will be ignored even it's configured.

Or, if you want to create a new deployment slot and deploy to it at the same time, you could specify configuration source as you like.

`<configurationSource>` could be set to **New** to create a brand new slot without any configuration,
or set to **Parent** to create a slot and copy the configuration from parent web app.
Besides, you could set it to the name of another existing deployment slot.
It will copy the configuration of that slot during creation.
If not configured, **Parent** will be used as default.

Check out samples at [Web App Samples](../docs/web-app-samples.md) for the configuration settings.

## Advanced Configurations 

Common configurations of all Maven Plugins for Azure can be found [here](../docs/common-configuration.md).

Maven Plugin for Azure App Service supports the following configuration properties:

Property | Required | Description | Version
---|---|---|---
`<resourceGroup>` | true | Specifies the Azure Resource Group for your Web App. | 0.1.0+
`<appName>` | true | Specifies the name of your Web App. | 0.1.0+
`<region>`* | false | Specifies the region where your Web App will be hosted; the default value is **westeurope**. All valid regions at [Supported Regions](#supported-regions) section. | 0.1.0+
`<pricingTier>`* | false | Specifies the pricing tier for your Web App; the default value is **S1**. All valid tiers are at [Supported Pricing Tiers](#supported-pricing-tiers) section. | 0.1.0+
`<deploymentSlot>` | false | Specifies the deployment slot to deploy your web app. | 1.3.0+
`<appServicePlanResourceGroup>` | false | Specifies the resource group of the existing App Service Plan when you do not want to create a new one. If this setting is not specified, plugin will use the value defined in `<resourceGroup>`. | 1.0.0+
`<appServicePlanName>` | false | Specifies the name of the existing App Service Plan when you do not want to create a new one. | 1.0.0+
`<javaVersion>` | false | Specifies the JVM version for your Web App.<br>This setting is only applicable for `Web App on Windows`; see the [Java Runtime](#java-runtime) section of this README for details. | 0.1.0+
`<javaWebContainer>` | false | Specified the Web Container for your Web App.<br>This setting is only applicable for `Web App on Windows`; see the [Web Container](#web-container) section of this README for details. | 0.1.0+
`<linuxRuntime>` | false | Specified the runtime stack for your Web App.<br>This setting is only applicable for `Web App on Linux`; see the [Runtime Stacks](#runtime-stacks) section of this README for details. | 0.2.0+
`<containerSettings>` | false | Specifies the docker container image to deploy to your Web App.<br>This setting is only applicable for `Web App for Containers`. Docker hubs and private container registries are both supported; see the [Container Setting](#container-setting) section of this README for details. | 0.1.0+
`<appSettings>` | false | Specifies the application settings for your Web App, which are defined in name-value pairs like following example:<br>`<property>`<br>&nbsp;&nbsp;&nbsp;&nbsp;`<name>xxxx</name>`<br>&nbsp;&nbsp;&nbsp;&nbsp;`<value>xxxx</value>`<br>`</property>` | 0.1.0+
`<stopAppDuringDeployment>` | false | Specifies whether stop target Web App during deployment. This will prevent deployment failure caused by IIS locking files. | 0.1.4+
`<deploymentType>` | false | Specifies the deployment approach you want to use, available options: <br><ul><li>ftp (by default): since 0.1.0</li><li>war: since 1.1.0</li></ul> | 0.1.0+
`<resources>` | false | Specifies the artifacts to be deployed to your Web App when `<deploymentType>` is set to `ftp` or `zip`. | 0.1.0+
`<warFile>` | false | Specifies the location of the war file which is to be deployed when `<deploymentType>` is set to `war`.<br>If this configuration is not specified, plugin will find the war file according to the `finalName` in the project build directory. | 1.1.0+
`<path>` | false | Specify the context path for the deployment when `<deploymentType>` is set to `war`.<br>If this configuration is not specified, plugin will deploy to the context path: `/`, which is also known as the `ROOT`. | 1.1.0+
>*: This setting will be used only when you are creating a new Web App; if the Web App already exists, this setting will be ignored

## Supported Regions
All valid regions are listed as below. Read more at [Azure Region Availability](https://azure.microsoft.com/en-us/regions/services/).
- `westus`
- `westus2`
- `centralus`
- `eastus`
- `eastus2`
- `northcentralus`
- `southcentralus`
- `westcentralus`
- `canadacentral`
- `canadaeast`
- `brazilsouth`
- `northeurope`
- `westeurope`
- `uksouth`
- `ukwest`
- `eastasia`
- `southeastasia`
- `japaneast`
- `japanwest`
- `australiaeast`
- `australiasoutheast`
- `centralindia`
- `southindia`
- `westindia`
- `koreacentral`
- `koreasouth`

## Supported Pricing Tiers
All valid pricing tiers are listed as below. Read more at [Azure App Service Plan Pricing](https://azure.microsoft.com/en-us/pricing/details/app-service/).
- `F1`
- `D1`
- `B1`
- `B2`
- `B3`
- `S1`
- `S2`
- `S3`
- `P1`
- `P2`
- `P3`
